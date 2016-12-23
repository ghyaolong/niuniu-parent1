package com.mouchina.moumou_server.entity.vo;

import java.io.Serializable;
import java.math.BigDecimal;

@SuppressWarnings("serial")
public class AgentStatisticsVo implements Serializable {
	private String incomDate;
	private int countSeller;
	private BigDecimal money;
	private BigDecimal myMoney;
	private int countSmallSeller;
	private BigDecimal smallSellerMoney;
	private BigDecimal mySellerMoney;
	private BigDecimal sum;


	public String getIncomDate() {
		return incomDate;
	}

	public void setIncomDate(String incomDate) {
		this.incomDate = incomDate;
	}

	public int getCountSeller() {
		return countSeller;
	}

	public void setCountSeller(int countSeller) {
		this.countSeller = countSeller;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getMyMoney() {
		return myMoney;
	}

	public void setMyMoney(BigDecimal myMoney) {
		this.myMoney = myMoney;
	}

	public int getCountSmallSeller() {
		return countSmallSeller;
	}

	public void setCountSmallSeller(int countSmallSeller) {
		this.countSmallSeller = countSmallSeller;
	}

	public BigDecimal getSmallSellerMoney() {
		return smallSellerMoney;
	}

	public void setSmallSellerMoney(BigDecimal smallSellerMoney) {
		this.smallSellerMoney = smallSellerMoney;
	}

	public BigDecimal getMySellerMoney() {
		return mySellerMoney;
	}

	public void setMySellerMoney(BigDecimal mySellerMoney) {
		this.mySellerMoney = mySellerMoney;
	}

	public BigDecimal getSum() {
		return sum;
	}

	public void setSum(BigDecimal sum) {
		this.sum = sum;
	}

}
