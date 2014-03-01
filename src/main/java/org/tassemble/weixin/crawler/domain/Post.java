package org.tassemble.weixin.crawler.domain;

import com.netease.framework.dao.sql.annotation.DataProperty;
import java.math.BigDecimal;

public class Post {

private static final long serialVersionUID = 1L;


	public static final String POST_TYPE_SMALL_FUNNY = "small_funny";
	public static final String POST_TYPE_LARGE_FUNNY = "large_funny";
	private Long id;
	private String content;
	private String postType;
	private Long gmtCreate;
	private Long gmtModified;
	private String title;
	private String picUrl;
	private Long linkId;
	
	
	
	
	
	

	@DataProperty(column="title")
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@DataProperty(column = "id")	
	public Long getId() {
		return id;
	}	

	
	public void setContent(String content) {
		this.content = content;
	}
	
	@DataProperty(column = "content")	
	public String getContent() {
		return content;
	}	

	
	public void setPostType(String postType) {
		this.postType = postType;
	}
	
	@DataProperty(column = "post_type")	
	public String getPostType() {
		return postType;
	}	

	
	public void setGmtCreate(Long gmtCreate) {
		this.gmtCreate = gmtCreate;
	}
	
	@DataProperty(column = "gmt_create")	
	public Long getGmtCreate() {
		return gmtCreate;
	}	

	
	public void setGmtModified(Long gmtModified) {
		this.gmtModified = gmtModified;
	}
	
	@DataProperty(column = "gmt_modified")	
	public Long getGmtModified() {
		return gmtModified;
	}

	
	@DataProperty(column="pic_url")
	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	
	@DataProperty(column="link_id")
	public Long getLinkId() {
		return linkId;
	}

	public void setLinkId(Long linkId) {
		this.linkId = linkId;
	}	



}
