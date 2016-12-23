package com.mouchina.moumou_server_interface.order;

import java.util.List;
import java.util.Map;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.member.UserAssets;
import com.mouchina.moumou_server.entity.order.Order;
import com.mouchina.moumou_server.entity.order.OrdersDetail;
import com.mouchina.moumou_server_interface.member.UserIdentity;
import com.mouchina.moumou_server_interface.view.OrderDetailClocks;
import com.mouchina.moumou_server_interface.view.OrderException;
import com.mouchina.moumou_server_interface.view.OrderResult;
import com.mouchina.moumou_server_interface.view.UserReimburseException;

/**
 * 订单服务
 * 
 * @author larry 2015年6月19日下午3:38:15
 */

public interface OrderService {

	/**
	 * 更新订单
	 * 
	 * @param Order
	 * @return
	 */
	OrderResult<Integer> updateOrderByOrderNo(Order order);

	/**
	 * 根据订单流水号得到订单视图对象
	 * 
	 * @param orderNo
	 * @return
	 */
	OrderResult<OrderView> selectOrderView(String orderNo);

	/**
	 * 根据订单流水号得到订单视图对象 （管理后台专用）
	 * 
	 * @param orderNo
	 * @return
	 */
	OrderResult<OrderView> selectAdminOrderView(String orderNo);

	/**
	 * 根据订单流水号得到订单对象
	 * 
	 * @param orderNo
	 * @return
	 */
	OrderResult<Order> selectOrder(String orderNo);

	/**
	 * 获取用户订单视图分页列表
	 * 
	 * @param map
	 * @return
	 */
	OrderResult<ListRange<OrderView>> listRangeOrdersPageByUser(Map map);

	/**
	 * 获取订单视图分页列表（老师端和管理后台使用）
	 * 
	 * @param map
	 * @return
	 */
	OrderResult<ListRange<OrderView>> listRangeOrdersPage(Map map);

	/**
	 * 获取订单分页列表
	 * 
	 * @param map
	 * @return
	 */
	OrderResult<ListRange<Order>> selectlistRangeOrdersPage(Map map);

	/**
	 * 获取订单列表数量
	 * 
	 * @param map
	 * @return
	 */
	Integer listOrderDetailsByOrderNoCount(Map map);

	/**
	 * 获取订单详情列表
	 * 
	 * @param map
	 * @return
	 */
	OrderResult<List<OrdersDetail>> listOrderDetailsByOrderNo(Map map);

	/**
	 * 删除订单
	 * 
	 * @param orderNo
	 * @return
	 */
	OrderResult<Integer> deleteOrder(String orderNo);

	/***
	 * 用户根据订单号释放锁并关闭订单（app接口）
	 * 
	 * @param orderNo
	 * @return -1 订单不存在 0 用户和订单不匹配 -2订单已经不可取消 －3订单已经自动关闭 －4用户不存在 －5已经取消
	 */
	OrderResult<Integer> updateUserCancelOrderNo(String orderNo,
												 UserIdentity userIdentity) throws OrderException;

	/***
	 * 更新订单order和用户信息userAssets并释放锁
	 * 
	 * @param
	 * @return
	 */
	OrderResult<OrderDetailClocks> updateOrderAndUnlockOrder(String orderNo,
															 String payNo) throws OrderException;

	/**
	 * 添加订单并加订单锁
	 * 
	 * @param OrderView
	 * @return
	 */
	OrderResult<OrderView> addApplyOrderAndLockOrder(OrderView orderView)
			throws OrderException;
	
	/**
	 * 获取订单价格
	 * 
	 * @param OrderView
	 * @return
	 */
	OrderResult<OrderView>  getOrderPrice(OrderView orderView)
			throws OrderException;

	/**
	 * 根据订单明细分析课程发布锁对象
	 * 
	 * @param applyOrdersDetails
	 * @return coursePublicPorcessType 1为下单 2 为解锁 3支付释放资源
	 */
	OrderDetailClocks analysisOrderDetailClocks(
			List<OrdersDetail> applyOrdersDetails, int coursePublicPorcessType);

	/***
	 * 根据订单号释放锁并关闭订单
	 * 
	 * @param orderNo
	 * @return
	 */
	OrderResult<Integer> updateReleaseCouresePublicLockAndCloseOrderByOrderNo(
			String orderNo) throws OrderException;

	/***
	 * 根据订单对象释放锁并关闭订单
	 * 
	 * @param orderView
	 * @return
	 */
	OrderResult<Integer> updateReleaseCouresePublicLockAndCloseOrder(
			OrderView orderView);

	/***
	 * 定时自动释放订单锁并关闭订单
	 * 
	 * @param orderNo
	 * @return
	 */
	OrderResult<Integer> updateTimingAutoBatchReleaseCouresePublicLockAndCloseOrder();


	/***
	 * 获取订单详情对象
	 * 
	 * @param map
	 * @return
	 */
	OrderResult<OrdersDetail> selectOrdersDetail(String orderNo, Long userId,
												 String ordersDetailNo);

	/***
	 * 获取订单详情对象
	 * 
	 * @param map
	 * @return
	 */
	OrderResult<OrdersDetail> selectOrdersDetail(String orderNo,
												 String ordersDetailNo);

	/***
	 * 更新订单明细
	 * 
	 * @param ordersDetail
	 * @return
	 */
	OrderResult<Integer> updateOrdersDetail(OrdersDetail ordersDetail);

	/**
	 * 用户退款并且释放课程锁
	 * 
	 * @param ordersDetails
	 * @return
	 */
	OrderResult<OrderDetailClocks> updateUserReimburseAndReleaseCoursePulicsLocked(
			List<OrdersDetail> ordersDetails) throws UserReimburseException;

	/**
	 * 根据订单对象插入order表,并且返回包装的order订单对象
	 * @param order
	 * @return OrderResult<Order>
	 */
	OrderResult<Order> addOrderWithReturn(Order order);
	/**
	 * 兑换广告币
	 * @param userAssets 用户资产对象
	 * @param configId 充值配置主键id
	 * @param isPayed 是否已付款，没有付款表示余额兑换广告币，否则表示充值兑换广告币
	 * @return true兑换成功 false兑换失败
	 */
	Boolean updateBalanceToCoins(UserAssets userAssets,Long configId , boolean isPayed);
}
