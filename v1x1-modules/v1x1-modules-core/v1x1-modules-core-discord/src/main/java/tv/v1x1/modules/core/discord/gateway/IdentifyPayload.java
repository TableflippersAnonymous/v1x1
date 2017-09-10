package tv.v1x1.modules.core.discord.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import tv.v1x1.common.services.discord.dto.user.StatusUpdate;

import java.util.List;

/**
 * Created by cobi on 9/10/2017.
 */
@JsonDeserialize
public class IdentifyPayload extends Payload {
    public static class ConnectionProperties {
        @JsonProperty("$os")
        private String os;
        @JsonProperty("$browser")
        private String browser;
        @JsonProperty("$device")
        private String device;

        public ConnectionProperties() {
        }

        public ConnectionProperties(final String os, final String browser, final String device) {
            this.os = os;
            this.browser = browser;
            this.device = device;
        }

        public String getOs() {
            return os;
        }

        public void setOs(final String os) {
            this.os = os;
        }

        public String getBrowser() {
            return browser;
        }

        public void setBrowser(final String browser) {
            this.browser = browser;
        }

        public String getDevice() {
            return device;
        }

        public void setDevice(final String device) {
            this.device = device;
        }
    }

    public static class IdentifyData {
        @JsonProperty
        private String token;
        @JsonProperty
        private ConnectionProperties properties;
        @JsonProperty
        private boolean compress;
        @JsonProperty("large_threshold")
        private Integer largeThreshold;
        @JsonProperty
        private List<Integer> shard;
        @JsonProperty
        private StatusUpdate presence;

        public IdentifyData() {
        }

        public IdentifyData(final String token, final ConnectionProperties properties, final boolean compress,
                            final Integer largeThreshold, final List<Integer> shard, final StatusUpdate presence) {
            this.token = token;
            this.properties = properties;
            this.compress = compress;
            this.largeThreshold = largeThreshold;
            this.shard = shard;
            this.presence = presence;
        }

        public String getToken() {
            return token;
        }

        public void setToken(final String token) {
            this.token = token;
        }

        public ConnectionProperties getProperties() {
            return properties;
        }

        public void setProperties(final ConnectionProperties properties) {
            this.properties = properties;
        }

        public boolean isCompress() {
            return compress;
        }

        public void setCompress(final boolean compress) {
            this.compress = compress;
        }

        public Integer getLargeThreshold() {
            return largeThreshold;
        }

        public void setLargeThreshold(final Integer largeThreshold) {
            this.largeThreshold = largeThreshold;
        }

        public List<Integer> getShard() {
            return shard;
        }

        public void setShard(final List<Integer> shard) {
            this.shard = shard;
        }

        public StatusUpdate getPresence() {
            return presence;
        }

        public void setPresence(final StatusUpdate presence) {
            this.presence = presence;
        }
    }

    @JsonProperty("d")
    private IdentifyData identifyData;

    public IdentifyPayload() {
    }

    public IdentifyPayload(final IdentifyData identifyData) {
        super(2);
        this.identifyData = identifyData;
    }

    public IdentifyData getIdentifyData() {
        return identifyData;
    }

    public void setIdentifyData(final IdentifyData identifyData) {
        this.identifyData = identifyData;
    }
}
