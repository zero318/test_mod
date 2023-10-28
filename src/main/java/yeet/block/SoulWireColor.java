package yeet.block;
import yeet.block.SoulRedstoneWire;
import javax.annotation.Nullable;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
public class SoulWireColor implements BlockColor {
 @Override
 public int getColor(BlockState blockState, @Nullable BlockAndTintGetter tint_thign, BlockPos blockPos, int tintIndex) {
  return SoulRedstoneWire.getColorForPower(blockState.getValue(SoulRedstoneWire.POWER));
 }
}
