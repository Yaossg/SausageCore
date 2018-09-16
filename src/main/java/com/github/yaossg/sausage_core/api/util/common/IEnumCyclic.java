package com.github.yaossg.sausage_core.api.util.common;

import java.util.function.Predicate;

import static com.github.yaossg.sausage_core.api.util.common.SausageUtils.rawtype;

public interface IEnumCyclic<E extends Enum<E> & IEnumCyclic<E>> {
    default E offset(int i) {
        E e = rawtype(this);
        E[] values = e.getDeclaringClass().getEnumConstants();
        while (i < 0) i += values.length;
        return values[(e.ordinal() + i) % values.length];
    }
    default E next() {
        return offset(1);
    }
    default E previous() {
        return offset(-1);
    }
    default E next(Predicate<E> predicate) {
        E e = rawtype(this);
        predicate = predicate.negate();
        while (predicate.test(e = e.next()));
        return e;
    }
    default E previous(Predicate<E> predicate) {
        E e = rawtype(this);
        predicate = predicate.negate();
        while (predicate.test(e = e.previous()));
        return e;
    }
}