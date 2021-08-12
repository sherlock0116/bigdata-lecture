package cgs.bigdata.spark.kafka

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.dstream.{DStream, InputDStream}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils}
import org.apache.spark.streaming.{Milliseconds, StreamingContext}

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object ReadKafkaDStream {
	
	private val localBrokers = "127.0.0.1:9092"
	private val groupId = "spark-manual-cmt"
	private val topic: String = "cy_naturals"
	private val kafkaParams: Map[String, String] = Map[String, String](
		"bootstrap.servers" -> localBrokers,
		"key.deserializer" -> classOf[StringDeserializer].getName,
		"value.deserializer" -> classOf[StringDeserializer].getName,
		"group.id" -> groupId,
		"auto.offset.reset" -> "earliest",
		"enable.auto.commit" -> "true"
	)
	
	def main(args: Array[String]): Unit = {
		
		val sparkSession: SparkSession = SparkSession.builder()
				.appName("Kafka Reader")
				.master("local[1]")
				.getOrCreate()
		
		val sc: SparkContext = sparkSession.sparkContext
		val checkpointDir: String = "/Users/sherlock/IdeaProjects/Bigdata/bigdata-lecture/bdl-spark/checkpoints"
		val ssc: StreamingContext = new StreamingContext(sc, Milliseconds(10))
		ssc.checkpoint("hdfs://localhost:9000/spark/checkpoint")
		val stream: InputDStream[ConsumerRecord[String, String]] = KafkaUtils createDirectStream[String, String](
				ssc,
				PreferConsistent,
				Subscribe[String, String](Set(topic), kafkaParams)
		)
		//从 kafka 读取出来的数据 ConsumerRecord 包含了很多信息，包括了信息时间戳等，所以要获取 value 值
		val resultDStream: DStream[String] = stream.map(_.value())
		resultDStream.print()
		ssc.start()
		ssc.awaitTermination()
	}
}
