package cgs.bdl.nio.channel

import java.io.{File, FileInputStream, FileNotFoundException}
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.charset.Charset

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object FileChannelDemo {
	
	private val filePath = "/Users/sherlock/IdeaProjects/Bigdata/bigdata-lecture/bdl-netty/files/apache.txt"
	
	
	def main(args: Array[String]): Unit = {
		println(readFileWithChannel(filePath))
	}
	
	def readFileWithChannel(filePath: String): String = {
		val charset: Charset = Charset.forName("UTF-8")
		val sb: StringBuffer = new StringBuffer()
		val byteBuffer: ByteBuffer = ByteBuffer.allocate(1024)
		var fis: FileInputStream = null
		try {
			val file: File = new File(filePath)
			if (!file.exists() || file.isDirectory) {
				throw new FileNotFoundException()
			}
			fis = new FileInputStream(file)
			val fileChannel: FileChannel = fis.getChannel
			while (fileChannel.read(byteBuffer) >= 0) {
				byteBuffer.flip()
				sb.append(charset.decode(byteBuffer))
				byteBuffer.clear()
			}
		} finally {
			if (fis != null) fis.close()
		}
		sb.toString
	}
}
