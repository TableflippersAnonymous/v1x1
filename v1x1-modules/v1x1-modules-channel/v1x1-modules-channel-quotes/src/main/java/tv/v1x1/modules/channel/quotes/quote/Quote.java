package tv.v1x1.modules.channel.quotes.quote;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * @author Josh
 */
@Table(name = "quote")
public class Quote {
    @PartitionKey(0)
    @Column(name = "tenant_id")
    private UUID tenantId;
    @PartitionKey(1)
    private int id;
    private String quote;
    private String game;
    private Date date;
    private boolean deleted;

    Quote() {
        // For serialization
    }

    public Quote(final tv.v1x1.common.dto.core.UUID tenantId, final String quote, final String game, final int quoteId) {
        this.tenantId = tenantId.getValue();
        this.quote = quote;
        this.date = Calendar.getInstance().getTime();
        if(game != null && game.isEmpty())
            this.game = null;
        else
            this.game = game;
        this.id = quoteId;
        this.deleted = false;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public int getId() {
        return id;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(final String quote) {
        this.quote = quote;
    }

    public String getGame() {
        return game;
    }

    public Date getDate() {
        return date;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void delete() {
        this.deleted = true;
        this.quote = null;
        this.game = null;
        this.date = null;
    }
}
