package com.mouchina.moumou_server_interface.view;


/**
 * 类说明
 *
 * @author larry 2015年7月2日下午9:32:00
 */

public enum OrderProcessState {

	/**
	 * 100-199 下单状态 100 订单成功 108 超时取消 109 用户取消 200-299 订单支付状态 200 支付成功 201支付完成 通知失败 300-399 订单服务状态 300服务成功
	 * 310老师服务签到 320 服务完成 390 服务投诉 400-499 订单交易完成状态 400 交易完成 401 交易完成并评价 900-999
	 * 系统强制管理状态
	 */
	// 利用构造函数传参
	ORDERAPPLYSUCCESS(100), ORDEREXPIRECANCEL(108),ORDERUSERCANCEL(109), ORDERPAYSUCCESS(200),ORDERPAYFINISH(201),ORDERSERVICEFINISH(320),  ORDERDEALSUCCESS(
			400), ORDERSYSTEM(900);

	// 定义私有变量
	private int nCode = 0;

	// 构造函数，枚举类型只能为私有
	private OrderProcessState(int _nCode) {
		this.nCode = _nCode;
	}

	@Override
	public String toString() {
		return String.valueOf(this.nCode);
	}

	public Integer getCode() {
		return this.nCode;
	}
}
