package com.mouchina.moumou_server_interface.view;

import com.mouchina.moumou_server.entity.order.OrdersDetail;

import java.io.Serializable;

/**
 *类说明
 *@author larry
 * 2015年7月7日下午5:04:30
 */

public class OrdersDetailsView implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8095516295083279784L;

	private OrdersDetail ordersDetail;
	
	public OrdersDetail getOrdersDetail() {
		return ordersDetail;
	}

	public void setOrdersDetail(OrdersDetail ordersDetail) {
		this.ordersDetail = ordersDetail;
	}
}
