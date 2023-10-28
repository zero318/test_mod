package yeet.block;

import yeet.BlockRegistration;
import java.lang.Integer;
import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ComparatorMode;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.ticks.LevelTickAccess;
import net.minecraft.world.ticks.TickPriority;
public class SoulComparator extends DiodeBlock {
    public static final EnumProperty<ComparatorMode> MODE = BlockStateProperties.MODE_COMPARATOR;
    public static final IntegerProperty POWER = BlockStateProperties.POWER;
 public SoulComparator(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(
   this.stateDefinition.any()
    .setValue(FACING, Direction.NORTH)
    .setValue(POWERED, false)
    .setValue(MODE, ComparatorMode.COMPARE)
    .setValue(POWER, 0)
  );
    }
 @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, MODE, POWERED, POWER);
    }
 @Override
    protected int getDelay(BlockState blockState) {
        return ((1) * 2);
    }
 @Override
    protected int getOutputSignal(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
        return blockState.getValue(POWER);
    }
 private int calculateOutputSignal(Level level, BlockPos blockPos, BlockState blockState) {
  final int signal = this.getInputSignal(level, blockPos, blockState);
        if (signal != 0) {
   final int signal_sub = signal - this.getAlternateSignal(level, blockPos, blockState);
   if (signal_sub >= 0) {
    return blockState.getValue(MODE) == ComparatorMode.COMPARE ? signal : signal_sub;
   }
  }
  return 0;
    }
 @Override
    protected boolean shouldTurnOn(Level level, BlockPos blockPos, BlockState blockState) {
  final int signal = this.getInputSignal(level, blockPos, blockState);
        if (signal != 0) {
   final int signal_sub = signal - this.getAlternateSignal(level, blockPos, blockState);
   if (signal_sub >= 0) {
    return signal_sub != 0 || blockState.getValue(MODE) == ComparatorMode.COMPARE;
   }
        }
  return false;
    }
 @Override
    protected int getInputSignal(Level level, BlockPos blockPos, BlockState blockState) {
        final Direction direction = blockState.getValue(FACING);
        final BlockPos block_pos_2 = blockPos.relative(direction);
        final BlockState block_state_2 = level.getBlockState(block_pos_2);
  if (block_state_2.hasAnalogOutputSignal()) {
   return block_state_2.getAnalogOutputSignal(level, block_pos_2);
        }
  int signal = super.getInputSignal(level, blockPos, blockState);
  if (signal < 15 && block_state_2.isRedstoneConductor(level, block_pos_2)) {
   final BlockPos block_pos_3 = block_pos_2.relative(direction);
   final BlockState block_state_3 = level.getBlockState(block_pos_3);
   int signal_2 = 0;
   do {
    if (block_state_3.hasAnalogOutputSignal()) {
     signal = block_state_3.getAnalogOutputSignal(level, block_pos_3);
     if (signal >= ItemFrame.NUM_ROTATIONS) break;
     signal_2 = signal;
    }
    final ItemFrame item_frame = this.getItemFrame(level, direction, block_pos_3);
    if (item_frame != null) {
     signal = item_frame.getAnalogOutput();
     if (signal_2 > signal) return signal_2;
    }
   } while (false);
  }
  return signal;
    }
 @Nullable
    private ItemFrame getItemFrame(Level level, Direction direction, BlockPos blockPos) {
  final double X = blockPos.getX();
  final double Y = blockPos.getY();
  final double Z = blockPos.getZ();
        List<ItemFrame> list = level.getEntitiesOfClass(ItemFrame.class, new AABB(X,Y,Z,X+1.0,Y+1.0,Z+1.0), item_frame -> item_frame != null && item_frame.getDirection() == direction);
        return list.size() != 1 ? null : list.get(0);
    }
 @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if (player.getAbilities().mayBuild) {
            final float f = (blockState = blockState.cycle(MODE)).getValue(MODE) == ComparatorMode.SUBTRACT ? 0.55f : 0.5f;
   level.playSound(player, blockPos, SoundEvents.COMPARATOR_CLICK, SoundSource.BLOCKS, 0.3f, f);
   level.setBlock(blockPos, blockState, 0x02);
    this.refreshOutputState(level, blockPos, blockState);
   return InteractionResult.sidedSuccess(level.isClientSide);
        }
  return InteractionResult.PASS;
    }
 @Override
    protected void checkTickOnNeighbor(Level level, BlockPos blockPos, BlockState blockState) {
  if (!level.getBlockTicks().hasScheduledTick(blockPos, this)) {
            final int output = this.calculateOutputSignal(level, blockPos, blockState);
   if (output != blockState.getValue(POWER) || blockState.getValue(POWERED) != (output != 0)) {
    level.scheduleTick(blockPos, this, ((1) * 2), this.shouldPrioritize(level, blockPos, blockState) ? TickPriority.HIGH : TickPriority.NORMAL);
   }
        }
    }
 private void refreshOutputState(Level level, BlockPos blockPos, BlockState blockState) {
        final int output = this.calculateOutputSignal(level, blockPos, blockState) & 15;
  final int output_state = blockState.getValue(POWER);
  if (output != output_state) {
   level.setBlock(blockPos,
    blockState.setValue(POWERED, output != 0)
        .setValue(POWER, output)
   , 0x02);
            this.updateNeighborsInFront(level, blockPos, blockState);
  }
    }
 @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random random) {
        this.refreshOutputState(serverLevel, blockPos, blockState);
    }
 @Override
 public boolean shouldPrioritize(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
  final Direction direction = blockState.getValue(FACING);
        final BlockState block_state_2 = blockGetter.getBlockState(blockPos.relative(direction.getOpposite()));
        return DiodeBlock.isDiode(block_state_2) && block_state_2.getValue(FACING) != direction;
    }
 @Override
 public boolean getWeakChanges(BlockState state, LevelReader world, BlockPos pos) {
  return true;
 }
 @Override
 public void onNeighborChange(BlockState self_state, LevelReader level_reader, BlockPos self_pos, BlockPos neighbor_pos) {
  if (self_pos.getY() == neighbor_pos.getY() && level_reader instanceof Level) {
   final Level level = (Level)level_reader;
   if (!level.isClientSide) {
    self_state.neighborChanged(level, self_pos, level.getBlockState(neighbor_pos).getBlock(), neighbor_pos, false);
   }
  }
 }
}
