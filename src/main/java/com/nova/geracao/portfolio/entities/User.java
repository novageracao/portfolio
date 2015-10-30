package com.nova.geracao.portfolio.entities;

import com.google.gson.annotations.SerializedName;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class User {

	@Id
	@SerializedName("user-id")
	private Long id;
	@SerializedName("user-name")
	private String name;
	@SerializedName("user-email")
	@Index
	private String email;
	@SerializedName("user-password")
	private String password;
	private String confirmCode;
	private Boolean accountConfirmed;

	
	public User(String name, String email, String password, String confirmCode, Boolean accountConfirmed) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.confirmCode = confirmCode;
		this.accountConfirmed = accountConfirmed;
	}

	public User() {
		super();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getConfirmCode() {
		return confirmCode;
	}

	public void setConfirmCode(String confirmCode) {
		this.confirmCode = confirmCode;
	}

	public Boolean getAccountConfirmed() {
		return accountConfirmed;
	}

	public void setAccountConfirmed(Boolean accountConfirmed) {
		this.accountConfirmed = accountConfirmed;
	}

}
