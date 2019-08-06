package sausage_core.api.util.common;

import net.minecraft.item.ItemStack;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public final class Opts {
	public static Optional<String> opt(String string) {
		return string.isEmpty() ? Optional.empty() : Optional.of(string);
	}

	public static String string(Optional<String> opt) {
		return opt.orElse("");
	}

	public static Optional<ItemStack> opt(ItemStack stack) {
		return stack.isEmpty() ? Optional.empty() : Optional.of(stack);
	}

	public static ItemStack stack(Optional<ItemStack> opt) {
		return opt.orElse(ItemStack.EMPTY);
	}

	public static OptionalInt parseInt(String s) {
		try {
			return OptionalInt.of(Integer.parseInt(s));
		} catch (NumberFormatException e) {
			return OptionalInt.empty();
		}
	}

	public static OptionalLong parseLong(String s) {
		try {
			return OptionalLong.of(Long.parseLong(s));
		} catch (NumberFormatException e) {
			return OptionalLong.empty();
		}
	}

	public static OptionalDouble parseDouble(String s) {
		try {
			return OptionalDouble.of(Double.parseDouble(s));
		} catch (NumberFormatException e) {
			return OptionalDouble.empty();
		}
	}
}
