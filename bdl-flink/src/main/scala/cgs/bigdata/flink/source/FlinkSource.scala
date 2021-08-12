package cgs.bigdata.flink.source

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.apache.flink.streaming.connectors.kafka.internals.KafkaTopicPartition

import java.{lang, util}
import java.util.Properties

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object FlinkSource {
	
	case class SensorReading(id: String, ts: Long, tempture: Double)
	
	def main(args: Array[String]): Unit = {
		import org.apache.flink.api.scala._
		val sEnv: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
		sEnv.setParallelism(1)
		
		/*
			1. 从集合读数据
		*/
		val sensorSourceStream: DataStream[SensorReading] = sEnv.fromCollection(Seq(
			SensorReading("sensor_1", 1547718199L, 35.8),
			SensorReading("sensor_6", 1547718200L, 15.4),
			SensorReading("sensor_7", 1547718202L, 6.7),
			SensorReading("sensor_10", 1547718205L, 38.1)
		)).name("source").uid("source")
		sensorSourceStream.print()
		
		val numberSourceStream: DataStream[Int] = sEnv.fromElements(1, 3, 5, 7, 9)
		numberSourceStream.print()
		
		/*
		 	2. 从文本中读取
		 */
		val fileSourceStream: DataStream[String] = sEnv.readTextFile("/Users/sherlock/IdeaProjects/Bigdata/bigdata-lecture/bdl-flink/data/sensor.txt")
		fileSourceStream.print()
		
		/*
			3. 从第三方kafka connector 中读取数据
			Kafka 版本					依赖													消费者生产者的类名称
			universal (>= 1.0.0)		flink-connector-kafka_2.11			FlinkKafkaConsumer/FlinkKafkaProducer
			0.11.x							flink-connector-kafka-011_2.11		FlinkKafkaConsumer011/FlinkKafkaProducer011
			0.10.x							flink-connector-kafka-010_2.11	FlinkKafkaConsumer010/FlinkKafkaProducer010
		 */
		// kafka connector
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
		
		
		sEnv.execute("Flink Source")
	}
}
