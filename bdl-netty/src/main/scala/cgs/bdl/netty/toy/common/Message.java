package cgs.bdl.netty.toy.common;

import cgs.bdl.netty.toy.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.buffer.ByteBuf;
import lombok.Data;

import java.nio.charset.StandardCharsets;

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */


public abstract class Message<T extends MessageBody> {

	private MessageHeader messageHeader;
	private T messageBody;

	public T getMessageBody () {
		return messageBody;
	}

	public void setMessageBody (T messageBody) {
		this.messageBody = messageBody;
	}

	public MessageHeader getMessageHeader () {
		return messageHeader;
	}

	public void setMessageHeader (MessageHeader messageHeader) {
		this.messageHeader = messageHeader;
	}

	public abstract Class<T> getMessageBodyDecodeClass (int opCode);

	public void encode(ByteBuf byteBuf) {
		byteBuf.writeInt(messageHeader.getVersion());
		byteBuf.writeLong(messageHeader.getStreamId());
		byteBuf.writeInt(messageHeader.getOpCode());
	}

	public void decode(ByteBuf msg) throws JsonProcessingException {
		int version = msg.readInt();
		int opCode = msg.readInt();
		long streamId = msg.readLong();

		MessageHeader messageHeader = new MessageHeader();
		messageHeader.setVersion(version);
		messageHeader.setOpCode(opCode);
		messageHeader.setStreamId(streamId);
		this.messageHeader = messageHeader;

		Class<T> bodyClazz = getMessageBodyDecodeClass(opCode);
		this.messageBody = JsonUtils.fromJson(msg.toString(StandardCharsets.UTF_8), bodyClazz);
	}

}
