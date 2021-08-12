package cgs.bigdata.flink.state

import org.apache.flink.api.common.functions.MapFunction
import org.apache.flink.streaming.api.checkpoint.ListCheckpointed
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

import java.util
import java.util.Collections
import scala.collection.JavaConversions._

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object FlinkState {
	
	def main(args: Array[String]): Unit = {
		import org.apache.flink.api.scala._
		
		val sEnv: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
		sEnv.socketTextStream("localhost", 2525)
				.map(new AccMapFunction())
				.print()
		
		sEnv.execute("Flink state")
	}
	
}
