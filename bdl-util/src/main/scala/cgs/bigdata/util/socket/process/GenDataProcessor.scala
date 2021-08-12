package cgs.bigdata.util.socket.process

import cgs.bigdata.util.data.DataGenerator
import cgs.bigdata.util.socket.StreamUtil

import java.io.{IOException, PrintWriter}
import java.net.Socket
import java.util.concurrent.TimeUnit

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
class GenDataProcessor[T](socket: Socket, dataGen: DataGenerator[T], interval: Int) extends Processor with Runnable {
	
	@volatile private var isRunning = true
	
	// 每隔1秒向端口内发送一条数据
	override def process(): Unit = {
		var pw: PrintWriter = null
		try {
			pw = new PrintWriter(socket.getOutputStream, true)
			while (isRunning) {
				val data: T = dataGen.generate()
				pw.println(data.toString)
				TimeUnit.SECONDS.sleep(interval)
			}
		} catch {
			case e: IOException => logger.error("Error on continouSendClickData", e)
		} finally {
			StreamUtil.close(pw)
		}
	}
	
	override def run(): Unit = process()
	
}

object GenDataProcessor {
	
	def apply[T](socket: Socket, dataGen: DataGenerator[T], interval: Int = 1): GenDataProcessor[T] = new GenDataProcessor(socket, dataGen, interval)
}
