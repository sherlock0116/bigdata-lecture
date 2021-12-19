package cgs.bdl.hadoop.hdfs.write

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, LocatedFileStatus, Path, RemoteIterator}

import java.io.RandomAccessFile


/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object DummyHDFSClient {
	
	var hdfsClient: FileSystem = _
	
	val src_path: String = "/Users/sherlock/IdeaProjects/Bigdata/bigdata-lecture/bdl-hadoop/data/example.txt"
	val tgt_path: String = "/yxy"
	
	def main(args: Array[String]): Unit = {
		try {
			System.setProperty("HADOOP_USER_NAME", "yxy")
			val conf: Configuration = new Configuration()
			conf.set("fs.defaultFS", "hdfs://gaea:9669")
			conf.set("dfs.replication", "1")
			hdfsClient = getHDFSClient(conf)
			hdfsClient.copyFromLocalFile(false, true, new Path(src_path), new Path(tgt_path))
		} finally {
			hdfsClient.close()
		}
		
	}
	
	def getHDFSClient(configuration: Configuration): FileSystem = {
		FileSystem.get(configuration)
	}
}
