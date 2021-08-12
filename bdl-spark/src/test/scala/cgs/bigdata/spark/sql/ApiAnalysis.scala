package cgs.bigdata.spark.sql

import org.apache.spark.sql.execution.datasources.jdbc.{JDBCOptions, JdbcUtils}
import org.apache.spark.sql.jdbc.{JdbcDialect, JdbcDialects}
import org.apache.spark.sql.types.StructType

import java.sql.{Connection, DatabaseMetaData, PreparedStatement, ResultSet, ResultSetMetaData, SQLException}

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object ApiAnalysis {
	
	def main(args: Array[String]): Unit = {
		
		val options: Map[String, String] = Map(
			"url" -> "jdbc:mysql://127.0.0.1:3306/employees",
			"driver" -> "com.mysql.jdbc.Driver",
			"user" -> "root",
			"password" -> "1234",
			"dbtable" -> "employees")
		
		val jdbcOptions: JDBCOptions = new JDBCOptions(options)
		
		val conn: Connection = JdbcUtils.createConnectionFactory(jdbcOptions)()
		val metaData: DatabaseMetaData = conn.getMetaData
		
		val tableExists: Boolean = JdbcUtils.tableExists(conn, jdbcOptions)
		if (tableExists) {
			val dialect: JdbcDialect = JdbcDialects.get(jdbcOptions.url)
			try {
				val statement: PreparedStatement = conn.prepareStatement(dialect.getSchemaQuery(jdbcOptions.table))
				println(s"PreparedStatement => ${statement.toString}")
				
				val resultSet: ResultSet = statement.executeQuery()
				println(s"ResultSet => ${resultSet.toString}")
				
				val rsmd: ResultSetMetaData = resultSet.getMetaData
				println(s"ResultSetMetaData => ${rsmd.toString}")
				
				val ncols: Int = rsmd.getColumnCount
				try {
					val optionStructType: Option[StructType] = JdbcUtils.getSchemaOption(conn, jdbcOptions)
					println(s"StructType => ${optionStructType.get}")
				} catch {
					case _: SQLException => None
				} finally {
					statement.close()
				}
			} catch {
				case _: SQLException => None
			}
		}
	}
}
