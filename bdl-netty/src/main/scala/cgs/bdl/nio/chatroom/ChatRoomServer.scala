package cgs.bdl.nio.chatroom

import cgs.bdl.nio.chatroom.ChatRoomServer.PORT

import java.io.IOException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.{Channel, SelectableChannel, SelectionKey, Selector, ServerSocketChannel, SocketChannel}
import java.util
import scala.collection.JavaConverters._

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
class ChatRoomServer {
	
	private var print_flag = false
	private lazy val selector = Selector.open()
	private lazy val listenChannel = ServerSocketChannel.open()
	listenChannel.socket().bind(new InetSocketAddress(PORT))
	listenChannel.configureBlocking(false)
	listenChannel.register(selector, SelectionKey.OP_ACCEPT)
	
	def listen(): Unit = {
		var clientChannel: SocketChannel = null
		try {
			while (true) {
				val count: Int = selector.select(1000L)
				if (count > 0) {
					val selectionKeys: util.Iterator[SelectionKey] = selector.selectedKeys().iterator()
					while (selectionKeys.hasNext) {
						val selectionKey: SelectionKey = selectionKeys.next()
						selectionKey match {
							case key: SelectionKey if key.isAcceptable =>
								clientChannel = listenChannel.accept()
								clientChannel.configureBlocking(false)
								clientChannel.register(selector, SelectionKey.OP_READ)
								val msg: String = s"===> 用户 ${clientChannel.getRemoteAddress} 上线了......"
								println(msg)
								sendToOthers(clientChannel, msg)
							case key: SelectionKey if key.isReadable =>
								handleUserMsg(selectionKey)
							case key: SelectionKey if key.isWritable =>
						}
						selectionKeys.remove()
					}
				} else {
					if (!print_flag) {
						println("等待用户连接上线......")
						print_flag = true
					}
				}
			}
		} catch {
				case e: IOException =>
					println(s"===> 用户 ${clientChannel.getRemoteAddress} 下线了......")
		}
	}
	
	
	def handleUserMsg(selectionKey: SelectionKey): Unit = {
		
		val channel: SocketChannel = selectionKey.channel().asInstanceOf[SocketChannel]
		val buffer: ByteBuffer = ByteBuffer.allocate(1024)
		val count: Int = channel.read(buffer)
		while (count > 0) {
			val msg: String = new String(buffer.array())
			println(s"===> from client [${channel.getRemoteAddress}] get message: [$msg]")
			sendToOthers(channel, msg)
		}
	}
	
	def sendToOthers(selef: SocketChannel, msg: String): Unit = {
		for (key <- selector.keys().asScala) {
			val channel: Channel = key.channel()
			channel match {
				case target: SocketChannel if channel != selef =>
					target.write(ByteBuffer.wrap(msg.getBytes))
				case _ =>
			}
		}
	}
}

object ChatRoomServer {
	
	private lazy val PORT = 6667
	
	def apply(): ChatRoomServer = new ChatRoomServer()
	
	def main(args: Array[String]): Unit = {
		val chatRoomServer: ChatRoomServer = ChatRoomServer()
		chatRoomServer.listen()
	}
}
