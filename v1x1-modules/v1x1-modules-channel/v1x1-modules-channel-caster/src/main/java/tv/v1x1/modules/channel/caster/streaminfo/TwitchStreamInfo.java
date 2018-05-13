package tv.v1x1.modules.channel.caster.streaminfo;

import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.services.state.DisplayNameService;
import tv.v1x1.common.services.state.NoSuchUserException;
import tv.v1x1.modules.channel.caster.Caster;
import tv.v1x1.modules.channel.caster.StreamActivity;

import javax.ws.rs.WebApplicationException;

public class TwitchStreamInfo implements IStreamInfo {
    String target;
    final private DisplayNameService displayNameService;
    private tv.v1x1.common.services.twitch.dto.channels.Channel videoChannel;

    TwitchStreamInfo() {
        this.displayNameService = Caster.getInstance().getInjector().getInstance(DisplayNameService.class);
    }

    @Override
    public IStreamInfo setTarget(final String target) {
        this.target = target;
        return this;
    }

    @Override
    public String getDisplayName() throws NoSuchUserException {
        return displayNameService.getDisplayNameFromUsername(Platform.TWITCH, getUsername());
    }

    @Override
    public String getUrl() {
        return "https://twitch.tv/" + getUsername();
    }

    @Override
    public String getActivity() throws NoSuchUserException {
        return StreamActivity.getVerb(getGame());
    }

    @Override
    public String getGame() throws NoSuchUserException {
        if(videoChannel == null)
            fetchStreamInfo();
        return videoChannel.getGame();
    }


    private void fetchStreamInfo() throws NoSuchUserException {
        this.videoChannel = Caster.getInstance().getTwitchApi().getChannels().getChannel(getUserId());
    }

    private String getUsername() {
        try {
            return displayNameService.getUsernameFromDisplayName(Platform.TWITCH, target);
        } catch (NoSuchUserException ex) {
            return target.toLowerCase(); // Ok I guess
        }
    }

    private String getUserId() throws NoSuchUserException {
        return displayNameService.getIdFromUsername(Platform.TWITCH, getUsername());
    }

    @Override
    public String toString() {
        try {
            return "TwitchStreamInfo{target="+target+",displayName="+getDisplayName()
                    +",username="+getUsername()+",userId="+getUserId()+",game="+getGame()+"}";
        } catch(Exception e) {
            return "TwitchStreamInfo{target="+target+",Exception="+e.getClass().getCanonicalName();
        }
    }
}
