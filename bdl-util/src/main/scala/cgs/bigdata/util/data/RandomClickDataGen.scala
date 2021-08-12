package cgs.bigdata.util.data

import java.time.Instant
import scala.util.Random

/**
 * @Description 模拟用户点击网站数据
 * @Author sherlock
 * @Date
 */
class RandomClickDataGen private extends DataGenerator[(String, Long, String)] {
	
	import RandomClickDataGen._
	
	override def generate(): (String, Long, String) = {
		val user: String = users(Random.nextInt(users_size))
		val now: Long = Instant.now().getEpochSecond
		val url: String = urls(Random.nextInt(urls_size))
		(user, now, url)
	}
	
}

object RandomClickDataGen {
	
	private val users = Seq("Jark", "Tom", "Lily", "Bear")
	private val urls = Seq("taobao.com", "google.com", "sina.com", "qq.com", "baidu.com", "2345.com")
	private val users_size = users.size
	private val urls_size = urls.size
	
	def apply(): RandomClickDataGen = new RandomClickDataGen
}
