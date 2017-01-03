package tv.v1x1.common.services.machinelearning;

import com.lambdaworks.redis.RedisClient;
import tv.v1x1.common.services.persistence.DAOManager;

/**
 * Created by cobi on 1/2/2017.
 */
public class MachineLearningManager {
    private final RedisClient neuralRedis;
    private final DAOManager daoManager;

    public MachineLearningManager(final RedisClient neuralRedis, final DAOManager daoManager) {
        this.neuralRedis = neuralRedis;
        this.daoManager = daoManager;
    }
}
