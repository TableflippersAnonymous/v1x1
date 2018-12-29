package tv.v1x1.modules.core.tmi;

import io.netty.handler.codec.LineBasedFrameDecoder;

public class TmiFrameDecoder extends LineBasedFrameDecoder {
    public TmiFrameDecoder() {
        super(4096, true, false);
    }
}
