package cgs.bigdata.flink.wordcount

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object FlinkWordCount {
	
	def main(args: Array[String]): Unit = {
		import org.apache.flink.api.scala._
		
		val sEnv: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
		sEnv.setParallelism(1)
		val source: DataStream[String] = sEnv.fromCollection(Seq("a", "b", "c", "a", "a", "b", "a"))
		source.map {word => (word, 1)}
				.keyBy(0)
				.sum(1)
				.print()
		
		sEnv.execute("Flink WordCount")
	}
}
