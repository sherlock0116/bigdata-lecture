package cgs.bigdata.kafka

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig}
import org.apache.kafka.common.serialization.StringSerializer
import org.scalatest.{BeforeAndAfterAll, FunSuite}
import org.scalatest.matchers.should.Matchers

import java.util.Properties
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
class KafkaTransactionSuite extends FunSuite
		with BeforeAndAfterAll
		with Matchers {
	
	private var kafkaProducer: KafkaProducer[String, String] = _
	private val localBrokers = "127.0.0.1:9092"
	private val groupId = "sher-trans"
	private val transId = "trans-id-001"
	private val topic: String = "cy_transaction"
	private val isRunning: AtomicBoolean = new AtomicBoolean(true)
	
	// 支持事务的 KafkaProducer 的配置
	private val prdcConfig: Properties = KafkaParamsBuilder.builder()
			.bootstraps(localBrokers)
			.keySerializer(classOf[StringSerializer].getName)
			.valueSerializer(classOf[StringSerializer].getName)
			.transactionId(transId)
			.build()
	
	
	test("produce msgs into Topic: [cy_transaction]") {
		val prdc1: KafkaProducer[String, String] = new KafkaProducer[String, String](prdcConfig)
		val prdc2: KafkaProducer[String, String] = new KafkaProducer[String, String](prdcConfig)
		
		
	}
	
	override protected def beforeAll(): Unit = {
		if (kafkaProducer != null) kafkaProducer = new KafkaProducer(prdcConfig)
	}
	
	override protected def afterAll(): Unit = {
		if (kafkaProducer != null) kafkaProducer.close()
	}
	
}
