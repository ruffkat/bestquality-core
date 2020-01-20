package io.bestquality.lang;

import java.util.function.Function;

import static java.util.Objects.requireNonNull;

@FunctionalInterface
public interface CheckedFunction<T, R> {
    R apply(T value)
            throws Exception;

    default Function<T, R> asFunction() {
        return value -> {
            try {
                return apply(value);
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    default <V> CheckedFunction<V, R> compose(CheckedFunction<? super V, ? extends T> before) {
        requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    default <V> CheckedFunction<T, V> andThen(CheckedFunction<? super R, ? extends V> after) {
        requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }
}