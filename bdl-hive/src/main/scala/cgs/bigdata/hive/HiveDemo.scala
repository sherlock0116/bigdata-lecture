package cgs.bigdata.hive

import org.apache.hadoop.hive.conf.HiveConf
import org.apache.hadoop.hive.metastore.HiveMetaStoreClient
import org.apache.hadoop.hive.metastore.api.{FieldSchema, MetaException}

import scala.collection.JavaConversions._

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object HiveDemo {
	
	def main(args: Array[String]): Unit = {
		
		val hiveConf: HiveConf = new HiveConf()
//		hiveConf.addResource("hive-site.xml")
		hiveConf.setVar(HiveConf.ConfVars.METASTOREURIS, "thrift://localhost:9083")
		var metaStoreCli: HiveMetaStoreClient = null
		try {
			metaStoreCli = new HiveMetaStoreClient(hiveConf)
			
		} catch {
			case e: MetaException => e.printStackTrace()
		}
		sys.addShutdownHook{
			metaStoreCli.close()
		}
		// 获取 hive 所有数据库, 表元数据
		val databases: Seq[String] = metaStoreCli.getAllDatabases
		println(databases.mkString(", "))
		
		databases.filter(_ != "default").map{db =>
			println(s"|--DB: $db")
			val tables: Seq[String] = metaStoreCli.getAllTables(db)
			tables.map{tb =>
				println(s"|----Table: $tb")
				val fieldSchemas: Seq[FieldSchema] = metaStoreCli.getFields(db, tb)
				println(s"|------${fieldSchemas.mkString("\n|------")}")
			}
		}
	}
}
