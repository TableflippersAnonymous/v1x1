package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Paging<T> extends SimplePaging {
    @JsonProperty
    private List<T> items;
    @JsonProperty
    private long limit;
    @JsonProperty
    private String next;
    @JsonProperty
    private long offset;
    @JsonProperty
    private String previous;

    public Paging() {
    }

    public Paging(final String href, final List<T> items, final long limit, final String next, final long offset,
                  final String previous, final long total) {
        super(href, total);
        this.items = items;
        this.limit = limit;
        this.next = next;
        this.offset = offset;
        this.previous = previous;
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(final List<T> items) {
        this.items = items;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(final long limit) {
        this.limit = limit;
    }

    public String getNext() {
        return next;
    }

    public void setNext(final String next) {
        this.next = next;
    }

    public long getOffset() {
        return offset;
    }

    public void setOffset(final long offset) {
        this.offset = offset;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(final String previous) {
        this.previous = previous;
    }
}
