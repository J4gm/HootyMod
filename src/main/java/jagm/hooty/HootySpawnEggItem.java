package jagm.hooty;

import java.util.Map;

import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class HootySpawnEggItem extends SpawnEggItem {

	private static HootySpawnEggItem hootyEgg;

	public static void init() {
		final Map<EntityType<?>, SpawnEggItem> EGGS = ObfuscationReflectionHelper.getPrivateValue(SpawnEggItem.class, null, "field_195987_b");
		DefaultDispenseItemBehavior dispenserBehaviour = new DefaultDispenseItemBehavior() {
			@Override
			protected ItemStack dispenseStack(IBlockSource source, ItemStack stack) {
				Direction direction = source.getBlockState().get(DispenserBlock.FACING);
				EntityType<?> type = ((SpawnEggItem) stack.getItem()).getType(stack.getTag());
				type.spawn(source.getWorld(), stack, null, source.getBlockPos(), SpawnReason.DISPENSER, direction != Direction.UP, false);
				stack.shrink(1);
				return stack;
			}
		};
		EGGS.put(hootyEgg.getType(null), hootyEgg);
		DispenserBlock.registerDispenseBehavior(hootyEgg, dispenserBehaviour);
	}

	private final Lazy<? extends EntityType<?>> entityTypeSupplier;

	public HootySpawnEggItem(final RegistryObject<? extends EntityType<?>> entityTypeSupplier, int primaryColorIn, int secondaryColorIn, Properties builder) {
		super(null, primaryColorIn, secondaryColorIn, builder);
		this.entityTypeSupplier = Lazy.of(entityTypeSupplier::get);
		hootyEgg = this;
	}

	@Override
	public EntityType<?> getType(CompoundNBT nbt) {
		return this.entityTypeSupplier.get();
	}

}
