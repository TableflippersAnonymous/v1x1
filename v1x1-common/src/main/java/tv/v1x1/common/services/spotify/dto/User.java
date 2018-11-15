package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends PublicUser {
    @JsonProperty
    private String birthdate;
    @JsonProperty
    private String country;
    @JsonProperty
    private String email;
    @JsonProperty
    private String product;

    public User() {
    }

    public User(final String type, final String href, final Map<String, String> externalUrls, final String uri,
                final String displayName, final Followers followers, final String id, final List<Image> images,
                final String birthdate, final String country, final String email, final String product) {
        super(type, href, externalUrls, uri, displayName, followers, id, images);
        this.birthdate = birthdate;
        this.country = country;
        this.email = email;
        this.product = product;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(final String birthdate) {
        this.birthdate = birthdate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(final String country) {
        this.country = country;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(final String product) {
        this.product = product;
    }
}
