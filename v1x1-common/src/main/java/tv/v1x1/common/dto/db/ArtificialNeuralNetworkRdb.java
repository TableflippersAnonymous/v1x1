package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Created by cobi on 12/29/2016.
 */
@Table(name = "artificial_neural_network_rdb")
public class ArtificialNeuralNetworkRdb {
    @PartitionKey
    private UUID id;
    private ByteBuffer data;

    public ArtificialNeuralNetworkRdb() {
    }

    public ArtificialNeuralNetworkRdb(final UUID id, final ByteBuffer data) {
        this.id = id;
        this.data = data;
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public ByteBuffer getData() {
        return data;
    }

    public void setData(final ByteBuffer data) {
        this.data = data;
    }
}
