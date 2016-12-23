package com.mouchina.base.utils;

import java.util.List;

/**
 * 广告内容实体类
 * @author Administrator
 *
 */
public class AdvertContentUtil {

	private List<String> photo;
	
	private String content;

	public AdvertContentUtil(List<String> photo, String content) {
		super();
		this.photo = photo;
		this.content = content;
	}

	public List<String> getPhoto() {
		return photo;
	}

	public void setPhoto(List<String> photo) {
		this.photo = photo;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}
