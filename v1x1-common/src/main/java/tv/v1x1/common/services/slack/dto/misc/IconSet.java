package tv.v1x1.common.services.slack.dto.misc;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IconSet {
    @JsonProperty("image_36")
    private String image36;
    @JsonProperty("image_48")
    private String image48;
    @JsonProperty("image_72")
    private String image72;

    public IconSet() {
    }

    public IconSet(final String image36, final String image48, final String image72) {
        this.image36 = image36;
        this.image48 = image48;
        this.image72 = image72;
    }

    public String getImage36() {
        return image36;
    }

    public void setImage36(final String image36) {
        this.image36 = image36;
    }

    public String getImage48() {
        return image48;
    }

    public void setImage48(final String image48) {
        this.image48 = image48;
    }

    public String getImage72() {
        return image72;
    }

    public void setImage72(final String image72) {
        this.image72 = image72;
    }
}
