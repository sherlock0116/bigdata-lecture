package cgs.bdl.java.classloader

/**
 * 使用 ClassLoader 去加载 hadoop_class_path 下的 org.apache.hadoop.conf.Configuration
 */
object ClassLoader_Demo {
	
	def main(args: Array[String]): Unit = {
		val env: Map[String, String] = sys.env
		println(env.mkString("\n"))
		
		println("============================================")
		
		val props: Map[String, String] = sys.props.toMap
		println(props.mkString("\n"))
	}
}
