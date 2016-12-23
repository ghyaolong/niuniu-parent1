package com.mouchina.moumou_server.entity.social;

/**
 * 用户评论、点赞实体帮助类
 * @author Administrator
 *
 */
public enum UserCommentHelper {

	STATE_0(0, "数据无效"),
	STATE_1(1, "数据有效"),
	
	COMMENT_TYPE_1(1,"评论"),
	COMMENT_TYPE_2(2,"点赞"),
	COMMENT_TYPE_3(3,"系统消息");
	
	private Integer marker;
	
	private String display;

	private UserCommentHelper(Integer marker, String display) {
		this.marker = marker;
		this.display = display;
	}

	public Integer getMarker() {
		return marker;
	}

	public void setMarker(Integer marker) {
		this.marker = marker;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}
	
}
