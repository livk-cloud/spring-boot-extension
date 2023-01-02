package com.livk.autoconfigure.curator;

import com.livk.commons.function.Customizer;
import org.apache.curator.framework.CuratorFrameworkFactory;

/**
 * <p>
 * CuratorFrameworkBuilder
 * </p>
 *
 * @author livk
 * @date 2023/1/2
 */
public interface CuratorFrameworkBuilderCustomizer extends Customizer<CuratorFrameworkFactory.Builder> {
}
