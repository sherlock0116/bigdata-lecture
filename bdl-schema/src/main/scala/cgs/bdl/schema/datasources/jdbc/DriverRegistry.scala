package cgs.bdl.schema.datasources.jdbc

import cgs.bdl.schema.util.Utils
import cgs.bigdata.util.log.Logging

import java.sql._
import scala.collection.mutable
import scala.collection.JavaConverters._

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object DriverRegistry extends Logging {
	
	private val registerMap: mutable.Map[String, Driver] = mutable.Map.empty
	
	def register(className: String): Unit = {
		val cls: Class[_] = Utils.getContextOrGlobalClassLoader.loadClass(className)
		if (cls.getClassLoader != null) {
			logTrace(s"$className has been loaded with bootstrap ClassLoader, wrapper is not required")
		} else {
			synchronized {
				if (!registerMap.contains(className)) {
					val driver: Driver = cls.getConstructor().newInstance().asInstanceOf[Driver]
					DriverManager.registerDriver(driver)
					registerMap(className) = driver
					logTrace(s"Driver: [$className] registered")
				}
			}
		}
	}
	
	def get(className: String): Driver = {
		DriverManager.getDrivers.asScala.collectFirst {
			case d if d.getClass.getCanonicalName == className => d
		}.getOrElse {
			throw new IllegalStateException(
				s"Did not find registered driver with class $className")
		}
	}
}
