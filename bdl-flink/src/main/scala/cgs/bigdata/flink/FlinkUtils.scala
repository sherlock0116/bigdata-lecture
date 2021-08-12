package cgs.bigdata.flink

import org.apache.flink.api.common.restartstrategy.RestartStrategies
import org.apache.flink.runtime.executiongraph.restart.RestartStrategy
import org.apache.flink.runtime.state.filesystem.FsStateBackend
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.environment.CheckpointConfig.ExternalizedCheckpointCleanup
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.kafka.clients.consumer.ConsumerConfig

import java.util.Properties

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object FlinkUtils {
	
	def setParallism(parallelism: Int = 1)(implicit sEnv: StreamExecutionEnvironment): Unit = sEnv.setParallelism(parallelism)
	
	def flinkKafkaConsumerEocPorps(kfk_servers: String = "localhost:9092", kfk_group: String = "cy_20210416"): Properties = {
		val properties: Properties = new Properties()
		properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kfk_servers)
		properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, kfk_group)
		properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
		properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false")
		properties
	}
	
	def enableCheckpoint(interval: Long = 2000)(implicit sEnv: StreamExecutionEnvironment): Unit = {
		sEnv.enableCheckpointing(interval)
		// advanced options:
		// set mode to exactly-once (this is the default)
		sEnv.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)
		// checkpoints have to complete within one minute, or are discarded
		sEnv.getCheckpointConfig.setCheckpointTimeout(300000)
		// make sure 500 ms of progress happen between checkpoints
		sEnv.getCheckpointConfig.setMinPauseBetweenCheckpoints(500)
		// allow only one checkpoint to be in progress at the same time
		sEnv.getCheckpointConfig.setMaxConcurrentCheckpoints(1)
		// enable externalized checkpoints which are retained after job cancellation
		sEnv.getCheckpointConfig.enableExternalizedCheckpoints(ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION)
		// This determines if a task will be failed if an error occurs in the execution of the task’s checkpoint procedure.
//		sEnv.getCheckpointConfig.setFailOnCheckpointingErrors(true)
	}
	
	def fixedDelayRestart(restartAttempts: Int = 5, delayBetweenAttempts: Long = 3000)(implicit sEnv: StreamExecutionEnvironment): Unit = {
		sEnv.setRestartStrategy(RestartStrategies.fixedDelayRestart(restartAttempts, delayBetweenAttempts))
	}
	
	def setLocalFsStateBackend(path: String = "file:///Users/sherlock/IdeaProjects/Bigdata/bigdata-lecture/bdl-flink/checkpoint")(implicit sEnv: StreamExecutionEnvironment): StreamExecutionEnvironment = {
		sEnv.setStateBackend(new FsStateBackend(path))
	}
}
