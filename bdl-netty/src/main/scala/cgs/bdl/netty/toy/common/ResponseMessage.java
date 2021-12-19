package cgs.bdl.netty.toy.common;

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
public class ResponseMessage extends Message <OperationResult>{

	@Override
	public Class getMessageBodyDecodeClass(int opcode) {
		return OperationType.fromOpCode(opcode).getOperationResultClazz();
	}
}
