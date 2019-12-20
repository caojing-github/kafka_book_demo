package chapter12

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
 * 代码清单12-2 Spark Streaming 示例程序
 * Created by 朱小厮 on 2019-03-04.
 */
object StreamingWordCount {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setMaster("local[2]").setAppName("NetworkWordCount")
    // 第2个参数表示多长时间处理一次新数据的批次间隔（Batch Duration），这里把它设置为1秒
    val ssc = new StreamingContext(conf, Seconds(1))
    // 创建基于本地9999端口上收到的文本数据的 DStream
    val lines = ssc.socketTextStream("localhost", 9999)
    val words = lines.flatMap(_.split(" "))
    val pairs = words.map(word => (word, 1))
    val wordCounts = pairs.reduceByKey(_ + _)
    wordCounts.print()
    //    val windowedWordCounts = pairs.reduceByKeyAndWindow((a:Int, b:Int) => (a + b), Seconds(30), Seconds(10))
    //    windowedWordCounts.print()
    // 开始接收数据，把 Spark 作业不断交给下面的 SparkContext 去调度执行。执行会在另一个线程中进行
    ssc.start()
    // 等待流计算完成，以防止应用退出
    ssc.awaitTermination()
  }
}
