package com.mouchina.moumou_server.entity.vo;

import java.io.Serializable;

/**
 * 接力福袋vo
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class RelayLuckyBagVo implements Serializable{

	private String nickName; //用户昵称
	
	private Integer state; //状态(0审核 1已审核 2暂停 3已完成 4未通过 5超时完成)
	
	private Integer num; //传递人数
	
	private Integer amount; //福袋累积总金额
	
	private Integer openFlag; //开启标志(0:开启成功  1:开启失败)
	
	private Integer errorCode; //错误代码

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Integer getOpenFlag() {
		return openFlag;
	}

	public void setOpenFlag(Integer openFlag) {
		this.openFlag = openFlag;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}
	
}
