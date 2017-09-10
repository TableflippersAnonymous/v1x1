package tv.v1x1.modules.channel.ops_tool;

import com.google.common.collect.ImmutableSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tv.v1x1.common.dao.DAOTenantGroup;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.GlobalUser;
import tv.v1x1.common.dto.core.Module;
import tv.v1x1.common.dto.core.Permission;
import tv.v1x1.common.dto.core.Tenant;
import tv.v1x1.common.dto.core.User;
import tv.v1x1.common.dto.db.ChannelGroupPlatformMapping;
import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.dto.db.TenantConfiguration;
import tv.v1x1.common.dto.db.TenantGroup;
import tv.v1x1.common.modules.RegisteredThreadedModule;
import tv.v1x1.common.services.chat.Chat;
import tv.v1x1.common.util.commands.CommandDelegator;
import tv.v1x1.modules.channel.ops_tool.commands.OpsToolCommand;
import tv.v1x1.modules.channel.ops_tool.config.OpsToolGlobalConfiguration;
import tv.v1x1.modules.channel.ops_tool.config.OpsToolUserConfiguration;

import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author Josh
 */
public class OpsTool extends RegisteredThreadedModule<OpsToolGlobalConfiguration, OpsToolUserConfiguration> {
    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static Permission GENERAL_PERM = new Permission("opstool.use");
    /*pkg-private*/ CommandDelegator delegator;
    @Override
    public String getName() {
        return "ops_tool";
    }
    public static void main(final String[] args) throws Exception {
        new OpsTool().entryPoint(args);
    }

    @Override
    protected void initialize() {
        super.initialize();
        delegator = new CommandDelegator("!");
        delegator.registerCommand(new OpsToolCommand(this));
        registerListener(new OpsToolListener(this));
    }

    public void respond(final Channel channel, final String message) {
        Chat.message(this, channel, "[OT] " + message);
    }

    public boolean hasGlobalPerm(User sender) {
        for(tv.v1x1.common.dto.db.Permission perm : getDaoManager().getDaoTenantGroup().getGlobalPermissions(sender.getGlobalUser())) {
            LOG.debug("Checking global permission " + perm.getNode());
            if(perm.toCore().equals(OpsTool.GENERAL_PERM)) return true;
        }
        return false;
    }

    public Tenant getTenantByChannel(final Platform platform, final String search) {
        if(search.equals("__GLOBAL_TENANT__"))
            return DAOTenantGroup.GLOBAL_TENANT;
        final tv.v1x1.common.dto.db.Tenant t = getDaoManager().getDaoTenant().getByChannel(platform, search);
        return (t == null ? null : t.toCore(getDaoManager().getDaoTenant()));
    }

    public GlobalUser getUser(final Platform platform, final String id) {
        final tv.v1x1.common.dto.db.GlobalUser dbGlobalUser = getDaoManager().getDaoGlobalUser().getByUser(platform, id);
        if(dbGlobalUser == null)
            return null;
        return dbGlobalUser.toCore();
    }

    public tv.v1x1.common.dto.db.Channel getChannelByName(final Platform platform, final String search) {
        return getDaoManager().getDaoTenant().getChannel(platform, search);
    }

    public TenantConfiguration getTenantConfiguration(final String module, final Channel channel) {
        return getDaoManager().getDaoTenantConfiguration().get(new Module(module), channel.getChannelGroup().getTenant());

    }

    /************************ Permissions Helpers ************************/

    public Iterable<TenantGroup> getGroups(final Tenant t) {
        return getDaoManager().getDaoTenantGroup().getAllGroupsByTenant(t);
    }

    public TenantGroup groupById(final Tenant tenant, final UUID groupId) {
        return getDaoManager().getDaoTenantGroup().getTenantGroup(tenant, groupId);
    }

    public void createGroup(final Tenant t, final String group) {
        getDaoManager().getDaoTenantGroup().createGroup(t, group);
    }

    public TenantGroup groupByName(final Tenant t, final String groupName) {
        for(TenantGroup checkGroup : getGroups(t)) {
            if(!checkGroup.getName().equals(groupName)) continue;
            return checkGroup;
        }
        return null;
    }

    public boolean destroyGroup(final Tenant t, final String groupName) {
        TenantGroup group = groupByName(t, groupName);
        if(group == null) return false;
        getDaoManager().getDaoTenantGroup().deleteGroup(group);
        return true;
    }

    public boolean addPerm(final Tenant t, final String groupName, final String permNode) {
        final TenantGroup group = groupByName(t, groupName);
        if(group == null) return false;
        final tv.v1x1.common.dto.db.Permission perm = new tv.v1x1.common.dto.db.Permission(permNode);
        getDaoManager().getDaoTenantGroup().addPermissionsToGroup(group, ImmutableSet.of(perm));
        return true;
    }

    public boolean delPerm(final Tenant t, final String groupName, final String permNode) {
        final TenantGroup group = groupByName(t, groupName);
        if(group == null) return false;
        final tv.v1x1.common.dto.db.Permission perm = new tv.v1x1.common.dto.db.Permission(permNode);
        getDaoManager().getDaoTenantGroup().removePermissionsFromGroup(group, ImmutableSet.of(perm));
        return true;
    }

    public Set<Permission> getPerms(final Tenant t, final String groupName) {
        final TenantGroup group = groupByName(t, groupName);
        if(group == null) return null;
        final Set<Permission> perms = new HashSet<>();
        for(tv.v1x1.common.dto.db.Permission perm : group.getPermissions())
            perms.add(perm.toCore());
        return perms;
    }

    public boolean linkGroup(final Channel c, final Platform platform, final String vgroup, final String pgroup) {
        final TenantGroup group = groupByName(c.getChannelGroup().getTenant(), vgroup);
        if(group == null) return false;
        getDaoManager().getDaoTenantGroup().setChannelGroupPlatformMapping(platform, c.getId(), pgroup, group);
        return true;
    }

    public boolean clearLink(final Channel c, final Platform platform, final String vgroup, final String pgroup) {
        final TenantGroup group = groupByName(c.getChannelGroup().getTenant(), vgroup);
        if(group == null) return false;
        getDaoManager().getDaoTenantGroup().clearChannelGroupPlatformMapping(platform, c.getId(), pgroup);
        return true;
    }

    public ChannelGroupPlatformMapping getLink(final Channel c, final String pgroup) {
        return getDaoManager().getDaoTenantGroup().getChannelGroupPlatformMapping(c.getChannelGroup().getPlatform(), c.getId(), pgroup);
    }
}
