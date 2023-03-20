/**
 * Copyright (c) 2022 KCloud-Platform-Alibaba Authors. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.livk.rocket.producer.listener;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

/**
 * @author laokou
 */
@Component
@RocketMQTransactionListener
@Slf4j
public class TestTxListener implements RocketMQLocalTransactionListener {

    /**
     * 监听发送half消息，执行本地事务
     * @param msg
     * @param arg
     * @return
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        String transactionId = msg.getHeaders().get(RocketMQHeaders.TRANSACTION_ID).toString();
        log.info("执行本地事务 => 事务id：{}",transactionId);
        // 这里测试一下事务状态回查，mq默认是一分钟查询，可以在broker设置
        // 状态未知时，才会触发回查
        if (unKnowTransaction(transactionId)) {
            log.info("事务未知，触发事务回查");
            return RocketMQLocalTransactionState.UNKNOWN;
        }
        // 插入成功提交，插入失败回滚
        // 这里图个方便，返回true提交，返回false回滚
        if (selectTransaction(transactionId)) {
            log.info("事务提交");
            return RocketMQLocalTransactionState.COMMIT;
        }
        log.info("事务回滚");
        return RocketMQLocalTransactionState.ROLLBACK;
    }

    /**
     * 本地事务检查，检查本地事务是否成功
     * @param msg
     * @return
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        String transactionId = msg.getHeaders().get(RocketMQHeaders.TRANSACTION_ID).toString();
        log.info("检查本地事务 => 事务id：{}",transactionId);
        // 从事务表查询，有事务记录则提交事务
        if (selectTransaction(transactionId)) {
            log.info("事务提交");
            return RocketMQLocalTransactionState.COMMIT;
        }
        log.info("事务回滚");
        // 否则，回滚事务
        return RocketMQLocalTransactionState.ROLLBACK;
    }

    private boolean selectTransaction(String transactionId) {
        // TODO 查询数据库
        // 为了方便，我就用请求头，真实业务需要查询数据库的事务表
        return "2".equals(transactionId);
    }

    private boolean insertTransaction(String transactionId) {
        // TODO 插入业务的事务表
        // 为了方便，我还是用请求头，真实业务需要插入数据到事务表
        return "2".equals(transactionId);
    }

    private boolean unKnowTransaction(String transactionId) {
        return "3".equals(transactionId);
    }

}
