package yeet.mixins;
import yeet.BlockRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.RedStoneWireBlock;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
@Mixin(RedStoneWireBlock.class)
public class world_level_block_redstonewireblock {
 @Redirect(
  method = "shouldConnectTo(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;)Z",
  at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"
  ),
  require = 3
 )
 @Debug(print = true)
 private static boolean wire_connection_fix(BlockState block_state, Block block_type) {
  if (block_type == Blocks.REDSTONE_WIRE) {
   return block_state.is(BlockRegistration.WIRE_CONNECTIONS_TAG);
  }
  if (block_type == Blocks.REPEATER) {
   return block_state.is(BlockRegistration.REPEATER_CONNECTIONS_TAG);
  }
  if (block_type == Blocks.OBSERVER) {
   return block_state.is(BlockRegistration.OBSERVER_CONNECTIONS_TAG);
  }
  return false;
 }
 @Redirect(
  method =
  "getWireSignal(Lnet/minecraft/world/level/block/state/BlockState;)I",
  at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"
  ),
  require = 1
 )
 @Debug(print = true)
 private boolean wire_power_fixA(BlockState block_state, Block block_type) {
  return block_state.is(BlockRegistration.REDSTONE_WIRES_TAG);
 }
 @Redirect(
  method =
  "checkCornerChangeAt(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;)V",
  at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"
  ),
  require = 1
 )
 @Debug(print = true)
 private boolean wire_power_fixB(BlockState block_state, Block block_type) {
  return block_state.is(BlockRegistration.WIRE_CONNECTIONS_TAG);
 }
}
