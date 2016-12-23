package com.mouchina.moumou_server_interface.view;

import com.mouchina.moumou_server.entity.member.UserAgent;
import com.mouchina.moumou_server_interface.member.UserInfo;

/**
 *类说明
 *@author larry
 * 2015年6月17日下午6:05:04
 */

public class UserResult<T> extends Result<T> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8742570512212380842L;
	
	
	private UserInfo userInfo;
	
	private UserAgent userAgent;
	
	private UserAgent parentUserAgent;
	
	


	public UserInfo getUserInfo() {
		return userInfo;
	}


	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}


	public UserAgent getUserAgent() {
		return userAgent;
	}


	public void setUserAgent(UserAgent userAgent) {
		this.userAgent = userAgent;
	}


	public UserAgent getParentUserAgent() {
		return parentUserAgent;
	}


	public void setParentUserAgent(UserAgent parentUserAgent) {
		this.parentUserAgent = parentUserAgent;
	}

}
