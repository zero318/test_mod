package yeet;

//import yeet.AsmUtil;
import yeet.Deencapsulator;
import yeet.BlockRegistration;
import yeet.ItemRegistration;
import yeet.SoundEventRegistration;
import yeet.block.SoulRedstoneWire;
import yeet.block.SoulWireColor;
import yeet.JankyHackMate;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(test_mod.MOD_ID)
public class test_mod {
	
	//static{
		//AsmUtil.run();
	//}
	
	public static final String MOD_ID = "test_mod";

    public test_mod() {
		
		JankyHackMate.init_funcs();
		//JankyHackMate.breakpoint();
		
        var event_bus = FMLJavaModLoadingContext.get().getModEventBus();
		
		BlockRegistration.BLOCKS.register(event_bus);
		ItemRegistration.ITEMS.register(event_bus);
		SoundEventRegistration.SOUND_EVENTS.register(event_bus);
		
        MinecraftForge.EVENT_BUS.register(this);
    }
	
	@Mod.EventBusSubscriber(modid = test_mod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class init_crap {
		
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public static void init_block_render_types(final FMLClientSetupEvent event) {
			final var cutout_render_type = RenderType.cutout();
			
			ItemBlockRenderTypes.setRenderLayer(BlockRegistration.SOUL_REDSTONE_WIRE.get(), cutout_render_type);
			ItemBlockRenderTypes.setRenderLayer(BlockRegistration.SOUL_REDSTONE_TORCH.get(), cutout_render_type);
			ItemBlockRenderTypes.setRenderLayer(BlockRegistration.SOUL_REDSTONE_WALL_TORCH.get(), cutout_render_type);
			ItemBlockRenderTypes.setRenderLayer(BlockRegistration.SOUL_REPEATER.get(), cutout_render_type);
			ItemBlockRenderTypes.setRenderLayer(BlockRegistration.SOUL_COMPARATOR.get(), cutout_render_type);
		}
		
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public static void register_block_colors(final ColorHandlerEvent.Block event) {
			event.getBlockColors().register(new SoulWireColor(), BlockRegistration.SOUL_REDSTONE_WIRE.get());
			/* event.getBlockColors().register(
				(blockState, tint_thing_idk, blockPos, tintIndex) ->
					SoulRedstoneWire.getColorForPower(blockState.getValue(SoulRedstoneWire.POWER))
				, BlockRegistration.SOUL_REDSTONE_WIRE.get()
			); */
		}
	}
	
	
}
