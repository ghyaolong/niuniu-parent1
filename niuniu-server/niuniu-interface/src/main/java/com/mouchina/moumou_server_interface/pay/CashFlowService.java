package com.mouchina.moumou_server_interface.pay;

import java.util.List;
import java.util.Map;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.order.Order;
import com.mouchina.moumou_server.entity.order.OrdersDetail;
import com.mouchina.moumou_server.entity.pay.UserCashFlow;
import com.mouchina.moumou_server_interface.view.PayResult;

/**
 * 老师现金流水
 * @author larry
 *
 */
public interface CashFlowService {
	/**
	 * 查询用户流水
	 * @param userCashFlowId
	 * @return
	 */
	PayResult<UserCashFlow> selectUserCashFlow(Map map);
	/**
	 * 添加用户流水
	 * @param userCashFlow
	 * @return
	 */
	PayResult<UserCashFlow> addUserCashFlow(UserCashFlow userCashFlow);
	/**
	 * 获取用户流水分页信息
	 * @return
	 */
	PayResult<ListRange<UserCashFlow>> selectListUserCashFlowPage(Map map);
	/**
	 * 删除用户流水信息
	 * @param userCashFlow
	 * @return
	 */
	PayResult<Integer> deleteUserCashFlow(String cashFlowNo);
	/**
	 * 更新用户流水
	 * @param userCashFlow
	 * @return
	 */
	PayResult<Integer> updateUserCashFlow(UserCashFlow userCashFlow);
	/**
	 * 添加用户流水入账
	 * @param order
	 * @return
	 */
	PayResult<UserCashFlow> addUserOrderCashFlowIn(Order order, int balance);

	/**
	 * 添加用户流水出账
	 * @param order
	 * @return
	 */
	PayResult<UserCashFlow> addUserOrderCashFlowOut(Order order, int balance);
	
}
