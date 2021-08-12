package cgs.bigdata.flink.source

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.streaming.connectors.kafka.internals.KafkaTopicPartition

import java.util.Properties
import java.{lang, util}

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object FlinkKafka2Consumer {
	
	def main(args: Array[String]): Unit = {
		
		import org.apache.flink.api.scala._
		val sEnv: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
		sEnv.setParallelism(1)
		
		val properties: Properties = new Properties()
		properties.setProperty("bootstrap.servers", "localhost:9092")
		properties.setProperty("group.id", "test")
		val kafkaConsumer: FlinkKafkaConsumer[String] = new FlinkKafkaConsumer[String]("cy_naturals", new SimpleStringSchema(), properties)
		kafkaConsumer.setStartFromEarliest()
//		val partitionToLong: util.HashMap[KafkaTopicPartition, lang.Long] = new util.HashMap[KafkaTopicPartition, lang.Long]()
//		partitionToLong.put(new KafkaTopicPartition("cy_naturals", 0), 0L)
//		kafkaConsumer.setStartFromSpecificOffsets(partitionToLong)
		val kafkaSourceStream: DataStream[String] = sEnv.addSource(kafkaConsumer)
		kafkaSourceStream.print()
		
		sEnv.execute("Flink Kafka Source")
	}
}
