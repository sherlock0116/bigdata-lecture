package cgs.bigdata.spark

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers


/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
class SparkSuite extends AnyFunSuiteLike
		with Matchers
		with BeforeAndAfterAll {
	
	val transFile: String = "data/ch04_data_transactions.txt"
	val prodsFile: String = "data/ch04_data_products.txt"
	
	var sparkSession: SparkSession = _
	var sparkContext: SparkContext = _
	
	test("RDD # distinct()") {
		val rdd: RDD[String] = sparkContext.textFile(transFile)
		val cnt: Long = rdd.map(_.split("#"))
				.map { arr => (arr(2).toInt, arr) }
				.keys
				.distinct()
				.count()
		println(cnt)		// 100
	}
	
	test("RDD # countByKey()") {
		val rdd: RDD[String] = sparkContext.textFile(transFile)
		val transByCust: RDD[(Int, Array[String])] = rdd.map(_.split("#"))
				.map { arr => (arr(2).toInt, arr) }
		/*
			Map(69 -> 7, 88 -> 5, 5 -> 11, 10 -> 7, 56 -> 17, 42 -> 7, 24 -> 9, 37 -> 7, 25 -> 12,
			 		52 -> 9, 14 -> 8, 20 -> 8, 46 -> 9, 93 -> 12, 57 -> 8, 78 -> 11, 29 -> 9, 84 -> 9,
			 		61 -> 8, 89 -> 9, 1 -> 9, 74 -> 11, 6 -> 7, 60 -> 4, 85 -> 9, 28 -> 11, 38 -> 9,
			 		70 -> 8, 21 -> 13, 33 -> 9, 92 -> 8, 65 -> 10, 97 -> 12, 9 -> 7, 53 -> 19, 77 -> 11,
			 		96 -> 8, 13 -> 12, 41 -> 12, 73 -> 7, 2 -> 15, 32 -> 14, 34 -> 14, 45 -> 11, 64 -> 10,
			 		17 -> 13, 22 -> 10, 44 -> 8, 59 -> 9, 27 -> 7, 71 -> 10, 12 -> 7, 54 -> 7, 49 -> 8,
			 		86 -> 9, 81 -> 9, 76 -> 15, 7 -> 10, 39 -> 11, 98 -> 11, 91 -> 13, 66 -> 11, 3 -> 13,
			 		80 -> 7, 35 -> 10, 48 -> 5, 63 -> 12, 18 -> 9, 95 -> 8, 50 -> 14, 67 -> 5, 16 -> 8,
			 		31 -> 14, 11 -> 8, 72 -> 7, 43 -> 12, 99 -> 12, 87 -> 10, 40 -> 10, 26 -> 11, 55 -> 13,
			 		23 -> 13, 8 -> 10, 75 -> 10, 58 -> 13, 82 -> 13, 36 -> 5, 30 -> 5, 51 -> 18, 19 -> 6,
			 		4 -> 11, 79 -> 13, 94 -> 12, 47 -> 13, 15 -> 10, 68 -> 12, 62 -> 6, 90 -> 8, 83 -> 12, 100 -> 12)
		 */
		val maxShoppingCustom: (Int, Long) = transByCust.countByKey().toSeq.maxBy(_._2)
		println(maxShoppingCustom)		// (53,19)
	}
	
	test("RDD # lookup()") {
		val rdd: RDD[String] = sparkContext.textFile(transFile)
		val transByCust: RDD[(Int, Array[String])] = rdd.map(_.split("#"))
				.map { arr => (arr(2).toInt, arr) }
		/*
			2015-03-30, 6:18 AM, 53, 42, 5, 2197.85
			2015-03-30, 4:42 AM, 53, 44, 6, 9182.08
			2015-03-30, 2:51 AM, 53, 59, 5, 3154.43
			2015-03-30, 5:57 PM, 53, 31, 5, 6649.27
			2015-03-30, 6:11 AM, 53, 33, 10, 2353.72
			2015-03-30, 9:46 PM, 53, 93, 1, 2889.03
			2015-03-30, 4:15 PM, 53, 72, 7, 9157.55
			2015-03-30, 2:42 PM, 53, 94, 1, 921.65
			2015-03-30, 8:30 AM, 53, 38, 5, 4000.92
			2015-03-30, 6:06 AM, 53, 12, 6, 2174.02
			2015-03-30, 3:44 AM, 53, 47, 1, 7556.32
			2015-03-30, 10:25 AM, 53, 30, 2, 5107.0
			2015-03-30, 1:48 AM, 53, 58, 4, 718.93
			2015-03-30, 9:31 AM, 53, 18, 4, 8214.79
			2015-03-30, 9:04 AM, 53, 68, 4, 9246.59
			2015-03-30, 1:51 AM, 53, 40, 1, 4095.5
			2015-03-30, 1:53 PM, 53, 85, 9, 1630.24
			2015-03-30, 6:51 PM, 53, 100, 1, 1694.52
			2015-03-30, 7:39 PM, 53, 100, 8, 7885.35
		 */
		transByCust.lookup(53).foreach {tran => println(tran.mkString(", "))}
	}
	
	test("RDD  # mapValues()") {
		val rdd: RDD[String] = sparkContext.textFile(transFile)
		val transByCust: RDD[(Int, Array[String])] = rdd.map(_.split("#"))
				.map { arr => (arr(2).toInt, arr) }
		
		/*
			打折前:
			2015-03-30, 7:27 AM, 93, 25, 7, 2749.15
			2015-03-30, 1:07 AM, 68, 25, 9, 8391.61
			2015-03-30, 1:23 AM, 59, 25, 5, 5296.69
			2015-03-30, 6:26 PM, 17, 25, 6, 7193.11
			2015-03-30, 9:45 AM, 42, 25, 10, 1363.97
			2015-03-30, 10:40 PM, 77, 25, 3, 3345.81
			2015-03-30, 12:53 PM, 22, 25, 9, 6996.42
			2015-03-30, 12:44 AM, 32, 25, 8, 8849.5
			2015-03-30, 5:30 PM, 75, 25, 10, 3557.01
		 */
		transByCust.values.foreach{tran =>
			if (tran(3).toInt == 25 && tran(4).toDouble > 1)
				println(tran.mkString(", "))
		}
		
		val transByCustDisc: RDD[(Int, Array[String])] = transByCust.mapValues { arr =>
			if (arr(3).toInt == 25 && arr(4).toDouble > 1)
				arr(5) = (arr(5).toDouble * 0.85).formatted("%.2f")
			arr
		}
		/*
			打折后:
			2015-03-30, 7:27 AM, 93, 25, 7, 2336.78
			2015-03-30, 1:07 AM, 68, 25, 9, 7132.87
			2015-03-30, 1:23 AM, 59, 25, 5, 4502.19
			2015-03-30, 6:26 PM, 17, 25, 6, 6114.14
			2015-03-30, 9:45 AM, 42, 25, 10, 1159.37
			2015-03-30, 10:40 PM, 77, 25, 3, 2843.94
			2015-03-30, 12:53 PM, 22, 25, 9, 5946.96
			2015-03-30, 12:44 AM, 32, 25, 8, 7522.08
			2015-03-30, 5:30 PM, 75, 25, 10, 3023.46
		 */
		transByCustDisc.values.foreach{tran =>
			if (tran(3).toInt == 25 && tran(4).toDouble > 1)
				println(tran.mkString(", "))
		}
	}
	
	override protected def beforeAll(): Unit = {
		sparkSession = SparkSession.builder()
				.appName("spark api test")
				.master("local[*]")
				.getOrCreate()
		
		sparkContext = sparkSession.sparkContext
	}
	
	override protected def afterAll(): Unit = {
		if (sparkSession != null) sparkSession.close()
	}
}
