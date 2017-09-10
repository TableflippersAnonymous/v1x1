package tv.v1x1.common.services.discord.dto.voice;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 9/17/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class VoiceRegion {
    @JsonProperty
    private String id;
    @JsonProperty
    private String name;
    @JsonProperty("sample_hostname")
    private String sampleHostname;
    @JsonProperty("sample_port")
    private Integer samplePort;
    @JsonProperty
    private boolean vip;
    @JsonProperty
    private boolean optimal;
    @JsonProperty
    private boolean deprecated;
    @JsonProperty
    private boolean custom;

    public VoiceRegion() {
    }

    public VoiceRegion(final String id, final String name, final String sampleHostname, final Integer samplePort,
                       final boolean vip, final boolean optimal, final boolean deprecated, final boolean custom) {
        this.id = id;
        this.name = name;
        this.sampleHostname = sampleHostname;
        this.samplePort = samplePort;
        this.vip = vip;
        this.optimal = optimal;
        this.deprecated = deprecated;
        this.custom = custom;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getSampleHostname() {
        return sampleHostname;
    }

    public void setSampleHostname(final String sampleHostname) {
        this.sampleHostname = sampleHostname;
    }

    public Integer getSamplePort() {
        return samplePort;
    }

    public void setSamplePort(final Integer samplePort) {
        this.samplePort = samplePort;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(final boolean vip) {
        this.vip = vip;
    }

    public boolean isOptimal() {
        return optimal;
    }

    public void setOptimal(final boolean optimal) {
        this.optimal = optimal;
    }

    public boolean isDeprecated() {
        return deprecated;
    }

    public void setDeprecated(final boolean deprecated) {
        this.deprecated = deprecated;
    }

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(final boolean custom) {
        this.custom = custom;
    }
}
