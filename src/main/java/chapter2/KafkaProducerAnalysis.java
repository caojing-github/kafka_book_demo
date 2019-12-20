package chapter2;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * 代码清单2-1 生产者客户端示例代码
 * Created by 朱小厮 on 2018/8/29.
 */
public class KafkaProducerAnalysis {

    public static final String brokerList = "localhost:9092";
    public static final String topic = "topic-demo";

    public static Properties initConfig() {
        Properties props = new Properties();
        props.put("bootstrap.servers", brokerList);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("client.id", "producer.client.id.demo");
        return props;
    }

    public static Properties initNewConfig() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "producer.client.id.demo");
        return props;
    }

    /**
     * 推荐配置写法
     */
    public static Properties initPerferConfig() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokerList);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        // 用来设定 KafkaProducer 对应的客户端id
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "producer.client.id.demo");
        // 重试次数
//        props.put(ProducerConfig.RETRIES_CONFIG, 10);
        // 自定义分区器
        props.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, DemoPartitioner.class.getName());
        // 自定义拦截器
        props.put(ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, ProducerInterceptorPrefix.class.getName());
        return props;
    }

    public static void main(String[] args) throws InterruptedException {
        Properties props = initPerferConfig();
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);

//        KafkaProducer<String, String> producer = new KafkaProducer<>(props,
//                new StringSerializer(), new StringSerializer());

        ProducerRecord<String, String> record = new ProducerRecord<>(topic, "hello, Kafka!");
        try {
            for (int i = 0; i < 10; i++) {
                producer.send(record);
//            producer.send(record, new Callback() {
//                @Override
//                public void onCompletion(RecordMetadata metadata, Exception exception) {
//                    if (exception == null) {
//                        System.out.println(metadata.partition() + ":" + metadata.offset());
//                    }
//                }
//            });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        TimeUnit.SECONDS.sleep(5);
        // 关闭生产者客户端示例
        producer.close();
    }
}
