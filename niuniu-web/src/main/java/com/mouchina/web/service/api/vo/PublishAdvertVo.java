package com.mouchina.web.service.api.vo;

import java.io.Serializable;

public class PublishAdvertVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9002759799608105622L;

	private AdvertVo advertVo;

	private String loginKey;

	private String result;

	private Integer errorCode;

	public AdvertVo getAdvertVo() {
		return advertVo;
	}

	public void setAdvertVo(AdvertVo advertVo) {
		this.advertVo = advertVo;
	}

	public String getLoginKey() {
		return loginKey;
	}

	public void setLoginKey(String loginKey) {
		this.loginKey = loginKey;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

}
