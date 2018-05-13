package tv.v1x1.modules.channel.caster.streaminfo;

import tv.v1x1.common.services.state.NoSuchUserException;

import javax.ws.rs.WebApplicationException;

public interface IStreamInfo {
    /**
     * Set the search term for stream we want to find
     * @param target
     * @return The modified object
     */
    public IStreamInfo setTarget(final String target);

    /**
     * Get the target tream's display name
     * @return string of the display name
     * @throws NoSuchUserException
     */
    public String getDisplayName() throws NoSuchUserException;

    /**
     * Get the URL for viewers to follow and watch
     * @return string of the generated URL
     */
    public String getUrl();

    /**
     * Returns the summary of the stream based on details we can get from the API
     * @return String representing the (last) stream content, or null if there was none
     * @throws NoSuchUserException when a user cannot be found
     * @throws WebApplicationException when the upstream API fails
     */
    public String getActivity() throws NoSuchUserException, WebApplicationException;

    /**
     * Get the string representing the game being played on the stream
     * @return the game being played
     * @throws NoSuchUserException
     * @throws WebApplicationException
     */
    public String getGame() throws NoSuchUserException, WebApplicationException;

}
