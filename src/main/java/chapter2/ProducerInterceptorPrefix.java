package chapter2;

import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Map;

/**
 * 代码清单2-6 生产者拦截器
 * Created by 朱小厮 on 2018/8/1.
 */
public class ProducerInterceptorPrefix implements ProducerInterceptor<String, String> {

    private volatile long sendSuccess = 0;
    private volatile long sendFailure = 0;

    /**
     * KafkaProducer 在将消息序列化和计算分区之前会调用
     */
    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        String modifiedValue = "prefix1-" + record.value();
        return new ProducerRecord<>(record.topic(), record.partition(), record.timestamp(), record.key(), modifiedValue, record.headers());
//        if (record.value().length() < 100) {
//            throw new RuntimeException();
//        }
//        return record;
    }

    /**
     * KafkaProducer 会在将消息被应答或消息发送失败时调用，优先于用户设定的Callback之前执行。
     * 运行在Producer的IO线程中，逻辑越简单越好，否则会影响消息的发送速度
     */
    @Override
    public void onAcknowledgement(RecordMetadata recordMetadata, Exception e) {
        if (e == null) {
            sendSuccess++;
        } else {
            sendFailure++;
        }
    }

    @Override
    public void close() {
        double successRatio = (double) sendSuccess / (sendFailure + sendSuccess);
        System.out.println("[INFO] 发送成功率=" + String.format("%f", successRatio * 100) + "%");
    }

    @Override
    public void configure(Map<String, ?> map) {
    }
}
