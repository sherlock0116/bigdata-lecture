package cgs.bdl.schema.datasources.jdbc

import cgs.bdl.schema.datasources.jdbc.JDBCOptions._
import cgs.bdl.schema.util.CaseInsensitiveMap
import cgs.bigdata.util.log.Logging

import java.sql.DriverManager
import java.util.{Locale, Properties}

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
class JDBCOptions (
						  val parameters: CaseInsensitiveMap[String]
				  ) extends Serializable with Logging {
	
	def this(parameters: Map[String, String]) = this(CaseInsensitiveMap(parameters))
	
	def this(url: String, user: String, password: String) = {
		this(CaseInsensitiveMap(Map (
			JDBC_URL -> url,
			JDBC_USER -> user,
			JDBC_PASSWORD -> password)))
	}
	
	def this(url: String, user: String, password: String, parameters: Map[String, String]) = {
		this(CaseInsensitiveMap(parameters ++ Map (
			JDBC_URL -> url,
			JDBC_USER -> user,
			JDBC_PASSWORD -> password)))
	}
	
	val asProperties: Properties = {
		val properties: Properties = new Properties()
		parameters.originalMap.foreach { case (k, v) => properties.setProperty(k, v) }
		properties
	}
	
	val asConnectionProperties: Properties = {
		val properties: Properties = new Properties()
		parameters.originalMap.filterKeys(key => !jdbcOptionNames(key.toLowerCase(Locale.ROOT))).foreach {case (k, v) => properties.setProperty(k, v)}
		properties
	}
	
	// ------------------------------------------------------------
	// Required parameters
	// ------------------------------------------------------------
	require(parameters.isDefinedAt(JDBC_URL), s"Option '$JDBC_URL' is required.")
	require(parameters.isDefinedAt(JDBC_USER), s"Option '$JDBC_USER' is required.")
	require(parameters.isDefinedAt(JDBC_PASSWORD), s"Option '$JDBC_PASSWORD' is required.")
	// a JDBC URL
	val url: String = parameters(JDBC_URL)
	val user: String = parameters(JDBC_USER)
	val password: String = parameters(JDBC_PASSWORD)
	
	// ------------------------------------------------------------
	// Optional parameters
	// ------------------------------------------------------------
	val driverClass: String = {
		val userSpecifiedDriverClass: Option[String] = parameters.get(JDBC_DRIVER_CLASS)
		userSpecifiedDriverClass.foreach(DriverRegistry.register)
		// 如果用户没有传递 Driver, 可以通过 url 获取 Driver Class Name
		userSpecifiedDriverClass.getOrElse {
			DriverManager.getDriver(url).getClass.getCanonicalName
		}
	}
}

object JDBCOptions {
	
	private val curId = new java.util.concurrent.atomic.AtomicLong(0L)
	private val jdbcOptionNames = collection.mutable.Set[String]()
	
	private def newOption(name: String): String = {
		jdbcOptionNames += name.toLowerCase(Locale.ROOT)
		name
	}
	
	val JDBC_URL: String 																= newOption("url")
	val JDBC_USER: String 																= newOption("user")
	val JDBC_PASSWORD: String 													= newOption("password")
	val JDBC_TABLE_NAME: String 												= newOption("dbtable")
	val JDBC_QUERY_STRING: String 											= newOption("query")
	val JDBC_DRIVER_CLASS: String 												= newOption("driver")
	val JDBC_PARTITION_COLUMN: String 									= newOption("partitionColumn")
	val JDBC_LOWER_BOUND: String 											= newOption("lowerBound")
	val JDBC_UPPER_BOUND: String 												= newOption("upperBound")
	val JDBC_NUM_PARTITIONS: String			 							= newOption("numPartitions")
	val JDBC_QUERY_TIMEOUT: String 											= newOption("queryTimeout")
	val JDBC_BATCH_FETCH_SIZE: String 									= newOption("fetchsize")
	val JDBC_TRUNCATE: String 														= newOption("truncate")
	val JDBC_CASCADE_TRUNCATE: String 									= newOption("cascadeTruncate")
	val JDBC_CREATE_TABLE_OPTIONS: String 							= newOption("createTableOptions")
	val JDBC_CREATE_TABLE_COLUMN_TYPES: String 				= newOption("createTableColumnTypes")
	val JDBC_CUSTOM_DATAFRAME_COLUMN_TYPES: String 	= newOption("customSchema")
	val JDBC_BATCH_INSERT_SIZE: String 									= newOption("batchsize")
	val JDBC_TXN_ISOLATION_LEVEL: String 								= newOption("isolationLevel")
	val JDBC_SESSION_INIT_STATEMENT: String 						= newOption("sessionInitStatement")
	val JDBC_PUSHDOWN_PREDICATE: String 								= newOption("pushDownPredicate")
	val JDBC_KEYTAB: String 															= newOption("keytab")
	val JDBC_PRINCIPAL: String														= newOption("principal")
	val JDBC_TABLE_COMMENT: String 										= newOption("tableComment")
	val JDBC_REFRESH_KRB5_CONFIG: String 								= newOption("refreshKrb5Config")
}
