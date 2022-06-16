package com.livk.shardingsphere.jdbc.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;

import java.util.Collection;
import java.util.Properties;

/**
 * <p>
 * LivkPreciseShardingAlgorithm
 * </p>
 *
 * @author livk
 * @date 2022/4/19
 */
@Slf4j
public class LivkPreciseShardingAlgorithm implements StandardShardingAlgorithm<String> {

    private Properties props = new Properties();

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
        log.info("{}", value);
        for (var targetName : availableTargetNames) {
            if (targetName.endsWith(value)) {
                return targetName;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames,
                                         RangeShardingValue<String> shardingValue) {
        return availableTargetNames;
    }

    @Override
    public Properties getProps() {
        return props;
    }

    @Override
    public void setProps(Properties props) {
        this.props = props;
    }

}
