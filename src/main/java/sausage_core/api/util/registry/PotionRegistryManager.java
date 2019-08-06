package sausage_core.api.util.registry;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionType;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import sausage_core.api.annotation.AutoCall;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class PotionRegistryManager implements IRegistryManager {
	final String modid;
	final List<Runnable> tasks = new ArrayList<>();

	public PotionRegistryManager(String modid) {
		this.modid = modid;
		AutoCall.When.checkState();
	}

	public Potion register(String name, Potion potion) {
		potion.setRegistryName(name).setPotionName(String.join(".", "effect", modid, name));
		ForgeRegistries.POTIONS.register(potion);
		return potion;
	}

	public PotionType[] register(Potion potion, IPotionTypeLoader loader) {
		Pair<PotionType[], Runnable> pair = loader.loadPotionType(potion);
		ForgeRegistries.POTION_TYPES.registerAll(pair.getKey());
		tasks.add(pair.getValue());
		return pair.getKey();
	}

	{
		L.add(this::load);
	}
	@AutoCall(when = AutoCall.When.INIT)
	private static final Set<Runnable> L = new HashSet<>();
	private void load() {
		tasks.forEach(Runnable::run);
	}

	@Override
	public String modid() {
		return modid;
	}

	@Override
	public Class<?>[] types() {
		return new Class[] {Potion.class, PotionType.class};
	}
}
