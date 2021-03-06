package tv.v1x1.common.rpc.client;

import com.google.common.util.concurrent.AbstractFuture;

import javax.annotation.Nullable;

/**
 * Created by cobi on 10/8/2016.
 */
public class ServiceFuture<U> extends AbstractFuture<U> {
    @Override
    public boolean set(@Nullable final U value) {
        return super.set(value);
    }

    @Override
    public boolean setException(final Throwable throwable) {
        return super.setException(throwable);
    }
}
