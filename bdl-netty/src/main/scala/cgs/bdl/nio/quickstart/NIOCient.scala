package cgs.bdl.nio.quickstart

import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SocketChannel

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object NIOCient {
	
	def main(args: Array[String]): Unit = {
		
		val socketChannel: SocketChannel = SocketChannel.open()
		socketChannel.configureBlocking(false)
		val serverAddress: InetSocketAddress = new InetSocketAddress("127.0.0.1", 6666)
		
		if (!socketChannel.connect(serverAddress)) {
			while (!socketChannel.finishConnect()) {
				println("trying to connect to server......")
			}
		}
		
		val buffer: ByteBuffer = ByteBuffer.wrap("hello, i am client".getBytes)
		socketChannel.write(buffer)
		
		System.in.read()
	}
	
}
