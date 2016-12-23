package com.mouchina.moumou_server_interface.view;


/**
 *类说明
 *@author larry
 * 2015年7月3日上午10:10:32
 */
public class PayOrderException  extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3713931282835294866L;
	
	private PayOrderState  payOrderState;
	private String  payOrderSateContent;
	
	public PayOrderException()
	{
		super();
	}

	public PayOrderException(PayOrderState payOrderState)
	{
		this.payOrderState = payOrderState;
	}
	public PayOrderException(PayOrderState payOrderState, String payOrderSateContent)
	{
		super();
		this.payOrderState=payOrderState;
		this.payOrderSateContent=payOrderSateContent;
	}
	public PayOrderException(PayOrderState payOrderState, String message, Throwable cause)
	{
		super(message, cause);
		this.payOrderState = payOrderState;
	}
	public String toExecptionString(){
		
		return this.payOrderState+","+this.payOrderSateContent+"";
	}

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
