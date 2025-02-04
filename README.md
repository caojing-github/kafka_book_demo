[使用MQ的原因](https://mp.weixin.qq.com/s/2998ryj46sLNbAsK4IwZXA)  

# kafka_book_demo
《深入理解Kafka：核心设计与设计实践》书籍中的代码示例。

代码清单已经更新完毕，如有疑问可以提issue.

-----
欢迎支持笔者新作：《深入理解Kafka:核心设计与实践原理》和《RabbitMQ实战指南》，同时欢迎关注笔者的微信公众号：朱小厮的博客。
![](https://img-blog.csdnimg.cn/20190221231530525.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3UwMTMyNTY4MTY=,size_16,color_FFFFFF,t_70)

[作者的CSDN博客](https://blog.csdn.net/u013256816)

```java
public class ProducerRecord<K, V> {
    // 主题
    private final String topic;
    // 分区号
    private final Integer partition;
    // 消息头部
    private final Headers headers;
    // 键
    private final K key;
    // 值
    private final V value;
    // 消息的时间戳
    private final Long timestamp;
}
```
```java
public class PartitionInfo {
    // 主题名称
    private final String topic;
    // 分区编号
    private final int partition;
    // 分区的leader副本所在的位置
    private final Node leader;
    // 分区的AR集合
    private final Node[] replicas;
    // 代表分区的ISR集合
    private final Node[] inSyncReplicas;
    // 代表分区的OSR集合
    private final Node[] offlineReplicas;
}
```
消费位移存储在Kafka内部的主题`_consumer_offsets`中(62页)

# 107页暂时跳过看
# 149页代码清单4-7暂时跳过看