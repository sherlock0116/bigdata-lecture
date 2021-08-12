package cgs.bigdata.util.socket

import cgs.bigdata.util.log.Logging

import java.io.{Closeable, IOException}

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object StreamUtil extends Logging {
	
	def close(c: Closeable): Unit = {
		if (c == null) return
		try {
			c.close()
		} catch {
			case e: IOException => logger.error(s"Error on close, ${c.getClass.getName}", e)
		}
	}
	
}
