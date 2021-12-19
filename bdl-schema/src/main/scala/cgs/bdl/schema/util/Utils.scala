package cgs.bdl.schema.util

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object Utils {
	
	
	def getContextOrGlobalClassLoader: ClassLoader =
		Option(Thread.currentThread().getContextClassLoader).getOrElse(getLocalClassLoader)
	
	def getLocalClassLoader: ClassLoader = getClass.getClassLoader
}
