package sausage_core.api.util.registry;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A registry interface just like {@link net.minecraftforge.registries.IForgeRegistry}
 * but much easier and friendlier for modders
 * */
public interface IModdedRegistry<E> extends Iterable<E> {
    Collection<E> view();
    Class<E> type();

    void register(E e);
    void remove(E e);

    @SuppressWarnings("unchecked")
    default void registerAll(E... es) {
        for(E e : es) register(e);
    }
    default void registerAll(Collection<E> es) {
        for(E e : es) register(e);
    }
    @SuppressWarnings("unchecked")
    default void removeAll(E... es) {
        for(E e : es) remove(e);
    }
    default void removeAll(Collection<E> es) {
        for(E e : es) remove(e);
    }
    default void removeIf(Predicate<E> predicate) {
        for(E e : findAll(predicate)) remove(e);
    }

    @Override
    default Iterator<E> iterator() { return view().iterator(); }
    default Stream<E> stream() { return view().stream(); }

    default Optional<E> find(Predicate<E> predicate) {
        return stream().filter(predicate).findAny();
    }
    default List<E> findAll(Predicate<E> predicate) {
        return stream().filter(predicate).collect(Collectors.toList());
    }
}
