package cgs.bdl.nio.quickstart

import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.{SelectionKey, Selector, ServerSocketChannel, SocketChannel}
import java.util
import scala.util.control.Breaks._
import scala.collection.JavaConverters._
import scala.collection.mutable
/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object NIOServer {
	
	def main(args: Array[String]): Unit = {
		
		val serverSocketChannel: ServerSocketChannel = ServerSocketChannel.open()
		val selector: Selector = Selector.open()
		serverSocketChannel.socket().bind(new InetSocketAddress(6666))
		serverSocketChannel.configureBlocking(false)
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT)
		
		while (true) {
			val eventsNum: Int = selector.select(1000)
			breakable {
				if (eventsNum == 0) {
					break
				}
				println("===> Listened an accept event......")
				val selectionKeys: util.Iterator[SelectionKey] = selector.selectedKeys().iterator()
				while (selectionKeys.hasNext) {
					val selectionKey: SelectionKey = selectionKeys.next()
					if (selectionKey.isAcceptable) {
						val socketChannel: SocketChannel = serverSocketChannel.accept()
						socketChannel.configureBlocking(false)
						socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024))
					}
					if (selectionKey.isReadable) {
						val socketChannel: SocketChannel = selectionKey.channel().asInstanceOf[SocketChannel]
						val buffer: ByteBuffer = selectionKey.attachment().asInstanceOf[ByteBuffer]
						socketChannel.read(buffer)
						println(s"===> 接收到客户端消息: ${new String(buffer.array())}")
					}
					selectionKeys.remove()
				}
				
			}
		}
		
	}
}
