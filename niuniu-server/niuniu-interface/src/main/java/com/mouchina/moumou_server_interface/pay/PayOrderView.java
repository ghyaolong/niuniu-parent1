package com.mouchina.moumou_server_interface.pay;

import com.mouchina.moumou_server.entity.pay.PayOrder;
import com.mouchina.moumou_server_interface.member.UserIdentity;

import java.io.Serializable;

/**
 *类说明
 *@author larry
 * 2015年6月19日下午4:14:40
 */

public class PayOrderView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2723410965862009073L;
	
	private PayOrder payOrder;
	
	private UserIdentity  userIdentity;

	public UserIdentity getUserIdentity() {
		return userIdentity;
	}

	public void setUserIdentity(UserIdentity userIdentity) {
		this.userIdentity = userIdentity;
	}

	public PayOrder getPayOrder() {
		return payOrder;
	}

	public void setPayOrder(PayOrder payOrder) {
		this.payOrder = payOrder;
	}

}
