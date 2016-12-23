package com.mouchina.moumou_server_interface.view;

/**
 * 类说明
 *
 * @author larry 2015年7月2日下午9:32:00
 */

public enum OrderState {

	/**
	 * 0 系统错误 1 成功 ***** 2 金额错误 3 老师课程被锁定 4 优惠券不存在 5 优惠券不可用 6课程被锁定 7课程不尊在 8
	 * 订单明细数量错误 9 订单总价和明细总价错误 10订单商品总数错误 11用户信息错误 12 优惠券面额错误 13订单原始价格错误 14发布课程不存在 15订单明细统计错误
	 * 16用户资源被锁 17课程不可用  18优惠券价格错误 19积分价格错误20 折扣金额错误 21 宝宝不存在 22 没有权限访问宝宝 23订单地址用户错误
	 * 24订单释放错误 25课程价格错误 26课程额外可以预约宝宝数量有误  27课程类型错误 28老师课程不存在 29 课程额外宝宝价格错误
	 */
	// 利用构造函数传参
	SYSTEMERROR(0), SUCCESS(1), AMOUNTERROR(2), TEACHERCOURSELOCK(3), CASHCOUPONNOEXIST(
			4), CASHCOUPONAVAILABLE(5), COURSEPUBLCLOCKED(
			6), COURSENOEXITS(7), ORDERGOODSCOUNTERROR(8), ORDERORGIORIGINALERROR(
			9), ORDERGOODSTOTALCOUNTERROR(10), USERIDENTITYERROR(11), CASHCOUPONDENOMINATIONERROR(
			12), ORDERDEALPRICEERROR(13), COURSEPUBLCNOEXITS(14), ORDERDETAILCOUNTPRICEERROR(
			15), USERRESOURCECOURSEPUBLICLOCKED(16), COURSEAVAILABLE(17), CASHCOUPONPRICEEROR(18),
			INTERGRATIONPRICEERROR(19),DISCOUNTPRICEERROR(20),BABYNOEXTIS(21),BABYNOAUTH (22),
			ORDERADRESSERROR(23),ORDERRELESASEERROR(24), COURSEPRICEERROR(25),COURSEEXTRABESKPEKERROR(26),COURSETYPEERROR(27),TEACHERCOURSENOEXITS(28),COURSEEXTRABESKPEKPRICEERROR(29);

	// 定义私有变量
	private int nCode = 0;

	// 构造函数，枚举类型只能为私有
	private OrderState(int _nCode) {
		this.nCode = _nCode;
	}

	@Override
	public String toString() {
		return String.valueOf(this.nCode);
	}
}
