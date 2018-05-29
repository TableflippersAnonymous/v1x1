package tv.v1x1.modules.channel.voicelog;

import org.codehaus.jackson.annotate.JsonProperty;
import tv.v1x1.common.dto.core.Channel;
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
    private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(final Channel channel) {
        this.channel = channel;
    }
}
