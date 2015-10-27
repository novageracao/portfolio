package com.nova.geracao.portfolio.entities;

import java.util.HashSet;

import com.google.appengine.api.datastore.Text;
import com.google.gson.annotations.SerializedName;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class BlogPost implements BaseDataClass{

	@Id
	@SerializedName("blog_post_id")
	private Long id;
	@SerializedName("blog_post_body")
	private Text body;
	@SerializedName("blog_post_title")
	private String title;
	@SerializedName("blog_post_year")
	private Long year;
	@SerializedName("blog_post_month")
	private Long month;
	@SerializedName("blog_post_day")
	private Long day;
	@SerializedName("blog_post_source")
	private String source;
	@SerializedName("blog_post_tags")
	private HashSet<BlogTag> tags;
	@SerializedName("blog_post_author")
	private BlogPostAuthor author;
	@SerializedName("blog_post_fb")
	private String fbLink;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public HashSet<BlogTag> getTags() {
		return tags;
	}
	public void setTags(HashSet<BlogTag> tags) {
		this.tags = tags;
	}
	public BlogPostAuthor getAuthor() {
		return author;
	}
	public void setAuthor(BlogPostAuthor author) {
		this.author = author;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getFbLink() {
		return fbLink;
	}
	public void setFbLink(String fbLink) {
		this.fbLink = fbLink;
	}
	public Text getBody() {
		return body;
	}
	public void setBody(Text body) {
		this.body = body;
	}
	public Long getYear() {
		return year;
	}
	public void setYear(Long year) {
		this.year = year;
	}
	public Long getMonth() {
		return month;
	}
	public void setMonth(Long month) {
		this.month = month;
	}
	public Long getDay() {
		return day;
	}
	public void setDay(Long day) {
		this.day = day;
	}
	
	
}
