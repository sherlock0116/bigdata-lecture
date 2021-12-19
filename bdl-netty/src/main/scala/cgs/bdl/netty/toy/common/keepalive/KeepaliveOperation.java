package cgs.bdl.netty.toy.common.keepalive;

import cgs.bdl.netty.toy.common.Operation;
import cgs.bdl.netty.toy.common.OperationResult;
import lombok.Data;

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */

public class KeepaliveOperation extends Operation {

	private long time;

	public long getTime () {
		return time;
	}

	public void setTime (long time) {
		this.time = time;
	}

	public KeepaliveOperation() {
		this.time = System.nanoTime();
	}

	@Override
	public OperationResult execute () {
		return new KeepaliveOperationResult(time);
	}
}
