package tv.v1x1.common.services.discord.dto.channel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by cobi on 9/11/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BulkDeleteMessagesRequest {
    @JsonProperty("messages")
    private List<String> messageIds;

    public BulkDeleteMessagesRequest() {
    }

    public BulkDeleteMessagesRequest(final List<String> messageIds) {
        this.messageIds = messageIds;
    }

    public List<String> getMessageIds() {
        return messageIds;
    }

    public void setMessageIds(final List<String> messageIds) {
        this.messageIds = messageIds;
    }
}
