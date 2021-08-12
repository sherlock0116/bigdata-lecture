package cgs.bigdata.flink.timer

import cgs.bigdata.flink.source.FlinkUDTuple2Source
import org.apache.flink.streaming.api.functions.KeyedProcessFunction
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.util.Collector

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object FlinkTimer {
	
	def main(args: Array[String]): Unit = {
		
		import org.apache.flink.api.scala._
		val sEnv: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
		sEnv.setParallelism(1)
		sEnv.addSource(FlinkUDTuple2Source())
				.map(e => e)
				.keyBy(_._1)
				.process(MyKeyedProcessFunction())
				.print()
		sEnv.execute("Flink Timer Job")
	}
	
	case class MyKeyedProcessFunction() extends KeyedProcessFunction[String, (String, Long), String] {
		
		private val cache: mutable.Map[String, ListBuffer[Long]] = mutable.Map[String, ListBuffer[Long]]()
		private var first: Boolean = true
		private val builder: StringBuilder = new StringBuilder()
		
		override def onTimer(timestamp: Long, ctx: KeyedProcessFunction[String, (String, Long), String]#OnTimerContext, out: Collector[String]): Unit = {
			println(s"定时器触发: $timestamp")
			for (entry: (String, ListBuffer[Long]) <- cache) {
				if (entry._2.nonEmpty) {
					builder.append(entry._1).append(":")
					for (ele <- entry._2) {
						builder.append(ele).append(",")
					}
					builder.setLength(builder.length - 1)
					builder.append(";").append("\n")
					cache(entry._1).clear()
				}
			}
			val rslt: String = builder.toString()
			builder.setLength(0)
			println(s"定时器注册 $timestamp")
			// 该定时器执行完任务之后，重新注册一个定时器
			ctx.timerService().registerProcessingTimeTimer(timestamp + 5000)
			out.collect(rslt)
		}
		
		override def processElement(value: (String, Long), ctx: KeyedProcessFunction[String, (String, Long), String]#Context, out: Collector[String]): Unit = {
			// 仅在该算子接收到第一个数据时，注册一个定时器
			if (first) {
				first = false
				val current: Long = System.currentTimeMillis()
				println(s"定时器首次注册: $current")
				ctx.timerService().registerProcessingTimeTimer(current + 5000)
			}
			// 将流数据更新到缓存中
			if (cache.contains(value._1)) {
				cache(value._1).append(value._2)
			} else {
				cache.put(value._1, ListBuffer[Long](value._2))
			}
		}
	}
}
