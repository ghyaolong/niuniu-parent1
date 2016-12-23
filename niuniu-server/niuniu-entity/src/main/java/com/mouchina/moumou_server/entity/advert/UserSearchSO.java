package com.mouchina.moumou_server.entity.advert;

import com.mouchina.base.entity.SearchEntity;

public class UserSearchSO extends SearchEntity{
	private static final long serialVersionUID = 9134532592697879713L;
	/**
	 * 用户Id
	 */
	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
