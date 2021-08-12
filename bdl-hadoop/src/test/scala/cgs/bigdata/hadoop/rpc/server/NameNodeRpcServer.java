package cgs.bigdata.hadoop.rpc.server;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.ipc.RPC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 *
 */
public class NameNodeRpcServer implements ClientProtocols {

	private static final long serialVersionUID = -1285247792584623572L;
	private static final Logger log = LoggerFactory.getLogger(NameNodeRpcServer.class);

	@Override
	public void makeDir(String path) {

		log.info("make dir: [{}]", path);
	}

	public static void main(String[] args) throws IOException {

		RPC.Builder rpcBuilder = new RPC.Builder(new Configuration())
				.setBindAddress("localhost")
				.setPort(9999)
				.setProtocol(ClientProtocols.class)
				.setInstance(new NameNodeRpcServer());

		RPC.Server server = rpcBuilder.build();
		server.start();
	}
}
