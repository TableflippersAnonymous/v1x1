package tv.v1x1.common.rpc.client;

import com.google.common.base.Joiner;

import java.util.List;

/**
 * Created by cobi on 11/6/2016.
 */
public class RpcException extends Exception {
    public RpcException(final String exceptionClass, final String message, final List<String> stackTrace) {
        super(exceptionClass + ": " + message + "\n" + Joiner.on("\n").join(stackTrace), null, false, false);
    }
}
