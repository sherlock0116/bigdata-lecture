package cgs.bigdata.kafka

import org.apache.kafka.clients.producer.KafkaProducer

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
class KafkaMonotoneIncreasingProducer() {
	
	private var producer: KafkaProducer[String, String] = _
	
}

object KafkaMonotoneIncreasingProducer {
	
	val TOPIC: String = "cy_monotone_increasing"
	
}
