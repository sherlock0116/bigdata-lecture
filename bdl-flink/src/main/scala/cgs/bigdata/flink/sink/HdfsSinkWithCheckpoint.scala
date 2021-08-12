package cgs.bigdata.flink.sink

import cgs.bigdata.flink.FlinkUtils.{enableCheckpoint, setLocalFsStateBackend, setParallism}
import cgs.bigdata.flink.source.SimpleCheckpointSource
import cgs.bigdata.util.log.Logging
import org.apache.flink.api.common.serialization.SimpleStringEncoder
import org.apache.flink.core.fs.Path
import org.apache.flink.core.io.SimpleVersionedSerializer
import org.apache.flink.runtime.state.{FunctionInitializationContext, FunctionSnapshotContext}
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction
import org.apache.flink.streaming.api.functions.sink.SinkFunction
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.SimpleVersionedStringSerializer
import org.apache.flink.streaming.api.functions.sink.filesystem.{BucketAssigner, StreamingFileSink}
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.{DefaultRollingPolicy, OnCheckpointRollingPolicy}
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

import java.io.File
import java.time.{Instant, LocalDateTime, ZoneId}
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit


/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object HdfsSinkWithCheckpoint {
	
	def main(args: Array[String]): Unit = {
		
		import org.apache.flink.api.scala._
		
		implicit val sEnv: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
		setParallism()
		enableCheckpoint(10000)
		setLocalFsStateBackend()
		
		val source: DataStream[(Long, Long, String)] = sEnv.addSource[(Long, Long, String)](new SimpleCheckpointSource(100))
				.name("custom source").uid("custom source")
		
		val defaultRollingPolicy: DefaultRollingPolicy[Nothing, Nothing] = DefaultRollingPolicy.builder()
				.withRolloverInterval(TimeUnit.SECONDS.toMillis(30)) 		// 滚动周期. 默认 60 s
				.withInactivityInterval(TimeUnit.SECONDS.toMillis(10)) 	// 写入数据处于不活跃状态超时, 默认 60 s
				.withMaxPartSize(1024 * 1024 * 1) 												// 每个文件的最大大小 ,默认 128 mb
				.build()
		
		val hdfsSink: StreamingFileSink[(Long, Long, String)] = StreamingFileSink
				.forRowFormat(
					new Path("hdfs://localhost:9000/lecture/flink/"),
					new SimpleStringEncoder[(Long, Long, String)]("UTF-8"))
				.withBucketAssigner(new EventTimeBucketAssinger)
				.withRollingPolicy(OnCheckpointRollingPolicy.build())
				.build()
				
//		source.print()
		source.addSink(hdfsSink).name("hdfs sink").uid("hdfs sink")
		
		sEnv.execute("sink to HDFS")
	}
	
	case class WebClick(num: Long, ts: Long, url: String)
	
	// 基于流数据的 EventTime 的分桶策略
	class EventTimeBucketAssinger extends BucketAssigner[(Long, Long, String), String] {
		
		override def getBucketId(element: (Long, Long, String), context: BucketAssigner.Context): String = {
			val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH")
			val localDateTime: LocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(element._2), ZoneId.systemDefault())
			val dateTime: String = dateFormatter.format(localDateTime)
			val dateAndHour: Array[String] = dateTime.trim.split("\\s+")
			s"${dateAndHour(0)}${File.separator}${dateAndHour(1)}"
		}
		
		override def getSerializer: SimpleVersionedSerializer[String] = SimpleVersionedStringSerializer.INSTANCE
		
	}
	
}
