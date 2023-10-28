package yeet.block;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.RepeaterBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class SoulRepeater extends RepeaterBlock {
	
	public SoulRepeater(BlockBehaviour.Properties properties) {
        super(properties);
	}
	
	@Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, Random random) {
    }
	
	@Override
	public boolean shouldPrioritize(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
		final Direction direction = blockState.getValue(FACING);
        BlockState block_state_2 = blockGetter.getBlockState(blockPos.relative(direction.getOpposite()));
        return DiodeBlock.isDiode(block_state_2) && block_state_2.getValue(FACING) != direction;
    }
	
	@Override
	public boolean canConnectRedstone(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
		final Direction facing = blockState.getValue(FACING);
		return direction == facing || direction == facing.getOpposite();
	}
}