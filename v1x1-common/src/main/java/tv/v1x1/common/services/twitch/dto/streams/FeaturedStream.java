package tv.v1x1.common.services.twitch.dto.streams;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 10/30/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FeaturedStream {
    @JsonProperty
    private String image;
    @JsonProperty
    private long priority;
    @JsonProperty
    private boolean scheduled;
    @JsonProperty
    private boolean sponsored;
    @JsonProperty
    private Stream stream;
    @JsonProperty
    private String text;
    @JsonProperty
    private String title;

    public String getImage() {
        return image;
    }

    public void setImage(final String image) {
        this.image = image;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public boolean isSponsored() {
        return sponsored;
    }

    public void setSponsored(final boolean sponsored) {
        this.sponsored = sponsored;
    }

    public boolean isScheduled() {
        return scheduled;
    }

    public void setScheduled(final boolean scheduled) {
        this.scheduled = scheduled;
    }

    public Stream getStream() {
        return stream;
    }

    public void setStream(final Stream stream) {
        this.stream = stream;
    }

    public long getPriority() {
        return priority;
    }

    public void setPriority(final long priority) {
        this.priority = priority;
    }
}
