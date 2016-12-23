package com.mouchina.moumou_server_interface.view;


/**
 * 类说明
 *
 * @author larry 2015年7月2日下午9:32:00
 */

public enum CoursePublicState {

	/**
	 * 
	 * 状态 1(NORMAL)正常 2(ASSOCIATELOCK)关联锁定 3(SERVICECLOSE)业务自动关闭 4(CONSUME)消费 5(USERLOCK)用户关闭 6(COURSELOCK)课程锁定
	 */
	// 利用构造函数传参
	 NORMAL(1), ASSOCIATELOCK(2),SERVICECLOSE(3), CONSUME(4),USERLOCK(5),COURSELOCK(6);

	// 定义私有变量
	private int nCode = 0;

	// 构造函数，枚举类型只能为私有
	private CoursePublicState(int _nCode) {
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
