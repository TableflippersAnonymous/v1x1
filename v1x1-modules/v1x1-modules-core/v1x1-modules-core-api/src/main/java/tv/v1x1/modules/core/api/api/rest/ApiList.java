package tv.v1x1.modules.core.api.api.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableList;

import java.util.List;

/**
 * Created by naomi on 12/31/2016.
 */
public class ApiList<T> {
    @JsonProperty
    private long total;
    @JsonProperty
    private long returned;
    @JsonProperty
    private boolean more = false;
    @JsonProperty("continuation_token")
    private String continuationToken = "";
    @JsonProperty
    private List<T> entries;

    public ApiList() {
        this(ImmutableList.of());
    }

    public ApiList(final List<T> entries) {
        this(entries.size(), false, "", entries);
    }

    public ApiList(final boolean more, final String continuationToken, final List<T> entries) {
        this(-1, more, continuationToken, entries);
    }

    public ApiList(final long total, final boolean more, final String continuationToken, final List<T> entries) {
        this.total = total;
        this.more = more;
        this.continuationToken = continuationToken;
        this.entries = entries;
        this.returned = entries.size();
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(final long total) {
        this.total = total;
    }

    public long getReturned() {
        return returned;
    }

    public void setReturned(final long returned) {
        this.returned = returned;
    }

    public boolean isMore() {
        return more;
    }

    public void setMore(final boolean more) {
        this.more = more;
    }

    public String getContinuationToken() {
        return continuationToken;
    }

    public void setContinuationToken(final String continuationToken) {
        this.continuationToken = continuationToken;
    }

    public List<T> getEntries() {
        return entries;
    }

    public void setEntries(final List<T> entries) {
        this.entries = entries;
    }
}
