package tv.v1x1.modules.channel.quotes.quote;

import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.Result;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;

import java.util.List;
import java.util.UUID;

/**
 * @author Josh
 */
public class DAOQuote {
    private final Mapper<Quote> mapper;
    private final QuoteAccessor accessor;

    @Accessor
    public interface QuoteAccessor {
        @Query("SELECT * FROM quote WHERE tenant_id = ?")
        Result<Quote> all(final UUID tenantId);
        @Query("SELECT * FROM quote WHERE tenant_id = ? ORDER BY id DESC LIMIT 1")
        Result<Quote> last(final UUID tenantId);
    }

    public DAOQuote(final MappingManager mappingManager) {
        mapper = mappingManager.mapper(Quote.class);
        accessor = mappingManager.createAccessor(QuoteAccessor.class);
    }

    /**
     * returns a quote, or null if it's deleted/doesn't exist
     * @param tenantId
     * @param id
     * @return
     */
    public Quote getByid(final UUID tenantId, final int id) {
        final Quote quote = mapper.get(tenantId, id);
        if(quote == null || quote.isDeleted()) return null;
        return quote;
    }

    /**
     * returns the last quote, regardless of deletion status
     * @param tenantId
     * @return
     */
    public Quote getLastQuote(final UUID tenantId) {
        final Result<Quote> res = accessor.last(tenantId);
        return res.one();
    }

    public List<Quote> all(final UUID tenantId) {
        return accessor.all(tenantId).all();
    }

    public void save(final Quote quote) {
        mapper.save(quote);
    }
}
