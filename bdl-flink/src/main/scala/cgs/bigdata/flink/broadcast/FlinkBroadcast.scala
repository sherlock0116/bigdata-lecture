package cgs.bigdata.flink.broadcast

import org.apache.flink.api.common.state.MapStateDescriptor
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.api.scala._

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object FlinkBroadcast {
	
	def main(args: Array[String]): Unit = {
		
		val sEnv: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
		sEnv.setParallelism(2)
		val data: Seq[String] = Seq("java", "scala", "go", "java", "python", "ruby", "java")
		val source: DataStream[String] = sEnv.fromCollection(data).name("source").uid("source")
		val rule: DataStream[String] = sEnv.fromElements("java")
		//
//		new MapStateDescriptor()
		sEnv.execute("Flink Broadcast")
	}
}

