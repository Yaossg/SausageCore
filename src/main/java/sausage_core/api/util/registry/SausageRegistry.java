package sausage_core.api.util.registry;

import net.minecraft.util.NonNullList;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Simple implementation for {@link IModdedRegistry}
 * */
public class SausageRegistry<E> implements IModdedRegistry<E> {
    List<E> entries = NonNullList.create();
    Collection<E> view = Collections.unmodifiableCollection(entries);
    Class<E> type;

    public SausageRegistry(Class<E> type) {
        this.type = type;
    }

    @Override
    public void register(E e) {
        checkArgument(type.isInstance(e));
        entries.add(e);
    }

    @Override
    public void remove(E e) {
        entries.remove(e);
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
