package chapter1;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * 代码清单1-2 消费者客户端示例代码
 * Created by 朱小厮 on 2018/7/21.
 */
public class ConsumerFastStart {

    public static final String brokerList = "localhost:9092";
    public static final String topic = "topic-demo";
    public static final String groupId = "group.demo";

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put("bootstrap.servers", brokerList);
        // 设置消费组的名称
        properties.put("group.id", groupId);
        // 创建一个消费者客户端实例
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        // 订阅主题
        consumer.subscribe(Collections.singletonList(topic));
        // 循环消费信息
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(record.value());
            }
        }
    }
}
