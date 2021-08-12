package cgs.bigdata.hadoop.rpc.client

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.ipc.RPC
import cgs.bigdata.hadoop.rpc.server.ClientProtocols

import java.net.InetSocketAddress

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object DfsClient {
	
	def main(args: Array[String]): Unit = {
		val proxy: ClientProtocols = RPC.getProxy (
			classOf[Nothing],
			ClientProtocols.versionID,
			new InetSocketAddress("localhost", 9999),
			new Configuration)
		
		proxy.makeDir("/sherlock/tmp/data")
	}
}
