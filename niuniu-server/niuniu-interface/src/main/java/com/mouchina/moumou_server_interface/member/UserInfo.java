package com.mouchina.moumou_server_interface.member;

import com.mouchina.moumou_server.entity.member.User;

import java.io.Serializable;

/**
 *用户信息类
 *@author larry
 * 2015年6月15日下午3:21:20
 */

public class UserInfo  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8801442655601429294L;

	User user;
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}


}
