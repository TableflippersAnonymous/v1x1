package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CursorPaging<T> {
    @JsonProperty
    private String href;
    @JsonProperty
    private List<T> items;
    @JsonProperty
    private int limit;
    @JsonProperty
    private String next;
    @JsonProperty
    private Cursor cursors;
    @JsonProperty
    private int total;

    public CursorPaging() {
    }

    public CursorPaging(final String href, final List<T> items, final int limit, final String next,
                        final Cursor cursors, final int total) {
        this.href = href;
        this.items = items;
        this.limit = limit;
        this.next = next;
        this.cursors = cursors;
        this.total = total;
    }

    public String getHref() {
        return href;
    }

    public void setHref(final String href) {
        this.href = href;
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

    public Cursor getCursors() {
        return cursors;
    }

    public void setCursors(final Cursor cursors) {
        this.cursors = cursors;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(final int total) {
        this.total = total;
    }
}
