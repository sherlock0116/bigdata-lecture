package cgs.bigdata.util.socket.process

import cgs.bigdata.util.log.Logging

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
trait Processor extends Logging {
	
	def process(): Unit
}
