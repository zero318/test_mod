package yeet.block;

import yeet.block.CustomStates;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.ticks.LevelTickAccess;
import net.minecraft.world.ticks.TickPriority;
public class Resistor extends DiodeBlock {
 public static final IntegerProperty RESISTANCE = CustomStates.RESISTANCE;
    public static final IntegerProperty POWER = BlockStateProperties.POWER;
 public Resistor(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(
   this.stateDefinition.any()
    .setValue(FACING, Direction.NORTH)
    .setValue(POWERED, false)
    .setValue(RESISTANCE, 1)
    .setValue(POWER, 0)
  );
    }
 @Override
 protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
  builder.add(FACING, POWERED, POWER, RESISTANCE);
    }
 @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (player.getAbilities().mayBuild) {
   level.setBlock(blockPos, blockState = blockState.cycle(RESISTANCE), 0x02);
   this.update_output(level, blockPos, blockState);
   return InteractionResult.sidedSuccess(level.isClientSide);
        }
  return InteractionResult.PASS;
    }
 @Override
    protected void checkTickOnNeighbor(Level level, BlockPos blockPos, BlockState blockState) {
  if (!level.getBlockTicks().hasScheduledTick(blockPos, this)) {
   final int input_power = this.getInputSignal(level, blockPos, blockState);
   if ((input_power > 0) != blockState.getValue(POWERED) || this.calculateOutputSignal(level, blockPos, blockState, input_power) != blockState.getValue(POWER)) {
    level.scheduleTick(blockPos, this, 0, this.shouldPrioritize(level, blockPos, blockState) ? TickPriority.HIGH : TickPriority.NORMAL);
   }
        }
    }
 @Override
    protected int getDelay(BlockState blockState) {
        return 0;
    }
 protected int get_resistance(BlockState blockState) {
  return blockState.getValue(RESISTANCE);
 }
 private int calculateOutputSignal(Level level, BlockPos blockPos, BlockState blockState, int input_power) {
        int output_power = this.getInputSignal(level, blockPos, blockState) - this.get_resistance(blockState);
  return output_power >= 0 ? output_power : 0;
    }
 @Override
    protected int getOutputSignal(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
        return blockState.getValue(POWER);
    }
 private void update_output(Level level, BlockPos blockPos, BlockState blockState) {
  int input_power = this.getInputSignal(level, blockPos, blockState);
  int output_power = this.calculateOutputSignal(level, blockPos, blockState, input_power);
  boolean should_be_powered = input_power > 0;
  if (should_be_powered != blockState.getValue(POWERED) || output_power != blockState.getValue(POWER)) {
   level.setBlock(blockPos,
    blockState.setValue(POWERED, should_be_powered)
        .setValue(POWER, output_power)
   , 0x02);
   this.updateNeighborsInFront(level, blockPos, blockState);
  }
 }
 @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random random) {
        this.update_output(serverLevel, blockPos, blockState);
    }
 @Override
 public boolean canConnectRedstone(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
  final Direction facing = blockState.getValue(FACING);
  return direction == facing || direction == facing.getOpposite();
 }
}
