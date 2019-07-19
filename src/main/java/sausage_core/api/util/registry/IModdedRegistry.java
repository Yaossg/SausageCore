package sausage_core.api.util.registry;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A simple registry interface designed for modders
 */
public interface IModdedRegistry<E> extends Iterable<E> {
	Collection<E> view();

	Class<E> type();

	void register(E e);

	@SuppressWarnings("unchecked")
	default void registerAll(E... es) {
		for (E e : es) register(e);
	}

	default void registerAll(Collection<E> es) {
		for (E e : es) register(e);
	}

	@Override
	default Iterator<E> iterator() { return view().iterator(); }

	default Spliterator<E> spliterator() { return view().spliterator(); }

	default Stream<E> stream() { return view().stream(); }

	default Stream<E> parallelStream() { return view().parallelStream(); }

	default int size() { return view().size(); }

	@SuppressWarnings("unchecked")
	default E[] toArray() {
		return view().toArray((E[]) Array.newInstance(type(), 0));
	}

	default Optional<E> find(Predicate<E> predicate) {
		return stream().filter(predicate).findAny();
	}

	default List<E> findAll(Predicate<E> predicate) {
		return stream().filter(predicate).collect(Collectors.toList());
	}

	default boolean contains(Predicate<E> predicate) {
		return stream().anyMatch(predicate);
	}
}
