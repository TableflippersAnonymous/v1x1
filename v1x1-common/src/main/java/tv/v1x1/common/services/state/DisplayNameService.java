package tv.v1x1.common.services.state;

import com.google.inject.Singleton;
import tv.v1x1.common.dto.core.Channel;
import tv.v1x1.common.dto.core.TwitchChannel;
import tv.v1x1.common.dto.core.User;

/**
 * @author Josh
 */
@Singleton
public class DisplayNameService {

    public String getIdFromDisplayName(Channel context, String displayName) {
        if(context instanceof TwitchChannel) return displayName.toLowerCase();
        throw new IllegalArgumentException("Don't know how to deal with a " + context.getClass().getCanonicalName());
    }

    public String getDisplayNameFromId(Channel context, String id) {
        throw new IllegalArgumentException("Not yet implemented.");
    }
}
