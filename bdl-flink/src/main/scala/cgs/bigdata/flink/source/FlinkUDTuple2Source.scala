package cgs.bigdata.flink.source

import org.apache.flink.streaming.api.functions.source.SourceFunction

import java.util.concurrent.TimeUnit
import scala.util.Random

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
class FlinkUDTuple2Source extends SourceFunction[(String, Long)] {
	
	var isRunning: Boolean = true
	val names: Seq[String] = Seq("赵", "钱", "孙", "李", "周", "吴", "郑", "王")
	var acc: Long = 1L
	var index: Int = 0
	val random: Random = new Random()
	
	override def run(ctx: SourceFunction.SourceContext[(String, Long)]): Unit = {
		
		while (isRunning) {
			index = random.nextInt(names.size)
			ctx.collect((names(index), acc))
			acc += 1
			TimeUnit.SECONDS.sleep(1)
		}
	}
	
	override def cancel(): Unit = isRunning = false
}

object FlinkUDTuple2Source {
	
	def apply(): FlinkUDTuple2Source = new FlinkUDTuple2Source
}