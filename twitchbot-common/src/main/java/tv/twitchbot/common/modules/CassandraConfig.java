package tv.twitchbot.common.modules;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by naomi on 10/15/2016.
 */
public class CassandraConfig {
    private String clusterName;
    private String keyspace;
    private List<String> contactPoints;
    private int port = 9042;
    private boolean authenticated = false;
    private String username;
    private String password;

    private double exclusionThreshold = 3.0;
    private long scaleMs = 100;
    private long retryMs = 10000;
    private long updateMs = 100;
    private int minimumMeasurements = 50;

    private String consistencyLevel = "LOCAL_ONE";
    private String serialConsistencyLevel = "SERIAL";
    private int fetchSize = 5000;

    private long reconnectBaseDelayMs = 100;
    private long reconnectMaxDelayMs = 5000;

    private int connectTimeoutMs = 5000;
    private boolean tcpKeepAlive = true;

    private long highestTrackableLatencyMs = 15000;
    private double speculativeRetryPercentile = 99.0;
    private int speculativeMaxRetries = 2;

    @JsonProperty("cluster_name")
    public String getClusterName() {
        return clusterName;
    }

    @JsonProperty("cluster_name")
    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    @JsonProperty("keyspace")
    public String getKeyspace() {
        return keyspace;
    }

    @JsonProperty("keyspace")
    public void setKeyspace(String keyspace) {
        this.keyspace = keyspace;
    }

    @JsonProperty("contact_points")
    public List<String> getContactPoints() {
        return contactPoints;
    }

    @JsonProperty("contact_points")
    public void setContactPoints(List<String> contactPoints) {
        this.contactPoints = contactPoints;
    }

    @JsonProperty("port")
    public int getPort() {
        return port;
    }

    @JsonProperty("port")
    public void setPort(int port) {
        this.port = port;
    }

    @JsonProperty("authenticated")
    public boolean isAuthenticated() {
        return authenticated;
    }

    @JsonProperty("authenticated")
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    @JsonProperty("username")
    public void setUsername(String username) {
        this.username = username;
    }

    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("exclusion_threshold")
    public double getExclusionThreshold() {
        return exclusionThreshold;
    }

    @JsonProperty("exclusion_threshold")
    public void setExclusionThreshold(double exclusionThreshold) {
        this.exclusionThreshold = exclusionThreshold;
    }

    @JsonProperty("scale_ms")
    public long getScaleMs() {
        return scaleMs;
    }

    @JsonProperty("scale_ms")
    public void setScaleMs(long scaleMs) {
        this.scaleMs = scaleMs;
    }

    @JsonProperty("retry_ms")
    public long getRetryMs() {
        return retryMs;
    }

    @JsonProperty("retry_ms")
    public void setRetryMs(long retryMs) {
        this.retryMs = retryMs;
    }

    @JsonProperty("update_ms")
    public long getUpdateMs() {
        return updateMs;
    }

    @JsonProperty("update_ms")
    public void setUpdateMs(long updateMs) {
        this.updateMs = updateMs;
    }

    @JsonProperty("minimum_measurements")
    public int getMinimumMeasurements() {
        return minimumMeasurements;
    }

    @JsonProperty("minimum_measurements")
    public void setMinimumMeasurements(int minimumMeasurements) {
        this.minimumMeasurements = minimumMeasurements;
    }

    @JsonProperty("consistency_level")
    public String getConsistencyLevel() {
        return consistencyLevel;
    }

    @JsonProperty("consistency_level")
    public void setConsistencyLevel(String consistencyLevel) {
        this.consistencyLevel = consistencyLevel;
    }

    @JsonProperty("serial_consistency_level")
    public String getSerialConsistencyLevel() {
        return serialConsistencyLevel;
    }

    @JsonProperty("serial_consistency_level")
    public void setSerialConsistencyLevel(String serialConsistencyLevel) {
        this.serialConsistencyLevel = serialConsistencyLevel;
    }

    @JsonProperty("fetch_size")
    public int getFetchSize() {
        return fetchSize;
    }

    @JsonProperty("fetch_size")
    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    @JsonProperty("reconnect_base_delay_ms")
    public long getReconnectBaseDelayMs() {
        return reconnectBaseDelayMs;
    }

    @JsonProperty("reconnect_base_delay_ms")
    public void setReconnectBaseDelayMs(long reconnectBaseDelayMs) {
        this.reconnectBaseDelayMs = reconnectBaseDelayMs;
    }

    @JsonProperty("reconnect_max_delay_ms")
    public long getReconnectMaxDelayMs() {
        return reconnectMaxDelayMs;
    }

    @JsonProperty("reconnect_max_delay_ms")
    public void setReconnectMaxDelayMs(long reconnectMaxDelayMs) {
        this.reconnectMaxDelayMs = reconnectMaxDelayMs;
    }

    @JsonProperty("connect_timeout_ms")
    public int getConnectTimeoutMs() {
        return connectTimeoutMs;
    }

    @JsonProperty("connect_timeout_ms")
    public void setConnectTimeoutMs(int connectTimeoutMs) {
        this.connectTimeoutMs = connectTimeoutMs;
    }

    @JsonProperty("tcp_keep_alive")
    public boolean isTcpKeepAlive() {
        return tcpKeepAlive;
    }

    @JsonProperty("tcp_keep_alive")
    public void setTcpKeepAlive(boolean tcpKeepAlive) {
        this.tcpKeepAlive = tcpKeepAlive;
    }

    @JsonProperty("highest_trackable_latency_ms")
    public long getHighestTrackableLatencyMs() {
        return highestTrackableLatencyMs;
    }

    @JsonProperty("highest_trackable_latency_ms")
    public void setHighestTrackableLatencyMs(long highestTrackableLatencyMs) {
        this.highestTrackableLatencyMs = highestTrackableLatencyMs;
    }

    @JsonProperty("speculative_retry_percentile")
    public double getSpeculativeRetryPercentile() {
        return speculativeRetryPercentile;
    }

    @JsonProperty("speculative_retry_percentile")
    public void setSpeculativeRetryPercentile(double speculativeRetryPercentile) {
        this.speculativeRetryPercentile = speculativeRetryPercentile;
    }

    @JsonProperty("speculative_max_retries")
    public int getSpeculativeMaxRetries() {
        return speculativeMaxRetries;
    }

    @JsonProperty("speculative_max_retries")
    public void setSpeculativeMaxRetries(int speculativeMaxRetries) {
        this.speculativeMaxRetries = speculativeMaxRetries;
    }
}
