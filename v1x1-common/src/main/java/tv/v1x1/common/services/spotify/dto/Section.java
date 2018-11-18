package tv.v1x1.common.services.spotify.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Section {
    @JsonProperty
    private float start;
    @JsonProperty
    private float duration;
    @JsonProperty
    private float confidence;
    @JsonProperty
    private float loudness;
    @JsonProperty
    private float tempo;
    @JsonProperty("tempo_confidence")
    private float tempoConfidence;
    @JsonProperty
    private int key;
    @JsonProperty("key_confidence")
    private float keyConfidence;
    @JsonProperty
    private int mode;
    @JsonProperty("mode_confidence")
    private float modeConfidence;
    @JsonProperty("time_signature")
    private int timeSignature;
    @JsonProperty("time_signature_confidence")
    private float timeSignatureConfidence;

    public Section() {
    }

    public Section(final float start, final float duration, final float confidence, final float loudness,
                   final float tempo, final float tempoConfidence, final int key, final float keyConfidence,
                   final int mode, final float modeConfidence, final int timeSignature,
                   final float timeSignatureConfidence) {
        this.start = start;
        this.duration = duration;
        this.confidence = confidence;
        this.loudness = loudness;
        this.tempo = tempo;
        this.tempoConfidence = tempoConfidence;
        this.key = key;
        this.keyConfidence = keyConfidence;
        this.mode = mode;
        this.modeConfidence = modeConfidence;
        this.timeSignature = timeSignature;
        this.timeSignatureConfidence = timeSignatureConfidence;
    }

    public float getStart() {
        return start;
    }

    public void setStart(final float start) {
        this.start = start;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(final float duration) {
        this.duration = duration;
    }

    public float getConfidence() {
        return confidence;
    }

    public void setConfidence(final float confidence) {
        this.confidence = confidence;
    }

    public float getLoudness() {
        return loudness;
    }

    public void setLoudness(final float loudness) {
        this.loudness = loudness;
    }

    public float getTempo() {
        return tempo;
    }

    public void setTempo(final float tempo) {
        this.tempo = tempo;
    }

    public float getTempoConfidence() {
        return tempoConfidence;
    }

    public void setTempoConfidence(final float tempoConfidence) {
        this.tempoConfidence = tempoConfidence;
    }

    public int getKey() {
        return key;
    }

    public void setKey(final int key) {
        this.key = key;
    }

    public float getKeyConfidence() {
        return keyConfidence;
    }

    public void setKeyConfidence(final float keyConfidence) {
        this.keyConfidence = keyConfidence;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(final int mode) {
        this.mode = mode;
    }

    public float getModeConfidence() {
        return modeConfidence;
    }

    public void setModeConfidence(final float modeConfidence) {
        this.modeConfidence = modeConfidence;
    }

    public int getTimeSignature() {
        return timeSignature;
    }

    public void setTimeSignature(final int timeSignature) {
        this.timeSignature = timeSignature;
    }

    public float getTimeSignatureConfidence() {
        return timeSignatureConfidence;
    }

    public void setTimeSignatureConfidence(final float timeSignatureConfidence) {
        this.timeSignatureConfidence = timeSignatureConfidence;
    }
}
