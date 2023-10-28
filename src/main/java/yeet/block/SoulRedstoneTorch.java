package yeet.block;
import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
public class SoulRedstoneTorch extends RedstoneTorchBlock {
 public SoulRedstoneTorch(BlockBehaviour.Properties properties) {
        super(properties);
    }
 @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, Random random) {
    }
 @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random random) {
  final boolean is_lit = blockState.getValue(LIT).booleanValue();
  if (is_lit == this.hasNeighborSignal(serverLevel, blockPos, blockState)) {
   serverLevel.setBlock(blockPos, (BlockState)blockState.setValue(LIT, !is_lit), 3);
  }
    }
 @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        if (blockState.getValue(LIT).booleanValue() == this.hasNeighborSignal(level, blockPos, blockState) && !level.getBlockTicks().willTickThisTick(blockPos, this)) {
            level.scheduleTick(blockPos, this, 1);
        }
    }
}
