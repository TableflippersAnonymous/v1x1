package tv.v1x1.common.services.twitch.dto.feed;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by cobi on 10/29/2016.
 */
public class PostResponse {
    @JsonProperty
    private Post post;
    @JsonProperty
    private String tweet;

    public PostResponse() {
    }

    public Post getPost() {
        return post;
    }

    public void setPost(final Post post) {
        this.post = post;
    }

    public String getTweet() {
        return tweet;
    }

    public void setTweet(final String tweet) {
        this.tweet = tweet;
    }
}
