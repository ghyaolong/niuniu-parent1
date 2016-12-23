package com.mouchina.web.controller.enums;

import com.mouchina.base.enums.IEnum;

/**
 * 登陆枚举类
 * @author hpf
 *
 */
public enum EUserLogin implements IEnum{
	EXIST_NOT_LOGINKEY("600000","loginKey为空"),
	EXIST_NOT_PHONE("600001","手机号码为空"),
	EXIST_NOT_PASSWORD("600002","密码为空"),
	EXIST_NOT_PHONE_PASSWORD("600003","手机号码或密码错误"),
	FORMAT_ERRO_LOGINKEY("600010","loginKey格式有误"),
	FORMAT_ERRO_PHONE("600011","手机号码格式有误"),
	FORMAT_ERRO_PASSWORD("600012","密码格式有误"),
	;
	private EUserLogin(String key,String value){
		this.key = key;
		this.value = value;
	}

	private String key,value;
	
	@Override
	public String key() {
		return this.key;
	}

	@Override
	public String value() {
		return this.value;
	}

	@Override
	public String signature() {
		return this.toString();
	}

}
