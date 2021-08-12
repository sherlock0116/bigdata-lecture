package cgs.bigdata.spark.sql

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object ReadJDBC {
	
	def main(args: Array[String]): Unit = {
		
		val sparkSession: SparkSession = SparkSession.builder()
				.appName("Jdbc Reader")
				.master("local[1]")
				.getOrCreate()
		
		val df: DataFrame = sparkSession.read.format("jdbc")
				.option("url", "jdbc:mysql://127.0.0.1:3306/employees")
				.option("driver", "com.mysql.jdbc.Driver")
				.option("user", "root")
				.option("password", "1234")
				.option("dbtable", "employees")
				.load()
		df.show()
		sparkSession.close()
	}
}
