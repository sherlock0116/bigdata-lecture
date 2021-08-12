package cgs.bigdata.spark

import scala.io.{BufferedSource, Source}

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object ScalaHighFun {
	
	private val filePath = "/Users/sherlock/IdeaProjects/Bigdata/bigdata-lecture/datas/ch04_data_products.txt"
	
	val findIndex: String => String => Int= { filePath =>
			val source: BufferedSource = Source.fromFile(filePath, "UTF-8")
			val lines: Array[String] = source.getLines().toArray
			source.close()
			val searchMap: Map[String, Int] = lines.zip(lines.indices).toMap
			interest => searchMap.getOrElse(interest, -1)
	}
	
	val partFun: String => Int = findIndex(filePath)
	
}
