package cgs.bdl.netty.toy.common.keepalive;

import cgs.bdl.netty.toy.common.OperationResult;
import lombok.Data;

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */

public class KeepaliveOperationResult extends OperationResult {

	private final long time;

	public KeepaliveOperationResult (long time) {
		this.time = time;
	}

	public long getTime () {
		return time;
	}
}
