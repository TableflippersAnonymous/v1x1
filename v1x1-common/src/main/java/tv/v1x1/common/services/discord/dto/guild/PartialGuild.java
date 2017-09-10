package tv.v1x1.common.services.discord.dto.guild;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by naomi on 9/11/2017.
 */
public class PartialGuild extends UnavailableGuild {
    @JsonProperty
    private String name;
    @JsonProperty
    private String icon;
    @JsonProperty
    private String splash;

    public PartialGuild() {
    }

    public PartialGuild(final String id, final boolean unavailable, final String name, final String icon,
                        final String splash) {
        super(id, unavailable);
        this.name = name;
        this.icon = icon;
        this.splash = splash;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(final String icon) {
        this.icon = icon;
    }

    public String getSplash() {
        return splash;
    }

    public void setSplash(final String splash) {
        this.splash = splash;
    }
}
