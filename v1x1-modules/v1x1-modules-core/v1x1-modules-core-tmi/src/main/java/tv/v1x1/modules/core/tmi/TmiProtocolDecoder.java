package tv.v1x1.modules.core.tmi;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class TmiProtocolDecoder extends MessageToMessageDecoder<String> {
    @Override
    protected void decode(final ChannelHandlerContext ctx, final String msg, final List<Object> out) {
        out.add(IrcParser.parse(msg));
    }
}
