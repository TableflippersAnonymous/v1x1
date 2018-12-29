package tv.v1x1.modules.core.tmi;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import tv.v1x1.common.dto.irc.IrcStanza;

import java.util.List;

public class TmiProtocolDecoder extends MessageToMessageDecoder<String> {
    @Override
    protected void decode(final ChannelHandlerContext ctx, final String msg, final List<Object> out) {
        final IrcStanza ircStanza = IrcParser.parse(msg);
        if(ircStanza != null)
            out.add(ircStanza);
    }
}
