package com.mouchina.moumou_server.entity.vo;

import java.io.Serializable;

/**
 * 领取首页广告福袋vo
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class HomePageAwardVo implements Serializable {

	private Integer amount; //领取福袋金额
	
	private Integer errorCode; //错误代码


	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	
}
