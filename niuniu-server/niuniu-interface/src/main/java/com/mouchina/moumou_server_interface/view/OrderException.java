package com.mouchina.moumou_server_interface.view;


/**
 *类说明
 *@author larry
 * 2015年7月3日上午10:10:32
 */
public class OrderException  extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3713931282835294866L;
	
	private OrderState  orderState;
	private String orderSateContent;
	
	public OrderException()
	{
		super();
	}

	public OrderException(OrderState orderState)
	{
		this.orderState = orderState;
	}
	public OrderException(OrderState orderState, String orderSateContent)
	{
		super();
		this.orderState=orderState;
		this.orderSateContent=orderSateContent;
	}
	public OrderException(OrderState orderState, String message, Throwable cause)
	{
		super(message, cause);
		this.orderState = orderState;
	}

	public OrderState getOrderState()
	{
		return orderState;
	}

	public void setOrderState(OrderState orderState)
	{
		this.orderState = orderState;
	}
	
	public String getOrderSateContent() {
		return orderSateContent;
	}
	public void setOrderSateContent(String orderSateContent) {
		this.orderSateContent = orderSateContent;
	}
	
	public String toExecptionString(){
		
		return this.orderState+","+this.orderSateContent+"";
	}

}
