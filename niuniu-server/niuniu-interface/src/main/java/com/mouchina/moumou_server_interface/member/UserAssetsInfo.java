package com.mouchina.moumou_server_interface.member;

import com.mouchina.moumou_server.entity.member.UserAssets;

/**
 *用户信息类
 *@author larry
 * 2015年6月15日下午3:21:20
 */

public class UserAssetsInfo  extends UserInfo{


	/**
	 * 
	 */
	private static final long serialVersionUID = -8664735282511758074L;
	private UserAssets userAssets;
	public UserAssets getUserAssets() {
		return userAssets;
	}
	public void setUserAssets(UserAssets userAssets) {
		this.userAssets = userAssets;
	}
	

}
