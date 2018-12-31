package tv.v1x1.common.util;

import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadBlockingQueue<T> extends LinkedBlockingQueue<T> {
    public ThreadBlockingQueue(final int capacity) {
        super(capacity);
    }

    public ThreadBlockingQueue(final Collection<? extends T> c) {
        super(c);
    }

    @Override
    public boolean offer(final T t) {
        try {
            put(t);
            return true;
        } catch (final InterruptedException e) {
            return false;
        }
    }
}
