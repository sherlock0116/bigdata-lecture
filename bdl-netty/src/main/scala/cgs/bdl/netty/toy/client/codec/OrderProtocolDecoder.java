package cgs.bdl.netty.toy.client.codec;

import cgs.bdl.netty.toy.common.RequestMessage;
import cgs.bdl.netty.toy.common.ResponseMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
public class OrderProtocolDecoder extends MessageToMessageDecoder<ByteBuf> {

	@Override
	protected void decode (ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.decode(msg);

		out.add(responseMessage);
	}
}
