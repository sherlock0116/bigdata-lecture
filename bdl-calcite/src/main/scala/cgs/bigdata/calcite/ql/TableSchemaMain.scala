package cgs.bigdata.calcite.ql

import cgs.bigdata.calcite.ql.TableSchemaMain.{database, host, passwd, port, user}

import java.sql.{Connection, DriverManager, ResultSet, Statement}

/**
 * 尝试获取 Doris 表的 schema
 */
object TableSchemaMain {
	
	val host: String = "10.0.15.131"
	val port: Int = 9030
	val user: String = "root"
	val passwd: String = "si7v3#,a"
	val database: String = "dwd_homdo"
	
	// dwd_homedo.dwd_sub_product_info
	def main(args: Array[String]): Unit = {
		val table: String = "dwd_product_sub_info"
		
		val cnxt: Connection = getConn(host, port, user, passwd, database)
		println(cnxt.getSchema)
		println(cnxt.getMetaData)
		println(cnxt.getCatalog)
		val stmt: Statement = cnxt.prepareStatement(getSchemaQuery(table))
		
	}
	
	def getSchemaQuery(table: String): String = {
		s"SELECT * FROM $table WHERE 1=0"
	}
	
	def getConn(host: String, port: Int, user:String, passwd: String, database: String): Connection = {
		Class.forName("com.mysql.jdbc.Driver")
		val url: String = s"jdbc:mysql://$host:$port/$database?user=$user&password=$passwd"
		DriverManager.getConnection(url)
	}
	
}
