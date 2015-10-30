package com.nova.geracao.portfolio.core;

import com.google.gson.annotations.SerializedName;

public class MessageOutcome {

	@SerializedName("type")
	private MessageType type;
	@SerializedName("message")
	private String message;
	@SerializedName("data")
	private Object data;

	public MessageOutcome(MessageType type, String message, Object data) {
		super();
		this.type = type;
		this.message = message;
		this.data = data;
	}

	public MessageOutcome() {
		super();
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
