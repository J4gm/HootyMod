package jagm.hooty;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(Hooty.MOD_ID)
@Mod.EventBusSubscriber(modid = Hooty.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class Hooty {

	public static final String MOD_ID = "hooty";

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LogManager.getLogger();

	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Hooty.MOD_ID);
	public static final RegistryObject<EntityType<HootyEntity>> ENTITY_HOOTY = ENTITY_TYPES.register("hooty",
			() -> EntityType.Builder.<HootyEntity>create(HootyEntity::new, EntityClassification.CREATURE).size(0.5F, 0.5F)
					.build(new ResourceLocation(Hooty.MOD_ID, "hooty").toString()));

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
	public static final RegistryObject<HootySpawnEggItem> hootySpawnEgg = ITEMS.register("hooty_spawn_egg",
			() -> new HootySpawnEggItem(ENTITY_HOOTY, 0xF0C275, 0xBE742B, new Item.Properties().group(ItemGroup.MISC)));

	public Hooty() {
		ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
		ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public static void clientSetup(final FMLClientSetupEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(ENTITY_HOOTY.get(), HootyRenderer::new);
	}

	@SubscribeEvent
	public static void onRegisterEntities(final RegistryEvent.Register<EntityType<?>> event) {
		HootySpawnEggItem.init();
	}
}
