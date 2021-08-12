package cgs.bigdata.util.data

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
trait DataGenerator[T] {
	
	def generate(): T
}
