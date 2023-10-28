package yeet;
import yeet.test_mod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
public class SoundEventRegistration {
 public static final DeferredRegister<SoundEvent> SOUND_EVENTS;
 public static final RegistryObject<SoundEvent> INSPECTOR_CLICK;
 static {
  SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, test_mod.MOD_ID);
  INSPECTOR_CLICK = SOUND_EVENTS.register("block.inspector.click", () -> new SoundEvent(new ResourceLocation(test_mod.MOD_ID, "block.inspector.click")));
 }
}
