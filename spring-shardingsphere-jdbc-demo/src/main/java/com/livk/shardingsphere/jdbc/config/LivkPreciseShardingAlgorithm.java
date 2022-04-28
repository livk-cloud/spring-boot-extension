package com.livk.shardingsphere.jdbc.config;

import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.util.Collection;

/**
 * <p>
 * LivkPreciseShardingAlgorithm
 * </p>
 *
 * @author livk
 * @date 2022/4/19
 */
public class LivkPreciseShardingAlgorithm implements StandardShardingAlgorithm<String> {

    @Override
    public void init() {

    }

    @Override
    public String getType() {
        return "LIVK_PRECISE";
    }

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {
        var value = shardingValue.getValue();
        for (var targetName : availableTargetNames) {
            if (targetName.endsWith(value)) {
                return targetName;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<String> shardingValue) {
        return availableTargetNames;
    }
}
