package cgs.bdl.scheduler.timer

import java.time.LocalDateTime
import java.util.concurrent.TimeUnit
import java.util.{Date, Timer, TimerTask}

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object TimerSchedulerDemo {
	
	def main(args: Array[String]): Unit = {
		
		val timer: Timer = new Timer()
		
		for(i <- 1 to 2) {
			timer.schedule(new TimeTask(s"task-$i"), new Date(), 2000L)
		}
	}
}

class TimeTask (val name: String) extends TimerTask {
	
	override def run(): Unit = {
		println(s"execute task: [$name] at ${LocalDateTime.now()}")
		TimeUnit.SECONDS.sleep(3)
	}
	
}