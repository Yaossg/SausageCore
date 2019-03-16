package sausage_core.api.core.ienum;

import java.util.function.Predicate;

public interface IEnumCyclic<E extends Enum<E> & IEnumCyclic<E>> extends IEnum {
    default E offset(int i) {
        E e = getEnum();
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
        E e = getEnum();
        predicate = predicate.negate();
        while (predicate.test(e = e.next()));
        return e;
    }
    default E nextOrSame(Predicate<E> predicate) {
        E e = getEnum();
        return predicate.test(e) ? e : next(predicate);
    }
    default E previous(Predicate<E> predicate) {
        E e = getEnum();
        predicate = predicate.negate();
        while (predicate.test(e = e.previous()));
        return e;
    }
    default E previousOrSame(Predicate<E> predicate) {
        E e = getEnum();
        return predicate.test(e) ? e : previous(predicate);
    }
}