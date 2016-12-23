package com.mouchina.moumou_server_interface.order;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server_interface.view.OrderResult;
import com.mouchina.moumou_server_interface.view.OrderShowView;
import com.mouchina.moumou_server_interface.view.OrdersDetailsView;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *订单查询服务
 *@author larry
 * 2015年6月19日下午3:38:15
 */

public interface OrderQueryService {
	
	/**
	 * 获取用户订单视图分页列表
	 * @param map
	 * @return
	 */
	OrderResult<ListRange<OrderShowView>> listRangeOrderShowViewPageByUser(Map map);
	
	/**
	 * 获取订单视图分页列表（老师端和管理后台使用）
	 * @param map
	 * @return
	 */
	OrderResult<ListRange<OrderShowView>> listRangeOrderShowViewPage(Map map);
	/**
	 * 获取订单详情列表
	 * @param map
	 * @return
	 */
	OrderResult<List<OrdersDetailsView>> listOrderDetailViewByOrderNo(Map map);
	
	

	/**
	 * 根据宝宝和课程获取宝宝将要上第几课
	 * @return
	 */
	OrderResult<Integer> selectCourseByBabyIdAndCourseIdNext(long userId, long babyId, int courseId);
	/**
	 * 根据宝宝和课程获取宝宝将要上第几课
	 * @return
	 */
	OrderResult<Integer> selectCourseByBabyIdAndCourseIdNowNum(long userId, long babyId, int courseId, Date time);
	
}
