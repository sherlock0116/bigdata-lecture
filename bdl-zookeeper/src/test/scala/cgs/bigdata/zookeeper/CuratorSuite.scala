package cgs.bigdata.zookeeper

import org.apache.curator.framework.{CuratorFramework, CuratorFrameworkFactory}
import org.apache.curator.retry.RetryNTimes
import org.apache.zookeeper.CreateMode
import org.apache.zookeeper.KeeperException.NodeExistsException
import org.apache.zookeeper.data.Stat
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite

import java.util.concurrent.TimeUnit

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
class CuratorSuite extends AnyFunSuite
		with BeforeAndAfterAll {
	
	val zkServers: String = "localhost:2181"
	var zkCli: CuratorFramework = _
	val node: String = "/cy"
	
	override protected def beforeAll(): Unit = {
		zkCli = CuratorFrameworkFactory.builder()
				.connectString(zkServers)
				.connectionTimeoutMs(5 * 1000)
				.sessionTimeoutMs(60 * 1000)				// session 有效时长 1 min
				.retryPolicy(new RetryNTimes(10, 100))
				.build();
	}
	
	override protected def afterAll(): Unit = if (zkCli != null && zkCli.isStarted) zkCli.close()
	
	test("CuratorFrameworkState changes before and after calling method start()") {
		
		println(s"before call start() => state = ${zkCli.getState}")		// LATENT
		zkCli.start()
		println(s"after call start() => state = ${zkCli.getState}")		// STARTED
		zkCli.close()
		println(s"after call close() => state = ${zkCli.getState}")		// STOPPED
		
	}
	
	// 创建已存在的 Node 会抛异常 NodeExistsException
	test("create node existed") {
		zkCli.start()
		assertThrows[NodeExistsException](zkCli.create()
				.withMode(CreateMode.EPHEMERAL)
				.forPath("/cy"))
	}
	
	test("create node with data") {
		zkCli.start()
		val stat: Stat = zkCli.checkExists().forPath(node)
		if (stat != null) zkCli.delete().forPath(node)
		zkCli.create()
				.withMode(CreateMode.PERSISTENT)
				.forPath(node, "ragdoll".getBytes)
		println(zkCli.checkExists().forPath(node))
	}
	
	// 测试当会话超时的时候, 临时节点会被删除
	test("znode will be deleted when session expired") {
		try {
			zkCli.start()
			zkCli.create()
					.withMode(CreateMode.EPHEMERAL)
					.forPath("/cy")
			TimeUnit.MINUTES.sleep(2)
		} catch {
			case e: Exception => e.printStackTrace()
		}
		
	}
}
