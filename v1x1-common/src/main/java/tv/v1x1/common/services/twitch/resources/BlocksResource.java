package tv.v1x1.common.services.twitch.resources;

import com.google.common.collect.AbstractIterator;
import tv.v1x1.common.services.twitch.TwitchApi;
import tv.v1x1.common.services.twitch.dto.blocks.Block;
import tv.v1x1.common.services.twitch.dto.blocks.BlockList;

import javax.ws.rs.client.WebTarget;
import java.util.Iterator;
import java.util.List;

/**
 * Created by cobi on 10/28/2016.
 */
public class BlocksResource {
    private final WebTarget users;

    public BlocksResource(final WebTarget users) {
        this.users = users;
    }

    public Iterable<Block> get(final String user) {
        return () -> new AbstractIterator<Block>() {
            private int offset = 0;
            private List<Block> results;
            private Iterator<Block> iterator;

            @Override
            protected Block computeNext() {
                if(results == null) {
                    results = users.path(user).path("blocks").queryParam("limit", "100").queryParam("offset", offset).request(TwitchApi.ACCEPT).get().readEntity(BlockList.class).getBlocks();
                    iterator = results.iterator();
                    if(!iterator.hasNext())
                        return endOfData();
                }
                if(iterator.hasNext()) {
                    offset++;
                    return iterator.next();
                }
                if(results.size() == 100) {
                    results = null;
                    iterator = null;
                    return computeNext();
                }
                return endOfData();
            }
        };
    }

    public Block block(final String user, final String target) {
        return users.path(user).path("blocks").path(target).request(TwitchApi.ACCEPT).put(null).readEntity(Block.class);
    }

    public void unblock(final String user, final String target) {
        users.path(user).path("blocks").path(target).request(TwitchApi.ACCEPT).delete();
    }
}
