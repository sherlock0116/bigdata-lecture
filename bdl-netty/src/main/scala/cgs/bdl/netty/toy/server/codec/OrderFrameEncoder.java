package cgs.bdl.netty.toy.server.codec;

import io.netty.handler.codec.LengthFieldPrepender;

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
public class OrderFrameEncoder extends LengthFieldPrepender {

	public OrderFrameEncoder () {
		super(2);
	}
}
