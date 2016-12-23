package com.mouchina.moumou_server_interface.view;

/**
 * 类说明
 *
 * @author larry 2015年7月2日下午9:32:00
 */

public enum PayOrderState {

	/**
	 * 0系统错误
	 * 1成功
	 * 2支付订单不存在
	 * 3订单号不存在
	 * 4支付订单不存在
	 * 5支付订单被锁
	 * 6支付已经完成
	 * 7订单已经完成
	 * 8优惠券无效
	 * 9优惠券不存在
	 * 10支付完成
	 * 11支付金额错误
	 */
	// 利用构造函数传参
	SYSTEMERROR(0), SUCCESS(1),PAYORDERNOEXITS(2),ORDERNOEXITS(3),PAYNOLOCKED(5),
	PAYNOFINISHED(6),ORDERNOFINISHED(7),CASHCOUPONINVALID(8),CASHCOUPONNOEXITS(9),PAYFINISH(10),PAYPRICEERROR(11);

	// 定义私有变量
	private int nCode = 0;

	// 构造函数，枚举类型只能为私有
	private PayOrderState(int _nCode) {
		this.nCode = _nCode;
	}

	@Override
	public String toString() {
		return String.valueOf(this.nCode);
	}
}
