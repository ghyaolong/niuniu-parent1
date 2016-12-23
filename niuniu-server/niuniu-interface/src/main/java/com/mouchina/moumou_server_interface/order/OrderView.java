package com.mouchina.moumou_server_interface.order;

import com.mouchina.moumou_server.entity.order.Order;
import com.mouchina.moumou_server.entity.order.OrdersDetail;
import com.mouchina.moumou_server.entity.pay.PayOrder;
import com.mouchina.moumou_server_interface.member.UserIdentity;

import java.io.Serializable;
import java.util.List;

/**
 * 订单view
 *
 * @author larry 2015年6月19日下午3:58:35
 */

public class OrderView implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8134368138154661708L;
	
	/**
	 * 订单对象
	 */
	private Order order;
	private UserIdentity userIdentity;
	private PayOrder payOrder;
	
	/**
	 * 订单明细
	 */
	private List<OrdersDetail> ordersDetails; 
	
	
	
	
	
	public PayOrder getPayOrder() {
		return payOrder;
	}

	public void setPayOrder(PayOrder payOrder) {
		this.payOrder = payOrder;
	}

	
	public UserIdentity getUserIdentity() {
		return userIdentity;
	}

	public void setUserIdentity(UserIdentity userIdentity) {
		this.userIdentity = userIdentity;
	}





	public List<OrdersDetail> getOrdersDetails() {
		return ordersDetails;
	}

	public void setOrdersDetails(List<OrdersDetail> ordersDetails) {
		this.ordersDetails = ordersDetails;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

}
