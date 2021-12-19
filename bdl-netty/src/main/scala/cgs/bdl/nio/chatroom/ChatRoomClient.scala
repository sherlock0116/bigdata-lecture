package cgs.bdl.nio.chatroom

import cgs.bdl.nio.chatroom.ChatRoomClient.{server_host, server_port}

import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.{SelectionKey, Selector, SocketChannel}
import java.util
import java.util.Scanner
import java.util.concurrent.TimeUnit

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
class ChatRoomClient {
	
	private lazy val selector = Selector.open()
	private lazy val socketChannel: SocketChannel = SocketChannel.open(new InetSocketAddress(server_host, server_port))
	private val name = socketChannel.getLocalAddress
	socketChannel.configureBlocking(false)
	socketChannel.register(selector, SelectionKey.OP_READ)
	
	def sendToServer(msg: String): Unit = {
		val full_msg: String = s"$name : \n\t $msg"
		socketChannel.write(ByteBuffer.wrap(full_msg.getBytes))
	}
	
	def readFromServer(): Unit = {
		val channelCnt: Int = selector.select()
		if (channelCnt > 0) {
			val selectionKeys: util.Iterator[SelectionKey] = selector.selectedKeys().iterator()
			while (selectionKeys.hasNext) {
				val selectionKey: SelectionKey = selectionKeys.next()
				selectionKey match {
					case key: SelectionKey if key.isReadable =>
						val channel: SocketChannel = key.channel().asInstanceOf[SocketChannel]
						val buffer: ByteBuffer = ByteBuffer.allocate(1024)
						channel.read(buffer)
						println(s"${new String(buffer.array())}")
				}
			}
		} else {
		
		}
	}
}

object ChatRoomClient {
	
	private lazy val server_host = "127.0.0.1"
	private lazy val server_port = 6667
	
	def apply(): ChatRoomClient = new ChatRoomClient()
	
	def main(args: Array[String]): Unit = {
		
		val chatRoomClient: ChatRoomClient = ChatRoomClient()
		
		new Thread(() => {
			while (true) {
				chatRoomClient.readFromServer()
				TimeUnit.SECONDS.sleep(1)
			}
		})
		
		val scanner: Scanner = new Scanner(System.in)
		while (scanner.hasNextLine) {
			val line: String = scanner.nextLine()
			chatRoomClient.sendToServer(line)
		}
	}
}