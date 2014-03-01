package org.tassemble.weixin.gongzhong.dto;

/**
 * @author CHQ
 * @date Mar 1, 2014
 * @since 1.0
 */
public class GongArticle {
	String	title;
	String	content;
	String	picUrl;
	
	
	
	public static final String DEFAULT_PICTURE = "";

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}


}
