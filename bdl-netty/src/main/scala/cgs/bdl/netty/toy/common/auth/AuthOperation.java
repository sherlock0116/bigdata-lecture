package cgs.bdl.netty.toy.common.auth;

import cgs.bdl.netty.toy.common.Operation;
import cgs.bdl.netty.toy.common.OperationResult;
import lombok.Data;

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */

public class AuthOperation extends Operation {

	private String username;
	private String password;

	public String getUsername () {
		return username;
	}

	public void setUsername (String username) {
		this.username = username;
	}

	public String getPassword () {
		return password;
	}

	public void setPassword (String password) {
		this.password = password;
	}

	@Override
	public AuthOperationResult execute () {
		return "admin".equalsIgnoreCase(this.username) ? new AuthOperationResult(true): new AuthOperationResult(false);
	}
}
