package sausage_core.api.core.registry;

import net.minecraft.util.NonNullList;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiPredicate;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Simple implementation for {@link IModdedRegistry}
 */
public class SimpleRegistry<E> implements IModdedRegistry<E> {
	private final List<E> entries = NonNullList.create();
	private final List<E> view = Collections.unmodifiableList(entries);
	private final Class<E> type;
	BiPredicate<SimpleRegistry<E>, E> validator = (registry, e) -> true;

	public SimpleRegistry(Class<E> type) {
		this.type = type;
	}

	public SimpleRegistry<E> validator(BiPredicate<SimpleRegistry<E>, E> validator) {
		this.validator = validator;
		return this;
	}

	@Override
	public void register(E e) {
		checkArgument(type.isInstance(e));
		checkArgument(validator.test(this, e));
		entries.add(e);
	}

	@Override
	public Collection<E> view() {
		return view;
	}

	@Override
	public Class<E> type() {
		return type;
	}
}
