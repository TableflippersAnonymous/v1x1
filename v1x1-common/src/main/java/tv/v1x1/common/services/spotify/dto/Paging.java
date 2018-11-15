package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Paging<T> extends SimplePaging {
    @JsonProperty
    private List<T> items;
    @JsonProperty
    private int limit;
    @JsonProperty
    private String next;
    @JsonProperty
    private int offset;
    @JsonProperty
    private String previous;

    public Paging() {
    }

    public Paging(final String href, final List<T> items, final int limit, final String next, final int offset,
                  final String previous, final int total) {
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

    public int getLimit() {
        return limit;
    }

    public void setLimit(final int limit) {
        this.limit = limit;
    }

    public String getNext() {
        return next;
    }

    public void setNext(final String next) {
        this.next = next;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(final int offset) {
        this.offset = offset;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(final String previous) {
        this.previous = previous;
    }
}
