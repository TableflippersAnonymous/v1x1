package tv.v1x1.modules.channel.caster.streaminfo;

import tv.v1x1.common.dto.db.Platform;
import tv.v1x1.common.services.state.DisplayNameService;
import tv.v1x1.common.services.state.NoSuchTargetException;
import tv.v1x1.modules.channel.caster.Caster;
import tv.v1x1.modules.channel.caster.StreamActivity;

public class TwitchStreamInfo implements StreamInfo {
    private final String target;
    private final DisplayNameService displayNameService;
    private tv.v1x1.common.services.twitch.dto.channels.Channel videoChannel;

    TwitchStreamInfo(final String target) {
        this.target = target;
        this.displayNameService = Caster.getInstance().getInjector().getInstance(DisplayNameService.class);
    }

    @Override
    public String getDisplayName() throws NoSuchTargetException {
        return displayNameService.getDisplayNameFromUsername(Platform.TWITCH, getUsername());
    }

    @Override
    public String getUrl() {
        return "https://twitch.tv/" + getUsername();
    }

    @Override
    public String getActivity() throws NoSuchTargetException {
        return StreamActivity.getVerb(getGame());
    }

    @Override
    public String getGame() throws NoSuchTargetException {
        if(videoChannel == null)
            fetchStreamInfo();
        return videoChannel.getGame();
    }


    private void fetchStreamInfo() throws NoSuchTargetException {
        this.videoChannel = Caster.getInstance().getTwitchApi().getChannels().getChannel(getUserId());
    }

    private String getUsername() {
        try {
            return displayNameService.getUsernameFromDisplayName(Platform.TWITCH, target);
        } catch (NoSuchTargetException ex) {
            return target.toLowerCase(); // Ok I guess
        }
    }

    private String getUserId() throws NoSuchTargetException {
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
