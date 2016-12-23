package com.mouchina.web.service.api.vo;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 用户评论、点赞实体vo
 * @author Administrator
 *
 */
public class UserCommentVo {
	
    private Long commentId; //评论、点赞ID
    
    private Long praisedUserId; //点赞用户ID

    private String praisedNickName; //点赞用户昵称

    private Long commentUserId; //评论者ID

    private String commentNickName; //评论者昵称

    private Long commentedUserId; //被评论者ID

    private String commentedNickName; //被评论者昵称

    private String content; //评论内容
    
    private String userAvatar;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    private Byte commentType;//评论类型 1、评论   2、点赞   3、系统消息

	public Long getCommentId() {
		return commentId;
	}

	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

	public Long getPraisedUserId() {
		return praisedUserId;
	}

	public void setPraisedUserId(Long praisedUserId) {
		this.praisedUserId = praisedUserId;
	}

	public String getPraisedNickName() {
		return praisedNickName;
	}

	public void setPraisedNickName(String praisedNickName) {
		this.praisedNickName = praisedNickName;
	}

	public Long getCommentUserId() {
		return commentUserId;
	}

	public void setCommentUserId(Long commentUserId) {
		this.commentUserId = commentUserId;
	}

	public String getCommentNickName() {
		return commentNickName;
	}

	public void setCommentNickName(String commentNickName) {
		this.commentNickName = commentNickName;
	}

	public Long getCommentedUserId() {
		return commentedUserId;
	}

	public void setCommentedUserId(Long commentedUserId) {
		this.commentedUserId = commentedUserId;
	}

	public String getCommentedNickName() {
		return commentedNickName;
	}

	public void setCommentedNickName(String commentedNickName) {
		this.commentedNickName = commentedNickName;
	}

	public String getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Byte getCommentType() {
		return commentType;
	}

	public void setCommentType(Byte commentType) {
		this.commentType = commentType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}