package cgs.bigdata.thrift.example;


import cgs.bigdata.thrift.example.impl.HelloWordServiceImpl;
import cgs.bigdata.thrift.example.service.HelloWorldService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
public class HWSimpleServer {

	private final int port;
	private final TSimpleServer server;
	private static final Logger log = LoggerFactory.getLogger(HWSimpleServer.class);

	public HWSimpleServer (int port) throws TTransportException {
		this.port = port;
		this.server = initServer(port);
	}

	private TSimpleServer initServer(int serverPort) throws TTransportException {
		HelloWorldService.Processor<HelloWorldService.Iface> helloworldProcessor = new HelloWorldService.Processor<>(new HelloWordServiceImpl());
		TServerSocket serverSocket = new TServerSocket(serverPort);
		TServer.Args tArgs = new TServer.Args(serverSocket);
		tArgs.processor(helloworldProcessor);
		tArgs.protocolFactory(new TBinaryProtocol.Factory());
		return new TSimpleServer(tArgs);
	}

	public void startServer() {
		log.info(String.format("=====> Strat HW Simple Server on port: [%d]", port));
		this.server.serve();
	}

	public void shutdownServer() {
		this.server.stop();
	}

	public static void main (String[] args) throws TTransportException {

		HWSimpleServer server = new HWSimpleServer(5656);
		server.startServer();
		Runtime.getRuntime().addShutdownHook(new Thread(server::shutdownServer));
	}

}
