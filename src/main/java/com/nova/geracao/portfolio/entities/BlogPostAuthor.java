package com.nova.geracao.portfolio.entities;

import com.google.gson.annotations.SerializedName;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class BlogPostAuthor implements BaseDataClass{

	@Id
	@SerializedName("author_id")
	private Long id;
	@SerializedName("author_name")
	private String name;
	@SerializedName("author_bio")
	private String bio;
	@SerializedName("author_link")
	private String link;
	
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
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	
}
