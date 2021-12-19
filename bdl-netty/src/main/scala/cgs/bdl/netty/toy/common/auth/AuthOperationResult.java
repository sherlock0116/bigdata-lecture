package cgs.bdl.netty.toy.common.auth;

import cgs.bdl.netty.toy.common.OperationResult;
import lombok.Data;

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */

public class AuthOperationResult extends OperationResult {

	private final boolean passAuth;

	public boolean isPassAuth () {
		return passAuth;
	}

	public AuthOperationResult (boolean passAuth) {
		this.passAuth = passAuth;
	}
}
