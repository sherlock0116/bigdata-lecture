package cgs.bdl.netty.toy.client;

import cgs.bdl.netty.toy.common.RequestMessage;
import cgs.bdl.netty.toy.common.order.OrderOperation;
import cgs.bdl.netty.toy.client.codec.OrderFrameDecoder;
import cgs.bdl.netty.toy.client.codec.OrderFrameEncoder;
import cgs.bdl.netty.toy.client.codec.OrderProtocolDecoder;
import cgs.bdl.netty.toy.client.codec.OrderProtocolEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
public class Client {

	public static void main (String[] args) throws Exception {

		Bootstrap bootstrap = new Bootstrap();
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.group(new NioEventLoopGroup());

		bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
			@Override
			protected void initChannel (NioSocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast(new LoggingHandler(LogLevel.INFO));
				pipeline.addLast(new OrderFrameDecoder());
				pipeline.addLast(new OrderFrameEncoder());
				pipeline.addLast(new OrderProtocolDecoder());
				pipeline.addLast(new OrderProtocolEncoder());
			}
		});

		ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9223);
		channelFuture.sync();
		RequestMessage potatoes = new RequestMessage(1023L, new OrderOperation(11, "potatoes"));
		channelFuture.channel().writeAndFlush(potatoes);
		channelFuture.channel().closeFuture().get();
	}
}
