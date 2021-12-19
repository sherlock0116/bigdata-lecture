package cgs.bdl.java.concurrent

import java.util.concurrent.{ArrayBlockingQueue, ThreadPoolExecutor, TimeUnit}

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object DummyThreadPoolExecutor {
	
	def main(args: Array[String]): Unit = {
		
		val pool = new ThreadPoolExecutor(2, 5, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue[Runnable](4));
		
		submitTaskAtFixedTime(pool, new SleepTask(1), 3)
		
		pool.awaitTermination(10L, TimeUnit.MINUTES)
	}
	
	def submitTaskAtFixedTime(pool: ThreadPoolExecutor, runnable: Runnable, secs: Int): Unit = {
		while(true) {
			pool.execute(runnable)
//			println(s"core-pool-size: ${pool.get}")
			TimeUnit.SECONDS.sleep(secs)
		}
	}
}

class SleepTask(sleepSec:Int) extends Runnable {
	
	override def run(): Unit = {
		println(s"${Thread.currentThread().getName}")
		TimeUnit.SECONDS.sleep(sleepSec)
	}
}