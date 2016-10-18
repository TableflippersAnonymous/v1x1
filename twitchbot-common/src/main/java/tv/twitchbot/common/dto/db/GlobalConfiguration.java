package tv.twitchbot.common.dto.db;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

/**
 * Created by cobi on 10/17/2016.
 */
@Table(name = "global_configuration")
public class GlobalConfiguration {
    @PartitionKey
    private String module;
    private String json;

    public GlobalConfiguration(String module, String json) {
        this.module = module;
        this.json = json;
    }

    public String getModule() {
        return module;
    }

    public String getJson() {
        return json;
    }
}
