package com.nova.geracao.portfolio.core;

public enum MessageType {

	INFO("Info"),
	ERROR("Error"),
	SUCCESS("Success");
	
	private String description;
	
	MessageType(String description){
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
