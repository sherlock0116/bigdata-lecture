package cgs.bigdata.kafka

import org.apache.kafka.clients.admin.{AdminClient, ListConsumerGroupsResult}
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerConfig, ProducerRecord, RecordMetadata}
import org.apache.kafka.common.{PartitionInfo, TopicPartition}
import org.apache.kafka.common.serialization.{StringDeserializer, StringSerializer}
import org.scalatest.matchers.should.Matchers
import org.scalatest.{BeforeAndAfterAll, FunSuite}

import java.time.format.DateTimeFormatter
import java.time.{Duration, Instant, ZonedDateTime}
import java.{lang, util}
import java.util.{Locale, Properties}
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import scala.collection.JavaConversions._

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
class KafkaSuite extends FunSuite
		with BeforeAndAfterAll
		with Matchers {
	
	private val timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS", Locale.CHINA)
	private var kafkaProducer: KafkaProducer[String, String] = _
	private var autoCmtConsumer: KafkaConsumer[String, String] = _
	private var manualCmtConsumer: KafkaConsumer[String, String] = _
	
	private val localBrokers = "127.0.0.1:9092"
	private val auto_groupId = "sher-auto-cmt001"
	private val manual_groupId = "sher-manual-cmt"
	private val topic: String = "cy_naturals"
	private val isRunning: AtomicBoolean = new AtomicBoolean(true)
	
	test("Kafka: AdminClient") {
		val adminClient: AdminClient = AdminClient.create(initLocalKafkaConfig(KafkaMode.consumeMode).get)
		val consumerGroupsResult: ListConsumerGroupsResult = adminClient.listConsumerGroups()
		println(s"ConsumerGroupsResult: ${consumerGroupsResult.all().get()}")
		
	}
	
	// KafkaConsumer  subscribe VS assign
	// 如下代码有个很大问题: 同一个消费组内的不同消费者 使用 assign 订阅了同一个 TopicPartition
	// 									 每条消息都被同组内不同消费者消费了多次,
	// 									 由此可见, assign 并不会触发 group management, 违背了同一消费组内消费者不可消费统一分区数据的原则
	test("KafkaConsumer  subscribe VS assign") {
		val tp: TopicPartition = new TopicPartition(topic, 0)
		val props: Properties = new Properties()
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, localBrokers)
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "sher_test")
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true.toString)
		val kc1: KafkaConsumer[String, String] = new KafkaConsumer[String, String](props)
		val kc2: KafkaConsumer[String, String] = new KafkaConsumer[String, String](props)
		
		kc1.assign(Seq(tp))
		kc2.assign(Seq(tp))
		
		while (true) {
			val crs1: ConsumerRecords[String, String] = kc1.poll(1000L)
			if (crs1 != null) {
				for(cr <- crs1) {
					println(s"m1 offset: ${cr.offset()}, value: ${cr.value()}")
				}
			}
			
			val crs2: ConsumerRecords[String, String] = kc2.poll(1000L)
			if (crs2 != null) {
				for(cr <- crs2) {
					println(s"m2 offset: ${cr.offset()}, value: ${cr.value()}")
				}
			}
		}
	}
	
	// 向 topic: cy_naturals (1分区1 副本) 中输入 5000000 条测试数据
	test("produce continuous natural number into topic: cy_naturals") {
		val total: Int = 5000000
		var acc: Int = 0
		while (isRunning.get()) {
			val now: String = timeFormatter.format(ZonedDateTime.now())
			acc += 1
			val record: ProducerRecord[String, String] = new ProducerRecord[String, String](topic, s"$now [msg: $acc]")
			kafkaProducer.send(record, new Callback {
				override def onCompletion(recordMetadata: RecordMetadata, e: Exception): Unit = {
					if (e != null) e.printStackTrace()
					else println(s"$recordMetadata")
				}
			})
			if (acc >= total) {
				isRunning.set(false)
			}
		}
	}
	
	// 使用自动提交, 消费 200000 条数据
	// 测试证明: 当 Consumer 去消费一个还没有 committed offset 的 Topic 时候: auto.offset.reset=latest 会直接将 offset 指向 leader partition 的 LEO
	// 				 20/03/15 10:07:28 INFO ConsumerCoordinator: [Consumer clientId=consumer-sher-auto-cmt-1, groupId=sher-auto-cmt] Found no committed offset for partition cy_naturals-0
	//				 20/03/15 10:07:28 INFO SubscriptionState: [Consumer clientId=consumer-sher-auto-cmt-1, groupId=sher-auto-cmt] Resetting offset for partition cy_naturals-0 to offset 5000000.
	//
	// 使用 seek 方法重置 committed offset 后, 通过 KafkaTool 可以看到: groupId:[sher-auto-cmt] 在 topic:[cy_naturals-0] 上的 committed offset=0, lag=500000
	// 然后再次调用下面方法消费 20000 条数据后, 再通过 KafkaTool 可以看到: groupId:[sher-auto-cmt] 在 topic:[cy_naturals-0] 上的 committed offset=20087(实际消费数据量), lag=4799913
	//
	// tips: 不要随便使用 KafkaComsumer.unsubscribe(), 它会使 KafkaComsumer 在这些 topic 上的 committed offset 消费记录提交失败
	test("KafkaComsumer with AutoCommit consume parts of the msgs in topic: cy_naturals") {
		val total: Int = 200000
		autoCmtConsumer.subscribe(util.Arrays.asList(topic))
		var acc: Int = 0
		while (isRunning.get()) {
			val records: ConsumerRecords[String, String] = autoCmtConsumer.poll(Duration.ofSeconds(10))
			records.foreach(rcd => println(s"ConsumerRecord: ${rcd.toString}"))
			acc += records.size
			// KafkaConsumer: auto.commit.interval.ms=5000
//			TimeUnit.SECONDS.sleep(6000)
			if (acc >= total) {
				println(s"已经消费了 $acc 条数据......")
				isRunning.set(false)
			}
		}
	}
	
	// 使用自动提交, 消费 200000 条数据, Tips: 上面 group_id: [sher-auto-cmt] 的 KafkaConsumer 已经将这个 topic:[cy_naturals] 的 committed offset 值置为 5000000
	// 使用新 group_id: [sher-manual-cmt] 的 KafkaConsumer 去消费这个 Topic: [cy_naturals] 发现: 这个新的 KafkaConsumer 还是会重置 committed offset 为 5000000
	// 			21/03/15 10:22:29 INFO ConsumerCoordinator: [Consumer clientId=consumer-sher-manual-cmt-2, groupId=sher-manual-cmt] Found no committed offset for partition cy_naturals-0
	//			21/03/15 10:22:29 INFO SubscriptionState: [Consumer clientId=consumer-sher-manual-cmt-2, groupId=sher-manual-cmt] Resetting offset for partition cy_naturals-0 to offset 5000000.
	//
	// 这个结果说明了:
	// 			topic 的 committed offset 一定是与 KafkaConsumer 绑定的, 即: 不同 KafkaConsumer 消费同一个 topic, 各个 KafkaConsumer 都会有自己的 committed offset
	test("KafkaComsumer with Manual Commit consume parts of the msgs in topic: cy_naturals") {
		val total: Int = 200000
		manualCmtConsumer.subscribe(util.Arrays.asList(topic))
		var acc: Int = 0
		while (isRunning.get()) {
			val records: ConsumerRecords[String, String] = manualCmtConsumer.poll(Duration.ofSeconds(10))
			records.foreach(rcd => println(s"ConsumerRecord: ${rcd.toString}"))
			acc += records.size
			manualCmtConsumer.commitSync()
			if (acc >= total) {
				println(s"已经消费了 $acc 条数据......")
				isRunning.set(false)
			}
		}
	}
	
	// 重置 committed offset: 将committed offset 置为 log start offset 位置处
	test("reset seek to begin") {
		autoCmtConsumer.subscribe(util.Arrays.asList(topic))
		val topicPartitions: Seq[TopicPartition] = autoCmtConsumer.partitionsFor(topic).map(pi => new TopicPartition(pi.topic(), pi.partition()))
		val tpAndLSO: util.Map[TopicPartition, lang.Long] = autoCmtConsumer.beginningOffsets(topicPartitions)
		autoCmtConsumer.poll(Duration.ofSeconds(1))
		autoCmtConsumer.paused()
		tpAndLSO.foreach { e =>
			autoCmtConsumer.seek(e._1, e._2)
		}
	}
	
	// 指定 Consumer 从 0 开始消费
	// seek/seekToBeginning/seekToEnd 这些方法的调用一定要在 poll/position 之后
	// 因为 poll 方法中会进行 rebalance, rebalance 这一过程会为 Consumer分配对应的 TopicPartition
	test("consume from beginning offset 0") {
		autoCmtConsumer.subscribe(util.Arrays.asList(topic))
		val topicPartitions: Seq[TopicPartition] = autoCmtConsumer.partitionsFor(topic).map(pi => new TopicPartition(pi.topic(), pi.partition()))
		autoCmtConsumer.poll(Duration.ofSeconds(1))
		topicPartitions.foreach(tp => autoCmtConsumer.seek(tp, 0))
		var acc: Int = 0
		while (isRunning.get()) {
			val records: ConsumerRecords[String, String] = autoCmtConsumer.poll(Duration.ofSeconds(5))
			records.foreach(rcd => println(s"ConsumerRecord: ${rcd.toString}"))
			acc += records.size
			if (acc >= 500) {
				println(s"已经消费了 $acc 条数据......")
				isRunning.set(false)
			}
		}
	}
	
	// 测试 topic: cy_naturals 的一些元数据信息
	//
	//  KafkaConsumer # beginningOffsets/endOffsets/seekToBeginning/seekToEnd
	// 	这 4 个方法获取 TopicPartition 的 offset 时都需要要参数: Collection[TopicPartition]
	//	--> 获取 Collection[TopicPartition] 需要 KafkaConsumer # assignment()
	//	--> KafkaConsumer # assignment() 使用前又需要 KafkaConsumer.poll(0)
	//	--> KafkaConsumer.poll(Long) 这个方法已经被官方标注为废弃的, 建议使用 KafkaConsumer.poll(Duration)
	//	--> 但是使用 KafkaConsumer.poll(Duration), KafkaConsumer # assignment() 返回集合是 empty
	//	--> 真的让人头秃啊
	// 	--> 所以强烈建议使用: KafkaConsumer.partitionsFor(topic) 来获取各个分区元数据信息
	//
	//	--> 有坑: KafkaConsumer # seekToBeginning/seekToEnd
	//				   这两个方法会将 commitOffset 指向 start/end, 会影响后续的正常消费
	//				   如果只是要获取 Topic 的 start/end 的偏移量值, 不对 commitOffset 有影响,
	//				   建议使用 KafkaConsumer # beginningOffsets/endOffsets
	test ("about beginingOffsets and endOffsets") {
		autoCmtConsumer.subscribe(util.Arrays.asList(topic))
		
		// 这里不能使用 consumer.poll(Duration.ofMillis(0)), 否则 consumer.assignment() 返回 null
//		autoCmtConsumer.poll(Duration.ofMillis(1))
		autoCmtConsumer.poll(0)
		val topicPartitions: util.Set[TopicPartition] = autoCmtConsumer.assignment()
		autoCmtConsumer.pause(topicPartitions)
		// TopicPartitions: [cy_naturals-1,cy_naturals-0]
		println(s"TopicPartitions: [${topicPartitions.mkString(",")}]")
		
		// beginningOffsets: {cy_naturals-1=0, cy_naturals-0=0}
		// endOffsets: {cy_naturals-1=5567, cy_naturals-0=4433}
		val beginOffsets: util.Map[TopicPartition, lang.Long] = autoCmtConsumer.beginningOffsets(topicPartitions)
		val endOffsets: util.Map[TopicPartition, lang.Long] = autoCmtConsumer.endOffsets(topicPartitions)
		println(s"beginningOffsets: $beginOffsets")
		println(s"endOffsets: $endOffsets")
		
		// seekToBeginning: Map(cy_naturals-0 -> 0, cy_naturals-1 -> 0)
		// seekToEnd: Map(cy_naturals-0 -> 4433, cy_naturals-1 -> 5567)
		autoCmtConsumer.seekToBeginning(topicPartitions)
		val tpAndBeginOffset: Map[TopicPartition, Long] = topicPartitions.map { tp => tp -> autoCmtConsumer.position(tp) }.toMap
		println(s"seekToBeginning: $tpAndBeginOffset")
		autoCmtConsumer.seekToEnd(topicPartitions)
		val tpAndEndOffset: Map[TopicPartition, Long] = topicPartitions.map { tp => tp -> autoCmtConsumer.position(tp) }.toMap
		println(s"seekToEnd: $tpAndEndOffset")
	}
	
	// KafkaConsumer # partitionsFor(String) 这个方法
	// 	-- 优点: 不需要 poll 就可以返回 Topic 分区信息: PartitionInfo
	// 	-- 缺点: 参数是单个 Topic (Topic集合的话需要自己做处理)
	// 				 方法返回的 PartitionInfo 对象需要用户自己构造 TopicPartition 对象
	//
	// Tips: 因为 autoCmtConsumer 的 3 种订阅方式 subscribe(Collection), subscribe(Pattern), assign(Collection[TopicPartition])
	//		  分别代表了AUTO_TOPICS, AUTO_PATTERN, USER_ASSIGNED 3 种状态并且它们之间是互斥的。
	//		  而 assign() 方法并没有类似 unsubscribe() 这样的取消订阅的方法, 所以 assign 只适合一些临时的 autoCmtConsumer
	//		  拉取元数据信息, 之后关闭这个临时  autoCmtConsumer, 从而不会影响后续的正常的 autoCmtConsumer 消费 Topic。
	test("test: KafkaConsumer#partitionsFor(String)") {
		val partitionInfoes: util.List[PartitionInfo] = autoCmtConsumer.partitionsFor(topic)
		println(s"PartitionInfo: ${partitionInfoes}")
		val topicPartitions: Seq[TopicPartition] = partitionInfoes.map(pi => new TopicPartition(pi.topic(), pi.partition()))
		autoCmtConsumer.assign(topicPartitions)
		
		// beginningOffsets: {cy_naturals-1=0, cy_naturals-0=0}
		// endOffsets: {cy_naturals-1=5567, cy_naturals-0=4433}
		val beginOffsets: util.Map[TopicPartition, lang.Long] = autoCmtConsumer.beginningOffsets(topicPartitions)
		val endOffsets: util.Map[TopicPartition, lang.Long] = autoCmtConsumer.endOffsets(topicPartitions)
		println(s"beginningOffsets: $beginOffsets")
		println(s"endOffsets: $endOffsets")
		
		// seekToBeginning: Map(cy_naturals-0 -> 0, cy_naturals-1 -> 0)
		// seekToEnd: Map(cy_naturals-0 -> 4433, cy_naturals-1 -> 5567)
		autoCmtConsumer.seekToBeginning(topicPartitions)
		val tpAndBeginOffset: Map[TopicPartition, Long] = topicPartitions.map { tp => tp -> autoCmtConsumer.position(tp) }.toMap
		println(s"seekToBeginning: $tpAndBeginOffset")
		autoCmtConsumer.seekToEnd(topicPartitions)
		val tpAndEndOffset: Map[TopicPartition, Long] = topicPartitions.map { tp => tp -> autoCmtConsumer.position(tp) }.toMap
		println(s"seekToEnd: $tpAndEndOffset")
	}
	
	// 测试证明, 在 Kafka2.x 版本中, 似乎已经移除 ZkUtils 这个类, 所以无法在新版本的 Kafka 中使用这个类来读取 Topic 元数据。
	// 并且 Kafka 使用了新版本的 AdminClient 来代替了 ZkUtils 提供了以下功能
	// 		1. 创建 Topic - KafkaAdminClient # createTopics(Collection<NewTopic>)
	//		2. 删除 Topic - KafkaAdminClient # deleteTopics(Collection<String>)
	//		3. 显示所有的 Topic - KafkaAdminClient # listTopics()
	//		4. 查询 Topic - KafkaAdminClient # describeTopics(Collection<String>)
	//		5. 查询集群信息 - KafkaAdminClient # describeCluster()
	//		6. 查询/创建/删除 ACL 信息 - KafkaAdminClient # describeAcls(AclBindingFilter)
	//													 - KafkaAdminClient # createAcls(Collection<AclBinding>)
	//													 - KafkaAdminClient # deleteAcls(Collection<AclBinding>)
	//		7. 查询/修改配置信息 - KafkaAdminClient # describeConfigs(Collection<ConfigResource>)
	//										   - KafkaAdminClient # alterConfigs(Map<ConfigResource, Config>)
	//		8. 修改副本的日志目录 - KafkaAdminClient # alterReplicaLogDirs(Map<TopicPartitionReplica, String>)
	//		9. 查询节点的日志目录 - KafkaAdminClient # describeLogDirs(Collection<Integer>)
	//		10. 查询副本的日志目录信息 - KafkaAdminClient # describeReplicaLogDirs(Collection<TopicPartitionReplica>)
	//		11. 增加分区 - KafkaAdminClient # createPartitions(Map<String, NewPartitions>)
	test("read metadata of kafka2.x with ZkUtils from zookeeper") {
	
//		val (zkClient, zkConnection) = ZkUtils.createZkClientAndConnection(zkHosts, 10000, 10000)
//		val zkUtils: Any = new ZkUtils(zkClient, zkConnection, false)
	}
	
	override protected def beforeAll(): Unit = {
		val producerConfig: Option[Properties] = initLocalKafkaConfig(KafkaMode.produceMode)
		val autoCmtConsumerConfig: Option[Properties] = initLocalKafkaConfig(KafkaMode.consumeMode)
		val manualCmtConsumerConfig: Option[Properties] = initLocalKafkaConfig(KafkaMode.consumeMode, isAutoCommit = false)
		// 初始化 KafkaProducer
		if (kafkaProducer == null && producerConfig.isDefined) kafkaProducer = getKafkaProducer(producerConfig.get)
		// 初始化 Auto Commit KafkaConsumer
		if (autoCmtConsumer == null && autoCmtConsumerConfig.isDefined) autoCmtConsumer = getKafkaConsumer(autoCmtConsumerConfig.get)
		// 初始化 Manually Commit KafkaConsumer
		if (manualCmtConsumer == null && manualCmtConsumerConfig.isDefined) manualCmtConsumer =  getKafkaConsumer(manualCmtConsumerConfig.get)
	}
	
	override protected def afterAll(): Unit = {
		if (kafkaProducer != null) kafkaProducer.close()
		if (autoCmtConsumer != null) autoCmtConsumer.close()
		if (manualCmtConsumer != null) manualCmtConsumer.close()
	}
	
	def getKafkaProducer(props: Properties): KafkaProducer[String, String] = new KafkaProducer[String, String](props)
	def getKafkaConsumer(props: Properties): KafkaConsumer[String, String] = new KafkaConsumer[String, String](props)
	
	def initLocalKafkaConfig(mode: KafkaMode.Value, isAutoCommit: Boolean = true): Option[Properties] = {
		val props: Properties = new Properties()
		mode match {
			case KafkaMode.produceMode =>
				props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, localBrokers)
				props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
				props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
				Some(props)
			case KafkaMode.consumeMode =>
				val groupId: String = if (isAutoCommit) auto_groupId else manual_groupId
				props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, localBrokers)
				props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)
				props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, classOf[StringDeserializer].getName)
				props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId)
				props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, isAutoCommit.toString)
				Some(props)
			case _ => None
		}
	}
	
	object KafkaMode extends Enumeration {
		val produceMode, consumeMode = Value
	}
}
