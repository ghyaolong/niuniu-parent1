package com.mouchina.moumou_server_dubbo.provider.pay;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.redis.RedisHelper;
import com.mouchina.base.utils.DateUtils;
import com.mouchina.base.utils.UUIDGenerator;
import com.mouchina.moumou_server.dao.pay.UserCashFlowMapper;
import com.mouchina.moumou_server.entity.order.Order;
import com.mouchina.moumou_server.entity.pay.UserCashFlow;
import com.mouchina.moumou_server_dubbo.provider.order.OrderServiceSupport;
import com.mouchina.moumou_server_interface.pay.CashFlowService;
import com.mouchina.moumou_server_interface.view.PayResult;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CashFlowServiceSupport implements CashFlowService {

	@Resource
	private UserCashFlowMapper userCashFlowMapper;
	@Resource
	private RedisHelper redisHelper;


	@Override
	public PayResult<UserCashFlow> selectUserCashFlow(Map map) {
		// TODO Auto-generated method stub
		PayResult<UserCashFlow> payResult = new PayResult<UserCashFlow>();

		String tableNum = OrderServiceSupport.getOrderNoTableNum(map.get(
				"triggerNo").toString());
		map.put("tableNum", tableNum);
		UserCashFlow userCashFlow = userCashFlowMapper.selectByCashFlowNo(map);
		if (userCashFlow != null) {
			payResult.setContent(userCashFlow);
			payResult.setState(1);
		}
		return payResult;
	}

	@Override
	public PayResult<UserCashFlow> addUserCashFlow(UserCashFlow userCashFlow) {
		String cashFlowNo = UUIDGenerator.userCashFlowNoGenerator(redisHelper);
		userCashFlow.setCashFlowNo(cashFlowNo);
		String tableNum = OrderServiceSupport.getOrderNoTableNum(userCashFlow
				.getTriggerNo());
		userCashFlow.setTableNum(tableNum);
		PayResult<UserCashFlow> payResult = new PayResult<UserCashFlow>();

		payResult.setState(userCashFlowMapper.insertSelective(userCashFlow));
		payResult.setContent(userCashFlow);
		return payResult;
	}

	@Override
	public PayResult<ListRange<UserCashFlow>> selectListUserCashFlowPage(Map map) {

		String tableNum =map.containsKey("year")? map.get("year").toString():DateUtils.getDateStringYYYY(new Date());
		map.put("tableNum", tableNum);
		PayResult<ListRange<UserCashFlow>> payResult = new PayResult<ListRange<UserCashFlow>>();
		ListRange<UserCashFlow> listRange = new ListRange<UserCashFlow>();
		int count = userCashFlowMapper.selectCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		listRange.setData(userCashFlowMapper.selectPageList(map));
		payResult.setContent(listRange);
		payResult.setState(1);
		return payResult;
	}

	@Override
	public PayResult<Integer> deleteUserCashFlow(String cashFlowNo) {
		String tableNum = getUserCashFlowNoTableNum(cashFlowNo);
		PayResult<Integer> payResult = new PayResult<Integer>();
		Map map = new HashMap();
		map.put("tableNum", tableNum);
		map.put("cashFlowNo", cashFlowNo);
		payResult.setState(userCashFlowMapper.deleteByCashFlowNo(map));
		return payResult;
	}

	@Override
	public PayResult<Integer> updateUserCashFlow(UserCashFlow userCashFlow) {
		String tableNum = getUserCashFlowNoTableNum(userCashFlow
				.getCashFlowNo());
		userCashFlow.setTableNum(tableNum);
		PayResult<Integer> payResult = new PayResult<Integer>();
		payResult.setState(userCashFlowMapper
				.updateByPrimaryKeySelective(userCashFlow));
		return payResult;
	}

	public static String getTeacherCashFlowNoTableNum(String cashFlowNo) {
		String tableNum = cashFlowNo.substring(0, 4);
		return tableNum;
	}

	public static String getUserCashFlowNoTableNum(String cashFlowNo) {
		String tableNum = cashFlowNo.substring(0, 4);
		return tableNum;
	}

	@Override
	public PayResult<UserCashFlow> addUserOrderCashFlowIn(Order order,
			int balance) {

		return addBaseOrderUserCashFlow(order, (byte) 1, balance);

	}

	@Override
	public PayResult<UserCashFlow> addUserOrderCashFlowOut(Order order,
			int balance) {
		return addBaseOrderUserCashFlow(order, (byte) 2, balance);
	}

	/**
	 * 添加用户流水
	 * 
	 * @param order
	 */
	private PayResult<UserCashFlow> addBaseOrderUserCashFlow(Order order,
			byte cashFlowType, int balance) {
		UserCashFlow userCashFlow = new UserCashFlow();
		userCashFlow.setPrice(order.getDealPrice());
		userCashFlow.setTriggerNo(order.getOrderNo());
		userCashFlow.setUserId(order.getUserId());
		userCashFlow.setCashFlowTitle(order.getTitle());
		userCashFlow.setCashFlowType(cashFlowType);
		if (userCashFlow.getCashFlowType().intValue() == 1)
			userCashFlow.setBalance(balance + userCashFlow.getPrice());
		if (userCashFlow.getCashFlowType().intValue() == 2)
			userCashFlow.setBalance(balance - userCashFlow.getPrice());
		return addUserCashFlow(userCashFlow);
	}


}
