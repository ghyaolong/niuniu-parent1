package com.mouchina.moumou_server_interface.view;

/**
 * 类说明
 *
 * @author larry 2015年6月19日下午5:55:53
 */

public class PayResult<T> extends Result<T> {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6252073184901168767L;
	private PayOrderState payOrderState;
	private String payOrderSateContent;
	
	public PayOrderState getPayOrderState() {
		return payOrderState;
	}
	public void setPayOrderState(PayOrderState payOrderState) {
		this.payOrderState = payOrderState;
	}
	public String getPayOrderSateContent() {
		return payOrderSateContent;
	}
	public void setPayOrderSateContent(String payOrderSateContent) {
		this.payOrderSateContent = payOrderSateContent;
	}
}
