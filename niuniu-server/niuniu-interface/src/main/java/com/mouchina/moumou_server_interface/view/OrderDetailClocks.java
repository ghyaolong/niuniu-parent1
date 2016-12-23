package com.mouchina.moumou_server_interface.view;

import com.mouchina.moumou_server.entity.order.OrdersDetail;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *订单锁对象
 *@author larry
 * 2015年7月4日下午5:13:49
 */

public class OrderDetailClocks {

	/**
	 * 课程发布编号和老师
	 */
	private String coursePublicNumAndTeachers;
	/**
	 * 错误课程发布id
	 */
	private String errorIds;
	/**
	 * 正常课程发布id
	 */
	private List<Long> normalIds;
	/**
	 * 需要加锁集合
	 */
	private Set<Long> lockIdsSet;
	/**
	 * 课程发布期
	 */
	@Deprecated
	private String coursePublicNums;
	@Deprecated
	private String teacherIds;
	
	
	Map<Integer, List<OrdersDetail>> teacherOrdersDetailDealMap;
	
	

	
	

	public String getCoursePublicNumAndTeachers() {
		return coursePublicNumAndTeachers;
	}
	public void setCoursePublicNumAndTeachers(String coursePublicNumAndTeachers) {
		this.coursePublicNumAndTeachers = coursePublicNumAndTeachers;
	}
	public Map<Integer, List<OrdersDetail>> getTeacherOrdersDetailDealMap() {
		return teacherOrdersDetailDealMap;
	}
	public void setTeacherOrdersDetailDealMap(
			Map<Integer, List<OrdersDetail>> teacherOrdersDetailDealMap) {
		this.teacherOrdersDetailDealMap = teacherOrdersDetailDealMap;
	}
	public String getTeacherIds() {
		return teacherIds;
	}
	public void setTeacherIds(String teacherIds) {
		this.teacherIds = teacherIds;
	}
	public String getErrorIds() {
		return errorIds;
	}
	public void setErrorIds(String errorIds) {
		this.errorIds = errorIds;
	}
	
	public List<Long> getNormalIds() {
		return normalIds;
	}
	public void setNormalIds(List<Long> normalIds) {
		this.normalIds = normalIds;
	}
	public Set<Long> getLockIdsSet() {
		return lockIdsSet;
	}
	public void setLockIdsSet(Set<Long> lockIdsSet) {
		this.lockIdsSet = lockIdsSet;
	}
	public String getCoursePublicNums() {
		return coursePublicNums;
	}
	public void setCoursePublicNums(String coursePublicNums) {
		this.coursePublicNums = coursePublicNums;
	}
}
