package cgs.bigdata.flink.sink

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig

import java.util.Properties

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object FlinkKafkaProducerDemo {
	
	private val topic = "test-caiyi"
	private val properties = {
		val props: Properties = new Properties()
		props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "10.0.15.132:9092")
		props
	}
	
	def main(args: Array[String]): Unit = {
		
		new FlinkKafkaProducer(topic, new SimpleStringSchema(), properties, FlinkKafkaProducer.Semantic.EXACTLY_ONCE)
	}
}
