package cgs.bigdata.zookeeper

import org.apache.zookeeper.data.Stat
import org.apache.zookeeper.{WatchedEvent, Watcher, ZooKeeper}

import java.util.concurrent.CountDownLatch

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object ZkDebug {
	
	private val cnxcSemaphore = new CountDownLatch(1)
	private val ZK_SERVERS = "127.0.0.1:2181"
	private val SESSION_TIME = 30000
	private var zk: ZooKeeper = _
	private val watcher: Watcher = new Watcher {
		override def process(event: WatchedEvent): Unit = println(s"watch event: ${event.toString}")
	}
	
	/**
	 * 获取 Zk 客户端实例
	 */
	def createZkInstance(): Unit = {
		if (zk == null || !zk.getState.isAlive) {
			zk = new ZooKeeper(ZK_SERVERS, SESSION_TIME, watcher)
		}
		println(s"sessionId: ${zk.getSessionId}")
	}
	
	def setData(path: String, data: Array[Byte]): Unit = {
		createZkInstance()
		val stat: Stat = zk.setData(path, data, -1)
		if (stat != null) println("节点内容设置成功")
	}
	
	def main(args: Array[String]): Unit = {
	
	
	}
}
