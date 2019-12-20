package chapter4;

import org.apache.kafka.common.errors.PolicyViolationException;
import org.apache.kafka.server.policy.CreateTopicPolicy;

import java.util.Map;

/**
 * 代码清单4-6 主题合法性验证示例
 * Created by 朱小厮 on 2018/10/3.
 */
public class PolicyDemo implements CreateTopicPolicy {

    /**
     * Kafka服务启动的时候执行
     */
    @Override
    public void configure(Map<String, ?> configs) {
    }

    /**
     * 关闭Kafka服务时执行
     */
    @Override
    public void close() throws Exception {
    }

    /**
     * 鉴定主体参数的合法性
     */
    @Override
    public void validate(RequestMetadata requestMetadata) throws PolicyViolationException {
        if (requestMetadata.numPartitions() != null || requestMetadata.replicationFactor() != null) {
            if (requestMetadata.numPartitions() < 5) {
                throw new PolicyViolationException("Topic should have at " + "least 5 partitions, received: " + requestMetadata.numPartitions());
            }
            if (requestMetadata.replicationFactor() <= 1) {
                throw new PolicyViolationException("Topic should have at " + "least 2 replication factor, recevied: " + requestMetadata.replicationFactor());
            }
        }
    }
}
