package com.mouchina.moumou_server_interface.view;

/**
 * 类说明
 *
 * @author larry 2015年6月19日下午5:55:02
 */

public class OrderResult<T> extends Result<T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3523091544030572734L;
	private  OrderState  orderState;
	private String orderSateContent;
	public String getOrderSateContent() {
		return orderSateContent;
	}
	public void setOrderSateContent(String orderSateContent) {
		this.orderSateContent = orderSateContent;
	}
	public OrderState getOrderState() {
		return orderState;
	}
	public void setOrderState(OrderState orderState) {
		this.orderState = orderState;
	}
	
	
}
