package yeet.block;

#include "..\util.h"

import yeet.YeetUtil;
import yeet.UnsafeUtil;
import yeet.block.CustomStates;
import yeet.BlockRegistration;
import yeet.JankyHackMate;
import net.minecraft.world.level.block.RedStoneWireBlock;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.math.Vector3f;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.ObserverBlock;
import net.minecraft.world.level.block.RedStoneWireBlock;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

#define BASE_MASK		(0x03)

#define NORTH_MASK		(0x03)
#define SOUTH_MASK		(0x0C)
#define WEST_MASK		(0x30)
#define EAST_MASK		(0xC0)

#define BITS_PER_DIR	(2)

// I doubt Java is smart enough to optimize this to a LEA,
// but you never know. Keep as A * B - C instead of (A - C) * B
#define ordinal_to_shift(ordinal) ((ordinal) * 2 - 4)

#define ordinal_to_mask(ordinal) (BASE_MASK << ordinal_to_shift(ordinal))

#define NONE_BITS		(0)
#define SIDE_BITS		(1)
#define UP_BITS			(2)

#define NORTH_SHIFT		(0)
#define SOUTH_SHIFT		(2)
#define WEST_SHIFT		(4)
#define EAST_SHIFT		(6)

#define NO_CONNECTIONS	(0x00)
#define NORTH_SIDE		(0x01)
#define NORTH_UP		(0x02)
#define SOUTH_SIDE		(0x04)
#define SOUTH_UP		(0x08)
#define WEST_SIDE		(0x10)
#define WEST_UP			(0x20)
#define EAST_SIDE		(0x40)
#define EAST_UP			(0x80)

#define CROSS_SHAPE		(NORTH_SIDE | SOUTH_SIDE | WEST_SIDE | EAST_SIDE)

#define connection_type_is_not_none(type) ((type) != 0)

#define has_connection_by_ordinal(connections, ordinal) (((connections) & ordinal_to_mask(ordinal)) != 0)

#define has_north_connection(connections) (((connections) & (byte)NORTH_MASK) != 0)
#define has_any_swe_connection(connections) ((connections) > 0x03)
#define has_south_connection(connections) (((connections) & (byte)SOUTH_MASK) != 0)
#define has_any_nwe_connection(connections) (((connections) & (byte)~SOUTH_MASK) != 0)
#define has_west_connection(connections) (((connections) & (byte)WEST_MASK) != 0)
#define has_any_nse_connection(connections) (((connections) & (byte)~WEST_MASK) != 0)
#define has_east_connection(connections) ((connections) > 0x3F)
#define has_any_nsw_connection(connections) (((connections) & (byte)~EAST_MASK) != 0)

#define has_north_side_connection(connections) (((connections) & NORTH_SIDE) != 0)
#define has_north_up_connection(connections) (((connections) & NORTH_UP) != 0)
#define has_south_side_connection(connections) (((connections) & SOUTH_SIDE) != 0)
#define has_south_up_connection(connections) (((connections) & SOUTH_UP) != 0)
#define has_west_side_connection(connections) (((connections) & WEST_SIDE) != 0)
#define has_west_up_connection(connections) (((connections) & WEST_UP) != 0)
#define has_east_side_connection(connections) (((connections) & EAST_SIDE) != 0)
#define has_east_up_connection(connections) (((connections) & EAST_UP) != 0)

#define has_up_connection(connections) (((connections) & 0xAA) != 0)

#define has_no_connections(connections) ((connections) == NO_CONNECTIONS)

#define ExtendsVanillaWire 0

#if ExtendsVanillaWire
#define SoulWireBase RedStoneWireBlock
#define StateIsWire(blockstate) ((blockstate).getBlock() instanceof RedStoneWireBlock)
#define ShouldConnect(...) RedStoneWireBlock.shouldConnectTo(__VA_ARGS__)
#else
//import java.lang.reflect.Method;
#define SoulWireBase Block
#define StateIsWire(blockstate) ((blockstate).is(BlockRegistration.REDSTONE_WIRES_TAG))
#define ShouldConnect(...) SoulRedstoneWire.shouldConnectTo(__VA_ARGS__)
#endif

#define UseUnsafeHacks 0
#define UseAccessHacks 1
#if UseUnsafeHacks
import sun.misc.Unsafe;
#endif

//========================================
//
//
//
//
//
//========================================

public class SoulRedstoneWire extends SoulWireBase {
	
#define CountUpdateIters 1

#define AutoCrashIters 1
#define ITER_CRASH_COUNT 10000

#if CountUpdateIters
	private static int update_iters = 0;
#endif
	
	private static final VoxelShape[] SHAPES_LUT = new VoxelShape[171];
	
	static {
		VoxelShape shape_dot = SHAPES_LUT[0] = Block.box(3.0, 0.0, 3.0, 13.0, 1.0, 13.0);
		for (int i = 1; i < 171; ++i) {
			int side_count = POPCNT(i);
			if (side_count != 1 && side_count <= 4) {
				VoxelShape shape = shape_dot;
				if (has_north_connection(i)) {
					shape = Shapes.or(shape, Block.box(3.0, 0.0, 0.0, 13.0, 1.0, 13.0));
					if ((i & NORTH_UP) != 0) {
						shape = Shapes.or(shape, Block.box(3.0, 0.0, 0.0, 13.0, 16.0, 1.0));
					}
				}
				if (has_south_connection(i)) {
					shape = Shapes.or(shape, Block.box(3.0, 0.0, 3.0, 13.0, 1.0, 16.0));
					if ((i & SOUTH_UP) != 0) {
						shape = Shapes.or(shape, Block.box(3.0, 0.0, 15.0, 13.0, 16.0, 16.0));
					}
				}
				if (has_west_connection(i)) {
					shape = Shapes.or(shape, Block.box(0.0, 0.0, 3.0, 13.0, 1.0, 13.0));
					if ((i & WEST_UP) != 0) {
						shape = Shapes.or(shape, Block.box(0.0, 0.0, 3.0, 1.0, 16.0, 13.0));
					}
				}
				if (has_east_connection(i)) {
					shape = Shapes.or(shape, Block.box(3.0, 0.0, 3.0, 16.0, 1.0, 13.0));
					if ((i & EAST_UP) != 0) {
						shape = Shapes.or(shape, Block.box(15.0, 0.0, 3.0, 16.0, 16.0, 13.0));
					}
				}
				SHAPES_LUT[i] = shape;
			}
		}
	}
	
	public static final SparseIntegerProperty CONNECTIONS = CustomStates.CONNECTIONS;
	
	// UseUnsafeHacks will read/write the field of the vanilla wire instead so that they stay synced
	// Using raw pointers like this should be faster than reflection
#if UseUnsafeHacks
	private static Block redstone_wire_block = Blocks.REDSTONE_WIRE;
	private static intptr_t should_signal_offset;
	static {
		try_ignore(should_signal_offset = UnsafeUtil.UNSAFE.objectFieldOffset(RedStoneWireBlock.class.getDeclaredField("shouldSignal"));)
	}
#define set_should_signal(value) UnsafeUtil.UNSAFE.putBoolean(redstone_wire_block, should_signal_offset, (value))
#define get_should_signal() (UnsafeUtil.UNSAFE.getBoolean(redstone_wire_block, should_signal_offset))
#elif UseAccessHacks
	private static RedStoneWireBlock redstone_wire_block = (RedStoneWireBlock)Blocks.REDSTONE_WIRE;
#define set_should_signal(value) redstone_wire_block.shouldSignal = (value)
#define get_should_signal() (redstone_wire_block.shouldSignal)
#else
	private boolean shouldSignal = true;
#define set_should_signal(value) this.shouldSignal = (value)
#define get_should_signal() (this.shouldSignal)
#endif

#if !ExtendsVanillaWire
	public static final IntegerProperty POWER = BlockStateProperties.POWER;
#endif
	
	public SoulRedstoneWire(BlockBehaviour.Properties properties) {
        super(properties);
		this.registerDefaultState(
			this.stateDefinition.any()
				.setValue(POWER, 0)
				.setValue(CONNECTIONS, NO_CONNECTIONS)
		);
	}
	
	@Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWER, CONNECTIONS);
    }
	
	private boolean canSurviveOn(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
        return blockState.isFaceSturdy(blockGetter, blockPos, Direction.UP) || blockState.is(Blocks.HOPPER);
    }
	
#if !ExtendsVanillaWire
	@Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        BlockPos blockPos2 = blockPos.below();
        BlockState blockState2 = levelReader.getBlockState(blockPos2);
        return this.canSurviveOn(levelReader, blockPos2, blockState2);
    }

	@Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(CONNECTIONS, this.getConnectionState(blockPlaceContext.getLevel(), CROSS_SHAPE, blockPlaceContext.getClickedPos()));
    }
	
	@Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (!level.isClientSide && !blockState2.is(blockState.getBlock())) {
			BlockPos.MutableBlockPos mutable_pos = mutable_pos_create(blockPos);
            //this.updatePowerStrength(level, mutable_pos, blockState, UPDATE_CLIENTS | UPDATE_SUPPRESS_LIGHT);
            //this.updatePowerStrength2(level, mutable_pos, blockState, UPDATE_CLIENTS | UPDATE_SUPPRESS_LIGHT, -1);
            this.updatePowerStrength2(level, mutable_pos, blockState, UPDATE_CLIENTS | UPDATE_SUPPRESS_LIGHT, mutable_pos);
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, blockPos, UP_OFFSETS), this);
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, UP_TO_DOWN_DELTA), this);
			this.updateNeighborsOfNeighboringWires(level, mutable_pos_move(mutable_pos, DOWN_TO_MID_DELTA));
        }
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (!bl && !blockState.is(blockState2.getBlock())) {
            super.onRemove(blockState, level, blockPos, blockState2, false);
			if (!level.isClientSide) {
				BlockPos.MutableBlockPos mutable_pos = mutable_pos_create(blockPos, DOWN_OFFSETS);
				BlockPos.MutableBlockPos mutable_pos2 = mutable_pos_create(mutable_pos);
				YeetUtil.update_neighbors(level, mutable_pos, this, mutable_pos2);
				YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, DOWN_TO_UP_DELTA), this, mutable_pos_move(mutable_pos2, mutable_pos));
				YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, UP_TO_NORTH_DELTA), this, mutable_pos_move(mutable_pos2, mutable_pos));
				YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, NORTH_TO_SOUTH_DELTA), this, mutable_pos_move(mutable_pos2, mutable_pos));
				YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, SOUTH_TO_WEST_DELTA), this, mutable_pos_move(mutable_pos2, mutable_pos));
				YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, WEST_TO_EAST_DELTA), this, mutable_pos_move(mutable_pos2, mutable_pos));
				//this.updatePowerStrength(level, mutable_pos_move(mutable_pos, EAST_TO_MID_DELTA), blockState, UPDATE_CLIENTS | UPDATE_SUPPRESS_LIGHT);
				//this.updatePowerStrength2(level, mutable_pos_move(mutable_pos, EAST_TO_MID_DELTA), blockState, UPDATE_CLIENTS | UPDATE_SUPPRESS_LIGHT, -1);
				this.updatePowerStrength2(level, mutable_pos_move(mutable_pos, EAST_TO_MID_DELTA), blockState, UPDATE_CLIENTS | UPDATE_SUPPRESS_LIGHT, mutable_pos);
				this.updateNeighborsOfNeighboringWires(level, mutable_pos_move(mutable_pos, blockPos));
			}
        }
    }
	
	@Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos self_pos, Block block, BlockPos updated_pos, boolean bl) {
        if (!level.isClientSide) {
            if (blockState.canSurvive(level, self_pos)) {
				//this.updatePowerStrength(level, mutable_pos_create(self_pos), blockState, UPDATE_NEIGHBORS | UPDATE_CLIENTS | UPDATE_KNOWN_SHAPE | UPDATE_SUPPRESS_LIGHT, updated_pos);
				//this.updatePowerStrength2(level, mutable_pos_create(self_pos), blockState, UPDATE_CLIENTS | UPDATE_KNOWN_SHAPE | UPDATE_SUPPRESS_LIGHT, YeetUtil.calculate_neighbor_direction(self_pos, updated_pos));
				this.updatePowerStrength2(level, mutable_pos_create(self_pos), blockState, UPDATE_CLIENTS | UPDATE_KNOWN_SHAPE | UPDATE_SUPPRESS_LIGHT, updated_pos);
			} else {
				RedStoneWireBlock.dropResources(blockState, level, self_pos);
				level.removeBlock(self_pos, false);
			}
        }
    }
	
	@Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return get_should_signal() ? blockState.getSignal(blockGetter, blockPos, direction) : 0;
    }
	
	@Override
    public boolean isSignalSource(BlockState blockState) {
        return get_should_signal();
    }
	
#if 0
	// FIXME: This is where the duplicate updates are! Do something about it!
	private void updateNeighborsOfNeighboringWires(Level level, BlockPos.MutableBlockPos mutable_pos, int connections) {
        /* for (Direction direction : Direction.Plane.HORIZONTAL) {
            this.checkCornerChangeAt(level, blockPos.relative(direction));
        } */
		this.checkCornerChangeAt(level, mutable_pos_move(mutable_pos, NORTH_OFFSETS));
		this.checkCornerChangeAt(level, mutable_pos_move(mutable_pos, NORTH_TO_EAST_DELTA));
		this.checkCornerChangeAt(level, mutable_pos_move(mutable_pos, EAST_TO_SOUTH_DELTA));
		this.checkCornerChangeAt(level, mutable_pos_move(mutable_pos, SOUTH_TO_WEST_DELTA));
        /* for (Direction direction : Direction.Plane.HORIZONTAL) {
            BlockPos blockPos2 = blockPos.relative(direction);
			
			this.checkCornerChangeAt(level,
				PositionIsConductive(level, blockPos2)
					? blockPos2.above() : blockPos2.below());
        } */
		
		mutable_pos_move(mutable_pos, WEST_TO_NORTH_DELTA);
		BlockPos.MutableBlockPos mutable_pos2 = mutable_pos_create(mutable_pos, UP_OR_DOWN_OFFSETS(PositionIsConductive(level, mutable_pos)));
		this.checkCornerChangeAt(level, mutable_pos2);
		mutable_pos_move(mutable_pos, NORTH_TO_EAST_DELTA);
		this.checkCornerChangeAt(level, mutable_pos_move(mutable_pos2, mutable_pos, UP_OR_DOWN_OFFSETS(PositionIsConductive(level, mutable_pos))));
		mutable_pos_move(mutable_pos, EAST_TO_SOUTH_DELTA);
		this.checkCornerChangeAt(level, mutable_pos_move(mutable_pos2, mutable_pos, UP_OR_DOWN_OFFSETS(PositionIsConductive(level, mutable_pos))));
		mutable_pos_move(mutable_pos, SOUTH_TO_WEST_DELTA);
		this.checkCornerChangeAt(level, mutable_pos_move(mutable_pos2, mutable_pos, UP_OR_DOWN_OFFSETS(PositionIsConductive(level, mutable_pos))));
    }
	
	private void checkCornerChangeAt(Level level, BlockPos blockPos) {
        if (level.getBlockState(blockPos).is(BlockRegistration.WIRE_CONNECTIONS_TAG)) {
			BlockPos.MutableBlockPos mutable_pos = mutable_pos_create(blockPos, DOWN_OFFSETS);
			BlockPos.MutableBlockPos mutable_pos2 = mutable_pos_create(mutable_pos);
			YeetUtil.update_neighbors(level, mutable_pos, this, mutable_pos2);
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, DOWN_TO_UP_DELTA), this, mutable_pos_move(mutable_pos2, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, UP_TO_NORTH_DELTA), this, mutable_pos_move(mutable_pos2, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, NORTH_TO_SOUTH_DELTA), this, mutable_pos_move(mutable_pos2, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, SOUTH_TO_WEST_DELTA), this, mutable_pos_move(mutable_pos2, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, WEST_TO_EAST_DELTA), this, mutable_pos_move(mutable_pos2, mutable_pos));
        }
    }

#else
	
	private void updateNeighborsOfNeighboringWires(Level level, BlockPos.MutableBlockPos mutable_pos) {
        BlockPos.MutableBlockPos mutable_pos2 = mutable_pos_create(mutable_pos);
		BlockPos.MutableBlockPos mutable_pos3 = mutable_pos_create();
		boolean had_north;
		if (had_north = StateIsWire(level.getBlockState(mutable_pos_move(mutable_pos, NORTH_OFFSETS)))) {
			//YeetUtil.update_neighbors(level, mutable_pos, this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, DOWN_OFFSETS), this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, DOWN_TO_UP_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, UP_TO_NORTH_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
			// skip south
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, NORTH_TO_WEST_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, WEST_TO_EAST_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
			mutable_pos_move(mutable_pos, EAST_TO_MID_DELTA);
		}
		boolean had_east;
		if (had_east = StateIsWire(level.getBlockState(mutable_pos_move(mutable_pos, SOUTH_EAST_OFFSETS)))) {
			//YeetUtil.update_neighbors(level, mutable_pos, this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, DOWN_OFFSETS), this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, DOWN_TO_UP_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
			if (!had_north) YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, UP_TO_NORTH_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, mutable_pos2, SOUTH_EAST_OFFSETS), this, mutable_pos_move(mutable_pos3, mutable_pos));
			// skip west
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, SOUTH_TO_EAST_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
			mutable_pos_move(mutable_pos, EAST_TO_MID_DELTA);
		}
		boolean had_south;
		if (had_south = StateIsWire(level.getBlockState(mutable_pos_move(mutable_pos, SOUTH_WEST_OFFSETS)))) {
			//YeetUtil.update_neighbors(level, mutable_pos, this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, DOWN_OFFSETS), this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, DOWN_TO_UP_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
			// skip north
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, UP_TO_SOUTH_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, SOUTH_TO_WEST_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
			if (!had_east) YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, WEST_TO_EAST_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
		}
		boolean had_west;
		if (had_west = StateIsWire(level.getBlockState(mutable_pos_move(mutable_pos, mutable_pos2, WEST_OFFSETS)))) {
			//YeetUtil.update_neighbors(level, mutable_pos, this);
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, DOWN_OFFSETS), this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, DOWN_TO_UP_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
			if (!had_north) YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, UP_TO_NORTH_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
			// skip east
			if (!had_south) YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, mutable_pos2, SOUTH_WEST_OFFSETS), this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, mutable_pos2, EAST_TO_WEST_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
		}
		mutable_pos_move(mutable_pos2, NORTH_OFFSETS);
		if (!had_north && StateIsWire(level.getBlockState(mutable_pos_move(mutable_pos, mutable_pos2, UP_OR_DOWN_OFFSETS(PositionIsConductive(level, mutable_pos2)))))) {
			//YeetUtil.update_neighbors(level, mutable_pos, this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, DOWN_OFFSETS), this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, DOWN_TO_UP_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, UP_TO_NORTH_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, NORTH_TO_SOUTH_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, SOUTH_TO_WEST_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, WEST_TO_EAST_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
		}
		mutable_pos_move(mutable_pos2, NORTH_TO_EAST_DELTA);
		if (!had_east && StateIsWire(level.getBlockState(mutable_pos_move(mutable_pos, mutable_pos2, UP_OR_DOWN_OFFSETS(PositionIsConductive(level, mutable_pos2)))))) {
			//YeetUtil.update_neighbors(level, mutable_pos, this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, DOWN_OFFSETS), this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, DOWN_TO_UP_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, UP_TO_NORTH_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, NORTH_TO_SOUTH_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, SOUTH_TO_WEST_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, WEST_TO_EAST_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
		}
		mutable_pos_move(mutable_pos2, EAST_TO_SOUTH_DELTA);
		if (!had_south && StateIsWire(level.getBlockState(mutable_pos_move(mutable_pos, mutable_pos2, UP_OR_DOWN_OFFSETS(PositionIsConductive(level, mutable_pos2)))))) {
			//YeetUtil.update_neighbors(level, mutable_pos, this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, DOWN_OFFSETS), this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, DOWN_TO_UP_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, UP_TO_NORTH_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, NORTH_TO_SOUTH_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, SOUTH_TO_WEST_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, WEST_TO_EAST_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
		}
		mutable_pos_move(mutable_pos2, SOUTH_TO_WEST_DELTA);
		if (!had_west && StateIsWire(level.getBlockState(mutable_pos_move(mutable_pos, mutable_pos2, UP_OR_DOWN_OFFSETS(PositionIsConductive(level, mutable_pos2)))))) {
			//YeetUtil.update_neighbors(level, mutable_pos, this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, DOWN_OFFSETS), this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, DOWN_TO_UP_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, UP_TO_NORTH_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, NORTH_TO_SOUTH_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, SOUTH_TO_WEST_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, WEST_TO_EAST_DELTA), this, mutable_pos_move(mutable_pos3, mutable_pos));
		}
    }
#endif
	
	protected static boolean shouldConnectTo(BlockState blockState) {
        return ShouldConnect(blockState, null);
    }

    protected static boolean shouldConnectTo(BlockState blockState, @Nullable Direction direction) {
        if (blockState.is(BlockRegistration.WIRE_CONNECTIONS_TAG)) {
            return true;
        }
        if (blockState.is(BlockRegistration.REPEATER_CONNECTIONS_TAG)) {
            Direction direction2 = blockState.getValue(RepeaterBlock.FACING);
            return direction2 == direction || direction2.getOpposite() == direction;
        }
        if (blockState.is(BlockRegistration.OBSERVER_CONNECTIONS_TAG)) {
            return direction == blockState.getValue(ObserverBlock.FACING);
        }
        return blockState.isSignalSource() && direction != null;
    }
#endif
	
	private static final int[] COLORS = {
		0x0F4547,
		0x166669,
		0x186F72,
		0x1A787B,
		0x1C8185,
		0x1E8A8E,
		0x209397,
		0x229CA1,
		0x24A6AA,
		0x26AFB3,
		0x28BABE,
		0x2AC3C8,
		0x2CCCD1,
		0x2ED5DA,
		0x30DEE4,
		0x32E7ED
	};
	
	// isSignalSource not overwritten
	
	public static int getColorForPower(int n) {
        return COLORS[n];
    }
	
	// spawnParticlesAlongLine (omitted because animateTick is removed)
	@Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, Random random) {
	}
	
	// rotation of NONE will end up falling through the switch in RedstoneWireBlock
	@Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        int connections = blockState.getValue(CONNECTIONS);
		int temp;
		switch (rotation) {
			default:
				return blockState;
			case CLOCKWISE_90: {
				temp = connections >>> 4;
				temp += connections << 6 & 0xC0;
				connections &= 0x30;
				//temp += connections << 2 & 0x30;
				return blockState.setValue(CONNECTIONS, connections * 4 + temp);
            }
            case CLOCKWISE_180: {
				temp = connections >>> 2 & 0x33;
				connections &= 0xCC;
				//temp += connections << 2 & 0xCC;
				return blockState.setValue(CONNECTIONS, connections * 4 + temp);
            }
            case COUNTERCLOCKWISE_90: {
				temp = connections >>> 6;
				temp += connections >> 2 & 0x0C;
				temp += connections << 4 & 0xF0;
                return blockState.setValue(CONNECTIONS, temp);
            }
        }
    }
	
	// mirror of NONE will end up falling through the switch in RedstoneWireBlock
	@Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
		int connections = blockState.getValue(CONNECTIONS);
		int temp;
		switch (mirror) {
            case LEFT_RIGHT: {
				temp = connections >>> 2 & 0x03;
				temp += connections & 0xF0;
				connections &= 0x03;
				return blockState.setValue(CONNECTIONS, connections * 4 + temp);
            }
            case FRONT_BACK: {
				temp = connections >>> 2 & 0x30;
				temp += connections & 0x0F;
				connections &= 0x30;
				return blockState.setValue(CONNECTIONS, connections * 4 + temp);
            }
        }
        return super.mirror(blockState, mirror);
    }
	
	@Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (player.getAbilities().mayBuild) {
            int connections = blockState.getValue(CONNECTIONS);
			int number_of_connections = POPCNT(connections);
			if (number_of_connections == 0 || number_of_connections == 4) {
				int new_connections = this.getConnectionState(level, connections, blockPos);
				connections ^= new_connections;
				if (connections != 0) {
					blockState = blockState.setValue(CONNECTIONS, new_connections);
					level.setBlock(blockPos, blockState, UPDATE_ALL);
					BlockPos.MutableBlockPos mutable_pos = mutable_pos_create(blockPos, NORTH_OFFSETS);
					if (POPCNT(connections & NORTH_MASK) == 1 || PositionIsConductive(level, mutable_pos)) {
						level.updateNeighborsAtExceptFromFacing(mutable_pos, blockState.getBlock(), Direction.SOUTH);
					}
					mutable_pos_move(mutable_pos, NORTH_TO_EAST_DELTA);
					if (POPCNT(connections & EAST_MASK) == 1 || PositionIsConductive(level, mutable_pos)) {
						level.updateNeighborsAtExceptFromFacing(mutable_pos, blockState.getBlock(), Direction.WEST);
					}
					mutable_pos_move(mutable_pos, EAST_TO_SOUTH_DELTA);
					if (POPCNT(connections & SOUTH_MASK) == 1 || PositionIsConductive(level, mutable_pos)) {
						level.updateNeighborsAtExceptFromFacing(mutable_pos, blockState.getBlock(), Direction.NORTH);
					}
					mutable_pos_move(mutable_pos, SOUTH_TO_WEST_DELTA);
					if (POPCNT(connections & WEST_MASK) == 1 || PositionIsConductive(level, mutable_pos)) {
						level.updateNeighborsAtExceptFromFacing(mutable_pos, blockState.getBlock(), Direction.EAST);
					}
					return InteractionResult.SUCCESS;
				}
			}
        }
        return InteractionResult.PASS;
    }
	
	
	@Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPES_LUT[blockState.getValue(CONNECTIONS)];
    }
	
	private int getConnectingSide(BlockGetter blockGetter, BlockPos self_pos, Direction direction, boolean above_is_solid, BlockPos.MutableBlockPos mutable_pos) {
		BlockState neighbor_state = blockGetter.getBlockState(self_pos);
		int ret = NONE_BITS;
		do {
			if (!above_is_solid && this.canSurviveOn(blockGetter, self_pos, neighbor_state) && ShouldConnect(blockGetter.getBlockState(mutable_pos_move(mutable_pos, self_pos, UP_OFFSETS)))) {
				if (neighbor_state.isFaceSturdy(blockGetter, self_pos, direction.getOpposite())) {
					ret += UP_BITS;
					break;
				}
			}
			else if (!(ShouldConnect(neighbor_state, direction) || !neighbor_state.isRedstoneConductor(blockGetter, self_pos) && ShouldConnect(blockGetter.getBlockState(mutable_pos_move(mutable_pos, self_pos, DOWN_OFFSETS))))) {
				break;
			}
			ret += SIDE_BITS;
		} while (false);
		return ret << ordinal_to_shift(direction.ordinal());
    }
	
	private int getMissingConnections(BlockGetter blockGetter, BlockPos.MutableBlockPos mutable_pos) {
		boolean above_is_solid = blockGetter.getBlockState(mutable_pos_move(mutable_pos, UP_OFFSETS)).isRedstoneConductor(blockGetter, mutable_pos_move(mutable_pos, UP_TO_MID_DELTA));
		int connections = NO_CONNECTIONS;
		BlockPos.MutableBlockPos mutable_pos2 = mutable_pos_create();
		connections |= this.getConnectingSide(blockGetter, mutable_pos_move(mutable_pos, NORTH_OFFSETS), Direction.NORTH, above_is_solid, mutable_pos2);
		connections |= this.getConnectingSide(blockGetter, mutable_pos_move(mutable_pos, NORTH_TO_SOUTH_DELTA), Direction.SOUTH, above_is_solid, mutable_pos2);
		connections |= this.getConnectingSide(blockGetter, mutable_pos_move(mutable_pos, SOUTH_TO_WEST_DELTA), Direction.WEST, above_is_solid, mutable_pos2);
		connections |= this.getConnectingSide(blockGetter, mutable_pos_move(mutable_pos, WEST_TO_EAST_DELTA), Direction.EAST, above_is_solid, mutable_pos2);
        return connections;
    }
	
	/*
	@Override
	private static boolean isDot(BlockState blockState) {
		return has_no_connections(blockState.getValue(CONNECTIONS));
    }
	@Override
	private static boolean isCross(BlockState blockState) {
		return POPCNT(blockState.getValue(CONNECTIONS)) == 4;
	}
	*/
	
	// Can't use a mutable pos
	private int getConnectionState(BlockGetter blockGetter, int connections, BlockPos self_pos) {
		boolean started_as_dot = has_no_connections(connections);
		BlockPos.MutableBlockPos mutable_pos = mutable_pos_create(self_pos);
        connections = this.getMissingConnections(blockGetter, mutable_pos);
        if (started_as_dot && has_no_connections(connections)) {
			return NO_CONNECTIONS; // self_state.setValue(CONNECTIONS, NO_CONNECTIONS);
        }
		int new_connections = connections;
		if (!has_any_nwe_connection(connections)) new_connections += NORTH_SIDE;
		if (!has_any_swe_connection(connections)) new_connections += SOUTH_SIDE;
		if (!has_any_nsw_connection(connections)) new_connections += WEST_SIDE;
		if (!has_any_nse_connection(connections)) new_connections += EAST_SIDE;
		return new_connections;
    }
	
	@Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
#define direction_index direction_int
#define direction_mask direction_int
		int direction_index = direction.ordinal();
		if (direction_index != DOWN_ORDINAL) {
			int redstone_side;
			int connections = blockState.getValue(CONNECTIONS);
			if (direction_index == UP_ORDINAL) {
				redstone_side = this.getConnectionState(levelAccessor, connections, blockPos);
			}
			else {
				BlockPos.MutableBlockPos mutable_pos = mutable_pos_create(blockPos, UP_OFFSETS);
				boolean above_is_solid = levelAccessor.getBlockState(mutable_pos).isRedstoneConductor(levelAccessor, mutable_pos_move(mutable_pos, UP_TO_MID_DELTA));
				redstone_side = this.getConnectingSide(levelAccessor, mutable_pos.move(YeetUtil.all_direction_offsets[direction_index]), direction, above_is_solid, mutable_pos_create());
				
				direction_mask = ordinal_to_mask(direction_index) & connections;
				if (POPCNT(connections) != 4 &&
					//connection_type_is_not_none(redstone_side) == has_connection_by_ordinal(connections, direction_index)
					POPCNT(redstone_side ^ direction_mask) != 1
				) {
					redstone_side |= connections ^ direction_mask;
				}
				else {
					redstone_side = this.getConnectionState(levelAccessor, CROSS_SHAPE, blockPos);
				}
			}
			if (redstone_side != connections) { // Apparently it's an error to set a blockState to itself
				blockState = blockState.setValue(CONNECTIONS, redstone_side);
			}
		}
		return blockState;
#undef direction_index
#undef direction_mask
    }
	
	@Override
    public void updateIndirectNeighbourShapes(BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos, int n, int n2) {
        int connections = blockState.getValue(CONNECTIONS);
		if (!has_no_connections(connections)) {
			BlockPos.MutableBlockPos mutable_pos = mutable_pos_create();
			BlockPos below_pos = blockPos.below();
			BlockState below_state = levelAccessor.getBlockState(below_pos);
			BlockPos above_pos = blockPos.above();
			BlockState above_state = levelAccessor.getBlockState(above_pos);
			BlockState neighbor_state;
			if (has_north_connection(connections)) {
				if (!StateIsWire(levelAccessor.getBlockState(mutable_pos_move(mutable_pos, blockPos, NORTH_OFFSETS)))) {
					neighbor_state = levelAccessor.getBlockState(mutable_pos_move(mutable_pos, DOWN_OFFSETS));
					if (!neighbor_state.is(Blocks.OBSERVER)) {
						RedStoneWireBlock.updateOrDestroy(neighbor_state,
							neighbor_state.updateShape(Direction.SOUTH, below_state, levelAccessor, mutable_pos, below_pos)
						, levelAccessor, mutable_pos, n, n2);
					}
				}
				neighbor_state = levelAccessor.getBlockState(mutable_pos_move(mutable_pos, blockPos, NORTH_UP_OFFSETS));
				if (!neighbor_state.is(Blocks.OBSERVER)) {
					RedStoneWireBlock.updateOrDestroy(neighbor_state,
						neighbor_state.updateShape(Direction.SOUTH, above_state, levelAccessor, mutable_pos, above_pos)
					, levelAccessor, mutable_pos, n, n2);
				}
			}
			if (has_east_connection(connections)) {
				if (!StateIsWire(levelAccessor.getBlockState(mutable_pos_move(mutable_pos, blockPos, EAST_OFFSETS)))) {
					neighbor_state = levelAccessor.getBlockState(mutable_pos_move(mutable_pos, DOWN_OFFSETS));
					if (!neighbor_state.is(Blocks.OBSERVER)) {
						RedStoneWireBlock.updateOrDestroy(neighbor_state,
							neighbor_state.updateShape(Direction.WEST, below_state, levelAccessor, mutable_pos, below_pos)
						, levelAccessor, mutable_pos, n, n2);
					}
				}
				neighbor_state = levelAccessor.getBlockState(mutable_pos_move(mutable_pos, blockPos, EAST_UP_OFFSETS));
				if (!neighbor_state.is(Blocks.OBSERVER)) {
					RedStoneWireBlock.updateOrDestroy(neighbor_state,
						neighbor_state.updateShape(Direction.WEST, above_state, levelAccessor, mutable_pos, above_pos)
					, levelAccessor, mutable_pos, n, n2);
				}
			}
			if (has_south_connection(connections)) {
				if (!StateIsWire(levelAccessor.getBlockState(mutable_pos_move(mutable_pos, blockPos, SOUTH_OFFSETS)))) {
					neighbor_state = levelAccessor.getBlockState(mutable_pos_move(mutable_pos, DOWN_OFFSETS));
					if (!neighbor_state.is(Blocks.OBSERVER)) {
						RedStoneWireBlock.updateOrDestroy(neighbor_state,
							neighbor_state.updateShape(Direction.NORTH, below_state, levelAccessor, mutable_pos, below_pos)
						, levelAccessor, mutable_pos, n, n2);
					}
				}
				neighbor_state = levelAccessor.getBlockState(mutable_pos_move(mutable_pos, blockPos, SOUTH_UP_OFFSETS));
				if (!neighbor_state.is(Blocks.OBSERVER)) {
					RedStoneWireBlock.updateOrDestroy(neighbor_state,
						neighbor_state.updateShape(Direction.NORTH, above_state, levelAccessor, mutable_pos, above_pos)
					, levelAccessor, mutable_pos, n, n2);
				}
			}
			if (has_west_connection(connections)) {
				if (!StateIsWire(levelAccessor.getBlockState(mutable_pos_move(mutable_pos, blockPos, WEST_OFFSETS)))) {
					neighbor_state = levelAccessor.getBlockState(mutable_pos_move(mutable_pos, DOWN_OFFSETS));
					if (!neighbor_state.is(Blocks.OBSERVER)) {
						RedStoneWireBlock.updateOrDestroy(neighbor_state,
							neighbor_state.updateShape(Direction.EAST, below_state, levelAccessor, mutable_pos, below_pos)
						, levelAccessor, mutable_pos, n, n2);
					}
				}
				neighbor_state = levelAccessor.getBlockState(mutable_pos_move(mutable_pos, blockPos, WEST_UP_OFFSETS));
				if (!neighbor_state.is(Blocks.OBSERVER)) {
					RedStoneWireBlock.updateOrDestroy(neighbor_state,
						neighbor_state.updateShape(Direction.EAST, above_state, levelAccessor, mutable_pos, above_pos)
					, levelAccessor, mutable_pos, n, n2);
				}
			}
		}
    }
	
	// getDirectSignal not overwritten
	
	@Override
    public int getSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
		if (get_should_signal()) {
			int direction_index = direction.ordinal();
			if (direction_index != DOWN_ORDINAL) {
				int power = blockState.getValue(POWER);
				if (power != 0 && (
						direction_index == UP_ORDINAL ||
						has_connection_by_ordinal(this.getConnectionState(blockGetter, blockState.getValue(CONNECTIONS), blockPos), opposite_direction_index(direction_index))
					)
				) {
					return power;
				}
			}
		}
		return 0;
    }
	
	
#define RESISTANCE (1)
#define power_after_resistance(power) ((power) - RESISTANCE)

	private void updatePowerStrength2(Level level, BlockPos.MutableBlockPos mutable_pos, BlockState blockState, int flags, BlockPos neighbor_pos) {
		
#if CountUpdateIters
		++update_iters;
#if AutoCrashIters
		if (UCMP(update_iters, ITER_CRASH_COUNT) > 0) {
			JankyHackMate.breakpoint();
		}
#endif
#endif
		
//#define SET_SKIP_POWER_READS() update_state += 0x80000000
//#define SKIP_POWER_READS() (update_state < 0)

#define SHOULD_CHECK_DIRECT_POWER() (highest_power < MAX_REDSTONE_POWER)
#define SHOULD_CHECK_WIRE_POWER() (highest_power < 14)

#define NORTH_WIRE	(0x010000)
#define SOUTH_WIRE	(0x020000)
#define WEST_WIRE	(0x040000)
#define EAST_WIRE	(0x080000)

#define SET_NORTH_IS_WIRE() connections += NORTH_WIRE
#define NORTH_IS_WIRE() ((connections & NORTH_WIRE) != 0)
#define SET_SOUTH_IS_WIRE() connections += SOUTH_WIRE
#define SOUTH_IS_WIRE() ((connections & SOUTH_WIRE) != 0)
#define SET_WEST_IS_WIRE() connections += WEST_WIRE
#define WEST_IS_WIRE() ((connections & WEST_WIRE) != 0)
#define SET_EAST_IS_WIRE() connections += EAST_WIRE
#define EAST_IS_WIRE() ((connections & EAST_WIRE) != 0)

#define CLEAR_ALL_UP_IS_LOWER_POWER() connections &= ~0x0F0000
#define CLEAR_ALL_DOWN_IS_LOWER_POWER() connections &= ~0xF00000

#define NORTH_CONDUCTIVE	(0x100000)
#define SOUTH_CONDUCTIVE	(0x200000)
#define WEST_CONDUCTIVE		(0x400000)
#define EAST_CONDUCTIVE		(0x800000)

#define SET_NORTH_IS_CONDUCTIVE() connections += NORTH_CONDUCTIVE
#define NORTH_IS_CONDUCTIVE() ((connections & NORTH_CONDUCTIVE) != 0)
#define NORTH_IS_NONCONDUCTIVE() ((connections & NORTH_CONDUCTIVE) == 0)
#define SET_SOUTH_IS_CONDUCTIVE() connections += SOUTH_CONDUCTIVE
#define SOUTH_IS_CONDUCTIVE() ((connections & SOUTH_CONDUCTIVE) != 0)
#define SOUTH_IS_NONECONDUCTIVE() ((connections & SOUTH_CONDUCTIVE) == 0)
#define SET_WEST_IS_CONDUCTIVE() connections += WEST_CONDUCTIVE
#define WEST_IS_CONDUCTIVE() ((connections & WEST_CONDUCTIVE) != 0)
#define WEST_IS_NONCONDUCTIVE() ((connections & WEST_CONDUCTIVE) == 0)
#define SET_EAST_IS_CONDUCTIVE() connections += EAST_CONDUCTIVE
#define EAST_IS_CONDUCTIVE() ((connections & EAST_CONDUCTIVE) != 0)
#define EAST_IS_NONCONDUCTIVE() ((connections & EAST_CONDUCTIVE) == 0)
#define SET_BELOW_IS_CONDUCTIVE() connections += 0x01000000
#define BELOW_IS_CONDUCTIVE() ((connections & 0x01000000) != 0)
#define BELOW_IS_NONCONDUCTIVE() ((connections & 0x01000000) == 0)
//#define SET_ABOVE_IS_CONDUCTIVE() connections += 0x02000000
//#define ABOVE_IS_CONDUCTIVE() ((connections & 0x02000000) != 0)
//#define ABOVE_IS_NONCONDUCTIVE() ((connections & 0x02000000) == 0)

#define HAS_NORTH_BITS() ((connections & 0x110000) != 0)
#define HAS_SOUTH_BITS() ((connections & 0x220000) != 0)
#define HAS_WEST_BITS() ((connections & 0x440000) != 0)
#define HAS_EAST_BITS() ((connections & 0x880000) != 0)

#define UpdateHighestPower(value) highest_power = Math.max(highest_power, (value))
#define UpdateNeighborPower(value) neighbor_power = Math.max(highest_power, (value))
		set_should_signal(false);
		
		int north_power, south_power, west_power, east_power;
		
		/* int neighbor_direction = self_pos.getX() - neighbor_pos.getX() & 0x7;
		if ((neighbor_direction & 0x4) != 0) return;
		north_power = self_pos.getY() - neighbor_pos.getY() & 0x7;
		if ((north_power & 0x4) != 0) return;
		south_power = self_pos.getZ() - neighbor_pos.getZ() & 0x7;
		if ((south_power & 0x4) != 0) return; */
		
		int neighbor_direction = YeetUtil.calculate_neighbor_direction(mutable_pos, neighbor_pos);

		//int update_state = 0;
		int neighbor_power;
		int highest_power;
		int prev_power = blockState.getValue(POWER);
		int connections = blockState.getValue(CONNECTIONS);
		BlockPos.MutableBlockPos mutable_pos2;
		BlockState neighbor_state;
		
		north_power = 16;
		south_power = 16;
		west_power = 16;
		east_power = 16;
		//int north_down_power = MAX_REDSTONE_POWER;
		//int south_down_power = MAX_REDSTONE_POWER;
		//int west_down_power = MAX_REDSTONE_POWER;
		//int east_down_power = MAX_REDSTONE_POWER;
		
		// DOWN
		
		mutable_pos2 = mutable_pos_create(mutable_pos, DOWN_OFFSETS);
		
		neighbor_state = level.getBlockState(mutable_pos2);
		highest_power = neighbor_state.getSignal(level, mutable_pos2, Direction.DOWN);
		if (neighbor_state.isRedstoneConductor(level, mutable_pos2)) {
			SET_BELOW_IS_CONDUCTIVE();
			if (SHOULD_CHECK_DIRECT_POWER()) {
				UpdateHighestPower(level.getDirectSignal(mutable_pos_move(mutable_pos2, DOWN_OFFSETS), Direction.DOWN));
				UpdateHighestPower(level.getDirectSignal(mutable_pos_move(mutable_pos2, DOWN_TO_NORTH_DELTA), Direction.NORTH));
				UpdateHighestPower(level.getDirectSignal(mutable_pos_move(mutable_pos2, NORTH_TO_SOUTH_DELTA), Direction.SOUTH));
				UpdateHighestPower(level.getDirectSignal(mutable_pos_move(mutable_pos2, SOUTH_TO_WEST_DELTA), Direction.WEST));
				UpdateHighestPower(level.getDirectSignal(mutable_pos_move(mutable_pos2, WEST_TO_EAST_DELTA), Direction.EAST));
				mutable_pos_move(mutable_pos2, EAST_TO_MID_DELTA);
			}
		}
		
		// NORTH
		
		neighbor_state = level.getBlockState(mutable_pos_move(mutable_pos2, DOWN_TO_NORTH_DELTA));
		UpdateHighestPower(neighbor_state.getSignal(level, mutable_pos2, Direction.NORTH));
		
		goto_block(EndNorth) {
			if (neighbor_state.isRedstoneConductor(level, mutable_pos2)) {
				SET_NORTH_IS_CONDUCTIVE();
				if (SHOULD_CHECK_DIRECT_POWER()) {
					UpdateHighestPower(level.getDirectSignal(mutable_pos_move(mutable_pos2, DOWN_OFFSETS), Direction.DOWN));
					UpdateHighestPower(level.getDirectSignal(mutable_pos_move(mutable_pos2, DOWN_TO_UP_DELTA), Direction.UP));
					UpdateHighestPower(level.getDirectSignal(mutable_pos_move(mutable_pos2, UP_TO_NORTH_DELTA), Direction.NORTH));
					UpdateHighestPower(level.getDirectSignal(mutable_pos_move(mutable_pos2, NORTH_TO_WEST_DELTA), Direction.WEST));
					UpdateHighestPower(level.getDirectSignal(mutable_pos_move(mutable_pos2, WEST_TO_EAST_DELTA), Direction.EAST));
					mutable_pos_move(mutable_pos2, EAST_TO_MID_DELTA);
				}
			}
			else if (has_north_connection(connections)) {
				if (StateIsWire(neighbor_state)) {
					SET_NORTH_IS_WIRE();
					neighbor_power = neighbor_state.getValue(POWER);
					if (neighbor_has_north(neighbor_direction) || neighbor_power <= prev_power) {
						UpdateHighestPower(power_after_resistance(north_power = neighbor_power));
					}
					goto(EndNorth);
				}
				if (SHOULD_CHECK_WIRE_POWER()) {
					neighbor_state = level.getBlockState(mutable_pos_move(mutable_pos2, DOWN_OFFSETS));
					if (StateIsWire(neighbor_state)) {
						neighbor_power = neighbor_state.getValue(POWER);
						if (neighbor_has_north(neighbor_direction) || neighbor_power <= prev_power) {
							UpdateHighestPower(power_after_resistance(neighbor_power));
						}
					}
					mutable_pos_move(mutable_pos2, DOWN_TO_MID_DELTA);
				}
			}
			if (has_north_up_connection(connections)) {
				neighbor_state = level.getBlockState(mutable_pos_move(mutable_pos2, UP_OFFSETS));
				if (StateIsWire(neighbor_state)) {
					neighbor_power = neighbor_state.getValue(POWER);
					if (neighbor_has_north(neighbor_direction) || neighbor_power <= prev_power) {
						UpdateHighestPower(power_after_resistance(north_power = neighbor_power));
					}
				}
				mutable_pos_move(mutable_pos2, UP_TO_MID_DELTA);
			}
		} goto_target(EndNorth);
		
		// SOUTH
		
		neighbor_state = level.getBlockState(mutable_pos_move(mutable_pos2, NORTH_TO_SOUTH_DELTA));
		UpdateHighestPower(neighbor_state.getSignal(level, mutable_pos2, Direction.SOUTH));
		
		goto_block(EndSouth) {
			if (neighbor_state.isRedstoneConductor(level, mutable_pos2)) {
				SET_SOUTH_IS_CONDUCTIVE();
				if (SHOULD_CHECK_DIRECT_POWER()) {
					UpdateHighestPower(level.getDirectSignal(mutable_pos_move(mutable_pos2, DOWN_OFFSETS), Direction.DOWN));
					UpdateHighestPower(level.getDirectSignal(mutable_pos_move(mutable_pos2, DOWN_TO_UP_DELTA), Direction.UP));
					UpdateHighestPower(level.getDirectSignal(mutable_pos_move(mutable_pos2, UP_TO_SOUTH_DELTA), Direction.SOUTH));
					UpdateHighestPower(level.getDirectSignal(mutable_pos_move(mutable_pos2, SOUTH_TO_WEST_DELTA), Direction.WEST));
					UpdateHighestPower(level.getDirectSignal(mutable_pos_move(mutable_pos2, WEST_TO_EAST_DELTA), Direction.EAST));
					mutable_pos_move(mutable_pos2, EAST_TO_MID_DELTA);
				}
			}
			else if (has_south_connection(connections)) {
				if (StateIsWire(neighbor_state)) {
					SET_SOUTH_IS_WIRE();
					neighbor_power = neighbor_state.getValue(POWER);
					if (neighbor_has_south(neighbor_direction) || neighbor_power <= prev_power) {
						UpdateHighestPower(power_after_resistance(south_power = neighbor_power));
					}
					goto(EndSouth);
				}
				if (SHOULD_CHECK_WIRE_POWER()) {
					neighbor_state = level.getBlockState(mutable_pos_move(mutable_pos2, DOWN_OFFSETS));
					if (StateIsWire(neighbor_state)) {
						neighbor_power = neighbor_state.getValue(POWER);
						if (neighbor_has_south(neighbor_direction) || neighbor_power <= prev_power) {
							UpdateHighestPower(power_after_resistance(neighbor_power));
						}
					}
					mutable_pos_move(mutable_pos2, DOWN_TO_MID_DELTA);
				}
			}
			if (has_south_up_connection(connections)) {
				neighbor_state = level.getBlockState(mutable_pos_move(mutable_pos2, UP_OFFSETS));
				if (StateIsWire(neighbor_state)) {
					neighbor_power = neighbor_state.getValue(POWER);
					if (neighbor_has_south(neighbor_direction) || neighbor_power <= prev_power) {
						UpdateHighestPower(power_after_resistance(south_power = neighbor_power));
					}
				}
				mutable_pos_move(mutable_pos2, UP_TO_MID_DELTA);
			}
		} goto_target(EndSouth);
		
		// WEST
		
		neighbor_state = level.getBlockState(mutable_pos_move(mutable_pos2, SOUTH_TO_WEST_DELTA));
		UpdateHighestPower(neighbor_state.getSignal(level, mutable_pos2, Direction.WEST));
		
		goto_block(EndWest) {
			if (neighbor_state.isRedstoneConductor(level, mutable_pos2)) {
				SET_WEST_IS_CONDUCTIVE();
				if (SHOULD_CHECK_DIRECT_POWER()) {
					UpdateHighestPower(level.getDirectSignal(mutable_pos_move(mutable_pos2, DOWN_OFFSETS), Direction.DOWN));
					UpdateHighestPower(level.getDirectSignal(mutable_pos_move(mutable_pos2, DOWN_TO_UP_DELTA), Direction.UP));
					UpdateHighestPower(level.getDirectSignal(mutable_pos_move(mutable_pos2, UP_TO_NORTH_DELTA), Direction.NORTH));
					UpdateHighestPower(level.getDirectSignal(mutable_pos_move(mutable_pos2, NORTH_TO_SOUTH_DELTA), Direction.SOUTH));
					UpdateHighestPower(level.getDirectSignal(mutable_pos_move(mutable_pos2, SOUTH_TO_WEST_DELTA), Direction.WEST));
					mutable_pos_move(mutable_pos2, WEST_TO_MID_DELTA);
				}
			}
			else if (has_west_connection(connections)) {
				if (StateIsWire(neighbor_state)) {
					SET_WEST_IS_WIRE();
					neighbor_power = neighbor_state.getValue(POWER);
					if (neighbor_has_west(neighbor_direction) || neighbor_power <= prev_power) {
						UpdateHighestPower(power_after_resistance(west_power = neighbor_power));
					}
					goto(EndWest);
				}
				if (SHOULD_CHECK_WIRE_POWER()) {
					neighbor_state = level.getBlockState(mutable_pos_move(mutable_pos2, DOWN_OFFSETS));
					if (StateIsWire(neighbor_state)) {
						neighbor_power = neighbor_state.getValue(POWER);
						if (neighbor_has_west(neighbor_direction) || neighbor_power <= prev_power) {
							UpdateHighestPower(power_after_resistance(neighbor_power));
						}
					}
					mutable_pos_move(mutable_pos2, DOWN_TO_MID_DELTA);
				}
			}
			if (has_west_up_connection(connections)) {
				neighbor_state = level.getBlockState(mutable_pos_move(mutable_pos2, UP_OFFSETS));
				if (StateIsWire(neighbor_state)) {
					neighbor_power = neighbor_state.getValue(POWER);
					if (neighbor_has_west(neighbor_direction) || neighbor_power <= prev_power) {
						UpdateHighestPower(power_after_resistance(west_power = neighbor_power));
					}
				}
				mutable_pos_move(mutable_pos2, UP_TO_MID_DELTA);
			}
		} goto_target(EndWest);
		
		// EAST
		
		neighbor_state = level.getBlockState(mutable_pos_move(mutable_pos2, WEST_TO_EAST_DELTA));
		UpdateHighestPower(neighbor_state.getSignal(level, mutable_pos2, Direction.EAST));
		
		goto_block(EndEast) {
			if (neighbor_state.isRedstoneConductor(level, mutable_pos2)) {
				SET_EAST_IS_CONDUCTIVE();
				if (SHOULD_CHECK_DIRECT_POWER()) {
					UpdateHighestPower(level.getDirectSignal(mutable_pos_move(mutable_pos2, DOWN_OFFSETS), Direction.DOWN));
					UpdateHighestPower(level.getDirectSignal(mutable_pos_move(mutable_pos2, DOWN_TO_UP_DELTA), Direction.UP));
					UpdateHighestPower(level.getDirectSignal(mutable_pos_move(mutable_pos2, UP_TO_NORTH_DELTA), Direction.NORTH));
					UpdateHighestPower(level.getDirectSignal(mutable_pos_move(mutable_pos2, NORTH_TO_SOUTH_DELTA), Direction.SOUTH));
					UpdateHighestPower(level.getDirectSignal(mutable_pos_move(mutable_pos2, SOUTH_TO_EAST_DELTA), Direction.EAST));
					mutable_pos_move(mutable_pos2, EAST_TO_MID_DELTA);
				}
			}
			else if (has_east_connection(connections)) {
				if (StateIsWire(neighbor_state)) {
					SET_EAST_IS_WIRE();
					neighbor_power = neighbor_state.getValue(POWER);
					if (neighbor_has_east(neighbor_direction) || neighbor_power <= prev_power) {
						UpdateHighestPower(power_after_resistance(east_power = neighbor_power));
					}
					goto(EndEast);
				}
				if (SHOULD_CHECK_WIRE_POWER()) {
					neighbor_state = level.getBlockState(mutable_pos_move(mutable_pos2, DOWN_OFFSETS));
					if (StateIsWire(neighbor_state)) {
						neighbor_power = neighbor_state.getValue(POWER);
						if (neighbor_has_east(neighbor_direction) || neighbor_power <= prev_power) {
							UpdateHighestPower(power_after_resistance(neighbor_power));
						}
					}
					mutable_pos_move(mutable_pos2, DOWN_TO_MID_DELTA);
				}
			}
			if (has_east_up_connection(connections)) {
				neighbor_state = level.getBlockState(mutable_pos_move(mutable_pos2, UP_OFFSETS));
				if (StateIsWire(neighbor_state)) {
					neighbor_power = neighbor_state.getValue(POWER);
					if (neighbor_has_east(neighbor_direction) || neighbor_power <= prev_power) {
						UpdateHighestPower(power_after_resistance(east_power = neighbor_power));
					}
				}
				mutable_pos_move(mutable_pos2, UP_TO_MID_DELTA);
			}
		} goto_target(EndEast);
		
		// UP
		
		mutable_pos_move(mutable_pos2, EAST_TO_UP_DELTA);
		
		if (SHOULD_CHECK_DIRECT_POWER()) {
			neighbor_state = level.getBlockState(mutable_pos2);
			UpdateHighestPower(neighbor_state.getSignal(level, mutable_pos2, Direction.UP));
			if (neighbor_state.isRedstoneConductor(level, mutable_pos2)) {
				UpdateHighestPower(level.getDirectSignal(mutable_pos_move(mutable_pos2, UP_OFFSETS), Direction.UP));
				UpdateHighestPower(level.getDirectSignal(mutable_pos_move(mutable_pos2, UP_TO_NORTH_DELTA), Direction.NORTH));
				UpdateHighestPower(level.getDirectSignal(mutable_pos_move(mutable_pos2, NORTH_TO_SOUTH_DELTA), Direction.SOUTH));
				UpdateHighestPower(level.getDirectSignal(mutable_pos_move(mutable_pos2, SOUTH_TO_WEST_DELTA), Direction.WEST));
				UpdateHighestPower(level.getDirectSignal(mutable_pos_move(mutable_pos2, WEST_TO_EAST_DELTA), Direction.EAST));
				mutable_pos_move(mutable_pos2, EAST_TO_MID_DELTA);
			}
		}
		
		set_should_signal(true);
		
		// Perform Updates
		
		highest_power &= MAX_REDSTONE_POWER;
		
		if (highest_power != prev_power) {
			level.setBlock(mutable_pos_move(mutable_pos2, UP_TO_MID_DELTA), blockState.setValue(POWER, highest_power), flags);
			
			// DOWN
			
			//if (neighbor_direction != DOWN_ORDINAL) {
				level.neighborChanged(mutable_pos_move(mutable_pos2, DOWN_OFFSETS), this, mutable_pos);
				if (BELOW_IS_CONDUCTIVE()) {
					level.neighborChanged(mutable_pos_move(mutable_pos2, DOWN_OFFSETS), this, mutable_pos_move(mutable_pos, DOWN_OFFSETS));
					level.neighborChanged(mutable_pos_move(mutable_pos2, DOWN_TO_NORTH_DELTA), this, mutable_pos);
					level.neighborChanged(mutable_pos_move(mutable_pos2, NORTH_TO_SOUTH_DELTA), this, mutable_pos);
					level.neighborChanged(mutable_pos_move(mutable_pos2, SOUTH_TO_WEST_DELTA), this, mutable_pos);
					level.neighborChanged(mutable_pos_move(mutable_pos2, WEST_TO_EAST_DELTA), this, mutable_pos);
					//mutable_pos_move(mutable_pos2, EAST_TO_MID_DELTA);
					mutable_pos_move(mutable_pos, DOWN_TO_MID_DELTA);
				}
				//mutable_pos_move(mutable_pos2, DOWN_TO_MID_DELTA);
			//}
			
			// NORTH
			
			goto_block(EndNorthUpdates) {
				if (/* neighbor_direction != NORTH_ORDINAL && */ has_north_connection(connections)
					//&& !(NORTH_IS_WIRE() && north_power > highest_power)
				) {
					level.neighborChanged(mutable_pos_move(mutable_pos2, mutable_pos, NORTH_OFFSETS), this, mutable_pos);
					if ((connections & (NORTH_CONDUCTIVE | NORTH_UP)) != 0) {
						if (has_north_up_connection(connections)
							//&& north_power > highest_power
						) {
							goto(EndNorthUpdates);
						}
						level.neighborChanged(mutable_pos_move(mutable_pos2, UP_OFFSETS), this, mutable_pos_move(mutable_pos, NORTH_OFFSETS));
						if (NORTH_IS_CONDUCTIVE()) {
							level.neighborChanged(mutable_pos_move(mutable_pos2, UP_TO_DOWN_DELTA), this, mutable_pos);
							level.neighborChanged(mutable_pos_move(mutable_pos2, DOWN_TO_NORTH_DELTA), this, mutable_pos);
							level.neighborChanged(mutable_pos_move(mutable_pos2, NORTH_TO_WEST_DELTA), this, mutable_pos);
							level.neighborChanged(mutable_pos_move(mutable_pos2, WEST_TO_EAST_DELTA), this, mutable_pos);
						}
						mutable_pos_move(mutable_pos, NORTH_TO_MID_DELTA);
					}
				}
			} goto_target(EndNorthUpdates);
			
			// SOUTH
			
			goto_block(EndSouthUpdates) {
				if (/* neighbor_direction != SOUTH_ORDINAL && */ has_south_connection(connections)
					//&& !(SOUTH_IS_WIRE() && south_power > highest_power)
				) {
					level.neighborChanged(mutable_pos_move(mutable_pos2, mutable_pos, SOUTH_OFFSETS), this, mutable_pos);
					if ((connections & (SOUTH_CONDUCTIVE | SOUTH_UP)) != 0) {
						if (has_south_up_connection(connections)
							//&& south_power > highest_power
						) {
							goto(EndSouthUpdates);
						}
						level.neighborChanged(mutable_pos_move(mutable_pos2, UP_OFFSETS), this, mutable_pos_move(mutable_pos, SOUTH_OFFSETS));
						if (SOUTH_IS_CONDUCTIVE()) {
							level.neighborChanged(mutable_pos_move(mutable_pos2, UP_TO_DOWN_DELTA), this, mutable_pos);
							level.neighborChanged(mutable_pos_move(mutable_pos2, DOWN_TO_SOUTH_DELTA), this, mutable_pos);
							level.neighborChanged(mutable_pos_move(mutable_pos2, SOUTH_TO_WEST_DELTA), this, mutable_pos);
							level.neighborChanged(mutable_pos_move(mutable_pos2, WEST_TO_EAST_DELTA), this, mutable_pos);
						}
						mutable_pos_move(mutable_pos, SOUTH_TO_MID_DELTA);
					}
				}
			} goto_target(EndSouthUpdates);
			
			// WEST
			
			goto_block(EndWestUpdates) {
				if (/* neighbor_direction != WEST_ORDINAL && */ has_west_connection(connections)
					//&& !(WEST_IS_WIRE() && west_power > highest_power)
				) {
					level.neighborChanged(mutable_pos_move(mutable_pos2, mutable_pos, WEST_OFFSETS), this, mutable_pos);
					if ((connections & (WEST_CONDUCTIVE | WEST_UP)) != 0) {
						if (has_west_up_connection(connections)
							//&& west_power > highest_power
						) {
							goto(EndWestUpdates);
						}
						level.neighborChanged(mutable_pos_move(mutable_pos2, UP_OFFSETS), this, mutable_pos_move(mutable_pos, WEST_OFFSETS));
						if (WEST_IS_CONDUCTIVE()) {
							level.neighborChanged(mutable_pos_move(mutable_pos2, UP_TO_DOWN_DELTA), this, mutable_pos);
							level.neighborChanged(mutable_pos_move(mutable_pos2, DOWN_TO_NORTH_DELTA), this, mutable_pos);
							level.neighborChanged(mutable_pos_move(mutable_pos2, NORTH_TO_SOUTH_DELTA), this, mutable_pos);
							level.neighborChanged(mutable_pos_move(mutable_pos2, SOUTH_TO_WEST_DELTA), this, mutable_pos);
						}
						mutable_pos_move(mutable_pos, WEST_TO_MID_DELTA);
					}
				}
			} goto_target(EndWestUpdates);
			
			// EAST
			
			//goto_block(EndEastUpdates) {
				if (/* neighbor_direction != EAST_ORDINAL && */ has_east_connection(connections)
					//&& !(EAST_IS_WIRE() && east_power > highest_power)
				) {
					level.neighborChanged(mutable_pos_move(mutable_pos2, mutable_pos, EAST_OFFSETS), this, mutable_pos);
					if ((connections & (EAST_CONDUCTIVE | EAST_UP)) != 0) {
						if (has_east_up_connection(connections)
							//&& east_power > highest_power
						) {
							return;
							//goto(EndEastUpdates);
						}
						level.neighborChanged(mutable_pos_move(mutable_pos2, UP_OFFSETS), this, mutable_pos_move(mutable_pos, EAST_OFFSETS));
						if (EAST_IS_CONDUCTIVE()) {
							level.neighborChanged(mutable_pos_move(mutable_pos2, UP_TO_DOWN_DELTA), this, mutable_pos);
							level.neighborChanged(mutable_pos_move(mutable_pos2, DOWN_TO_NORTH_DELTA), this, mutable_pos);
							level.neighborChanged(mutable_pos_move(mutable_pos2, NORTH_TO_SOUTH_DELTA), this, mutable_pos);
							level.neighborChanged(mutable_pos_move(mutable_pos2, SOUTH_TO_EAST_DELTA), this, mutable_pos);
						}
						mutable_pos_move(mutable_pos, EAST_TO_MID_DELTA);
					}
				}
			//} goto_target(EndEastUpdates);
		}
	}

	private void updatePowerStrength(Level level, BlockPos.MutableBlockPos mutable_pos, BlockState blockState, int flags, BlockPos neighbor_pos) {
#define highest_power temp_int
#define connections temp_int
		int temp_int;
		int neighbor_direction = YeetUtil.calculate_neighbor_direction(mutable_pos, neighbor_pos);
		BlockPos.MutableBlockPos mutable_pos2 = mutable_pos_create(mutable_pos);
		set_should_signal(false);
        highest_power = YeetUtil.get_best_neighbor_signal_unrolled(level, mutable_pos2, neighbor_direction);
		set_should_signal(true);
		mutable_pos_move(mutable_pos2, mutable_pos);
        if (highest_power < 14) {
			BlockState neighbor_state;
			mutable_pos_move(mutable_pos, mutable_pos2, UP_OFFSETS);
			boolean above_is_solid = PositionIsConductive(level, mutable_pos);
			
			for (int i = 0; i < 4; ++i) {
				mutable_pos_move(mutable_pos, mutable_pos2, YeetUtil.horizontal_direction_offsets[i]);
				neighbor_state = level.getBlockState(mutable_pos);
				if (StateIsWire(neighbor_state)) {
					highest_power = Math.max(highest_power, power_after_resistance(neighbor_state.getValue(POWER)));
				}
				else if (neighbor_state.isRedstoneConductor(level, mutable_pos)) {
					if (!above_is_solid && StateIsWire(neighbor_state = level.getBlockState(mutable_pos_move(mutable_pos, UP_OFFSETS)))) {
						highest_power = Math.max(highest_power, power_after_resistance(neighbor_state.getValue(POWER)));
					}
				}
				else if (StateIsWire(neighbor_state = level.getBlockState(mutable_pos_move(mutable_pos, DOWN_OFFSETS)))) {
					highest_power = Math.max(highest_power, power_after_resistance(neighbor_state.getValue(POWER)));
				}
				if (highest_power == 14) break;
			}
        }
        if (blockState.getValue(POWER) != highest_power) {
            //if (level.getBlockState(mutable_pos2) == blockState) { // Why is this here?
                level.setBlock(mutable_pos2, blockState.setValue(POWER, highest_power), flags);
            //}
			mutable_pos_move(mutable_pos, mutable_pos2, DOWN_OFFSETS);
			
			mutable_pos_move(mutable_pos2, DOWN_OFFSETS);
			if (PositionIsConductive(level, mutable_pos2)) {
				level.neighborChanged(mutable_pos_move(mutable_pos, mutable_pos2, WEST_OFFSETS), this, mutable_pos2);
				level.neighborChanged(mutable_pos_move(mutable_pos, WEST_TO_EAST_DELTA), this, mutable_pos2);
				level.neighborChanged(mutable_pos_move(mutable_pos, EAST_TO_DOWN_DELTA), this, mutable_pos2);
				level.neighborChanged(mutable_pos_move(mutable_pos, DOWN_TO_NORTH_DELTA), this, mutable_pos2);
				level.neighborChanged(mutable_pos_move(mutable_pos, NORTH_TO_SOUTH_DELTA), this, mutable_pos2);
			}
			connections = blockState.getValue(CONNECTIONS);
			mutable_pos_move(mutable_pos2, DOWN_TO_NORTH_DELTA);
			if (has_north_connection(connections) && PositionIsConductive(level, mutable_pos2)) {
				//YeetUtil.update_neighbors(level, mutable_pos, this, mutable_pos_move(mutable_pos2, mutable_pos));
				level.neighborChanged(mutable_pos_move(mutable_pos, mutable_pos2, WEST_OFFSETS), this, mutable_pos2);
				level.neighborChanged(mutable_pos_move(mutable_pos, WEST_TO_EAST_DELTA), this, mutable_pos2);
				level.neighborChanged(mutable_pos_move(mutable_pos, EAST_TO_DOWN_DELTA), this, mutable_pos2);
				level.neighborChanged(mutable_pos_move(mutable_pos, DOWN_TO_UP_DELTA), this, mutable_pos2);
				level.neighborChanged(mutable_pos_move(mutable_pos, UP_TO_NORTH_DELTA), this, mutable_pos2);
			}
			mutable_pos_move(mutable_pos2, NORTH_TO_SOUTH_DELTA);
			if (has_south_connection(connections) && PositionIsConductive(level, mutable_pos2)) {
				//YeetUtil.update_neighbors(level, mutable_pos, this, mutable_pos_move(mutable_pos2, mutable_pos));
				level.neighborChanged(mutable_pos_move(mutable_pos, mutable_pos2, WEST_OFFSETS), this, mutable_pos2);
				level.neighborChanged(mutable_pos_move(mutable_pos, WEST_TO_EAST_DELTA), this, mutable_pos2);
				level.neighborChanged(mutable_pos_move(mutable_pos, EAST_TO_DOWN_DELTA), this, mutable_pos2);
				level.neighborChanged(mutable_pos_move(mutable_pos, DOWN_TO_UP_DELTA), this, mutable_pos2);
				level.neighborChanged(mutable_pos_move(mutable_pos, UP_TO_SOUTH_DELTA), this, mutable_pos2);
			}
			mutable_pos_move(mutable_pos2, SOUTH_TO_WEST_DELTA);
			if (has_west_connection(connections) && PositionIsConductive(level, mutable_pos2)) {
				//YeetUtil.update_neighbors(level, mutable_pos, this, mutable_pos_move(mutable_pos2, mutable_pos));
				level.neighborChanged(mutable_pos_move(mutable_pos, mutable_pos2, WEST_OFFSETS), this, mutable_pos2);
				level.neighborChanged(mutable_pos_move(mutable_pos, WEST_TO_DOWN_DELTA), this, mutable_pos2);
				level.neighborChanged(mutable_pos_move(mutable_pos, DOWN_TO_UP_DELTA), this, mutable_pos2);
				level.neighborChanged(mutable_pos_move(mutable_pos, UP_TO_NORTH_DELTA), this, mutable_pos2);
				level.neighborChanged(mutable_pos_move(mutable_pos, NORTH_TO_SOUTH_DELTA), this, mutable_pos2);
			}
			mutable_pos_move(mutable_pos2, WEST_TO_EAST_DELTA);
			if (has_east_connection(connections) && PositionIsConductive(level, mutable_pos2)) {
				//YeetUtil.update_neighbors(level, mutable_pos, this, mutable_pos_move(mutable_pos2, mutable_pos));
				level.neighborChanged(mutable_pos_move(mutable_pos, mutable_pos2, EAST_OFFSETS), this, mutable_pos2);
				level.neighborChanged(mutable_pos_move(mutable_pos, EAST_TO_DOWN_DELTA), this, mutable_pos2);
				level.neighborChanged(mutable_pos_move(mutable_pos, DOWN_TO_UP_DELTA), this, mutable_pos2);
				level.neighborChanged(mutable_pos_move(mutable_pos, UP_TO_NORTH_DELTA), this, mutable_pos2);
				level.neighborChanged(mutable_pos_move(mutable_pos, NORTH_TO_SOUTH_DELTA), this, mutable_pos2);
			}
			if (has_up_connection(connections)) {
				//YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos2, EAST_TO_UP_DELTA), this, mutable_pos_move(mutable_pos, mutable_pos2));
				mutable_pos_move(mutable_pos2, EAST_TO_UP_DELTA);
				level.neighborChanged(mutable_pos_move(mutable_pos, mutable_pos2, WEST_OFFSETS), this, mutable_pos2);
				level.neighborChanged(mutable_pos_move(mutable_pos, WEST_TO_EAST_DELTA), this, mutable_pos2);
				level.neighborChanged(mutable_pos_move(mutable_pos, EAST_TO_UP_DELTA), this, mutable_pos2);
				level.neighborChanged(mutable_pos_move(mutable_pos, UP_TO_NORTH_DELTA), this, mutable_pos2);
				level.neighborChanged(mutable_pos_move(mutable_pos, NORTH_TO_SOUTH_DELTA), this, mutable_pos2);
			}
		}
#undef highest_power
#undef connections
	}
	
	private void updatePowerStrength(Level level, BlockPos.MutableBlockPos mutable_pos, BlockState blockState, int flags) {
#if 0
		if (blockState != level.getBlockState(mutable_pos)) {
			return;
		}
		int highest_power = 0;
		BlockPos blockPos = mutable_pos.immutable();
		mutable_pos_move(mutable_pos, blockPos, UP_OFFSETS);
		boolean above_is_solid = PositionIsConductive(level, mutable_pos);
		for (int i = 0; i < 6; ++i) {
			BlockState neighbor_state = level.getBlockState(mutable_pos.setWithOffset(blockPos, YeetUtil.all_direction_offsets[i]));
			boolean is_wire = StateIsWire(neighbor_state);
			if (i >= 2 || !is_wire) {
#if 0
				int power = level.getSignal(mutable_pos, YeetUtil.ALL_DIRECTIONS[i]);
				if (is_wire) {
					power -= RESISTANCE;
				}
				else if (neighbor_state.isRedstoneConductor(level, mutable_pos)) {
					if (!above_is_solid && StateIsWire(neighbor_state = level.getBlockState(mutable_pos_move(mutable_pos, UP_OFFSETS)))) {
						power = Math.max(power, neighbor_state.getValue(POWER) - RESISTANCE);
					}
				}
				else if (StateIsWire(neighbor_state = level.getBlockState(mutable_pos_move(mutable_pos, DOWN_OFFSETS)))) {
					power = Math.max(power, neighbor_state.getValue(POWER) - RESISTANCE);
				}
#else
				int power;
				if (is_wire) {
					power = neighbor_state.getValue(POWER) - RESISTANCE;
				}
				else {
					power = level.getSignal(mutable_pos, YeetUtil.ALL_DIRECTIONS[i]);
					if (neighbor_state.isRedstoneConductor(level, mutable_pos)) {
						if (!above_is_solid && StateIsWire(neighbor_state = level.getBlockState(mutable_pos_move(mutable_pos, UP_OFFSETS)))) {
							power = Math.max(power, neighbor_state.getValue(POWER) - RESISTANCE);
						}
					}
					else if (StateIsWire(neighbor_state = level.getBlockState(mutable_pos_move(mutable_pos, DOWN_OFFSETS)))) {
						power = Math.max(power, neighbor_state.getValue(POWER) - RESISTANCE);
					}
				}
#endif
				highest_power = Math.max(highest_power, power);
				if (highest_power >= MAX_REDSTONE_POWER) {
					break;
				}
			}
		}
		highest_power &= MAX_REDSTONE_POWER;
		if (highest_power != blockState.getValue(POWER)) {
			level.setBlock(blockPos, blockState.setValue(POWER, highest_power), flags);
			int connections = blockState.getValue(CONNECTIONS);
			BlockPos.MutableBlockPos mutable_pos2 = mutable_pos_create();
			for (int i = 0; i < 4; ++i) {
				if ((connections & BASE_MASK) != 0) {
					mutable_pos.setWithOffset(blockPos, YeetUtil.all_direction_offsets[i + 2]);
					if (PositionIsConductive(level,mutable_pos)) {
						YeetUtil.update_neighbors(level, mutable_pos, this, mutable_pos_move(mutable_pos2, mutable_pos));
					}
				}
				connections >>>= BITS_PER_DIR;
			}
		}
#else
		set_should_signal(false);
        int highest_power = level.getBestNeighborSignal(mutable_pos);
		set_should_signal(true);
		BlockPos.MutableBlockPos mutable_pos2 = mutable_pos_create(mutable_pos);
        if (highest_power < 14) {
			BlockState neighbor_state;
			mutable_pos_move(mutable_pos, mutable_pos2, UP_OFFSETS);
			boolean above_is_solid = PositionIsConductive(level,mutable_pos);
			
			for (int i = 0; i < 4; ++i) {
				mutable_pos_move(mutable_pos, mutable_pos2, YeetUtil.horizontal_direction_offsets[i]);
				neighbor_state = level.getBlockState(mutable_pos);
				if (StateIsWire(neighbor_state)) {
					highest_power = Math.max(highest_power, power_after_resistance(neighbor_state.getValue(POWER)));
				}
				
				if (neighbor_state.isRedstoneConductor(level, mutable_pos)) {
					if (!above_is_solid && StateIsWire(neighbor_state = level.getBlockState(mutable_pos_move(mutable_pos, UP_OFFSETS)))) {
						highest_power = Math.max(highest_power, power_after_resistance(neighbor_state.getValue(POWER)));
					}
				}
				else if (StateIsWire(neighbor_state = level.getBlockState(mutable_pos_move(mutable_pos, DOWN_OFFSETS)))) {
					highest_power = Math.max(highest_power, power_after_resistance(neighbor_state.getValue(POWER)));
				}
				if (highest_power == 14) break;
			}
        }
        if (blockState.getValue(POWER) != highest_power) {
            //if (level.getBlockState(mutable_pos2) == blockState) { // Why is this here?
                level.setBlock(mutable_pos2, blockState.setValue(POWER, highest_power), flags);
            //}
			mutable_pos_move(mutable_pos, mutable_pos2, DOWN_OFFSETS);
			if (PositionIsConductive(level, mutable_pos)) {
				YeetUtil.update_neighbors(level, mutable_pos, this, mutable_pos_move(mutable_pos2, mutable_pos));
			}
			int connections = blockState.getValue(CONNECTIONS);
			mutable_pos_move(mutable_pos, DOWN_TO_NORTH_DELTA);
			if (has_north_connection(connections) && PositionIsConductive(level, mutable_pos)) {
				YeetUtil.update_neighbors(level, mutable_pos, this, mutable_pos_move(mutable_pos2, mutable_pos));
			}
			mutable_pos_move(mutable_pos, NORTH_TO_SOUTH_DELTA);
			if (has_south_connection(connections) && PositionIsConductive(level, mutable_pos)) {
				YeetUtil.update_neighbors(level, mutable_pos, this, mutable_pos_move(mutable_pos2, mutable_pos));
			}
			mutable_pos_move(mutable_pos, SOUTH_TO_WEST_DELTA);
			if (has_west_connection(connections) && PositionIsConductive(level, mutable_pos)) {
				YeetUtil.update_neighbors(level, mutable_pos, this, mutable_pos_move(mutable_pos2, mutable_pos));
			}
			mutable_pos_move(mutable_pos, WEST_TO_EAST_DELTA);
			if (has_east_connection(connections) && PositionIsConductive(level, mutable_pos)) {
				YeetUtil.update_neighbors(level, mutable_pos, this, mutable_pos_move(mutable_pos2, mutable_pos));
			}
			if (has_up_connection(connections)) {
				YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, EAST_TO_UP_DELTA), this, mutable_pos_move(mutable_pos2, mutable_pos));
			}
			//if (())
			/* YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, DOWN_OFFSETS), this);
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, DOWN_TO_UP_DELTA), this);
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, UP_TO_NORTH_DELTA), this);
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, NORTH_TO_SOUTH_DELTA), this);
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, SOUTH_TO_WEST_DELTA), this);
			YeetUtil.update_neighbors(level, mutable_pos_move(mutable_pos, WEST_TO_EAST_DELTA), this); */
        }
#endif
    }
}