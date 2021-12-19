package cgs.bdl.netty.toy.server.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
public class OrderFrameDecoder extends LengthFieldBasedFrameDecoder {

	public OrderFrameDecoder () {
		super(Integer.MAX_VALUE, 0, 2, 0, 2);
	}
}
