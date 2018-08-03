package tv.v1x1.modules.channel.voicelog;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.codehaus.jackson.annotate.JsonProperty;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.ChannelRef;
import tv.v1x1.common.modules.BasicUserConfiguration;
import tv.v1x1.common.scanners.config.*;

/**
 * @author Josh
 */
@ModuleConfig("voicelog")
@DisplayName("Voice Log")
@Description("Log voice channel join/parts to a text channel")
@TenantPermission(Permission.READ_WRITE)
@Version(0)
public class VoiceLogUserConfiguration extends BasicUserConfiguration {
    @DisplayName("Channel")
    @Description("The channel to log voice activity to")
    @Type(ConfigType.CHANNEL)
    @JsonProperty("channel")
    private ChannelRef channel;

    public ChannelRef getChannel() {
        return channel;
    }

    public void setChannel(final Channel channel) {
        if(channel != null)
            this.channel = new ChannelRef(channel.getPlatform(), channel.getId());
        else
            this.channel = null;
    }

    @JsonSetter("channel")
    private void setChannel(final ChannelRef ref) {
        // For Jackson deserialization
        this.channel = ref;
    }
}
