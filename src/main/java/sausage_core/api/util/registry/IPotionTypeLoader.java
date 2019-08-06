package sausage_core.api.util.registry;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import sausage_core.api.core.common.PotionTypeModID;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;

public interface IPotionTypeLoader {
	Pair<PotionType[], Runnable> loadPotionType(Potion... potions);

	interface ISimpleVariant {
		String variantName();
		default int processDuration(int duration) {
			return duration;
		}
		default int processAmplifier(int amplifier) {
			return amplifier;
		}
		default void addMix(PotionType base, PotionType type) {

		}

		static ISimpleVariant of(String name,
								 IntUnaryOperator processDuration,
								 IntUnaryOperator processAmplifier,
								 BiConsumer<PotionType, PotionType> addMix) {
			return new ISimpleVariant() {
				@Override
				public String variantName() {
					return name;
				}

				@Override
				public int processDuration(int duration) {
					return processDuration.applyAsInt(duration);
				}

				@Override
				public int processAmplifier(int amplifier) {
					return processAmplifier.applyAsInt(amplifier);
				}

				@Override
				public void addMix(PotionType base, PotionType type) {
					addMix.accept(base, type);
				}
			};
		}

		static ISimpleVariant of(String name,
								 IntUnaryOperator processDuration,
								 IntUnaryOperator processAmplifier,
								 Supplier<Ingredient> mixture) {
			return of(name, processDuration, processAmplifier, (t, u) -> PotionHelper.addMix(t, mixture.get(), u));
		}

	}

	enum VanillaVariant implements ISimpleVariant {
		LONG {
			@Override
			public int processDuration(int duration) {
				return duration * 8 / 3;
			}

			@Override
			public void addMix(PotionType base, PotionType type) {
				PotionHelper.addMix(base, Items.REDSTONE, type);
			}
		},
		STRONG {
			@Override
			public int processDuration(int duration) {
				return duration / 2;
			}

			@Override
			public int processAmplifier(int amplifier) {
				return amplifier + 1;
			}

			@Override
			public void addMix(PotionType base, PotionType type) {
				PotionHelper.addMix(base, Items.GLOWSTONE_DUST, type);
			}
		},
		STRONG_POISON {
			@Override
			public int processDuration(int duration) {
				return duration * 432 / 900;
			}

			@Override
			public int processAmplifier(int amplifier) {
				return amplifier + 1;
			}

			@Override
			public void addMix(PotionType base, PotionType type) {
				PotionHelper.addMix(base, Items.GLOWSTONE_DUST, type);
			}
		},
		/**
		 * From Latest Version: Potion of Slowness IV
		 * */
		STRONG_SLOWNESS {
			@Override
			public int processDuration(int duration) {
				return duration * 2 / 9;
			}

			@Override
			public int processAmplifier(int amplifier) {
				return amplifier + 3;
			}

			@Override
			public void addMix(PotionType base, PotionType type) {
				PotionHelper.addMix(base, Items.GLOWSTONE_DUST, type);
			}
		};

		@Override
		public String variantName() {
			return name().toLowerCase();
		}

	}

	final class VanillaBaseDurations {
		public static final int SHORT = 900;	// 00:45
		public static final int NORMAL = 1800;	// 01:30
		public static final int LONG = 3600;	// 03:00
		/**
		 * From Latest Version: Potion of the Turtle Master
		 * */
		public static final int TURTLE = 400;   // 00:20
	}

	static IPotionTypeLoader simpleVanilla(int baseDuration, Supplier<Ingredient> ingredient, ISimpleVariant... variants) {
		return potions -> {
			checkArgument(potions.length == 1);
			Potion potion = potions[0];
			String name = potion.getRegistryName().getResourcePath();
			PotionType base = new PotionTypeModID(new PotionEffect(potion, baseDuration)).setRegistryName(name);
			Set<Runnable> set = new HashSet<>();
			set.add(() -> PotionHelper.addMix(PotionTypes.AWKWARD, ingredient.get(), base));
			return Pair.of(ArrayUtils.addAll(new PotionType[] {base}, Arrays.stream(variants).map(variant -> {
				PotionType type = new PotionTypeModID(name, new PotionEffect(potion,
						variant.processDuration(baseDuration),
						variant.processAmplifier(0)))
						.setRegistryName(variant.variantName() + "_" + name);
				set.add(() -> variant.addMix(base, type));
				return type;
			}).toArray(PotionType[]::new)), () -> set.forEach(Runnable::run));
		};
	}

	/**
	 * From Latest Version: Potion of the Turtle Master
	 * */
	static IPotionTypeLoader complexVanilla(String name, int baseDuration, Supplier<Ingredient> ingredient, ISimpleVariant... variants) {
		return potions -> {
			PotionType base = new PotionTypeModID(Arrays.stream(potions)
					.map(potion -> new PotionEffect(potion, baseDuration))
					.toArray(PotionEffect[]::new)).setRegistryName(name);
			Set<Runnable> set = new HashSet<>();
			set.add(() -> PotionHelper.addMix(PotionTypes.AWKWARD, ingredient.get(), base));
			return Pair.of(ArrayUtils.addAll(new PotionType[] {base}, Arrays.stream(variants).map(variant -> {
				PotionType type = new PotionTypeModID(name, Arrays.stream(potions)
						.map(potion -> new PotionEffect(potion,
								variant.processDuration(baseDuration),
								variant.processAmplifier(0)))
						.toArray(PotionEffect[]::new))
						.setRegistryName(variant.variantName() + "_" + name);
				set.add(() -> variant.addMix(base, type));
				return type;
			}).toArray(PotionType[]::new)), () -> set.forEach(Runnable::run));
		};
	}

}
