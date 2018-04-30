package tv.v1x1.modules.channel.factoids.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import tv.v1x1.common.modules.BasicUserConfiguration;
import tv.v1x1.common.scanners.config.ComplexType;
import tv.v1x1.common.scanners.config.ConfigType;
import tv.v1x1.common.scanners.config.Description;
import tv.v1x1.common.scanners.config.DisplayName;
import tv.v1x1.common.scanners.config.ModuleConfig;
import tv.v1x1.common.scanners.config.Type;
import tv.v1x1.common.scanners.config.Version;
import tv.v1x1.modules.channel.factoids.Factoid;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Josh
 */
@ModuleConfig("factoids")
@DisplayName("Factoids")
@Description("This module adds factoids -- or custom commands, if you will")
@Version(2)
public class FactoidsUserConfiguration extends BasicUserConfiguration {
    @DisplayName("Factoids")
    @Description("List of facts you've made")
    @JsonProperty("factoids")
    @Type(ConfigType.COMPLEX_STRING_MAP)
    @ComplexType(Factoid.class)
    private HashMap<String, Factoid> factoids = new HashMap<>();

    /**
     * get the factoid for tenant
     * @param id
     * @return
     */
    public Factoid getById(final String id) {
        return factoids.get(id.toLowerCase());
    }

    /**
     * get the factoid for a tenant, traversing aliases if required
     * @param id
     * @return
     */
    public Factoid chaseDownById(final String id) {
        return chaseDownById(id, 10);
    }

    public Factoid chaseDownById(final String id, final int maxLevel) {
        final Factoid factoid = getById(id.toLowerCase());
        if(factoid == null) return null;
        if(factoid.isAlias() && maxLevel > 0) return chaseDownById(factoid.getData(), maxLevel - 1);
        if(maxLevel < 1) throw new RuntimeException("Too many chained aliases");
        factoid.setId(id);
        return factoid;
    }

    /**
     * Save/overwrite a factoid
     * @param fact
     */
    public void add(final String id, final Factoid fact) {
        factoids.put(id.toLowerCase(), fact);
    }

    public Factoid del(final String id) {
        final Factoid fact = getById(id.toLowerCase());
        if(fact != null)
            factoids.remove(id);
        return fact;
    }

    public Set<Map.Entry<String,Factoid>> all() {
        return factoids.entrySet();
    }
}
