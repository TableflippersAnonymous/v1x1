package tv.v1x1.common.util;

import tv.v1x1.common.dao.DAOGlobalUser;
import tv.v1x1.common.dao.DAOTenant;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.GlobalUser;
import tv.v1x1.common.dto.core.User;
import tv.v1x1.common.dto.db.Platform;

/**
 * @author Josh
 * Because I don't want to validate every DTO.db object isn't null before calling .toCore() on it
 * The naming and location is extremely unserious until I get PR feedback because idk where to put it
 */
public class GoGetter {
    public static Channel getMeAChannel(DAOTenant daoTenant, Platform platform, String channelId) throws NoSuchThingException {
        final tv.v1x1.common.dto.db.Channel dbChannel = daoTenant.getChannel(platform, channelId);
        if(dbChannel != null)
            return dbChannel.toCore(daoTenant);
        throw new NoSuchThingException("Channel not known");
    }

    public static Channel getMeAChannelFromTheSameTenant(DAOTenant daoTenant, Channel context, String channelId) throws NoSuchThingException {
        final Channel target = getMeAChannel(daoTenant, context.getPlatform(), channelId);
        if(target.getTenant().equals(context.getTenant()))
            return target;
        throw new NoSuchThingException("Channel not in same tenant");
    }

    public static GlobalUser getMeAGlobalUser(DAOGlobalUser daoGlobalUser, Platform platform, String userId) throws NoSuchThingException {
        final tv.v1x1.common.dto.db.GlobalUser dbGlobalUser = daoGlobalUser.getByUser(platform, userId);
        if(dbGlobalUser != null)
            return dbGlobalUser.toCore();
        throw new NoSuchThingException("GlobalUser does not exist");
    }

    public static User getMeAUser(DAOGlobalUser daoGlobalUser, Platform platform, String userId) throws NoSuchThingException {
        final GlobalUser globalUser = getMeAGlobalUser(daoGlobalUser, platform, userId);
        if(globalUser != null)
            return globalUser.getUser(platform, userId).orElse(null);
        throw new NoSuchThingException("GlobalUser does not have a " + platform + " user");
    }
}
