package cgs.bigdata.kafka.zerocopy

import java.io.{ByteArrayInputStream, File, RandomAccessFile}
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.Scanner

/**
 * Java 中  MappedByteBuffer 实现了基于 mmap 机制的零拷贝
 */
object ZeroCopyDemo {
	
	val file_path: String = "/Users/sherlock/IdeaProjects/Bigdata/bigdata-lecture/bdl-kafka/data/apache.txt"
	
	// 128MB
	val length: Int = 0x8FFFFFF;
	
	def main(args: Array[String]): Unit = {
		
		val file: File = new File(file_path)
		val fileLength: Int = file.length().toInt
		val buffer: Array[Byte] = new Array[Byte](fileLength)
		
		try {
			val mappedByteBuffer: MappedByteBuffer = new RandomAccessFile(file, "r")
					.getChannel
					.map(FileChannel.MapMode.READ_ONLY, 0, fileLength)
			for (index <- 0 until fileLength) {
				buffer(index) = mappedByteBuffer.get()
			}
			val scanner: Scanner = new Scanner(new ByteArrayInputStream(buffer)).useDelimiter("\n")
			while (scanner.hasNext) {
				println(scanner.next())
			}
		} catch {
			case _: Exception =>
		}
	}
}
