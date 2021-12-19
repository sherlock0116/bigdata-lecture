package cgs.bdl.netty.toy.server.codec.handler;

import cgs.bdl.netty.toy.common.Operation;
import cgs.bdl.netty.toy.common.OperationResult;
import cgs.bdl.netty.toy.common.RequestMessage;
import cgs.bdl.netty.toy.common.ResponseMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
public class OrderServerProcessHandler extends SimpleChannelInboundHandler<RequestMessage> {

	@Override
	protected void channelRead0 (ChannelHandlerContext ctx, RequestMessage requestMessage) throws Exception {

		Operation operation = requestMessage.getMessageBody();
		OperationResult operationResult = operation.execute();

		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setMessageHeader(requestMessage.getMessageHeader());
		responseMessage.setMessageBody(operationResult);

		ctx.writeAndFlush(responseMessage);
	}
}
