package cgs.bigdata.util.socket

import cgs.bigdata.util.data.RandomClickDataGen
import cgs.bigdata.util.log.Logging
import cgs.bigdata.util.socket.SingleEchoServer._
import cgs.bigdata.util.socket.process.GenDataProcessor

import java.net.{ServerSocket, Socket}
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

/**
 * @Description 只接收单连接请求的 TCP Server (主要用于测试 Flink 的 socketTextStream 数据收发)
 * @Author sherlock
 * @Date
 */

class SingleEchoServer(port: Int = 2525) extends Logging with Runnable {
	
	private val accepts: AtomicInteger = new AtomicInteger(NO_CLIENT)
	private val servers: AtomicInteger = new AtomicInteger(NO_SERVER)
	private var server: ServerSocket = _
	private val threadPool = Executors.newFixedThreadPool(2)
	
	override def run(): Unit = {
		logger.info("Waiting for client connecting......")
		var socket: Socket = null
		while({
			socket = server.accept()
			socket != null && accepts.compareAndSet(NO_CLIENT, socket.getLocalPort)
		}) {
			logger.info(s"Client: [${socket.getRemoteSocketAddress}] Connected")
			// 不同 process 逻辑可以实现 Processor 接口来自定义
			// 测试数据可以实现 DataGenerator 接口来自定义
			threadPool.execute(GenDataProcessor(socket, RandomClickDataGen()))
		}
	}
	
	private def start(): Unit = {
		if (servers.compareAndSet(NO_SERVER, 1)) {
			server = new ServerSocket(port)
			logger.info(s"Server:[${server.getLocalSocketAddress}] started")
			threadPool.execute(this)
		} else {
			throw new ExceptionInInitializerError(s"Server: [$server] has been initialied")
		}
	}
	
}

object SingleEchoServer {
	
	private val NO_CLIENT: Int = -1
	private val NO_SERVER: Int = -1
	
	def main(args: Array[String]): Unit = {
		
		val server: SingleEchoServer = new SingleEchoServer()
		server.start()
		
	}
}
