package yeet;

import yeet.test_mod;

import yeet.block.Resistor;
import yeet.block.Inspector;
import yeet.block.SoulRedstoneWire;
import yeet.block.SoulRedstoneTorch;
import yeet.block.SoulRedstoneWallTorch;
import yeet.block.SoulRepeater;
import yeet.block.SoulComparator;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegistration {
	
	public static final DeferredRegister<Block> BLOCKS;
	
	public static final RegistryObject<Block> RESISTOR;
	public static final RegistryObject<Block> INSPECTOR;
	
	public static final RegistryObject<Block> SOUL_REDSTONE_WIRE;
	public static final RegistryObject<Block> SOUL_REDSTONE_TORCH;
	public static final RegistryObject<Block> SOUL_REDSTONE_WALL_TORCH;
	public static final RegistryObject<Block> SOUL_REPEATER;
	public static final RegistryObject<Block> SOUL_COMPARATOR;
	
	public static final TagKey<Block> REDSTONE_WIRES_TAG;
	public static final TagKey<Block> REDSTONE_TORCHES_TAG;
	public static final TagKey<Block> REPEATERS_TAG;
	public static final TagKey<Block> COMPARATORS_TAG;
	public static final TagKey<Block> RESISTORS_TAG;
	public static final TagKey<Block> OBSERVERS_TAG;
	
	public static final TagKey<Block> WIRE_CONNECTIONS_TAG;
	public static final TagKey<Block> REPEATER_CONNECTIONS_TAG;
	public static final TagKey<Block> OBSERVER_CONNECTIONS_TAG;
	
	static {
		// Registry Init
		
		BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, test_mod.MOD_ID);
		
		// Block Init
		
		final BlockBehaviour.Properties resistor_properties = BlockBehaviour.Properties.copy(Blocks.REPEATER);
		
		RESISTOR = BLOCKS.register("resistor", () -> new Resistor(resistor_properties));
		
		final BlockBehaviour.Properties observer_properties = BlockBehaviour.Properties.copy(Blocks.OBSERVER);
		
		INSPECTOR = BLOCKS.register("inspector", () -> new Inspector(observer_properties));
		
		final BlockBehaviour.Properties soul_redstone_wire_properties = BlockBehaviour.Properties.copy(Blocks.REDSTONE_WIRE);
		
		SOUL_REDSTONE_WIRE = BLOCKS.register("soul_redstone_wire", () -> new SoulRedstoneWire(soul_redstone_wire_properties));
		
		final BlockBehaviour.Properties soul_redstone_torch_properties = BlockBehaviour.Properties.of(Material.DECORATION).noCollission().instabreak().sound(SoundType.WOOD);
		
		SOUL_REDSTONE_TORCH = BLOCKS.register("soul_redstone_torch", () -> new SoulRedstoneTorch(soul_redstone_torch_properties));
		SOUL_REDSTONE_WALL_TORCH = BLOCKS.register("soul_redstone_wall_torch", () -> new SoulRedstoneWallTorch(soul_redstone_torch_properties));
		
		final BlockBehaviour.Properties soul_component_properties = BlockBehaviour.Properties.of(Material.DECORATION, MaterialColor.COLOR_BLACK).instabreak().sound(SoundType.STONE);
		
		SOUL_REPEATER = BLOCKS.register("soul_repeater", () -> new SoulRepeater(soul_component_properties));
		SOUL_COMPARATOR = BLOCKS.register("soul_comparator", () -> new SoulComparator(soul_component_properties));
		
		// Tag Init
		
		REDSTONE_WIRES_TAG = BlockTags.create(new ResourceLocation(test_mod.MOD_ID, "redstone_wire"));
		REDSTONE_TORCHES_TAG = BlockTags.create(new ResourceLocation(test_mod.MOD_ID, "redstone_torches"));
		REPEATERS_TAG = BlockTags.create(new ResourceLocation(test_mod.MOD_ID, "repeaters"));
		COMPARATORS_TAG = BlockTags.create(new ResourceLocation(test_mod.MOD_ID, "comparators"));
		RESISTORS_TAG = BlockTags.create(new ResourceLocation(test_mod.MOD_ID, "resistors"));
		OBSERVERS_TAG = BlockTags.create(new ResourceLocation(test_mod.MOD_ID, "observers"));
		WIRE_CONNECTIONS_TAG = BlockTags.create(new ResourceLocation(test_mod.MOD_ID, "redstone_wire_like_connections"));
		REPEATER_CONNECTIONS_TAG = BlockTags.create(new ResourceLocation(test_mod.MOD_ID, "repeater_like_connections"));
		OBSERVER_CONNECTIONS_TAG = BlockTags.create(new ResourceLocation(test_mod.MOD_ID, "observer_like_connections"));
	}
}