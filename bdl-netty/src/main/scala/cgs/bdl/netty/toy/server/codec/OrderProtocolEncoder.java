package cgs.bdl.netty.toy.server.codec;

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
public class OrderProtocolEncoder extends MessageToMessageEncoder<ResponseMessage> {

	@Override
	protected void encode (ChannelHandlerContext ctx, ResponseMessage responseMessage, List<Object> out) throws Exception {

		ByteBuf byteBuf = ctx.alloc().buffer();
		responseMessage.encode(byteBuf);
		out.add(byteBuf);
	}
}
