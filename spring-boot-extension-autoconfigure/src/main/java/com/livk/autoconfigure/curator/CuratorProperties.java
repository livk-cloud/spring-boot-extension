/*
 * Copyright 2021 spring-boot-extension the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.livk.autoconfigure.curator;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * CuratorProperties
 * </p>
 *
 * @author livk
 */
@Data
@ConfigurationProperties(CuratorProperties.PREFIX)
public class CuratorProperties {

    /**
     * The constant PREFIX.
     */
    public static final String PREFIX = "spring.zookeeper.curator";

    /**
     * zk连接集群，多个用逗号隔开
     */
    private String servers = "localhost:2181";

    /**
     * 会话超时时间
     */
    private int sessionTimeout = 60000;

    /**
     * 连接超时时间
     */
    private int connectionTimeout = 15000;

    /**
     * 初始重试等待时间(毫秒)
     */
    private int baseSleepTime = 1000;

    /**
     * 重试最大次数
     */
    private int maxRetries = 10;

}
