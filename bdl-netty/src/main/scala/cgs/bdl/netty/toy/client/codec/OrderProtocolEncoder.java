package cgs.bdl.netty.toy.client.codec;

import cgs.bdl.netty.toy.common.RequestMessage;
import cgs.bdl.netty.toy.common.ResponseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
public class OrderProtocolEncoder extends MessageToMessageEncoder<RequestMessage> {

	@Override
	protected void encode (ChannelHandlerContext ctx, RequestMessage requestMessage, List<Object> out) throws Exception {

		ByteBuf byteBuf = ctx.alloc().buffer();
		requestMessage.encode(byteBuf);
		out.add(byteBuf);
	}
}
