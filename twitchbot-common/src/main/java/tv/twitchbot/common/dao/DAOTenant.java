package tv.twitchbot.common.dao;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import tv.twitchbot.common.dto.db.InverseTenant;
import tv.twitchbot.common.dto.db.Platform;
import tv.twitchbot.common.dto.db.Tenant;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by naomi on 10/16/2016.
 */
public class DAOTenant {
    private Mapper<Tenant> tenantMapper;
    private Mapper<InverseTenant> inverseTenantMapper;
    private Session session;

    public DAOTenant(MappingManager mappingManager) {
        this.session = mappingManager.getSession();
        this.tenantMapper = mappingManager.mapper(Tenant.class);
        this.inverseTenantMapper = mappingManager.mapper(InverseTenant.class);
    }

    public Tenant getById(UUID id) {
        return tenantMapper.get(id);
    }

    public InverseTenant getByChannel(Platform platform, String channelId) {
        return inverseTenantMapper.get(platform, channelId);
    }

    public Tenant getOrCreate(Platform platform, String channelId, String displayName) {
        InverseTenant inverseTenant = getByChannel(platform, channelId);
        if(inverseTenant != null) {
            Tenant tenant = getById(inverseTenant.getTenantId());
            if(tenant == null)
                throw new IllegalStateException("Tenant null but inverse_tenant for: " + inverseTenant.getTenantId().toString() + " " + inverseTenant.getChannelId() + " " + inverseTenant.getPlatform());
            return tenant;
        } else {
            return createTenant(platform, channelId, displayName);
        }
    }

    public Tenant createTenant(Platform platform, String channelId, String displayName) {
        Tenant tenant = new Tenant(UUID.randomUUID(), new ArrayList<>());
        tenant.getEntries().add(new Tenant.Entry(platform, displayName, channelId));
        InverseTenant inverseTenant = new InverseTenant(platform, channelId, tenant.getId());
        BatchStatement b = new BatchStatement();
        b.add(tenantMapper.saveQuery(tenant));
        b.add(inverseTenantMapper.saveQuery(inverseTenant));
        session.execute(b);
        return tenant;
    }

    public Tenant addChannel(Tenant tenant, Platform platform, String channelId, String displayName) {
        tenant.getEntries().add(new Tenant.Entry(platform, displayName, channelId));
        InverseTenant inverseTenant = new InverseTenant(platform, channelId, tenant.getId());
        BatchStatement b = new BatchStatement();
        b.add(tenantMapper.saveQuery(tenant));
        b.add(inverseTenantMapper.saveQuery(inverseTenant));
        session.execute(b);
        return tenant;
    }

    public Tenant removeChannel(Tenant tenant, Platform platform, String channelId) {
        BatchStatement b = new BatchStatement();
        if(tenant.getEntries().removeIf(entry -> entry.getPlatform() == platform && entry.getChannelId().equals(channelId)))
            b.add(tenantMapper.saveQuery(tenant));
        InverseTenant inverseTenant = getByChannel(platform, channelId);
        if(inverseTenant != null)
            b.add(inverseTenantMapper.saveQuery(inverseTenant));
        if(b.size() > 0)
            session.execute(b);
        return tenant;
    }

    public void delete(Tenant tenant) {
        BatchStatement b = new BatchStatement();
        b.add(tenantMapper.deleteQuery(tenant));
        for(Tenant.Entry entry : tenant.getEntries()) {
            InverseTenant inverseTenant = getByChannel(entry.getPlatform(), entry.getChannelId());
            if(inverseTenant != null)
                b.add(inverseTenantMapper.deleteQuery(inverseTenant));
        }
        session.execute(b);
    }
}
