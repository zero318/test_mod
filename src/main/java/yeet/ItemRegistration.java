package yeet;
import yeet.test_mod;
import yeet.BlockRegistration;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
public class ItemRegistration {
 public static final DeferredRegister<Item> ITEMS;
 public static final RegistryObject<Item> RESISTOR;
 public static final RegistryObject<Item> INSPECTOR;
 public static final RegistryObject<Item> SOUL_REDSTONE;
 public static final RegistryObject<Item> SOUL_REDSTONE_TORCH;
 public static final RegistryObject<Item> SOUL_REPEATER;
 public static final RegistryObject<Item> SOUL_COMPARATOR;
 static {
  ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, test_mod.MOD_ID);
  final Item.Properties redstone_tab = new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE);
  RESISTOR = ITEMS.register("resistor", () -> new BlockItem(BlockRegistration.RESISTOR.get(), redstone_tab));
  INSPECTOR = ITEMS.register("inspector", () -> new BlockItem(BlockRegistration.INSPECTOR.get(), redstone_tab));
  SOUL_REDSTONE = ITEMS.register("soul_redstone", () -> new ItemNameBlockItem(BlockRegistration.SOUL_REDSTONE_WIRE.get(), redstone_tab));
  SOUL_REDSTONE_TORCH = ITEMS.register("soul_redstone_torch", () -> new StandingAndWallBlockItem(BlockRegistration.SOUL_REDSTONE_TORCH.get(), BlockRegistration.SOUL_REDSTONE_WALL_TORCH.get(), redstone_tab));
  SOUL_REPEATER = ITEMS.register("soul_repeater", () -> new BlockItem(BlockRegistration.SOUL_REPEATER.get(), redstone_tab));
  SOUL_COMPARATOR = ITEMS.register("soul_comparator", () -> new BlockItem(BlockRegistration.SOUL_COMPARATOR.get(), redstone_tab));
 }
}
