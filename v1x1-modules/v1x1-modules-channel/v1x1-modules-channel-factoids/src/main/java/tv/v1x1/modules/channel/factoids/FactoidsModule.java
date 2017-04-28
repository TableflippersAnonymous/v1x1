package tv.v1x1.modules.channel.factoids;

import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.messages.events.ChatMessageEvent;
import tv.v1x1.common.i18n.I18n;
import tv.v1x1.common.modules.RegisteredThreadedModule;
import tv.v1x1.common.util.commands.CommandDelegator;
import tv.v1x1.modules.channel.factoids.commands.FactCommand;
import tv.v1x1.modules.channel.factoids.config.FactoidsChannelConfiguration;
import tv.v1x1.modules.channel.factoids.config.FactoidsGlobalConfiguration;
import tv.v1x1.modules.channel.factoids.config.FactoidsModuleSettings;
import tv.v1x1.modules.channel.factoids.config.FactoidsTenantConfiguration;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Josh
 */
public class FactoidsModule extends RegisteredThreadedModule<FactoidsModuleSettings, FactoidsGlobalConfiguration, FactoidsTenantConfiguration, FactoidsChannelConfiguration> {
    static {
        final Module module = new Module("factoids");
        I18n.registerDefault(module, "help.blurb", "Factoids are little bits of text you can call upon as a custom command");
        /* generic failures */
        I18n.registerDefault(module, "invalid.args", "%commander%, that command is missing something. Usage: %usage%");
        I18n.registerDefault(module, "invalid.subcommand", "%commander%, what do you want to do with a fact? Type !fact help for a list");
        I18n.registerDefault(module, "noexist", "%commander%, fact \"%id%\" doesn't exist");
        I18n.registerDefault(module, "alreadyexists", "%commander%, the fact \"%fact%\" already exists");
        I18n.registerDefault(module, "toomany.aliases", "%commander%, \"%alias%\" is an alias to an alias to an alias... And so on. Consider aliasing \"%alias%\" to a fact directly.");
        /* success */
        I18n.registerDefault(module, "add.success", "%commander%, \"%id%\" has been added as \"%fact%\"");
        I18n.registerDefault(module, "alias.success", "%commander%, \"%id%\" is now aliased to \"%alias%\"");
        I18n.registerDefault(module, "setperm.success", "%commander%, \"%id%\" is now available only to those with %perm%");
        I18n.registerDefault(module, "edit.success", "%commander%, \"%id%\" has been modified to say \"%fact%\"");
        I18n.registerDefault(module, "remove.fact.success", "%commander%, \"%id%\" has been deleted. It said: %fact%");
        I18n.registerDefault(module, "remove.alias.success", "%commander%, \"%id%\" is no longer aliased to \"%alias%\"");
        I18n.registerDefault(module, "list", "%commander%, here's a list of all the facts: %list%");
        I18n.registerDefault(module, "list.nofacts", "%commander%, there are no facts set up... Add one with !fact add");
        I18n.registerDefault(module, "info.standard", "%commander%, \"%id%\" is %enabled%. It has the permission %perm%. It looks like this: %fact%");
        I18n.registerDefault(module, "info.noperm", "%commander%, \"%id%\" is %enabled%. It looks like this: %fact%");
        I18n.registerDefault(module, "info.alias", "%commander%, \"%id%\" is an alias for \"%to%\"");
        I18n.registerDefault(module, "toggle.success", "%commander%, \"%id%\" is now %status%");
    }

    public static final String CUSTOM_PREM_PREFIX = "factoids.use.";

    @Override
    public String getName() {
        return "factoids";
    }

    // Global command provider, for manipulating factoids
    private CommandDelegator delegator;
    private CommandDelegator customDelegators;

    public static void main(final String[] args) throws Exception {
        new FactoidsModule().entryPoint(args);
    }

    public void initialize() {
        super.initialize();
        delegator = new CommandDelegator("!");
        delegator.registerCommand(new FactCommand(this));
        customDelegators = new CommandDelegator(new FactoidCommandProvider(this), "!");
        registerListener(new FactoidsListener(this));
    }

    public void handleMessage(final ChatMessageEvent ev) {
        delegator.handleChatMessage(ev);
        customDelegators.handleChatMessage(ev);
    }

    public boolean isEnabled(final Channel channel) {
        if(getChannelConfiguration(channel).isOverridden()) {
            return getChannelConfiguration(channel).isEnabled();
        } else {
            return getTenantConfiguration(channel.getTenant()).isEnabled();
        }
    }

    /**
     * add a factoid
     * @param tenant
     * @param id
     * @param reply
     * @return
     */
    public Factoid addFact(final Tenant tenant, final String id, final String reply) {
        return addFact(tenant, id, reply, null);
    }

    /**
     * add a factoid with an optional permission
     * @param tenant
     * @param id
     * @param reply
     * @param permission
     * @return
     */
    public Factoid addFact(final Tenant tenant, final String id, final String reply, final Permission permission) {
        return addFact(tenant, id, reply, permission, false);
    }

    /**
     * Add a factoid or an alias with an optional permission
     * @param tenant tenant to add it to
     * @param id id of the fact or alias
     * @param data reply if fact, target if alias
     * @param permission permission required for the command; ignored if an alias
     * @param isAlias true if this is an alias
     * @return
     */
    public Factoid addFact(final Tenant tenant, final String id, final String data, final Permission permission, final boolean isAlias) {
        final tv.v1x1.common.dto.db.Permission dbPermission;
        if(permission == null) dbPermission = null;
        else dbPermission = new tv.v1x1.common.dto.db.Permission(permission.getNode());
        final Factoid fact = new Factoid(tenant, data, dbPermission, isAlias);
        return addFact(tenant, id, fact);
    }

    public Factoid addFact(final Tenant tenant, final String id, final Factoid fact) {
        final FactoidsTenantConfiguration config = getTenantConfiguration(tenant);
        config.add(id, fact);
        getTenantConfigProvider().save(tenant, config);
        return fact;
    }

    public boolean enableFact(final Tenant tenant, final String id, final boolean enabled) {
        final FactoidsTenantConfiguration config = getTenantConfiguration(tenant);
        final Factoid fact = config.chaseDownById(id);
        if(fact == null)
            return false;
        fact.setEnabled(enabled);
        getTenantConfigProvider().save(tenant, config);
        return true;
    }

    /**
     * remove a factoid or alias
     * @param tenant
     * @param id
     * @return
     */
    public Factoid delFact(final Tenant tenant, final String id) {
        final FactoidsTenantConfiguration config = getTenantConfiguration(tenant);
        Factoid fact = config.del(id);
        if(fact != null) {
            getTenantConfigProvider().save(tenant, config);
            pruneAliases(tenant, id);
        }
        return fact;
    }

    /**
     * return a factoid to spit out the fact or check permissions
     * @param tenant
     * @param id
     * @return
     */
    public Factoid getFact(final Tenant tenant, final String id) {
        return getTenantConfiguration(tenant).chaseDownById(id);
    }

    /**
     * returns the factoid given, even if it's an alias
     * @param tenant
     * @param id
     * @return
     */
    public Factoid getFactDirectly(final Tenant tenant, final String id) {
        return getTenantConfiguration(tenant).getById(id);
    }

    /**
     * get a list of all facts
     * @param tenant
     * @return all facts
     */
    public Set<Map.Entry<String,Factoid>> getFacts(final Tenant tenant) {
        return getTenantConfiguration(tenant).all();
    }

    /**
     * Delete all aliases for a fact; usually called before deleting a fact
     * @param tenant
     * @param id
     */
    public void pruneAliases(final Tenant tenant, final String id) {
        final FactoidsTenantConfiguration config = getTenantConfiguration(tenant);
        for(Map.Entry<String,Factoid> fact : config.all()) {
            if(!fact.getValue().isAlias()) continue;
            if(fact.getValue().getData().equals(id)) delFact(tenant, fact.getKey());
        }
    }

    /**
     * Delete all aliases for all deleted facts; delete all aliases to aliases
     * @param tenant
     */
    public void pruneAliases(final Tenant tenant) {
        final FactoidsTenantConfiguration config = getTenantConfiguration(tenant);
        for(Map.Entry<String, Factoid> fact : config.all()) {
            if(fact.getValue().isAlias()) {
                final Factoid target = config.getById(fact.getValue().getData());
                if(target == null || target.isAlias())
                    delFact(tenant, fact.getKey());
            }
        }
    }

    /**
     * Delete all facts that have no message
     * @param tenant
     */
    public void pruneBlanks(final Tenant tenant) {
        final FactoidsTenantConfiguration config = getTenantConfiguration(tenant);
        for(Map.Entry<String, Factoid> fact : config.all()) {
            if(fact.getValue().getData() == null)
                delFact(tenant, fact.getKey());
        }
    }
}
