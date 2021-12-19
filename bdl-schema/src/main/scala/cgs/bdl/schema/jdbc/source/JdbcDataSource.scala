package cgs.bdl.schema.jdbc.source

import java.sql.Connection

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
abstract class JdbcDataSource(
					val url: String,
					val username: String,
					val password: String
					) {
	 
	def getConnection: Connection
}
