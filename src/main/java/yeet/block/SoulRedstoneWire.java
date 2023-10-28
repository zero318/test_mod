package yeet.block;

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
public class SoulRedstoneWire extends Block {
 private static int update_iters = 0;
 private static final VoxelShape[] SHAPES_LUT = new VoxelShape[171];
 static {
  VoxelShape shape_dot = SHAPES_LUT[0] = Block.box(3.0, 0.0, 3.0, 13.0, 1.0, 13.0);
  for (int i = 1; i < 171; ++i) {
   int side_count = (Integer.bitCount(((int)(i))));
   if (side_count != 1 && side_count <= 4) {
    VoxelShape shape = shape_dot;
    if ((((i) & (byte)(0x03)) != 0)) {
     shape = Shapes.or(shape, Block.box(3.0, 0.0, 0.0, 13.0, 1.0, 13.0));
     if ((i & (0x02)) != 0) {
      shape = Shapes.or(shape, Block.box(3.0, 0.0, 0.0, 13.0, 16.0, 1.0));
     }
    }
    if ((((i) & (byte)(0x0C)) != 0)) {
     shape = Shapes.or(shape, Block.box(3.0, 0.0, 3.0, 13.0, 1.0, 16.0));
     if ((i & (0x08)) != 0) {
      shape = Shapes.or(shape, Block.box(3.0, 0.0, 15.0, 13.0, 16.0, 16.0));
     }
    }
    if ((((i) & (byte)(0x30)) != 0)) {
     shape = Shapes.or(shape, Block.box(0.0, 0.0, 3.0, 13.0, 1.0, 13.0));
     if ((i & (0x20)) != 0) {
      shape = Shapes.or(shape, Block.box(0.0, 0.0, 3.0, 1.0, 16.0, 13.0));
     }
    }
    if (((i) > 0x3F)) {
     shape = Shapes.or(shape, Block.box(3.0, 0.0, 3.0, 16.0, 1.0, 13.0));
     if ((i & (0x80)) != 0) {
      shape = Shapes.or(shape, Block.box(15.0, 0.0, 3.0, 16.0, 16.0, 13.0));
     }
    }
    SHAPES_LUT[i] = shape;
   }
  }
 }
 public static final SparseIntegerProperty CONNECTIONS = CustomStates.CONNECTIONS;
 private static RedStoneWireBlock redstone_wire_block = (RedStoneWireBlock)Blocks.REDSTONE_WIRE;
 public static final IntegerProperty POWER = BlockStateProperties.POWER;
 public SoulRedstoneWire(BlockBehaviour.Properties properties) {
        super(properties);
  this.registerDefaultState(
   this.stateDefinition.any()
    .setValue(POWER, 0)
    .setValue(CONNECTIONS, (0x00))
  );
 }
 @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWER, CONNECTIONS);
    }
 private boolean canSurviveOn(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
        return blockState.isFaceSturdy(blockGetter, blockPos, Direction.UP) || blockState.is(Blocks.HOPPER);
    }
 @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        BlockPos blockPos2 = blockPos.below();
        BlockState blockState2 = levelReader.getBlockState(blockPos2);
        return this.canSurviveOn(levelReader, blockPos2, blockState2);
    }
 @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return this.defaultBlockState().setValue(CONNECTIONS, this.getConnectionState(blockPlaceContext.getLevel(), ((0x01) | (0x04) | (0x10) | (0x40)), blockPlaceContext.getClickedPos()));
    }
 @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (!level.isClientSide && !blockState2.is(blockState.getBlock())) {
   BlockPos.MutableBlockPos mutable_pos = (new BlockPos.MutableBlockPos((blockPos).getX(),(blockPos).getY(),(blockPos).getZ()));
            this.updatePowerStrength2(level, mutable_pos, blockState, 0x02 | 0x80, mutable_pos);
   YeetUtil.update_neighbors(level, (mutable_pos).setX((blockPos).getX()).setY((blockPos).getY()+(1)).setZ((blockPos).getZ()), this);
   YeetUtil.update_neighbors(level, (mutable_pos).setY((mutable_pos).getY()+(-2)), this);
   this.updateNeighborsOfNeighboringWires(level, (mutable_pos).setY((mutable_pos).getY()+(1)));
        }
    }
    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (!bl && !blockState.is(blockState2.getBlock())) {
            super.onRemove(blockState, level, blockPos, blockState2, false);
   if (!level.isClientSide) {
    BlockPos.MutableBlockPos mutable_pos = (new BlockPos.MutableBlockPos((blockPos).getX(),(blockPos).getY()+(-1),(blockPos).getZ()));
    BlockPos.MutableBlockPos mutable_pos2 = (new BlockPos.MutableBlockPos((mutable_pos).getX(),(mutable_pos).getY(),(mutable_pos).getZ()));
    YeetUtil.update_neighbors(level, mutable_pos, this, mutable_pos2);
    YeetUtil.update_neighbors(level, (mutable_pos).setY((mutable_pos).getY()+(2)), this, (mutable_pos2).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
    YeetUtil.update_neighbors(level, (mutable_pos).setY((mutable_pos).getY()+(-1)).setZ((mutable_pos).getZ()+(-1)), this, (mutable_pos2).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
    YeetUtil.update_neighbors(level, (mutable_pos).setZ((mutable_pos).getZ()+(2)), this, (mutable_pos2).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
    YeetUtil.update_neighbors(level, (mutable_pos).setX((mutable_pos).getX()+(-1)).setZ((mutable_pos).getZ()+(-1)), this, (mutable_pos2).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
    YeetUtil.update_neighbors(level, (mutable_pos).setX((mutable_pos).getX()+(2)), this, (mutable_pos2).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
    this.updatePowerStrength2(level, (mutable_pos).setX((mutable_pos).getX()+(-1)), blockState, 0x02 | 0x80, mutable_pos);
    this.updateNeighborsOfNeighboringWires(level, (mutable_pos).setX((blockPos).getX()).setY((blockPos).getY()).setZ((blockPos).getZ()));
   }
        }
    }
 @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos self_pos, Block block, BlockPos updated_pos, boolean bl) {
        if (!level.isClientSide) {
            if (blockState.canSurvive(level, self_pos)) {
    this.updatePowerStrength2(level, (new BlockPos.MutableBlockPos((self_pos).getX(),(self_pos).getY(),(self_pos).getZ())), blockState, 0x02 | 0x10 | 0x80, updated_pos);
   } else {
    RedStoneWireBlock.dropResources(blockState, level, self_pos);
    level.removeBlock(self_pos, false);
   }
        }
    }
 @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return (redstone_wire_block.shouldSignal) ? blockState.getSignal(blockGetter, blockPos, direction) : 0;
    }
 @Override
    public boolean isSignalSource(BlockState blockState) {
        return (redstone_wire_block.shouldSignal);
    }
 private void updateNeighborsOfNeighboringWires(Level level, BlockPos.MutableBlockPos mutable_pos) {
        BlockPos.MutableBlockPos mutable_pos2 = (new BlockPos.MutableBlockPos((mutable_pos).getX(),(mutable_pos).getY(),(mutable_pos).getZ()));
  BlockPos.MutableBlockPos mutable_pos3 = (new BlockPos.MutableBlockPos(0,0,0));
  boolean had_north;
  if (had_north = ((level.getBlockState((mutable_pos).setZ((mutable_pos).getZ()+(-1)))).is(BlockRegistration.REDSTONE_WIRES_TAG))) {
   YeetUtil.update_neighbors(level, (mutable_pos).setY((mutable_pos).getY()+(-1)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setY((mutable_pos).getY()+(2)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setY((mutable_pos).getY()+(-1)).setZ((mutable_pos).getZ()+(-1)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setX((mutable_pos).getX()+(-1)).setZ((mutable_pos).getZ()+(1)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setX((mutable_pos).getX()+(2)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   (mutable_pos).setX((mutable_pos).getX()+(-1));
  }
  boolean had_east;
  if (had_east = ((level.getBlockState((mutable_pos).setX((mutable_pos).getX()+(1)).setZ((mutable_pos).getZ()+(1)))).is(BlockRegistration.REDSTONE_WIRES_TAG))) {
   YeetUtil.update_neighbors(level, (mutable_pos).setY((mutable_pos).getY()+(-1)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setY((mutable_pos).getY()+(2)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   if (!had_north) YeetUtil.update_neighbors(level, (mutable_pos).setY((mutable_pos).getY()+(-1)).setZ((mutable_pos).getZ()+(-1)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setX((mutable_pos2).getX()+(1)).setY((mutable_pos2).getY()).setZ((mutable_pos2).getZ()+(1)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setX((mutable_pos).getX()+(1)).setZ((mutable_pos).getZ()+(-1)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   (mutable_pos).setX((mutable_pos).getX()+(-1));
  }
  boolean had_south;
  if (had_south = ((level.getBlockState((mutable_pos).setX((mutable_pos).getX()+(-1)).setZ((mutable_pos).getZ()+(1)))).is(BlockRegistration.REDSTONE_WIRES_TAG))) {
   YeetUtil.update_neighbors(level, (mutable_pos).setY((mutable_pos).getY()+(-1)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setY((mutable_pos).getY()+(2)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setY((mutable_pos).getY()+(-1)).setZ((mutable_pos).getZ()+(1)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setX((mutable_pos).getX()+(-1)).setZ((mutable_pos).getZ()+(-1)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   if (!had_east) YeetUtil.update_neighbors(level, (mutable_pos).setX((mutable_pos).getX()+(2)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
  }
  boolean had_west;
  if (had_west = ((level.getBlockState((mutable_pos).setX((mutable_pos2).getX()+(-1)).setY((mutable_pos2).getY()).setZ((mutable_pos2).getZ()))).is(BlockRegistration.REDSTONE_WIRES_TAG))) {
   YeetUtil.update_neighbors(level, (mutable_pos).setY((mutable_pos).getY()+(-1)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setY((mutable_pos).getY()+(2)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   if (!had_north) YeetUtil.update_neighbors(level, (mutable_pos).setY((mutable_pos).getY()+(-1)).setZ((mutable_pos).getZ()+(-1)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   if (!had_south) YeetUtil.update_neighbors(level, (mutable_pos).setX((mutable_pos2).getX()+(-1)).setY((mutable_pos2).getY()).setZ((mutable_pos2).getZ()+(1)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setX((mutable_pos2).getX()+(-2)).setY((mutable_pos2).getY()).setZ((mutable_pos2).getZ()), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
  }
  (mutable_pos2).setZ((mutable_pos2).getZ()+(-1));
  if (!had_north && ((level.getBlockState((mutable_pos).setX((mutable_pos2).getX()).setY((mutable_pos2).getY()+((((level).getBlockState(mutable_pos2).isRedstoneConductor((level), (mutable_pos2)))) ? 1 : -1)).setZ((mutable_pos2).getZ()))).is(BlockRegistration.REDSTONE_WIRES_TAG))) {
   YeetUtil.update_neighbors(level, (mutable_pos).setY((mutable_pos).getY()+(-1)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setY((mutable_pos).getY()+(2)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setY((mutable_pos).getY()+(-1)).setZ((mutable_pos).getZ()+(-1)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setZ((mutable_pos).getZ()+(2)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setX((mutable_pos).getX()+(-1)).setZ((mutable_pos).getZ()+(-1)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setX((mutable_pos).getX()+(2)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
  }
  (mutable_pos2).setX((mutable_pos2).getX()+(1)).setZ((mutable_pos2).getZ()+(1));
  if (!had_east && ((level.getBlockState((mutable_pos).setX((mutable_pos2).getX()).setY((mutable_pos2).getY()+((((level).getBlockState(mutable_pos2).isRedstoneConductor((level), (mutable_pos2)))) ? 1 : -1)).setZ((mutable_pos2).getZ()))).is(BlockRegistration.REDSTONE_WIRES_TAG))) {
   YeetUtil.update_neighbors(level, (mutable_pos).setY((mutable_pos).getY()+(-1)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setY((mutable_pos).getY()+(2)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setY((mutable_pos).getY()+(-1)).setZ((mutable_pos).getZ()+(-1)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setZ((mutable_pos).getZ()+(2)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setX((mutable_pos).getX()+(-1)).setZ((mutable_pos).getZ()+(-1)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setX((mutable_pos).getX()+(2)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
  }
  (mutable_pos2).setX((mutable_pos2).getX()+(-1)).setZ((mutable_pos2).getZ()+(1));
  if (!had_south && ((level.getBlockState((mutable_pos).setX((mutable_pos2).getX()).setY((mutable_pos2).getY()+((((level).getBlockState(mutable_pos2).isRedstoneConductor((level), (mutable_pos2)))) ? 1 : -1)).setZ((mutable_pos2).getZ()))).is(BlockRegistration.REDSTONE_WIRES_TAG))) {
   YeetUtil.update_neighbors(level, (mutable_pos).setY((mutable_pos).getY()+(-1)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setY((mutable_pos).getY()+(2)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setY((mutable_pos).getY()+(-1)).setZ((mutable_pos).getZ()+(-1)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setZ((mutable_pos).getZ()+(2)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setX((mutable_pos).getX()+(-1)).setZ((mutable_pos).getZ()+(-1)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setX((mutable_pos).getX()+(2)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
  }
  (mutable_pos2).setX((mutable_pos2).getX()+(-1)).setZ((mutable_pos2).getZ()+(-1));
  if (!had_west && ((level.getBlockState((mutable_pos).setX((mutable_pos2).getX()).setY((mutable_pos2).getY()+((((level).getBlockState(mutable_pos2).isRedstoneConductor((level), (mutable_pos2)))) ? 1 : -1)).setZ((mutable_pos2).getZ()))).is(BlockRegistration.REDSTONE_WIRES_TAG))) {
   YeetUtil.update_neighbors(level, (mutable_pos).setY((mutable_pos).getY()+(-1)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setY((mutable_pos).getY()+(2)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setY((mutable_pos).getY()+(-1)).setZ((mutable_pos).getZ()+(-1)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setZ((mutable_pos).getZ()+(2)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setX((mutable_pos).getX()+(-1)).setZ((mutable_pos).getZ()+(-1)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   YeetUtil.update_neighbors(level, (mutable_pos).setX((mutable_pos).getX()+(2)), this, (mutable_pos3).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
  }
    }
 protected static boolean shouldConnectTo(BlockState blockState) {
        return SoulRedstoneWire.shouldConnectTo(blockState, null);
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
 public static int getColorForPower(int n) {
        return COLORS[n];
    }
 @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, Random random) {
 }
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
    return blockState.setValue(CONNECTIONS, connections * 4 + temp);
            }
            case CLOCKWISE_180: {
    temp = connections >>> 2 & 0x33;
    connections &= 0xCC;
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
   int number_of_connections = (Integer.bitCount(((int)(connections))));
   if (number_of_connections == 0 || number_of_connections == 4) {
    int new_connections = this.getConnectionState(level, connections, blockPos);
    connections ^= new_connections;
    if (connections != 0) {
     blockState = blockState.setValue(CONNECTIONS, new_connections);
     level.setBlock(blockPos, blockState, (0x01 | 0x02));
     BlockPos.MutableBlockPos mutable_pos = (new BlockPos.MutableBlockPos((blockPos).getX(),(blockPos).getY(),(blockPos).getZ()+(-1)));
     if ((Integer.bitCount(((int)(connections & (0x03))))) == 1 || ((level).getBlockState(mutable_pos).isRedstoneConductor((level), (mutable_pos)))) {
      level.updateNeighborsAtExceptFromFacing(mutable_pos, blockState.getBlock(), Direction.SOUTH);
     }
     (mutable_pos).setX((mutable_pos).getX()+(1)).setZ((mutable_pos).getZ()+(1));
     if ((Integer.bitCount(((int)(connections & (0xC0))))) == 1 || ((level).getBlockState(mutable_pos).isRedstoneConductor((level), (mutable_pos)))) {
      level.updateNeighborsAtExceptFromFacing(mutable_pos, blockState.getBlock(), Direction.WEST);
     }
     (mutable_pos).setX((mutable_pos).getX()+(-1)).setZ((mutable_pos).getZ()+(1));
     if ((Integer.bitCount(((int)(connections & (0x0C))))) == 1 || ((level).getBlockState(mutable_pos).isRedstoneConductor((level), (mutable_pos)))) {
      level.updateNeighborsAtExceptFromFacing(mutable_pos, blockState.getBlock(), Direction.NORTH);
     }
     (mutable_pos).setX((mutable_pos).getX()+(-1)).setZ((mutable_pos).getZ()+(-1));
     if ((Integer.bitCount(((int)(connections & (0x30))))) == 1 || ((level).getBlockState(mutable_pos).isRedstoneConductor((level), (mutable_pos)))) {
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
  int ret = (0);
  do {
   if (!above_is_solid && this.canSurviveOn(blockGetter, self_pos, neighbor_state) && SoulRedstoneWire.shouldConnectTo(blockGetter.getBlockState((mutable_pos).setX((self_pos).getX()).setY((self_pos).getY()+(1)).setZ((self_pos).getZ())))) {
    if (neighbor_state.isFaceSturdy(blockGetter, self_pos, direction.getOpposite())) {
     ret += (2);
     break;
    }
   }
   else if (!(SoulRedstoneWire.shouldConnectTo(neighbor_state, direction) || !neighbor_state.isRedstoneConductor(blockGetter, self_pos) && SoulRedstoneWire.shouldConnectTo(blockGetter.getBlockState((mutable_pos).setX((self_pos).getX()).setY((self_pos).getY()+(-1)).setZ((self_pos).getZ()))))) {
    break;
   }
   ret += (1);
  } while (false);
  return ret << ((direction.ordinal()) * 2 - 4);
    }
 private int getMissingConnections(BlockGetter blockGetter, BlockPos.MutableBlockPos mutable_pos) {
  boolean above_is_solid = blockGetter.getBlockState((mutable_pos).setY((mutable_pos).getY()+(1))).isRedstoneConductor(blockGetter, (mutable_pos).setY((mutable_pos).getY()+(-1)));
  int connections = (0x00);
  BlockPos.MutableBlockPos mutable_pos2 = (new BlockPos.MutableBlockPos(0,0,0));
  connections |= this.getConnectingSide(blockGetter, (mutable_pos).setZ((mutable_pos).getZ()+(-1)), Direction.NORTH, above_is_solid, mutable_pos2);
  connections |= this.getConnectingSide(blockGetter, (mutable_pos).setZ((mutable_pos).getZ()+(2)), Direction.SOUTH, above_is_solid, mutable_pos2);
  connections |= this.getConnectingSide(blockGetter, (mutable_pos).setX((mutable_pos).getX()+(-1)).setZ((mutable_pos).getZ()+(-1)), Direction.WEST, above_is_solid, mutable_pos2);
  connections |= this.getConnectingSide(blockGetter, (mutable_pos).setX((mutable_pos).getX()+(2)), Direction.EAST, above_is_solid, mutable_pos2);
        return connections;
    }
 private int getConnectionState(BlockGetter blockGetter, int connections, BlockPos self_pos) {
  boolean started_as_dot = ((connections) == (0x00));
  BlockPos.MutableBlockPos mutable_pos = (new BlockPos.MutableBlockPos((self_pos).getX(),(self_pos).getY(),(self_pos).getZ()));
        connections = this.getMissingConnections(blockGetter, mutable_pos);
        if (started_as_dot && ((connections) == (0x00))) {
   return (0x00);
        }
  int new_connections = connections;
  if (!(((connections) & (byte)~(0x0C)) != 0)) new_connections += (0x01);
  if (!((connections) > 0x03)) new_connections += (0x04);
  if (!(((connections) & (byte)~(0xC0)) != 0)) new_connections += (0x10);
  if (!(((connections) & (byte)~(0x30)) != 0)) new_connections += (0x40);
  return new_connections;
    }
 @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {
  int direction_int = direction.ordinal();
  if (direction_int != (0)) {
   int redstone_side;
   int connections = blockState.getValue(CONNECTIONS);
   if (direction_int == (1)) {
    redstone_side = this.getConnectionState(levelAccessor, connections, blockPos);
   }
   else {
    BlockPos.MutableBlockPos mutable_pos = (new BlockPos.MutableBlockPos((blockPos).getX(),(blockPos).getY()+(1),(blockPos).getZ()));
    boolean above_is_solid = levelAccessor.getBlockState(mutable_pos).isRedstoneConductor(levelAccessor, (mutable_pos).setY((mutable_pos).getY()+(-1)));
    redstone_side = this.getConnectingSide(levelAccessor, mutable_pos.move(YeetUtil.all_direction_offsets[direction_int]), direction, above_is_solid, (new BlockPos.MutableBlockPos(0,0,0)));
    direction_int = ((0x03) << ((direction_int) * 2 - 4)) & connections;
    if ((Integer.bitCount(((int)(connections)))) != 4 &&
     (Integer.bitCount(((int)(redstone_side ^ direction_int)))) != 1
    ) {
     redstone_side |= connections ^ direction_int;
    }
    else {
     redstone_side = this.getConnectionState(levelAccessor, ((0x01) | (0x04) | (0x10) | (0x40)), blockPos);
    }
   }
   if (redstone_side != connections) {
    blockState = blockState.setValue(CONNECTIONS, redstone_side);
   }
  }
  return blockState;
    }
 @Override
    public void updateIndirectNeighbourShapes(BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos, int n, int n2) {
        int connections = blockState.getValue(CONNECTIONS);
  if (!((connections) == (0x00))) {
   BlockPos.MutableBlockPos mutable_pos = (new BlockPos.MutableBlockPos(0,0,0));
   BlockPos below_pos = blockPos.below();
   BlockState below_state = levelAccessor.getBlockState(below_pos);
   BlockPos above_pos = blockPos.above();
   BlockState above_state = levelAccessor.getBlockState(above_pos);
   BlockState neighbor_state;
   if ((((connections) & (byte)(0x03)) != 0)) {
    if (!((levelAccessor.getBlockState((mutable_pos).setX((blockPos).getX()).setY((blockPos).getY()).setZ((blockPos).getZ()+(-1)))).is(BlockRegistration.REDSTONE_WIRES_TAG))) {
     neighbor_state = levelAccessor.getBlockState((mutable_pos).setY((mutable_pos).getY()+(-1)));
     if (!neighbor_state.is(Blocks.OBSERVER)) {
      RedStoneWireBlock.updateOrDestroy(neighbor_state,
       neighbor_state.updateShape(Direction.SOUTH, below_state, levelAccessor, mutable_pos, below_pos)
      , levelAccessor, mutable_pos, n, n2);
     }
    }
    neighbor_state = levelAccessor.getBlockState((mutable_pos).setX((blockPos).getX()).setY((blockPos).getY()+(1)).setZ((blockPos).getZ()+(-1)));
    if (!neighbor_state.is(Blocks.OBSERVER)) {
     RedStoneWireBlock.updateOrDestroy(neighbor_state,
      neighbor_state.updateShape(Direction.SOUTH, above_state, levelAccessor, mutable_pos, above_pos)
     , levelAccessor, mutable_pos, n, n2);
    }
   }
   if (((connections) > 0x3F)) {
    if (!((levelAccessor.getBlockState((mutable_pos).setX((blockPos).getX()+(1)).setY((blockPos).getY()).setZ((blockPos).getZ()))).is(BlockRegistration.REDSTONE_WIRES_TAG))) {
     neighbor_state = levelAccessor.getBlockState((mutable_pos).setY((mutable_pos).getY()+(-1)));
     if (!neighbor_state.is(Blocks.OBSERVER)) {
      RedStoneWireBlock.updateOrDestroy(neighbor_state,
       neighbor_state.updateShape(Direction.WEST, below_state, levelAccessor, mutable_pos, below_pos)
      , levelAccessor, mutable_pos, n, n2);
     }
    }
    neighbor_state = levelAccessor.getBlockState((mutable_pos).setX((blockPos).getX()+(1)).setY((blockPos).getY()+(1)).setZ((blockPos).getZ()));
    if (!neighbor_state.is(Blocks.OBSERVER)) {
     RedStoneWireBlock.updateOrDestroy(neighbor_state,
      neighbor_state.updateShape(Direction.WEST, above_state, levelAccessor, mutable_pos, above_pos)
     , levelAccessor, mutable_pos, n, n2);
    }
   }
   if ((((connections) & (byte)(0x0C)) != 0)) {
    if (!((levelAccessor.getBlockState((mutable_pos).setX((blockPos).getX()).setY((blockPos).getY()).setZ((blockPos).getZ()+(1)))).is(BlockRegistration.REDSTONE_WIRES_TAG))) {
     neighbor_state = levelAccessor.getBlockState((mutable_pos).setY((mutable_pos).getY()+(-1)));
     if (!neighbor_state.is(Blocks.OBSERVER)) {
      RedStoneWireBlock.updateOrDestroy(neighbor_state,
       neighbor_state.updateShape(Direction.NORTH, below_state, levelAccessor, mutable_pos, below_pos)
      , levelAccessor, mutable_pos, n, n2);
     }
    }
    neighbor_state = levelAccessor.getBlockState((mutable_pos).setX((blockPos).getX()).setY((blockPos).getY()+(1)).setZ((blockPos).getZ()+(1)));
    if (!neighbor_state.is(Blocks.OBSERVER)) {
     RedStoneWireBlock.updateOrDestroy(neighbor_state,
      neighbor_state.updateShape(Direction.NORTH, above_state, levelAccessor, mutable_pos, above_pos)
     , levelAccessor, mutable_pos, n, n2);
    }
   }
   if ((((connections) & (byte)(0x30)) != 0)) {
    if (!((levelAccessor.getBlockState((mutable_pos).setX((blockPos).getX()+(-1)).setY((blockPos).getY()).setZ((blockPos).getZ()))).is(BlockRegistration.REDSTONE_WIRES_TAG))) {
     neighbor_state = levelAccessor.getBlockState((mutable_pos).setY((mutable_pos).getY()+(-1)));
     if (!neighbor_state.is(Blocks.OBSERVER)) {
      RedStoneWireBlock.updateOrDestroy(neighbor_state,
       neighbor_state.updateShape(Direction.EAST, below_state, levelAccessor, mutable_pos, below_pos)
      , levelAccessor, mutable_pos, n, n2);
     }
    }
    neighbor_state = levelAccessor.getBlockState((mutable_pos).setX((blockPos).getX()+(-1)).setY((blockPos).getY()+(1)).setZ((blockPos).getZ()));
    if (!neighbor_state.is(Blocks.OBSERVER)) {
     RedStoneWireBlock.updateOrDestroy(neighbor_state,
      neighbor_state.updateShape(Direction.EAST, above_state, levelAccessor, mutable_pos, above_pos)
     , levelAccessor, mutable_pos, n, n2);
    }
   }
  }
    }
 @Override
    public int getSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
  if ((redstone_wire_block.shouldSignal)) {
   int direction_index = direction.ordinal();
   if (direction_index != (0)) {
    int power = blockState.getValue(POWER);
    if (power != 0 && (
      direction_index == (1) ||
      (((this.getConnectionState(blockGetter, blockState.getValue(CONNECTIONS), blockPos)) & ((0x03) << ((((direction_index) ^ 1)) * 2 - 4))) != 0)
     )
    ) {
     return power;
    }
   }
  }
  return 0;
    }
 private void updatePowerStrength2(Level level, BlockPos.MutableBlockPos mutable_pos, BlockState blockState, int flags, BlockPos neighbor_pos) {
  ++update_iters;
  if ((Integer.compareUnsigned(update_iters,10000)) > 0) {
   JankyHackMate.breakpoint();
  }
  redstone_wire_block.shouldSignal = (false);
  int north_power, south_power, west_power, east_power;
  int neighbor_direction = YeetUtil.calculate_neighbor_direction(mutable_pos, neighbor_pos);
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
  mutable_pos2 = (new BlockPos.MutableBlockPos((mutable_pos).getX(),(mutable_pos).getY()+(-1),(mutable_pos).getZ()));
  neighbor_state = level.getBlockState(mutable_pos2);
  highest_power = neighbor_state.getSignal(level, mutable_pos2, Direction.DOWN);
  if (neighbor_state.isRedstoneConductor(level, mutable_pos2)) {
   connections += 0x01000000;
   if ((highest_power < 15)) {
    highest_power = Math.max(highest_power, (level.getDirectSignal((mutable_pos2).setY((mutable_pos2).getY()+(-1)), Direction.DOWN)));
    highest_power = Math.max(highest_power, (level.getDirectSignal((mutable_pos2).setY((mutable_pos2).getY()+(1)).setZ((mutable_pos2).getZ()+(-1)), Direction.NORTH)));
    highest_power = Math.max(highest_power, (level.getDirectSignal((mutable_pos2).setZ((mutable_pos2).getZ()+(2)), Direction.SOUTH)));
    highest_power = Math.max(highest_power, (level.getDirectSignal((mutable_pos2).setX((mutable_pos2).getX()+(-1)).setZ((mutable_pos2).getZ()+(-1)), Direction.WEST)));
    highest_power = Math.max(highest_power, (level.getDirectSignal((mutable_pos2).setX((mutable_pos2).getX()+(2)), Direction.EAST)));
    (mutable_pos2).setX((mutable_pos2).getX()+(-1));
   }
  }
  neighbor_state = level.getBlockState((mutable_pos2).setY((mutable_pos2).getY()+(1)).setZ((mutable_pos2).getZ()+(-1)));
  highest_power = Math.max(highest_power, (neighbor_state.getSignal(level, mutable_pos2, Direction.NORTH)));
  EndNorth: do {
   if (neighbor_state.isRedstoneConductor(level, mutable_pos2)) {
    connections += (0x100000);
    if ((highest_power < 15)) {
     highest_power = Math.max(highest_power, (level.getDirectSignal((mutable_pos2).setY((mutable_pos2).getY()+(-1)), Direction.DOWN)));
     highest_power = Math.max(highest_power, (level.getDirectSignal((mutable_pos2).setY((mutable_pos2).getY()+(2)), Direction.UP)));
     highest_power = Math.max(highest_power, (level.getDirectSignal((mutable_pos2).setY((mutable_pos2).getY()+(-1)).setZ((mutable_pos2).getZ()+(-1)), Direction.NORTH)));
     highest_power = Math.max(highest_power, (level.getDirectSignal((mutable_pos2).setX((mutable_pos2).getX()+(-1)).setZ((mutable_pos2).getZ()+(1)), Direction.WEST)));
     highest_power = Math.max(highest_power, (level.getDirectSignal((mutable_pos2).setX((mutable_pos2).getX()+(2)), Direction.EAST)));
     (mutable_pos2).setX((mutable_pos2).getX()+(-1));
    }
   }
   else if ((((connections) & (byte)(0x03)) != 0)) {
    if (((neighbor_state).is(BlockRegistration.REDSTONE_WIRES_TAG))) {
     connections += (0x010000);
     neighbor_power = neighbor_state.getValue(POWER);
     if (((neighbor_direction & (0x10)) != 0) || neighbor_power <= prev_power) {
      highest_power = Math.max(highest_power, (((north_power = neighbor_power) - (1))));
     }
     break EndNorth;
    }
    if ((highest_power < 14)) {
     neighbor_state = level.getBlockState((mutable_pos2).setY((mutable_pos2).getY()+(-1)));
     if (((neighbor_state).is(BlockRegistration.REDSTONE_WIRES_TAG))) {
      neighbor_power = neighbor_state.getValue(POWER);
      if (((neighbor_direction & (0x10)) != 0) || neighbor_power <= prev_power) {
       highest_power = Math.max(highest_power, (((neighbor_power) - (1))));
      }
     }
     (mutable_pos2).setY((mutable_pos2).getY()+(1));
    }
   }
   if ((((connections) & (0x02)) != 0)) {
    neighbor_state = level.getBlockState((mutable_pos2).setY((mutable_pos2).getY()+(1)));
    if (((neighbor_state).is(BlockRegistration.REDSTONE_WIRES_TAG))) {
     neighbor_power = neighbor_state.getValue(POWER);
     if (((neighbor_direction & (0x10)) != 0) || neighbor_power <= prev_power) {
      highest_power = Math.max(highest_power, (((north_power = neighbor_power) - (1))));
     }
    }
    (mutable_pos2).setY((mutable_pos2).getY()+(-1));
   }
  } while(false);
  neighbor_state = level.getBlockState((mutable_pos2).setZ((mutable_pos2).getZ()+(2)));
  highest_power = Math.max(highest_power, (neighbor_state.getSignal(level, mutable_pos2, Direction.SOUTH)));
  EndSouth: do {
   if (neighbor_state.isRedstoneConductor(level, mutable_pos2)) {
    connections += (0x200000);
    if ((highest_power < 15)) {
     highest_power = Math.max(highest_power, (level.getDirectSignal((mutable_pos2).setY((mutable_pos2).getY()+(-1)), Direction.DOWN)));
     highest_power = Math.max(highest_power, (level.getDirectSignal((mutable_pos2).setY((mutable_pos2).getY()+(2)), Direction.UP)));
     highest_power = Math.max(highest_power, (level.getDirectSignal((mutable_pos2).setY((mutable_pos2).getY()+(-1)).setZ((mutable_pos2).getZ()+(1)), Direction.SOUTH)));
     highest_power = Math.max(highest_power, (level.getDirectSignal((mutable_pos2).setX((mutable_pos2).getX()+(-1)).setZ((mutable_pos2).getZ()+(-1)), Direction.WEST)));
     highest_power = Math.max(highest_power, (level.getDirectSignal((mutable_pos2).setX((mutable_pos2).getX()+(2)), Direction.EAST)));
     (mutable_pos2).setX((mutable_pos2).getX()+(-1));
    }
   }
   else if ((((connections) & (byte)(0x0C)) != 0)) {
    if (((neighbor_state).is(BlockRegistration.REDSTONE_WIRES_TAG))) {
     connections += (0x020000);
     neighbor_power = neighbor_state.getValue(POWER);
     if (((neighbor_direction & (0x20)) != 0) || neighbor_power <= prev_power) {
      highest_power = Math.max(highest_power, (((south_power = neighbor_power) - (1))));
     }
     break EndSouth;
    }
    if ((highest_power < 14)) {
     neighbor_state = level.getBlockState((mutable_pos2).setY((mutable_pos2).getY()+(-1)));
     if (((neighbor_state).is(BlockRegistration.REDSTONE_WIRES_TAG))) {
      neighbor_power = neighbor_state.getValue(POWER);
      if (((neighbor_direction & (0x20)) != 0) || neighbor_power <= prev_power) {
       highest_power = Math.max(highest_power, (((neighbor_power) - (1))));
      }
     }
     (mutable_pos2).setY((mutable_pos2).getY()+(1));
    }
   }
   if ((((connections) & (0x08)) != 0)) {
    neighbor_state = level.getBlockState((mutable_pos2).setY((mutable_pos2).getY()+(1)));
    if (((neighbor_state).is(BlockRegistration.REDSTONE_WIRES_TAG))) {
     neighbor_power = neighbor_state.getValue(POWER);
     if (((neighbor_direction & (0x20)) != 0) || neighbor_power <= prev_power) {
      highest_power = Math.max(highest_power, (((south_power = neighbor_power) - (1))));
     }
    }
    (mutable_pos2).setY((mutable_pos2).getY()+(-1));
   }
  } while(false);
  neighbor_state = level.getBlockState((mutable_pos2).setX((mutable_pos2).getX()+(-1)).setZ((mutable_pos2).getZ()+(-1)));
  highest_power = Math.max(highest_power, (neighbor_state.getSignal(level, mutable_pos2, Direction.WEST)));
  EndWest: do {
   if (neighbor_state.isRedstoneConductor(level, mutable_pos2)) {
    connections += (0x400000);
    if ((highest_power < 15)) {
     highest_power = Math.max(highest_power, (level.getDirectSignal((mutable_pos2).setY((mutable_pos2).getY()+(-1)), Direction.DOWN)));
     highest_power = Math.max(highest_power, (level.getDirectSignal((mutable_pos2).setY((mutable_pos2).getY()+(2)), Direction.UP)));
     highest_power = Math.max(highest_power, (level.getDirectSignal((mutable_pos2).setY((mutable_pos2).getY()+(-1)).setZ((mutable_pos2).getZ()+(-1)), Direction.NORTH)));
     highest_power = Math.max(highest_power, (level.getDirectSignal((mutable_pos2).setZ((mutable_pos2).getZ()+(2)), Direction.SOUTH)));
     highest_power = Math.max(highest_power, (level.getDirectSignal((mutable_pos2).setX((mutable_pos2).getX()+(-1)).setZ((mutable_pos2).getZ()+(-1)), Direction.WEST)));
     (mutable_pos2).setX((mutable_pos2).getX()+(1));
    }
   }
   else if ((((connections) & (byte)(0x30)) != 0)) {
    if (((neighbor_state).is(BlockRegistration.REDSTONE_WIRES_TAG))) {
     connections += (0x040000);
     neighbor_power = neighbor_state.getValue(POWER);
     if (((neighbor_direction & (0x01)) != 0) || neighbor_power <= prev_power) {
      highest_power = Math.max(highest_power, (((west_power = neighbor_power) - (1))));
     }
     break EndWest;
    }
    if ((highest_power < 14)) {
     neighbor_state = level.getBlockState((mutable_pos2).setY((mutable_pos2).getY()+(-1)));
     if (((neighbor_state).is(BlockRegistration.REDSTONE_WIRES_TAG))) {
      neighbor_power = neighbor_state.getValue(POWER);
      if (((neighbor_direction & (0x01)) != 0) || neighbor_power <= prev_power) {
       highest_power = Math.max(highest_power, (((neighbor_power) - (1))));
      }
     }
     (mutable_pos2).setY((mutable_pos2).getY()+(1));
    }
   }
   if ((((connections) & (0x20)) != 0)) {
    neighbor_state = level.getBlockState((mutable_pos2).setY((mutable_pos2).getY()+(1)));
    if (((neighbor_state).is(BlockRegistration.REDSTONE_WIRES_TAG))) {
     neighbor_power = neighbor_state.getValue(POWER);
     if (((neighbor_direction & (0x01)) != 0) || neighbor_power <= prev_power) {
      highest_power = Math.max(highest_power, (((west_power = neighbor_power) - (1))));
     }
    }
    (mutable_pos2).setY((mutable_pos2).getY()+(-1));
   }
  } while(false);
  neighbor_state = level.getBlockState((mutable_pos2).setX((mutable_pos2).getX()+(2)));
  highest_power = Math.max(highest_power, (neighbor_state.getSignal(level, mutable_pos2, Direction.EAST)));
  EndEast: do {
   if (neighbor_state.isRedstoneConductor(level, mutable_pos2)) {
    connections += (0x800000);
    if ((highest_power < 15)) {
     highest_power = Math.max(highest_power, (level.getDirectSignal((mutable_pos2).setY((mutable_pos2).getY()+(-1)), Direction.DOWN)));
     highest_power = Math.max(highest_power, (level.getDirectSignal((mutable_pos2).setY((mutable_pos2).getY()+(2)), Direction.UP)));
     highest_power = Math.max(highest_power, (level.getDirectSignal((mutable_pos2).setY((mutable_pos2).getY()+(-1)).setZ((mutable_pos2).getZ()+(-1)), Direction.NORTH)));
     highest_power = Math.max(highest_power, (level.getDirectSignal((mutable_pos2).setZ((mutable_pos2).getZ()+(2)), Direction.SOUTH)));
     highest_power = Math.max(highest_power, (level.getDirectSignal((mutable_pos2).setX((mutable_pos2).getX()+(1)).setZ((mutable_pos2).getZ()+(-1)), Direction.EAST)));
     (mutable_pos2).setX((mutable_pos2).getX()+(-1));
    }
   }
   else if (((connections) > 0x3F)) {
    if (((neighbor_state).is(BlockRegistration.REDSTONE_WIRES_TAG))) {
     connections += (0x080000);
     neighbor_power = neighbor_state.getValue(POWER);
     if (((neighbor_direction & (0x02)) != 0) || neighbor_power <= prev_power) {
      highest_power = Math.max(highest_power, (((east_power = neighbor_power) - (1))));
     }
     break EndEast;
    }
    if ((highest_power < 14)) {
     neighbor_state = level.getBlockState((mutable_pos2).setY((mutable_pos2).getY()+(-1)));
     if (((neighbor_state).is(BlockRegistration.REDSTONE_WIRES_TAG))) {
      neighbor_power = neighbor_state.getValue(POWER);
      if (((neighbor_direction & (0x02)) != 0) || neighbor_power <= prev_power) {
       highest_power = Math.max(highest_power, (((neighbor_power) - (1))));
      }
     }
     (mutable_pos2).setY((mutable_pos2).getY()+(1));
    }
   }
   if ((((connections) & (0x80)) != 0)) {
    neighbor_state = level.getBlockState((mutable_pos2).setY((mutable_pos2).getY()+(1)));
    if (((neighbor_state).is(BlockRegistration.REDSTONE_WIRES_TAG))) {
     neighbor_power = neighbor_state.getValue(POWER);
     if (((neighbor_direction & (0x02)) != 0) || neighbor_power <= prev_power) {
      highest_power = Math.max(highest_power, (((east_power = neighbor_power) - (1))));
     }
    }
    (mutable_pos2).setY((mutable_pos2).getY()+(-1));
   }
  } while(false);
  (mutable_pos2).setX((mutable_pos2).getX()+(-1)).setY((mutable_pos2).getY()+(1));
  if ((highest_power < 15)) {
   neighbor_state = level.getBlockState(mutable_pos2);
   highest_power = Math.max(highest_power, (neighbor_state.getSignal(level, mutable_pos2, Direction.UP)));
   if (neighbor_state.isRedstoneConductor(level, mutable_pos2)) {
    highest_power = Math.max(highest_power, (level.getDirectSignal((mutable_pos2).setY((mutable_pos2).getY()+(1)), Direction.UP)));
    highest_power = Math.max(highest_power, (level.getDirectSignal((mutable_pos2).setY((mutable_pos2).getY()+(-1)).setZ((mutable_pos2).getZ()+(-1)), Direction.NORTH)));
    highest_power = Math.max(highest_power, (level.getDirectSignal((mutable_pos2).setZ((mutable_pos2).getZ()+(2)), Direction.SOUTH)));
    highest_power = Math.max(highest_power, (level.getDirectSignal((mutable_pos2).setX((mutable_pos2).getX()+(-1)).setZ((mutable_pos2).getZ()+(-1)), Direction.WEST)));
    highest_power = Math.max(highest_power, (level.getDirectSignal((mutable_pos2).setX((mutable_pos2).getX()+(2)), Direction.EAST)));
    (mutable_pos2).setX((mutable_pos2).getX()+(-1));
   }
  }
  redstone_wire_block.shouldSignal = (true);
  highest_power &= 15;
  if (highest_power != prev_power) {
   level.setBlock((mutable_pos2).setY((mutable_pos2).getY()+(-1)), blockState.setValue(POWER, highest_power), flags);
    level.neighborChanged((mutable_pos2).setY((mutable_pos2).getY()+(-1)), this, mutable_pos);
    if (((connections & 0x01000000) != 0)) {
     level.neighborChanged((mutable_pos2).setY((mutable_pos2).getY()+(-1)), this, (mutable_pos).setY((mutable_pos).getY()+(-1)));
     level.neighborChanged((mutable_pos2).setY((mutable_pos2).getY()+(1)).setZ((mutable_pos2).getZ()+(-1)), this, mutable_pos);
     level.neighborChanged((mutable_pos2).setZ((mutable_pos2).getZ()+(2)), this, mutable_pos);
     level.neighborChanged((mutable_pos2).setX((mutable_pos2).getX()+(-1)).setZ((mutable_pos2).getZ()+(-1)), this, mutable_pos);
     level.neighborChanged((mutable_pos2).setX((mutable_pos2).getX()+(2)), this, mutable_pos);
     (mutable_pos).setY((mutable_pos).getY()+(1));
    }
   EndNorthUpdates: do {
    if ( (((connections) & (byte)(0x03)) != 0)
    ) {
     level.neighborChanged((mutable_pos2).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()+(-1)), this, mutable_pos);
     if ((connections & ((0x100000) | (0x02))) != 0) {
      if ((((connections) & (0x02)) != 0)
      ) {
       break EndNorthUpdates;
      }
      level.neighborChanged((mutable_pos2).setY((mutable_pos2).getY()+(1)), this, (mutable_pos).setZ((mutable_pos).getZ()+(-1)));
      if (((connections & (0x100000)) != 0)) {
       level.neighborChanged((mutable_pos2).setY((mutable_pos2).getY()+(-2)), this, mutable_pos);
       level.neighborChanged((mutable_pos2).setY((mutable_pos2).getY()+(1)).setZ((mutable_pos2).getZ()+(-1)), this, mutable_pos);
       level.neighborChanged((mutable_pos2).setX((mutable_pos2).getX()+(-1)).setZ((mutable_pos2).getZ()+(1)), this, mutable_pos);
       level.neighborChanged((mutable_pos2).setX((mutable_pos2).getX()+(2)), this, mutable_pos);
      }
      (mutable_pos).setZ((mutable_pos).getZ()+(1));
     }
    }
   } while(false);
   EndSouthUpdates: do {
    if ( (((connections) & (byte)(0x0C)) != 0)
    ) {
     level.neighborChanged((mutable_pos2).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()+(1)), this, mutable_pos);
     if ((connections & ((0x200000) | (0x08))) != 0) {
      if ((((connections) & (0x08)) != 0)
      ) {
       break EndSouthUpdates;
      }
      level.neighborChanged((mutable_pos2).setY((mutable_pos2).getY()+(1)), this, (mutable_pos).setZ((mutable_pos).getZ()+(1)));
      if (((connections & (0x200000)) != 0)) {
       level.neighborChanged((mutable_pos2).setY((mutable_pos2).getY()+(-2)), this, mutable_pos);
       level.neighborChanged((mutable_pos2).setY((mutable_pos2).getY()+(1)).setZ((mutable_pos2).getZ()+(1)), this, mutable_pos);
       level.neighborChanged((mutable_pos2).setX((mutable_pos2).getX()+(-1)).setZ((mutable_pos2).getZ()+(-1)), this, mutable_pos);
       level.neighborChanged((mutable_pos2).setX((mutable_pos2).getX()+(2)), this, mutable_pos);
      }
      (mutable_pos).setZ((mutable_pos).getZ()+(-1));
     }
    }
   } while(false);
   EndWestUpdates: do {
    if ( (((connections) & (byte)(0x30)) != 0)
    ) {
     level.neighborChanged((mutable_pos2).setX((mutable_pos).getX()+(-1)).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()), this, mutable_pos);
     if ((connections & ((0x400000) | (0x20))) != 0) {
      if ((((connections) & (0x20)) != 0)
      ) {
       break EndWestUpdates;
      }
      level.neighborChanged((mutable_pos2).setY((mutable_pos2).getY()+(1)), this, (mutable_pos).setX((mutable_pos).getX()+(-1)));
      if (((connections & (0x400000)) != 0)) {
       level.neighborChanged((mutable_pos2).setY((mutable_pos2).getY()+(-2)), this, mutable_pos);
       level.neighborChanged((mutable_pos2).setY((mutable_pos2).getY()+(1)).setZ((mutable_pos2).getZ()+(-1)), this, mutable_pos);
       level.neighborChanged((mutable_pos2).setZ((mutable_pos2).getZ()+(2)), this, mutable_pos);
       level.neighborChanged((mutable_pos2).setX((mutable_pos2).getX()+(-1)).setZ((mutable_pos2).getZ()+(-1)), this, mutable_pos);
      }
      (mutable_pos).setX((mutable_pos).getX()+(1));
     }
    }
   } while(false);
    if ( ((connections) > 0x3F)
    ) {
     level.neighborChanged((mutable_pos2).setX((mutable_pos).getX()+(1)).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()), this, mutable_pos);
     if ((connections & ((0x800000) | (0x80))) != 0) {
      if ((((connections) & (0x80)) != 0)
      ) {
       return;
      }
      level.neighborChanged((mutable_pos2).setY((mutable_pos2).getY()+(1)), this, (mutable_pos).setX((mutable_pos).getX()+(1)));
      if (((connections & (0x800000)) != 0)) {
       level.neighborChanged((mutable_pos2).setY((mutable_pos2).getY()+(-2)), this, mutable_pos);
       level.neighborChanged((mutable_pos2).setY((mutable_pos2).getY()+(1)).setZ((mutable_pos2).getZ()+(-1)), this, mutable_pos);
       level.neighborChanged((mutable_pos2).setZ((mutable_pos2).getZ()+(2)), this, mutable_pos);
       level.neighborChanged((mutable_pos2).setX((mutable_pos2).getX()+(1)).setZ((mutable_pos2).getZ()+(-1)), this, mutable_pos);
      }
      (mutable_pos).setX((mutable_pos).getX()+(-1));
     }
    }
  }
 }
 private void updatePowerStrength(Level level, BlockPos.MutableBlockPos mutable_pos, BlockState blockState, int flags, BlockPos neighbor_pos) {
  int temp_int;
  int neighbor_direction = YeetUtil.calculate_neighbor_direction(mutable_pos, neighbor_pos);
  BlockPos.MutableBlockPos mutable_pos2 = (new BlockPos.MutableBlockPos((mutable_pos).getX(),(mutable_pos).getY(),(mutable_pos).getZ()));
  redstone_wire_block.shouldSignal = (false);
        temp_int = YeetUtil.get_best_neighbor_signal_unrolled(level, mutable_pos2, neighbor_direction);
  redstone_wire_block.shouldSignal = (true);
  (mutable_pos2).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ());
        if (temp_int < 14) {
   BlockState neighbor_state;
   (mutable_pos).setX((mutable_pos2).getX()).setY((mutable_pos2).getY()+(1)).setZ((mutable_pos2).getZ());
   boolean above_is_solid = ((level).getBlockState(mutable_pos).isRedstoneConductor((level), (mutable_pos)));
   for (int i = 0; i < 4; ++i) {
    (mutable_pos).setX((mutable_pos2).getX()+(YeetUtil.horizontal_direction_offsets[i]).getX()).setY((mutable_pos2).getY()+(YeetUtil.horizontal_direction_offsets[i]).getY()).setZ((mutable_pos2).getZ()+(YeetUtil.horizontal_direction_offsets[i]).getZ());
    neighbor_state = level.getBlockState(mutable_pos);
    if (((neighbor_state).is(BlockRegistration.REDSTONE_WIRES_TAG))) {
     temp_int = Math.max(temp_int, ((neighbor_state.getValue(POWER)) - (1)));
    }
    else if (neighbor_state.isRedstoneConductor(level, mutable_pos)) {
     if (!above_is_solid && ((neighbor_state = level.getBlockState((mutable_pos).setY((mutable_pos).getY()+(1)))).is(BlockRegistration.REDSTONE_WIRES_TAG))) {
      temp_int = Math.max(temp_int, ((neighbor_state.getValue(POWER)) - (1)));
     }
    }
    else if (((neighbor_state = level.getBlockState((mutable_pos).setY((mutable_pos).getY()+(-1)))).is(BlockRegistration.REDSTONE_WIRES_TAG))) {
     temp_int = Math.max(temp_int, ((neighbor_state.getValue(POWER)) - (1)));
    }
    if (temp_int == 14) break;
   }
        }
        if (blockState.getValue(POWER) != temp_int) {
                level.setBlock(mutable_pos2, blockState.setValue(POWER, temp_int), flags);
   (mutable_pos).setX((mutable_pos2).getX()).setY((mutable_pos2).getY()+(-1)).setZ((mutable_pos2).getZ());
   (mutable_pos2).setY((mutable_pos2).getY()+(-1));
   if (((level).getBlockState(mutable_pos2).isRedstoneConductor((level), (mutable_pos2)))) {
    level.neighborChanged((mutable_pos).setX((mutable_pos2).getX()+(-1)).setY((mutable_pos2).getY()).setZ((mutable_pos2).getZ()), this, mutable_pos2);
    level.neighborChanged((mutable_pos).setX((mutable_pos).getX()+(2)), this, mutable_pos2);
    level.neighborChanged((mutable_pos).setX((mutable_pos).getX()+(-1)).setY((mutable_pos).getY()+(-1)), this, mutable_pos2);
    level.neighborChanged((mutable_pos).setY((mutable_pos).getY()+(1)).setZ((mutable_pos).getZ()+(-1)), this, mutable_pos2);
    level.neighborChanged((mutable_pos).setZ((mutable_pos).getZ()+(2)), this, mutable_pos2);
   }
   temp_int = blockState.getValue(CONNECTIONS);
   (mutable_pos2).setY((mutable_pos2).getY()+(1)).setZ((mutable_pos2).getZ()+(-1));
   if ((((temp_int) & (byte)(0x03)) != 0) && ((level).getBlockState(mutable_pos2).isRedstoneConductor((level), (mutable_pos2)))) {
    level.neighborChanged((mutable_pos).setX((mutable_pos2).getX()+(-1)).setY((mutable_pos2).getY()).setZ((mutable_pos2).getZ()), this, mutable_pos2);
    level.neighborChanged((mutable_pos).setX((mutable_pos).getX()+(2)), this, mutable_pos2);
    level.neighborChanged((mutable_pos).setX((mutable_pos).getX()+(-1)).setY((mutable_pos).getY()+(-1)), this, mutable_pos2);
    level.neighborChanged((mutable_pos).setY((mutable_pos).getY()+(2)), this, mutable_pos2);
    level.neighborChanged((mutable_pos).setY((mutable_pos).getY()+(-1)).setZ((mutable_pos).getZ()+(-1)), this, mutable_pos2);
   }
   (mutable_pos2).setZ((mutable_pos2).getZ()+(2));
   if ((((temp_int) & (byte)(0x0C)) != 0) && ((level).getBlockState(mutable_pos2).isRedstoneConductor((level), (mutable_pos2)))) {
    level.neighborChanged((mutable_pos).setX((mutable_pos2).getX()+(-1)).setY((mutable_pos2).getY()).setZ((mutable_pos2).getZ()), this, mutable_pos2);
    level.neighborChanged((mutable_pos).setX((mutable_pos).getX()+(2)), this, mutable_pos2);
    level.neighborChanged((mutable_pos).setX((mutable_pos).getX()+(-1)).setY((mutable_pos).getY()+(-1)), this, mutable_pos2);
    level.neighborChanged((mutable_pos).setY((mutable_pos).getY()+(2)), this, mutable_pos2);
    level.neighborChanged((mutable_pos).setY((mutable_pos).getY()+(-1)).setZ((mutable_pos).getZ()+(1)), this, mutable_pos2);
   }
   (mutable_pos2).setX((mutable_pos2).getX()+(-1)).setZ((mutable_pos2).getZ()+(-1));
   if ((((temp_int) & (byte)(0x30)) != 0) && ((level).getBlockState(mutable_pos2).isRedstoneConductor((level), (mutable_pos2)))) {
    level.neighborChanged((mutable_pos).setX((mutable_pos2).getX()+(-1)).setY((mutable_pos2).getY()).setZ((mutable_pos2).getZ()), this, mutable_pos2);
    level.neighborChanged((mutable_pos).setX((mutable_pos).getX()+(1)).setY((mutable_pos).getY()+(-1)), this, mutable_pos2);
    level.neighborChanged((mutable_pos).setY((mutable_pos).getY()+(2)), this, mutable_pos2);
    level.neighborChanged((mutable_pos).setY((mutable_pos).getY()+(-1)).setZ((mutable_pos).getZ()+(-1)), this, mutable_pos2);
    level.neighborChanged((mutable_pos).setZ((mutable_pos).getZ()+(2)), this, mutable_pos2);
   }
   (mutable_pos2).setX((mutable_pos2).getX()+(2));
   if (((temp_int) > 0x3F) && ((level).getBlockState(mutable_pos2).isRedstoneConductor((level), (mutable_pos2)))) {
    level.neighborChanged((mutable_pos).setX((mutable_pos2).getX()+(1)).setY((mutable_pos2).getY()).setZ((mutable_pos2).getZ()), this, mutable_pos2);
    level.neighborChanged((mutable_pos).setX((mutable_pos).getX()+(-1)).setY((mutable_pos).getY()+(-1)), this, mutable_pos2);
    level.neighborChanged((mutable_pos).setY((mutable_pos).getY()+(2)), this, mutable_pos2);
    level.neighborChanged((mutable_pos).setY((mutable_pos).getY()+(-1)).setZ((mutable_pos).getZ()+(-1)), this, mutable_pos2);
    level.neighborChanged((mutable_pos).setZ((mutable_pos).getZ()+(2)), this, mutable_pos2);
   }
   if ((((temp_int) & 0xAA) != 0)) {
    (mutable_pos2).setX((mutable_pos2).getX()+(-1)).setY((mutable_pos2).getY()+(1));
    level.neighborChanged((mutable_pos).setX((mutable_pos2).getX()+(-1)).setY((mutable_pos2).getY()).setZ((mutable_pos2).getZ()), this, mutable_pos2);
    level.neighborChanged((mutable_pos).setX((mutable_pos).getX()+(2)), this, mutable_pos2);
    level.neighborChanged((mutable_pos).setX((mutable_pos).getX()+(-1)).setY((mutable_pos).getY()+(1)), this, mutable_pos2);
    level.neighborChanged((mutable_pos).setY((mutable_pos).getY()+(-1)).setZ((mutable_pos).getZ()+(-1)), this, mutable_pos2);
    level.neighborChanged((mutable_pos).setZ((mutable_pos).getZ()+(2)), this, mutable_pos2);
   }
  }
 }
 private void updatePowerStrength(Level level, BlockPos.MutableBlockPos mutable_pos, BlockState blockState, int flags) {
  redstone_wire_block.shouldSignal = (false);
        int highest_power = level.getBestNeighborSignal(mutable_pos);
  redstone_wire_block.shouldSignal = (true);
  BlockPos.MutableBlockPos mutable_pos2 = (new BlockPos.MutableBlockPos((mutable_pos).getX(),(mutable_pos).getY(),(mutable_pos).getZ()));
        if (highest_power < 14) {
   BlockState neighbor_state;
   (mutable_pos).setX((mutable_pos2).getX()).setY((mutable_pos2).getY()+(1)).setZ((mutable_pos2).getZ());
   boolean above_is_solid = ((level).getBlockState(mutable_pos).isRedstoneConductor((level), (mutable_pos)));
   for (int i = 0; i < 4; ++i) {
    (mutable_pos).setX((mutable_pos2).getX()+(YeetUtil.horizontal_direction_offsets[i]).getX()).setY((mutable_pos2).getY()+(YeetUtil.horizontal_direction_offsets[i]).getY()).setZ((mutable_pos2).getZ()+(YeetUtil.horizontal_direction_offsets[i]).getZ());
    neighbor_state = level.getBlockState(mutable_pos);
    if (((neighbor_state).is(BlockRegistration.REDSTONE_WIRES_TAG))) {
     highest_power = Math.max(highest_power, ((neighbor_state.getValue(POWER)) - (1)));
    }
    if (neighbor_state.isRedstoneConductor(level, mutable_pos)) {
     if (!above_is_solid && ((neighbor_state = level.getBlockState((mutable_pos).setY((mutable_pos).getY()+(1)))).is(BlockRegistration.REDSTONE_WIRES_TAG))) {
      highest_power = Math.max(highest_power, ((neighbor_state.getValue(POWER)) - (1)));
     }
    }
    else if (((neighbor_state = level.getBlockState((mutable_pos).setY((mutable_pos).getY()+(-1)))).is(BlockRegistration.REDSTONE_WIRES_TAG))) {
     highest_power = Math.max(highest_power, ((neighbor_state.getValue(POWER)) - (1)));
    }
    if (highest_power == 14) break;
   }
        }
        if (blockState.getValue(POWER) != highest_power) {
                level.setBlock(mutable_pos2, blockState.setValue(POWER, highest_power), flags);
   (mutable_pos).setX((mutable_pos2).getX()).setY((mutable_pos2).getY()+(-1)).setZ((mutable_pos2).getZ());
   if (((level).getBlockState(mutable_pos).isRedstoneConductor((level), (mutable_pos)))) {
    YeetUtil.update_neighbors(level, mutable_pos, this, (mutable_pos2).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   }
   int connections = blockState.getValue(CONNECTIONS);
   (mutable_pos).setY((mutable_pos).getY()+(1)).setZ((mutable_pos).getZ()+(-1));
   if ((((connections) & (byte)(0x03)) != 0) && ((level).getBlockState(mutable_pos).isRedstoneConductor((level), (mutable_pos)))) {
    YeetUtil.update_neighbors(level, mutable_pos, this, (mutable_pos2).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   }
   (mutable_pos).setZ((mutable_pos).getZ()+(2));
   if ((((connections) & (byte)(0x0C)) != 0) && ((level).getBlockState(mutable_pos).isRedstoneConductor((level), (mutable_pos)))) {
    YeetUtil.update_neighbors(level, mutable_pos, this, (mutable_pos2).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   }
   (mutable_pos).setX((mutable_pos).getX()+(-1)).setZ((mutable_pos).getZ()+(-1));
   if ((((connections) & (byte)(0x30)) != 0) && ((level).getBlockState(mutable_pos).isRedstoneConductor((level), (mutable_pos)))) {
    YeetUtil.update_neighbors(level, mutable_pos, this, (mutable_pos2).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   }
   (mutable_pos).setX((mutable_pos).getX()+(2));
   if (((connections) > 0x3F) && ((level).getBlockState(mutable_pos).isRedstoneConductor((level), (mutable_pos)))) {
    YeetUtil.update_neighbors(level, mutable_pos, this, (mutable_pos2).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   }
   if ((((connections) & 0xAA) != 0)) {
    YeetUtil.update_neighbors(level, (mutable_pos).setX((mutable_pos).getX()+(-1)).setY((mutable_pos).getY()+(1)), this, (mutable_pos2).setX((mutable_pos).getX()).setY((mutable_pos).getY()).setZ((mutable_pos).getZ()));
   }
        }
    }
}
