package cgs.bigdata.hadoop.hdfs

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs._
import org.apache.hadoop.io.IOUtils
import org.apache.hadoop.security.UserGroupInformation
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.should.Matchers
import org.slf4j.{Logger, LoggerFactory}

import java.io.{FileInputStream, FileOutputStream}

/**
 *
 */

class HdfsSpec extends AnyFunSuiteLike
		with BeforeAndAfterAll
		with Matchers {
	
	private val log: Logger = LoggerFactory.getLogger(this.getClass)
	private val config: Configuration = new Configuration
	private var hdfs: FileSystem = _
	
	override protected def beforeAll(): Unit = {
//		hdfs = FileSystem.get(new URI("hdfs://localhost:9000"), new Configuration, "sherlock")
		config.set("fs.defaultFS", "hdfs://localhost:9000")
		config.setInt("dfs.replication", 1)
		hdfs = FileSystem.get(config);
	}
	
	override protected def afterAll(): Unit = {
		if (hdfs != null) hdfs.close()
	}
	
	test("mkDirs") {
		val dir: String = "/lecture/hadoop"
		val path: Path = new Path(dir)
		if (!hdfs.exists(path)) hdfs.mkdirs(path)
		else log.info(s"=====> HDFS Path:[${path.toString}] already exists !")
	}
	
	test("delete") {
		val dir: String = "/movies/小森林-冬春篇.mp4"
		val path: Path = new Path(dir)
		if (hdfs.exists(path)) hdfs.delete(path, true)
		else log.info(s"=====> HDFS Path:[${path.toString}] doesn't exists !")
	}
	
	/*
		21/02/28 00:43:45 INFO HdfsSpec: ====> FileBaseInfo: LocatedFileStatus {
				path=hdfs://localhost:9000/movies/小森林-冬春篇.mp4;
				isDirectory=false;
				length=1991321685;
				replication=1;
				blocksize=134217728;
				modification_time=1614444190388;
				access_time=1614444175652;
				owner=sherlock;
				group=supergroup;
				permission=rw-r--r--;
				isSymlink=false
		}
		21/02/28 00:43:45 INFO HdfsSpec: ====> BlockLocations: [
				0,134217728,localhost|
				134217728,134217728,localhost|
				268435456,134217728,localhost|
				402653184,134217728,localhost|
				536870912,134217728,localhost|
				671088640,134217728,localhost|
				805306368,134217728,localhost|
				939524096,134217728,localhost|
				1073741824,134217728,localhost|
				1207959552,134217728,localhost|
				1342177280,134217728,localhost|
				1476395008,134217728,localhost|
				1610612736,134217728,localhost|
				1744830464,134217728,localhost|
				1879048192,112273493,localhost
		]
	 */
	test("listFiles") {
		val dir: String = "/movies"
		val files: RemoteIterator[LocatedFileStatus] = hdfs.listFiles(new Path(dir), true)
		while(files.hasNext) {
			val fileStatus: LocatedFileStatus = files.next
			log.info(s"====> FileBaseInfo: ${fileStatus.toString}")
			val locations: Array[BlockLocation] = fileStatus.getBlockLocations
			log.info(s"====> BlockLocations: [${locations.mkString("|")}]")
		}
	}
	
	test("Transfer files using the base IO stream") {
		val file: String = "/Users/sherlock/Movies/小森林-冬春篇.mp4"
		val fis: FileInputStream = new FileInputStream(file)
		val fos: FSDataOutputStream = hdfs.create(new Path("/movies/小森林-冬春篇.mp4"))
		IOUtils.copyBytes(fis, fos, config)
		// 这里其实不同自己手动关闭流, 因为 IOUtils # copyBytes 中已经关闭了流
		IOUtils.closeStream(fos)
		IOUtils.closeStream(fis)
	}
	
	// 这里如果要改进, 可以使用线程池来完成
	// 经测试一部 1.2GB, 120 min 的电影被划分成 15 block, 一个 block 块刚好只能播放 8min 左右
	test("Download file in chunks") {
		val fis: FSDataInputStream = hdfs.open(new Path("/movies/小森林-冬春篇.mp4"))
		val _1MB: Array[Byte] = new Array[Byte](1024 * 1024)
		val block_size: Int = 1024 * 1024 * 128
		// 下载第一个 block 块
		val part1: FileOutputStream = new FileOutputStream("datas/小森林-冬春篇.mp4.part-001")
		for (i <- 0 until 128) {
			fis.read(_1MB)
			part1.write(_1MB)
		}
		IOUtils.closeStream(part1)
		// 下载第二个 block 块
		val part2: FileOutputStream = new FileOutputStream("datas/小森林-冬春篇.mp4.part-002")
		fis.seek(1 * block_size)
		for (i <- 0 until 128) {
			fis.read(_1MB)
			part2.write(_1MB)
		}
		IOUtils.closeStream(part2)
		
		IOUtils.closeStream(fis)
	}
	
	// 这只是一个示例代码, 无法运行, 因为 Ticket 并不存在
	test("auth Kerberos") {
		val principal: String = "root/admin@HADOOP.COM"					// principal
		val ticket: String = "path/to/root.keytab"								// ticket
		val conf: Configuration = new Configuration
		val hdfs_site: String = "path/to/hdfs-site.xml"
		conf.addResource(hdfs_site)
		conf.set("hadoop.security.authentication", "Kerberos")
		UserGroupInformation.setConfiguration(conf)
		// 这一步不报错, 就是真正验证通过了
		UserGroupInformation.loginUserFromKeytab(principal, ticket)
		import org.apache.hadoop.fs.FileSystem
		val hdfs: FileSystem = FileSystem.get(conf)
	}
	
}
