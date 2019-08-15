package sausage_core.api.util.common;

import jdk.nashorn.internal.ir.annotations.Immutable;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkNotNull;

@Immutable
public final class LazyOptional<T> {
	private final Supplier<T> supplier;
	private AtomicReference<T> resolved;
	private static final LazyOptional<Void> EMPTY = new LazyOptional<>(null);

	public static <T> LazyOptional<T> empty() {
		return SausageUtils.rawtype(EMPTY);
	}

	public static <T> LazyOptional<T> of(Supplier<T> supplier) {
		return new LazyOptional<>(checkNotNull(supplier));
	}

	public static <T> LazyOptional<T> ofNullable(Supplier<T> supplier) {
		return supplier == null ? empty() : new LazyOptional<>(supplier);
	}

	public LazyOptional(Supplier<T> supplier) {
		this.supplier = supplier;
	}

	private T resolve() {
		if (resolved != null)
			return resolved.get();
		if (supplier != null)
			return (resolved = new AtomicReference<>(checkNotNull(
					supplier.get(), "Supplier must not return null value"))).get();
		return null;
	}

	public void invalidate() {
		resolved = null;
	}

	public boolean isPresent() {
		return supplier != null;
	}

	public T get() {
		if (!isPresent()) throw new NoSuchElementException();
		return resolve();
	}

	public void ifPresent(Consumer<? super T> consumer) {
		checkNotNull(consumer);
		if (isPresent())
			consumer.accept(get());
	}

	public void ifPresentElse(Consumer<? super T> consumer, Runnable runnable) {
		checkNotNull(consumer);
		checkNotNull(runnable);
		if (isPresent())
			consumer.accept(get());
		else
			runnable.run();
	}

	public <U> LazyOptional<U> map(Function<? super T, ? extends U> mapper) {
		checkNotNull(mapper);
		return isPresent() ? of(() -> mapper.apply(resolve())) : empty();
	}

	public LazyOptional<T> filter(Predicate<? super T> predicate) {
		checkNotNull(predicate);
		T value = resolve(); // To keep the non-null contract we have to evaluate right now
		return value != null && predicate.test(value) ? of(() -> value) : empty();
	}

	public LazyOptional<T> orLazy(Supplier<T> other) {
		return isPresent() ? this : of(other);
	}

	public LazyOptional<T> orLazyNullable(Supplier<T> other) {
		return isPresent() ? this : ofNullable(other);
	}

	public T or(T other) {
		return isPresent() ? get() : other;
	}

	public T orGet(Supplier<? extends T> other) {
		return isPresent() ? get() : other.get();
	}

	public <X extends Throwable> T orThrow(Supplier<? extends X> supplier) throws X {
		if (isPresent())
			return get();
		throw supplier.get();
	}
}
