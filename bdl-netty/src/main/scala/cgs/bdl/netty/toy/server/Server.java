package cgs.bdl.netty.toy.server;

import cgs.bdl.netty.toy.server.codec.OrderFrameDecoder;
import cgs.bdl.netty.toy.server.codec.OrderFrameEncoder;
import cgs.bdl.netty.toy.server.codec.OrderProtocolDecoder;
import cgs.bdl.netty.toy.server.codec.OrderProtocolEncoder;
import cgs.bdl.netty.toy.server.codec.handler.OrderServerProcessHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.concurrent.ExecutionException;

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
public class Server {

	public static void main (String[] args) throws ExecutionException, InterruptedException {

		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.channel(NioServerSocketChannel.class);
		serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));
		serverBootstrap.group(new NioEventLoopGroup());

		serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
			@Override
			protected void initChannel (NioSocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast(new LoggingHandler(LogLevel.INFO));
				pipeline.addLast(new OrderFrameDecoder());
				pipeline.addLast(new OrderFrameEncoder());
				pipeline.addLast(new OrderProtocolDecoder());
				pipeline.addLast(new OrderProtocolEncoder());
				pipeline.addLast(new OrderServerProcessHandler());
			}
		});

		ChannelFuture channelFuture = serverBootstrap.bind(9223).sync();
		channelFuture.channel().closeFuture().get();
	}
}
