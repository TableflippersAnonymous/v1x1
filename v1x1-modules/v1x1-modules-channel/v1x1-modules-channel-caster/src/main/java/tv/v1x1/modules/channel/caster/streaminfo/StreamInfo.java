package tv.v1x1.modules.channel.caster.streaminfo;

import tv.v1x1.common.services.state.NoSuchUserException;

import javax.ws.rs.WebApplicationException;

public interface StreamInfo {
    /**
     * Get the target tream's display name
     * @return string of the display name
     * @throws NoSuchUserException
     */
    String getDisplayName() throws NoSuchUserException;

    /**
     * Get the URL for viewers to follow and watch
     * @return string of the generated URL
     */
    String getUrl();

    /**
     * Returns the summary of the stream based on details we can get from the API
     * @return String representing the (last) stream content, or null if there was none
     * @throws NoSuchUserException when a user cannot be found
     * @throws WebApplicationException when the upstream API fails
     */
    String getActivity() throws NoSuchUserException, WebApplicationException;

    /**
     * Get the string representing the game being played on the stream
     * @return the game being played
     * @throws NoSuchUserException
     * @throws WebApplicationException
     */
    String getGame() throws NoSuchUserException, WebApplicationException;

}
