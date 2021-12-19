package cgs.bdl.schema.util

import java.util.Locale

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
class CaseInsensitiveMap[T] private (
									val originalMap: Map[String, T]
									) extends Map[String, T] with Serializable {
	
	val keyLowerCasedMap: Map[String, T] = originalMap.map(kv => kv.copy(_1 = kv._1.toLowerCase(Locale.ROOT)))
	
	override def get(key: String): Option[T] = keyLowerCasedMap.get(key.toLowerCase(Locale.ROOT))
	
	override def contains(key: String): Boolean = keyLowerCasedMap.contains(key.toLowerCase(Locale.ROOT))
	
	override def iterator: Iterator[(String, T)] = keyLowerCasedMap.iterator
	
	override def +[V1 >: T](kv: (String, V1)): CaseInsensitiveMap[V1] = new CaseInsensitiveMap[V1](originalMap.filter(!_._1.equalsIgnoreCase(kv._1)) + kv)
	
	def ++(xs: TraversableOnce[(String, T)]): CaseInsensitiveMap[T] = xs.foldLeft(this)(_ + _)
	
	override def -(key: String): Map[String, T] =  new CaseInsensitiveMap(originalMap.filter(!_._1.equalsIgnoreCase(key)))
	
	def toMap: Map[String, T] = originalMap
}

object CaseInsensitiveMap {
	
	def apply[T](params: Map[String, T]): CaseInsensitiveMap[T] = params match {
		case caseSensitiveMap: CaseInsensitiveMap[T] => caseSensitiveMap
		case _ => new CaseInsensitiveMap(params)
	}
}