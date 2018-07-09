package cn.qr.cryptozoic.function;

import java.lang.reflect.Method;
import java.util.function.*;

/**
 * @author kelaite
 * 2018/3/10
 */
public class ProxyFunction<T, U, R> implements Function<T, R>, BiFunction<T, U, R>, Consumer<T>, BiConsumer<T, U>, Supplier<T> {
    private Function<T, R> function;
    private BiFunction<T, U, R> biFunction;
    private Consumer<T> consumer;
    private BiConsumer<T, U> biConsumer;
    private Supplier<T> supplier;
    private int marker;
    private Method method;

    ProxyFunction(Function<T, R> function, Method method) {
        this.function = function;
        this.marker = 0;
        this.method = method;
    }

    ProxyFunction(BiFunction<T, U, R> biFunction, Method method) {
        this.biFunction = biFunction;
        this.marker = 1;
        this.method = method;
    }

    ProxyFunction(Consumer<T> consumer, Method method) {
        this.consumer = consumer;
        this.marker = 2;
        this.method = method;
    }

    public ProxyFunction(BiConsumer<T, U> biConsumer, Method method) {
        this.biConsumer = biConsumer;
        this.marker = 3;
        this.method = method;
    }

    ProxyFunction(Supplier<T> supplier, Method method) {
        this.supplier = supplier;
        this.marker = 4;
        this.method = method;
    }

    public String whatFunction() {
        switch (marker) {
            case 0:
                return "Function";
            case 1:
                return "BiFunction";
            case 2:
                return "Consumer";
            case 3:
                return "BiConsumer";
            case 4:
                return "Supplier";
            default:
                throw new RuntimeException("no way");
        }
    }

    @Override
    public <V> ProxyFunction andThen(Function<? super R, ? extends V> after) {
        throw new RuntimeException("not yet");
    }

    @Override
    public R apply(T t) {
        return function.apply(t);
    }


    @Override
    public R apply(T t, U u) {
        return biFunction.apply(t, u);
    }

    @Override
    public void accept(T t) {
        consumer.accept(t);
    }

    @Override
    public void accept(T t, U u) {
        biConsumer.accept(t, u);
    }

    @Override
    public T get() {
        return supplier.get();
    }

    @Override
    public String toString() {
        return whatFunction() + "=>" + method;
    }
}
