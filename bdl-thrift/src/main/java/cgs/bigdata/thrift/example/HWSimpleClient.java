package cgs.bigdata.thrift.example;

import cgs.bigdata.thrift.example.service.HelloWorldService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
public class HWSimpleClient {

	private final int port;
	private final TTransport transport;
	private final HelloWorldService.Client client;

	public HWSimpleClient (int port) throws UnknownHostException, TTransportException {
		this.port = port;
		this.transport = openSocket(port);

		this.client = initClient(this.transport);
	}

	private TTransport openSocket(int port) throws UnknownHostException, TTransportException {
		String hostName = InetAddress.getLocalHost().getHostName();
		TSocket socket = new TSocket(hostName, port);
		socket.open();
		Runtime.getRuntime().addShutdownHook(new Thread(socket::close));
		return socket;
	}

	private HelloWorldService.Client initClient(TTransport transport) {
		assert transport.isOpen();
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		return new HelloWorldService.Client(protocol);
	}

	public String sayHello(String name) throws TException {
		return this.client.sayHello(name);
	}

	public void ping() throws TException {
		this.client.ping();
	}

	public void shutdown() {
		this.transport.close();
	}
}
