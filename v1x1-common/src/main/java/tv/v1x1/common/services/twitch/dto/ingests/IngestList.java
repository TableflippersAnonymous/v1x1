package tv.v1x1.common.services.twitch.dto.ingests;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by cobi on 10/30/2016.
 */
public class IngestList {
    @JsonProperty
    private List<Ingest> ingests;

    public IngestList() {
    }

    public List<Ingest> getIngests() {
        return ingests;
    }

    public void setIngests(final List<Ingest> ingests) {
        this.ingests = ingests;
    }
}
