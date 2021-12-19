package cgs.bdl.netty.toy.common.order;

import cgs.bdl.netty.toy.common.Operation;
import cgs.bdl.netty.toy.common.OperationResult;
import lombok.Data;

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */

public class OrderOperation extends Operation {

	private int tableId;
	private String dish;

	public int getTableId () {
		return tableId;
	}

	public void setTableId (int tableId) {
		this.tableId = tableId;
	}

	public String getDish () {
		return dish;
	}

	public void setDish (String dish) {
		this.dish = dish;
	}

	public OrderOperation(int tableId, String dish) {
		this.tableId = tableId;
		this.dish = dish;
	}

	@Override
	public OperationResult execute () {
		System.out.println("order's executing startup with orderRequest: " + toString());
		//execute order logic
		System.out.println("order's executing complete");
		return new OrderOperationResult(tableId, dish, true);
	}
}
