package com.mouchina.moumou_server_interface.pay;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.pay.PayOrder;
import com.mouchina.moumou_server_interface.view.PayResult;

import java.util.Map;

/**
 *类说明
 *@author larry
 * 2015年6月19日下午4:14:28
 */

public interface PayOrderService {

	/**
	 * 添加支付订单
	 * @param payOrder
	 * @return
	 */
	PayResult<PayOrder> addPayOrder(PayOrder payOrder);
	
	/**
	 *  更新支付订单
	 * @param payOrder
	 * @return
	 */
	PayResult<Integer> updatePayOrder(PayOrder payOrder);
	/**
	 * 删除支付订单
	 * @param payOrdeNo
	 * @return
	 */
	PayResult<Integer> deletePayOrder(String payOrdeNo);
	/**
	 * 获取支付订单
	 * @param payNo
	 * @return
	 */
	PayResult<PayOrder> selectPayOrder(String payNo);
	/**
	 * 更新支付订单并且锁
	 * @param payOrderView
	 * @return 业务处理状态  payOrderState  流程处理状态仅标示流程 state 0 处理异常 1处理正常  
	 */
	PayResult<Integer> updatePayOrderCheckAndUnlock(PayOrderView payOrderView);
	PayResult<Integer> updatePayOrderCheck(PayOrderView payOrderView);
	/**
	 * 获取订单分页列表
	 * @param map
	 * @return
	 */
	PayResult<ListRange<PayOrderView>> selectListRangePayOrderViewPage(Map map);
	/***
	 * 获取订单分页列表
	 * @param map
	 * @return
	 */
	PayResult<ListRange<PayOrder>> selectListRangePayOrderPage(Map map);
	
	/**
	 * 继续支付获区支付订单号
	 * @param payOrder
	 * @return  state 1 获取成功 －1 获取失败，订单处理中。
	 */
	
	PayResult<String> addRePay(PayOrder payOrder);
	
	/***
	 * 根据订单号获取最近订单
	 * @return
	 */
	PayResult<PayOrder> selectLatestPayOrderByOrderNo(String orderNo);
	

	/**
	 * 退款查询
	 * @param orderNo 
	 * @param orderDetailNos 可为空
	 * @return state 1 正常   0系统异常   2订单不存在  3子订单不存在  －1退款期限超时 -2超出最大可退金额  content  退款金额  -2超出可退金额 -3 子订单状态不符合退款条件
	 */
	PayResult<Integer> reimburseQueryPayOrder(String orderNo, String orderDetailNos);
	
	
	/**
	 * 退款请求提交
	 * @param orderNo 
	 * @param orderDetailNos 
	 * @param reimbursePrice 退款金额
	 * @return state 1 提交成功  0系统异常  2订单不存在 3子订单不存在 －1退款期限超时  4退款金额不一致   -3 子订单状态不符合退款条件
	 */
	PayResult<Integer> updateReimburseRequestPayOrder(String orderNo, String orderDetailNos, Integer reimbursePrice);
//	/**
//	 * 发送短信
//	 * @param smsContent
//	 * @param userId
//	 */
//	void sendBaseMessage(String smsContent, Long userId, int targetType);
	public PayOrder selectPayOrderModel(Map map);
}
