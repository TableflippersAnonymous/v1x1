package tv.v1x1.common.services.twitch.dto.blocks;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by naomi on 10/29/2016.
 */
public class BlockList {
    @JsonProperty
    private List<Block> blocks;

    public BlockList() {
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void setBlocks(final List<Block> blocks) {
        this.blocks = blocks;
    }
}
