package sausage_core.api.util.common;

public interface IEqualityComparator<T> {
    boolean areEqual(T a, T b);
}
