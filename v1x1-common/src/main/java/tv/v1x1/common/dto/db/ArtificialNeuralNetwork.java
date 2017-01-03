package tv.v1x1.common.dto.db;

import com.datastax.driver.mapping.annotations.Column;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.util.List;
import java.util.UUID;

/**
 * Created by cobi on 12/29/2016.
 */
@Table(name = "artificial_neural_network")
public class ArtificialNeuralNetwork {
    @PartitionKey
    private String name;
    private UUID id;
    @Column(name = "training_ttl")
    private Integer trainingTtl;
    private List<Integer> layout;

    public ArtificialNeuralNetwork() {
    }

    public ArtificialNeuralNetwork(final String name, final UUID id, final Integer trainingTtl, final List<Integer> layout) {
        this.name = name;
        this.id = id;
        this.trainingTtl = trainingTtl;
        this.layout = layout;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public Integer getTrainingTtl() {
        return trainingTtl;
    }

    public void setTrainingTtl(final Integer trainingTtl) {
        this.trainingTtl = trainingTtl;
    }

    public List<Integer> getLayout() {
        return layout;
    }

    public void setLayout(final List<Integer> layout) {
        this.layout = layout;
    }
}
