package cgs.bdl.netty.toy.common;

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
public class RequestMessage extends Message<Operation> {

	@Override
	public Class getMessageBodyDecodeClass (int opcode) {
		return OperationType.fromOpCode(opcode).getOperationClazz();
	}

	public RequestMessage(){}

	public RequestMessage(long streamId, Operation operation){
		MessageHeader messageHeader = new MessageHeader();
		messageHeader.setStreamId(streamId);
		messageHeader.setOpCode(OperationType.fromOperation(operation).getOpCode());
		this.setMessageHeader(messageHeader);
		this.setMessageBody(operation);
	}
}
