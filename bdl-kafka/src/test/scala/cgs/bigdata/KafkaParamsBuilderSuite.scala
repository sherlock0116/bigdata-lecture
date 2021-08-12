package cgs.bigdata

import cgs.bigdata.kafka.KafkaParamsBuilder
import org.apache.kafka.common.serialization.StringDeserializer
import org.scalatest.{BeforeAndAfterAll, FunSuite}
import org.scalatest.matchers.should.Matchers

import java.util.Properties

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
class KafkaParamsBuilderSuite extends FunSuite
		with BeforeAndAfterAll
		with Matchers {
	
	private val localBrokers = "127.0.0.1:9092"
	private val groupId = "sher-trans"
	private val topic: String = "cy_transaction"
	
	test("KafkaParamsBuilder") {
		val props1: Properties = KafkaParamsBuilder.builder()
				.bootstraps(localBrokers)
				.groupId(groupId)
				.keyDeserializer(classOf[StringDeserializer].getName)
				.valueDeserializer(classOf[StringDeserializer].getName)
				.build()
		
		val props2: Properties = KafkaParamsBuilder.builder()
				.bootstraps(localBrokers)
				.groupId(groupId)
				.keyDeserializer(classOf[StringDeserializer].getName)
				.valueDeserializer(classOf[StringDeserializer].getName)
				.build()
		
		println(props1 eq props2)
	}
}
