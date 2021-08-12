package cgs.bigdata.thrift.example

import org.scalatest.{BeforeAndAfterAll, FunSuite}

import scala.concurrent.Await

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
class HWSimpleServerSuite extends FunSuite with BeforeAndAfterAll {
	
	private var helloworldClient: HWSimpleClient = _
	
	test("sayHello") {
		helloworldClient = new HWSimpleClient(5656)
		val response: String = helloworldClient.sayHello("lily")
		println(response)
		
	}
	
	override protected def afterAll(): Unit = {
		if (helloworldClient != null) helloworldClient.shutdown()
	}
}
