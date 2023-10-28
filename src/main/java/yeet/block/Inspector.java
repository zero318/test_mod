package yeet.block;

import yeet.block.CustomStates;
import yeet.block.CustomStates.InspectorMode;
import yeet.SoundEventRegistration;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.ObserverBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
public class Inspector extends DirectionalBlock {
 public static final EnumProperty<InspectorMode> MODE = CustomStates.MODE_INSPECTOR;
 public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
 public static final IntegerProperty POWER = BlockStateProperties.POWER;
 public Inspector(BlockBehaviour.Properties properties) {
        super(properties);
  this.registerDefaultState(
   this.stateDefinition.any()
    .setValue(FACING, Direction.NORTH)
    .setValue(MODE, InspectorMode.DIGITAL)
    .setValue(POWERED, false)
    .setValue(POWER, 0)
  );
    }
 @Override
 protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
  builder.add(FACING, MODE, POWERED, POWER);
 }
 @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (player.getAbilities().mayBuild) {
            final float f = (blockState = blockState.cycle(MODE)).getValue(MODE) == InspectorMode.ANALOG ? 0.55f : 0.5f;
   level.playSound(player, blockPos, SoundEventRegistration.INSPECTOR_CLICK.get(), SoundSource.BLOCKS, 0.3f, f);
   level.setBlock(blockPos, blockState, 0x02);
   return InteractionResult.sidedSuccess(level.isClientSide);
        }
  return InteractionResult.PASS;
    }
 @Override
    public BlockState rotate(BlockState blockState, Rotation rotation) {
        return (BlockState)blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }
    @Override
    public BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }
 @Override
    public boolean isSignalSource(BlockState blockState) {
        return true;
    }
 @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return blockState.getSignal(blockGetter, blockPos, direction);
    }
 protected void updateNeighborsInFront(Level level, BlockPos blockPos, BlockState blockState) {
        Direction direction = blockState.getValue(FACING);
        BlockPos blockPos2 = blockPos.relative(direction.getOpposite());
        level.neighborChanged(blockPos2, this, blockPos);
        level.updateNeighborsAtExceptFromFacing(blockPos2, this, direction);
    }
 @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return (BlockState)this.defaultBlockState().setValue(FACING, blockPlaceContext.getNearestLookingDirection());
    }
 @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (!blockState.is(blockState2.getBlock()) && !level.isClientSide() && blockState.getValue(POWERED).booleanValue() && !level.getBlockTicks().hasScheduledTick(blockPos, this)) {
            BlockState blockState3 = blockState.setValue(POWERED, false);
            level.setBlock(blockPos, blockState3, 0x02 | 0x10);
            this.updateNeighborsInFront(level, blockPos, blockState3);
        }
    }
 @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        if (!blockState.is(blockState2.getBlock()) && !level.isClientSide && blockState.getValue(POWERED).booleanValue() && level.getBlockTicks().hasScheduledTick(blockPos, this)) {
            this.updateNeighborsInFront(level, blockPos, blockState.setValue(POWERED, false));
        }
    }
 @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random random) {
  if (!blockState.getValue(POWERED)) {
   int input_signal = this.getInputSignal(serverLevel, blockPos, blockState) & 15;
   if (input_signal == 0) {
    return;
   }
   serverLevel.setBlock(blockPos, blockState.setValue(POWERED, true).setValue(POWER, input_signal), 0x02);
   serverLevel.scheduleTick(blockPos, this, ((1) * 2));
  }
  else {
   serverLevel.setBlock(blockPos, blockState.setValue(POWERED, false).setValue(POWER, 0), 0x02);
  }
  this.updateNeighborsInFront(serverLevel, blockPos, blockState);
    }
    protected int getInputSignal(Level level, BlockPos blockPos, BlockState blockState) {
        final BlockPos block_pos_2 = blockPos.relative(blockState.getValue(FACING));
        final BlockState block_state_2 = level.getBlockState(block_pos_2);
  return block_state_2.hasAnalogOutputSignal() ?
  blockState.getValue(MODE) == InspectorMode.DIGITAL ? 15 :
  block_state_2.getAnalogOutputSignal(level, block_pos_2) : 0;
    }
 @Override
    public int getSignal(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
        return blockState.getValue(FACING) == direction
  ? blockState.getValue(POWER) : 0;
    }
 @Override
 public void onNeighborChange(BlockState blockState, LevelReader levelReader, BlockPos self_pos, BlockPos neighbor_pos) {
  if (levelReader instanceof Level) {
   Level level = (Level)levelReader;
   if (!level.isClientSide && !blockState.getValue(POWERED) && neighbor_pos.equals(self_pos.relative(blockState.getValue(FACING))) && !level.getBlockTicks().hasScheduledTick(self_pos, this)) {
    level.scheduleTick(self_pos, this, ((1) * 2));
   }
  }
 }
 @Override
 public boolean canConnectRedstone(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
  return direction == blockState.getValue(FACING);
 }
}
