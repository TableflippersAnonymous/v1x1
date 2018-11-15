package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AudioFeatures {
    @JsonProperty
    private float acousticness;
    @JsonProperty("analysis_url")
    private String analysisUrl;
    @JsonProperty
    private float danceability;
    @JsonProperty("duration_ms")
    private int durationMs;
    @JsonProperty
    private float energy;
    @JsonProperty
    private String id;
    @JsonProperty
    private float instrumentalness;
    @JsonProperty
    private int key;
    @JsonProperty
    private float liveness;
    @JsonProperty
    private float loudness;
    @JsonProperty
    private int mode;
    @JsonProperty
    private float speechiness;
    @JsonProperty
    private float tempo;
    @JsonProperty("time_signature")
    private int timeSignature;
    @JsonProperty("track_href")
    private String trackHref;
    @JsonProperty
    private String type;
    @JsonProperty
    private String uri;
    @JsonProperty
    private float valence;

    public AudioFeatures() {
    }

    public AudioFeatures(final float acousticness, final String analysisUrl, final float danceability,
                         final int durationMs, final float energy, final String id, final float instrumentalness,
                         final int key, final float liveness, final float loudness, final int mode,
                         final float speechiness, final float tempo, final int timeSignature, final String trackHref,
                         final String type, final String uri, final float valence) {
        this.acousticness = acousticness;
        this.analysisUrl = analysisUrl;
        this.danceability = danceability;
        this.durationMs = durationMs;
        this.energy = energy;
        this.id = id;
        this.instrumentalness = instrumentalness;
        this.key = key;
        this.liveness = liveness;
        this.loudness = loudness;
        this.mode = mode;
        this.speechiness = speechiness;
        this.tempo = tempo;
        this.timeSignature = timeSignature;
        this.trackHref = trackHref;
        this.type = type;
        this.uri = uri;
        this.valence = valence;
    }

    public float getAcousticness() {
        return acousticness;
    }

    public void setAcousticness(final float acousticness) {
        this.acousticness = acousticness;
    }

    public String getAnalysisUrl() {
        return analysisUrl;
    }

    public void setAnalysisUrl(final String analysisUrl) {
        this.analysisUrl = analysisUrl;
    }

    public float getDanceability() {
        return danceability;
    }

    public void setDanceability(final float danceability) {
        this.danceability = danceability;
    }

    public int getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(final int durationMs) {
        this.durationMs = durationMs;
    }

    public float getEnergy() {
        return energy;
    }

    public void setEnergy(final float energy) {
        this.energy = energy;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public float getInstrumentalness() {
        return instrumentalness;
    }

    public void setInstrumentalness(final float instrumentalness) {
        this.instrumentalness = instrumentalness;
    }

    public int getKey() {
        return key;
    }

    public void setKey(final int key) {
        this.key = key;
    }

    public float getLiveness() {
        return liveness;
    }

    public void setLiveness(final float liveness) {
        this.liveness = liveness;
    }

    public float getLoudness() {
        return loudness;
    }

    public void setLoudness(final float loudness) {
        this.loudness = loudness;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(final int mode) {
        this.mode = mode;
    }

    public float getSpeechiness() {
        return speechiness;
    }

    public void setSpeechiness(final float speechiness) {
        this.speechiness = speechiness;
    }

    public float getTempo() {
        return tempo;
    }

    public void setTempo(final float tempo) {
        this.tempo = tempo;
    }

    public int getTimeSignature() {
        return timeSignature;
    }

    public void setTimeSignature(final int timeSignature) {
        this.timeSignature = timeSignature;
    }

    public String getTrackHref() {
        return trackHref;
    }

    public void setTrackHref(final String trackHref) {
        this.trackHref = trackHref;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(final String uri) {
        this.uri = uri;
    }

    public float getValence() {
        return valence;
    }

    public void setValence(final float valence) {
        this.valence = valence;
    }
}
