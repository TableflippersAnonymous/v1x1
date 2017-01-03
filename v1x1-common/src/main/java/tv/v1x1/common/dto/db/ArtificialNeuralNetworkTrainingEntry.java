package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.List;
import java.util.UUID;

/**
 * Created by cobi on 12/29/2016.
 */
@Table(name = "artificial_neural_network_training_entry")
public class ArtificialNeuralNetworkTrainingEntry {
    @PartitionKey
    private UUID id;
    @ClusteringColumn
    @Column(name = "training_id")
    private UUID trainingId;
    private List<Float> inputs;
    private List<Float> outputs;

    public ArtificialNeuralNetworkTrainingEntry() {
    }

    public ArtificialNeuralNetworkTrainingEntry(final UUID id, final UUID trainingId, final List<Float> inputs, final List<Float> outputs) {
        this.id = id;
        this.trainingId = trainingId;
        this.inputs = inputs;
        this.outputs = outputs;
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public UUID getTrainingId() {
        return trainingId;
    }

    public void setTrainingId(final UUID trainingId) {
        this.trainingId = trainingId;
    }

    public List<Float> getInputs() {
        return inputs;
    }

    public void setInputs(final List<Float> inputs) {
        this.inputs = inputs;
    }

    public List<Float> getOutputs() {
        return outputs;
    }

    public void setOutputs(final List<Float> outputs) {
        this.outputs = outputs;
    }
}
