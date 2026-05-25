package biblioteca.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public final class IdGenerator {
    private static final ConcurrentHashMap<Class<?>, AtomicInteger> SEQUENCES = new ConcurrentHashMap<>();

    private IdGenerator() {
    }

    public static int nextId(Class<?> type) {
        return SEQUENCES.computeIfAbsent(type, key -> new AtomicInteger(0)).incrementAndGet();
    }
}
