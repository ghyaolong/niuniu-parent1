package com.mouchina.moumou_server_interface.view;

/**
 * 类说明
 *
 * @author larry 2015年7月2日下午9:32:00
 */

public enum UserReimburseState {
	
	/**
	 * 0系统错误
	 * 1成功
	 * 2退款编号不存在
	 * 3订单号不存在
	 * 4订单不存在
	 * 5退款订单被锁
	 * 6退款已经完成
	 * 7订单已经完成
	 * 8订单明细错误
	 * 9退款金额错误
	 */
	// 利用构造函数传参
	SYSTEMERROR(0), SUCCESS(1),REIMBURSENOEXITS(2),ORDERNOEXITS(3),REIMBURSENOLOCKED(5),
	REIMBURSENOFINISHED(6),ORDERNOFINISHED(7),ORDERDETAILERROR(8),REIMBURSENOPRICEERROR(9);

	// 定义私有变量
	private int nCode = 0;

	// 构造函数，枚举类型只能为私有
	private UserReimburseState(int _nCode) {
		this.nCode = _nCode;
	}
      
	@Override
	public String toString() {
		return String.valueOf(this.nCode);
	}
}
