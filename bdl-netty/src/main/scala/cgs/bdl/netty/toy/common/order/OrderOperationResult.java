package cgs.bdl.netty.toy.common.order;

import cgs.bdl.netty.toy.common.OperationResult;
import lombok.Data;

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */

public class OrderOperationResult extends OperationResult {

	private final int tableId;
	private final String dish;
	private final boolean complete;

	public OrderOperationResult (int tableId, String dish, boolean complete) {
		this.tableId = tableId;
		this.dish = dish;
		this.complete = complete;
	}

	public int getTableId () {
		return tableId;
	}

	public String getDish () {
		return dish;
	}

	public boolean isComplete () {
		return complete;
	}
}
