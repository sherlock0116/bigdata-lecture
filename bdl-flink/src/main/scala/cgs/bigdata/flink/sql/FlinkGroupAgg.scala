package cgs.bigdata.flink.sql

import org.apache.flink.api.common.eventtime._
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.apache.flink.table.api.{EnvironmentSettings, Table, UnresolvedFieldExpression}
import org.apache.flink.table.api.bridge.scala.StreamTableEnvironment
import org.apache.flink.types.Row

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object FlinkGroupAgg {
	
	def main(args: Array[String]): Unit = {
		
		val envSetting: EnvironmentSettings = EnvironmentSettings.newInstance()
				.useBlinkPlanner()
				.inStreamingMode()
				.build()
		
		val sEnv: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
		sEnv.setParallelism(1)
		sEnv.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
		sEnv.getConfig.setAutoWatermarkInterval(3000L)
		
		val tbEnv: StreamTableEnvironment = StreamTableEnvironment.create(sEnv, envSetting)
		
		val source: DataStream[String] = sEnv.socketTextStream("localhost", 2525)
		
		import org.apache.flink.api.scala._
		val sourceWithWM: DataStream[Click] = source.map { line =>
//			println(s"$line")
			val eles: Array[String] = line.substring(1, line.length - 1).trim.split("\\s*,\\s*")
//			println(s"${eles.mkString(",")}")
			clickGenFromArray(eles)
		}.assignTimestampsAndWatermarks(new AscendingPeriodicWatermark)
		
		import org.apache.flink.table.api._
		val table: Table = tbEnv.fromDataStream(sourceWithWM, 'name, 'ts.rowtime, 'url)
		tbEnv.createTemporaryView("tb_click", table)
		
		val PRINT_SINK_DDL: String =
			"""
			  |CREATE TABLE sink_print (
			  |	name STRING,
			  |	cnt BIGINT
			  |) with (
			  |	'connector' = 'print'
			  |)
			  |""".stripMargin
		
		val GROUP_AGG_DQL: String =
			"""
			  |SELECT
			  |	name,
			  | 	count(url) AS cnt
			  |FROM
			  |	tb_click
			  |GROUP BY name
			  |""".stripMargin
		
//		tbEnv.executeSql(PRINT_SINK_DDL)
		val resultTable: Table = tbEnv.sqlQuery(GROUP_AGG_DQL)
		tbEnv.toRetractStream[Row](resultTable).print()
		
		sEnv.execute("group aggregate")
	}
	
	// 自定义周期性单调增长 Watermark
	// Watermark 发射周期可以在 sEnv.getConfig.setAutoWatermarkInterval(3000L) 中设置
	class AscendingPeriodicWatermark extends WatermarkStrategy[Click] {
		override def createWatermarkGenerator(context: WatermarkGeneratorSupplier.Context): WatermarkGenerator[Click] = {
			
			new WatermarkGenerator[Click] {
				private var maxTS: Long = 0L
				private val delay: Long = 0L
				override def onEvent(event: Click, eventTimestamp: Long, output: WatermarkOutput): Unit = maxTS = Math.max(maxTS, event.ts)
				override def onPeriodicEmit(output: WatermarkOutput): Unit = output.emitWatermark(new Watermark(maxTS - delay))
			}
		}
	}
	
	case class Click(name: String, ts: Long, url: String)
	
	def clickGenFromArray(arr: Array[String]): Click = {
		require(arr.length == 3, s"Length of ${arr.mkString("Array(", ", ", ")")} == ${arr.length}")
		Click(arr(0), arr(1).toLong, arr(2))
	}
}
