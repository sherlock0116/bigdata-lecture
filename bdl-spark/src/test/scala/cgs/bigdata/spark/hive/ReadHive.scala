package cgs.bigdata.spark.hive

import org.apache.spark.sql.SparkSession

import java.io.File

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object ReadHive {
	
	val warehousePath = "bdl-spark/spark-warehouse"
	def main(args: Array[String]): Unit = {
		val warehouseLocation: String = new File(warehousePath).getAbsolutePath
		val sparkSession: SparkSession = SparkSession
				.builder
				.appName("Connect Hive")
				.master("local[1]")
				.config("spark.sql.warehouse.dir", warehouseLocation)
				.enableHiveSupport()
				.getOrCreate()
		
		import sparkSession.implicits._
		import sparkSession.sql
		
		sql("show databases").collect().foreach(println)
	}
}
