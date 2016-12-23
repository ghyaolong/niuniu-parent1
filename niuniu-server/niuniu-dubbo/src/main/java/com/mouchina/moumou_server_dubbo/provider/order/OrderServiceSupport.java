package com.mouchina.moumou_server_dubbo.provider.order;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.redis.RedisHelper;
import com.mouchina.base.redis.RedisLockHandler;
import com.mouchina.base.utils.*;
import com.mouchina.moumou_server.dao.order.OrderMapper;
import com.mouchina.moumou_server.dao.order.OrdersDetailMapper;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserAgent;
import com.mouchina.moumou_server.entity.member.UserAssets;
import com.mouchina.moumou_server.entity.order.Order;
import com.mouchina.moumou_server.entity.order.OrdersDetail;
import com.mouchina.moumou_server.entity.order.RechargeConfig;
import com.mouchina.moumou_server.entity.pay.PayOrder;
import com.mouchina.moumou_server_dubbo.provider.base.Env;
import com.mouchina.moumou_server_interface.member.UserAgentService;
import com.mouchina.moumou_server_interface.member.UserIdentity;
import com.mouchina.moumou_server_interface.member.UserVerifyService;
import com.mouchina.moumou_server_interface.order.OrderService;
import com.mouchina.moumou_server_interface.order.OrderView;
import com.mouchina.moumou_server_interface.order.RechargeConfigService;
import com.mouchina.moumou_server_interface.pay.CashFlowService;
import com.mouchina.moumou_server_interface.pay.PayOrderService;
import com.mouchina.moumou_server_interface.view.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.annotation.Resource;
import java.util.*;

/**
 * 类说明
 * 
 * @author larry 2015年6月19日下午9:11:00
 */

public class OrderServiceSupport implements OrderService {

	@Resource
	private OrderMapper orderMapper;
	@Resource
	private OrdersDetailMapper ordersDetailMapper;

	static Logger logger = LogManager.getLogger();
	@Resource
	private RedisHelper redisHelper;
	@Resource
	private RedisLockHandler redisLockHandler;

	@Resource
	private PayOrderService payOrderService;
	@Resource
	private UserAgentService userAgentService;
	@Resource
	private UserVerifyService userVerifyService;
	@Resource
	private RechargeConfigService rechargeConfigService;
	@Resource
	private CashFlowService cashFlowService;

	
	@Resource
	private Env env;
	public static final String USER_RESOURCE_COURSE_PUBLIC_LOCK_PREFIX = "user_resource_cousre_public_lock_prefix_";
	public static final String ORDER_NO_LOCK_PREFIX = "order_no_lock_prefix_";

	@Override
	public OrderResult<Integer> updateOrderByOrderNo(Order order) {
		OrderResult<Integer> orderResult = new OrderResult<Integer>();
		order.setModifyTime(new Date());
		String tableNum = getOrderNoTableNum(order.getOrderNo());
		order.setTableNum(tableNum);
		orderMapper.updateByPrimaryKeySelective(order);
		return orderResult;
	}

	@Override
	public OrderResult<OrderView> selectOrderView(String orderNo) {
		OrderResult<OrderView> orderResult = new OrderResult<OrderView>();

		OrderResult<Order> orderModelResult = selectOrder(orderNo);
		if (orderModelResult.getState() == 1) {
			OrderView orderView = new OrderView();
			orderView.setOrder(orderModelResult.getContent());
			orderResult.setState(1);
			String orderTableNum = getOrderNoTableNum(orderNo);
			String tableNum = getOrderDetailTableNum(orderModelResult
					.getContent().getUserId());
			Map map = new HashMap();
			map.put("orderTableNum", orderTableNum);
			map.put("tableNum", tableNum);
			map.put("orderNo", orderNo);
			OrderResult<List<OrdersDetail>> ordersDetailResult = listOrderDetailsByOrderNo(map);
			if (ordersDetailResult.getState() == 1) {
				orderView.setOrdersDetails(ordersDetailResult.getContent());
			}
			orderResult.setContent(orderView);
		}
		return orderResult;
	}

	@Override
	public OrderResult<OrderView> selectAdminOrderView(String orderNo) {
		OrderResult<OrderView> orderResult = selectOrderView(orderNo);

		if (orderResult.getState() == 1) {
			OrderView orderView = orderResult.getContent();
			// OrderResult<OrderAddress> orderAddressResult =
			// selectOrderAddress(
			// orderNo, orderView.getOrder().getOrderAddressId());
			// if (orderAddressResult.getState() == 1) {
			// orderView.setOrderAddress(orderAddressResult.getContent());
			// }
			UserResult<User> userResult = userVerifyService
					.selectUserByUserId(orderView.getOrder().getUserId());
			if (userResult.getState() == 1) {
				UserIdentity userIdentity = new UserIdentity();
				userIdentity.setPhone(userResult.getContent().getPhone());
				userIdentity.setUserId(userResult.getContent().getId());
				orderView.setUserIdentity(userIdentity);
			}
			if (StringUtil.isNotEmpty(orderView.getOrder().getPayNo())) {
				PayResult<PayOrder> payResult = payOrderService
						.selectPayOrder(orderView.getOrder().getPayNo());
				if (payResult.getState() == 1) {
					orderView.setPayOrder(payResult.getContent());
				}
			}
			orderResult.setContent(orderView);
		}
		return orderResult;
	}

	@Override
	public OrderResult<Order> selectOrder(String orderNo) {
		// TODO Auto-generated method stub
		OrderResult<Order> orderResult = new OrderResult<Order>();
		Map map = new HashMap();
		String tableNum = getOrderNoTableNum(orderNo);
		map.put("tableNum", tableNum);
		map.put("orderNo", orderNo);
		Order order = orderMapper.selectModel(map);
		if (order != null) {
			orderResult.setState(1);
			orderResult.setContent(order);

		}
		return orderResult;
	}

	@Override
	public OrderResult<ListRange<OrderView>> listRangeOrdersPageByUser(Map map) {
		return listRangeOrdersPage(map);
	}

	@Override
	public OrderResult<ListRange<OrderView>> listRangeOrdersPage(Map map) {
		// TODO Auto-generated method stub
		OrderResult<ListRange<OrderView>> orderResult = new OrderResult<ListRange<OrderView>>();
		ListRange<OrderView> listRange = new ListRange<OrderView>();

		String year = map.get("year") == null ? null : map.get("year")
				.toString();

		String tableNum = year == null ? DateUtils
				.getDateStringYYYY(new Date()) : year; // 默认从当前时间取年份。
		map.put("tableNum", tableNum);
		int count = orderMapper.selectCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		List<Order> orders = orderMapper.selectPageList(map);
		List<OrderView> orderViews = new ArrayList<OrderView>();
		if (orders.size() > 0) {
			for (Order order : orders) {
				OrderView orderView = new OrderView();
				orderView.setOrder(order);
				orderViews.add(orderView);
			}

			listRange.setData(orderViews);
			orderResult.setContent(listRange);
			orderResult.setState(1);

		}
		return orderResult;

	}

	private Map<String, List<OrdersDetail>> arrangeOrdersDetailsToMap(
			List<OrdersDetail> ordersDetails) {
		Map<String, List<OrdersDetail>> ordersDetailsMap = new HashMap<String, List<OrdersDetail>>();

		for (OrdersDetail ordersDetail : ordersDetails) {
			if (!ordersDetailsMap.containsKey(ordersDetail.getOrderNo())) {
				List<OrdersDetail> newOrderDetailList = new ArrayList<OrdersDetail>();
				newOrderDetailList.add(ordersDetail);
				ordersDetailsMap.put(ordersDetail.getOrderNo(),
						newOrderDetailList);
			} else {

				List<OrdersDetail> orderDetailList = ordersDetailsMap
						.get(ordersDetail.getOrderNo());
				orderDetailList.add(ordersDetail);
				ordersDetailsMap
						.put(ordersDetail.getOrderNo(), orderDetailList);
			}
		}

		return ordersDetailsMap;

	}

	private List<OrderView> arrangeOrderAndOrderViewToOrderViews(
			List<Order> orders, List<OrdersDetail> ordersDetails) {

		List<OrderView> orderViews = new ArrayList<OrderView>();
		Map<String, List<OrdersDetail>> ordersDetailsMap = arrangeOrdersDetailsToMap(ordersDetails);

		for (Order order : orders) {

			OrderView orderView = new OrderView();
			orderView.setOrder(order);
			if (ordersDetailsMap.containsKey(order.getOrderNo())) {
				orderView.setOrdersDetails(ordersDetailsMap.get(order
						.getOrderNo()));
				Map map = new HashMap();
				String tableNum = getOrderNoTableNum(order.getOrderNo());
				map.put("tableNum", tableNum);
				map.put("id", order.getOrderAddressId());
				// OrderAddress orderAddress =
				// orderAddressMapper.selectModel(map);
				// orderView.setOrderAddress(orderAddress);
				orderViews.add(orderView);

			}
		}
		return orderViews;
	}

	@Override
	public OrderResult<ListRange<Order>> selectlistRangeOrdersPage(Map map) {
		if (!map.containsKey("tableNum")) {
			String tableNum = DateUtils.getDateStringYYYY(new Date());
			map.put("tableNum", tableNum);
		}
		OrderResult<ListRange<Order>> orderResult = new OrderResult<ListRange<Order>>();
		ListRange<Order> listRange = new ListRange<Order>();
		int count = orderMapper.selectCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		listRange.setData(orderMapper.selectPageList(map));
		orderResult.setContent(listRange);
		return orderResult;
	}

	@Override
	public Integer listOrderDetailsByOrderNoCount(Map map) {
		return ordersDetailMapper.selectCount(map);
	}

	@Override
	public OrderResult<List<OrdersDetail>> listOrderDetailsByOrderNo(Map map) {
		OrderResult<List<OrdersDetail>> orderResult = new OrderResult<List<OrdersDetail>>();
		List<OrdersDetail> ordersDetails = ordersDetailMapper.selectList(map);
		orderResult.setContent(ordersDetails);
		orderResult.setState(1);
		return orderResult;
	}

	@Override
	public OrderResult<Integer> deleteOrder(String orderNo) {
		OrderResult<Integer> orderResult = new OrderResult<Integer>();
		String orderTableNum = getOrderNoTableNum(orderNo);

		OrderResult<Order> orderRes = selectOrder(orderNo);
		if (orderRes.getState() == 1) {
			String tableNum = getOrderDetailTableNum(orderRes.getContent()
					.getUserId());
			Map orderDetailMap = new HashMap();
			orderDetailMap.put("tableNum", tableNum);
			orderDetailMap.put("orderTableNum", orderTableNum);
			ordersDetailMapper.deleteOrderDetailsByOrderNo(orderDetailMap);
			Map orderMap = new HashMap();
			orderMap.put("tableNum", orderTableNum);
			orderMapper.deleteByOrderNo(orderMap);

			orderResult.setState(1);
		}
		return orderResult;
	}

	@Override
	public OrderResult<Integer> updateUserCancelOrderNo(String orderNo,
			UserIdentity userIdentity) throws OrderException {
		OrderResult<Integer> orderResult = new OrderResult<Integer>();
		OrderResult<OrderView> orderViewResult = selectOrderView(orderNo);
		if (orderViewResult.getState() != 1) {
			orderResult.setState(-1);
			return orderResult;
		}
		if (orderViewResult.getContent().getOrder().getOrderState() == 108) {
			orderResult.setState(-3);
			return orderResult;
		}
		User user = userVerifyService.selectUser(userIdentity);
		if (user == null) {
			orderResult.setState(-4);
			return orderResult;
		}
		if (orderViewResult.getContent().getOrder().getUserId().longValue() != user
				.getId().longValue()) {
			orderResult.setState(0);
			return orderResult;
		}
		if (orderViewResult.getContent().getOrder().getOrderState() >= 200) {
			orderResult.setState(-2);
			return orderResult;
		}
		if (orderViewResult.getContent().getOrder().getOrderState() == 109) {
			orderResult.setState(-5);
			return orderResult;
		}
		Order order = orderViewResult.getContent().getOrder();
		OrderDetailClocks orderDetailClocks = analysisOrderDetailClocks(
				orderViewResult.getContent().getOrdersDetails(), 2);

		if (orderDetailClocks.getErrorIds().length() > 0) {
			throw new OrderException(OrderState.SYSTEMERROR, orderDetailClocks
					.getErrorIds().trim());
		}
		if (orderDetailClocks.getNormalIds().size() == 0) {
			throw new OrderException(OrderState.SYSTEMERROR, "");
		}
		order.setOrderState(OrderProcessState.ORDERUSERCANCEL.getCode());
		updateOrderByOrderNo(order);
		// try {
		//
		// userCanceLAfterMessage(order, orderDetailClocks);
		// } catch (Exception e) {
		// logger.error("-------------------------userCanceLAfterMessage Exception e="
		// + e.getMessage());
		// }
		orderResult.setState(1);

		return orderResult;
	}

	@Override
	public Boolean updateBalanceToCoins(UserAssets userAssets,Long configId , boolean isPayed){
		boolean isSucc = false;
		RechargeConfig config = rechargeConfigService.selectByPK(configId);//充值配置记录
		if(config != null){
			logger.info("查出来的充值配置 : " + config.getId() + ", " + config.getExchangeCoins());
			UserResult<UserAssets> assetsResult = new UserResult<UserAssets>();
			
			Integer reChargeMoney = config.getRechargeMoney();//充值金额(分)
			Integer exChangeCoins = config.getExchangeCoins();//可兑换广告币
			Integer additionalCoins = config.getAdditionalCoins();//可兑换广告币
			if(isPayed){
				//说明已经付过款
				userAssets.setCashBalance(userAssets.getCashBalance() + exChangeCoins + additionalCoins);
				assetsResult = userVerifyService.updateUserAssets(userAssets);
			}else{
				//说明是用余额兑换广告币
				if(userAssets.getRedEnvelopeBalance() >= reChargeMoney){
					//说明余额足够
					userAssets.setRedEnvelopeBalance(userAssets.getRedEnvelopeBalance() - reChargeMoney);
					userAssets.setCashBalance(userAssets.getCashBalance() + exChangeCoins + additionalCoins);
					assetsResult = userVerifyService.updateUserAssets(userAssets);
				}
			}
			
			isSucc = assetsResult.getState() == 1 ? true : false;
		}else{
			logger.info("充值配置未查询到！配置id ： " + configId);
		}
		
		return isSucc;
	}
	
	/**
	 * 更新订单order和userAssets
	 */
	@Override
	public OrderResult<OrderDetailClocks> updateOrderAndUnlockOrder(
			String orderNo, String payNo) throws OrderException {
		// TODO Auto-generated method stub
		OrderResult<OrderDetailClocks> orderUpdateResult = new OrderResult<OrderDetailClocks>();
		OrderResult<OrderView> orderResult = selectOrderView(orderNo);
		Order order = orderResult.getContent().getOrder();
		UserResult<User> userResult = userVerifyService
				.selectUserByUserId(order.getUserId());
		PayResult<PayOrder> payResult = payOrderService.selectPayOrder(payNo);
		order.setOrderState(OrderProcessState.ORDERPAYSUCCESS.getCode());
		order.setFinshiTime(new Date());
		if (payResult.getContent().getPayState().intValue() != 1) {
			order.setOrderState(OrderProcessState.ORDERPAYSUCCESS.getCode());

		} else {
			order.setPayNo(payNo);
		}
		UserAssets userAssets = userVerifyService
				.getUserAssetsByUser(userResult.getContent());
		logger.debug("-------------userResult.getContent()id="
				+ userResult.getContent().getId()
				+ ",userResult.getContent().getTableNum()="
				+ userResult.getContent().getTableNum());
		userAssets.setTableNum(userResult.getContent().getTableNum());
		
		Integer provideId = order.getProviderId();//关联充值配置表id //-1代表代理商充值 ，0代表一般充值 ， 其他值代表广告币兑换充值
		
		if(provideId > 0){
			//广告币
			updateBalanceToCoins(userAssets,provideId.longValue(),true);
		}else if(provideId == 0){
			//正常充值
			userAssets.setRedEnvelopeBalance(userAssets.getRedEnvelopeBalance()
					+ order.getOriginalPirce());
			userAssets.setOrderCount(userAssets.getOrderCount() + 1);
			userVerifyService.updateUserAssets(userAssets);
		}else{
			//代理商后续操作
			UserAgent agent = new UserAgent();
			agent.setUserId(order.getUserId());
			agent.setAgentLevel(4);
			agent.setAgentPiont(0);
			userAgentService.addOnLineUserAgent(agent);
		}

		updateOrderByOrderNo(order);
		

		return orderUpdateResult;
	}

	/**
	 * 根据传入的订单对象添加order到数据库
	 * @param order
	 * @return OrderResult<Order>
	 */
	@Override
	public OrderResult<Order> addOrderWithReturn(Order order) {
		// TODO Auto-generated method stub
		OrderResult<Order> payResult = new OrderResult<Order>();

		String orderNo = order.getOrderNo();
		String tableNum = getOrderNoTableNum(orderNo);
		order.setOrderNo(orderNo);
		order.setTableNum(tableNum);
		payResult.setState(orderMapper.insertSelective(order));
		payResult.setContent(order);
		return payResult;

	}
	
	private void addOrder(Order order, OrderView orderView, String orderNo,
			String tableNum) {
		UserIdentity userIdentity = orderView.getUserIdentity();
		User user = userVerifyService.selectUser(userIdentity);
		order.setOrderNo(orderNo);
		order.setTableNum(tableNum);
		order.setUserName(user.getNickName().equals("") ? user.getPhone() : user.getNickName());

		orderMapper.insertSelective(order);
	}

	private void addOrderDetails(List<OrdersDetail> ordersDetails,
			String orderNo, String tableNum, String orderDetailTableNum) {
		for (OrdersDetail ordersDetail : ordersDetails) {

			String orderDetailNo = UUIDGenerator
					.orderDetailNoGenerator(redisHelper);
			ordersDetail.setOrderDetailNo(orderDetailNo);
			ordersDetail.setOrderNo(orderNo);
			ordersDetail.setOrderTableNum(tableNum);
			ordersDetail.setTableNum(orderDetailTableNum);
			ordersDetailMapper.insertSelective(ordersDetail);
		}
	}

	private PayResult<PayOrder> addPayOrder(Order order, String orderNo,
			OrderView orderView) {

//		PayOrder payOrder = new PayOrder();
		PayOrder payOrder = orderView.getPayOrder();
		payOrder.setPayPrice(order.getDealPrice());
		payOrder.setOrderNo(orderNo);
		payOrder.setPaySumPrice(order.getOriginalPirce());
		payOrder.setExchangePrice(0);
		payOrder.setUserId(order.getUserId());
		payOrder.setPayChannel(orderView.getPayOrder().getPayChannel());
		payOrder.setPayWay(orderView.getPayOrder().getPayWay());
		return payOrderService.addPayOrder(payOrder);
	}

	private OrderResult<OrderView> addOrderView(OrderView orderView)
			throws OrderException {
		// TODO Auto-generated method stub
		OrderResult<OrderView> orderViewResult = new OrderResult<OrderView>();
		Order order = orderView.getOrder();
		UserIdentity userIdentity = orderView.getUserIdentity();
		String orderNo = UUIDGenerator.orderNoGenerator(redisHelper);//生成订单流水号

		List<String> orderBuinessLocks = new ArrayList<String>();
		Date startTime = new Date();
		logger.info("----------------------------------addOrderView start  orderNo= "
				+ orderNo + ",startTime=" + startTime);
		String tableNum = getOrderNoTableNum(orderNo);

		logger.info("----------------------------------addOrderView lock  resources start "
				+ orderNo);
		order.setOrderNo(orderNo);
		
		User user = userVerifyService.selectUser(userIdentity);
		order.setOrderNo(orderNo);
		order.setTableNum(tableNum);
		order.setUserName(user.getNickName().equals("") ? user.getPhone() : user.getNickName());
		
		orderView.setOrder(order);

		String orderLock = USER_RESOURCE_COURSE_PUBLIC_LOCK_PREFIX
				+ order.getUserId();
		orderBuinessLocks.add(orderLock);
		try {
			order.setDealPrice(orderView.getOrder().getDealPrice());
			if (redisLockHandler.tryLock(orderLock)) {
				
				logger.info("----------------------------------addOrderView addOrderAddress  start "
						+ orderNo);
				logger.info("----------------------------------addOrderView addOrderAddress  end "
						+ orderNo);
				logger.info("----------------------------------addOrderView addOrder  start "
						+ orderNo);

				OrderResult<Order> addOrderResult = addOrderWithReturn(order);//添加订单对象order
				logger.info("----------------------------------addOrderView addOrder  end "
						+ orderNo);
				logger.info("----------------------------------addOrderView addOrderDetails  start "
						+ orderNo);
				
				logger.info("----------------------------------addOrderView addOrderDetails  end "
						+ orderNo);
				logger.info("----------------------------------addOrderView addPayOrder  start "
						+ orderNo);
				PayResult<PayOrder> payResult = addPayOrder(order, orderNo,orderView);//添加payOrder

				logger.info("----------------------------------addOrderView addPayOrder  end "
						+ orderNo);
				order.setPayNo(payResult.getContent().getPayNo());
				orderView.setOrder(order);
				orderView.setPayOrder(payResult.getContent());
				orderView.setUserIdentity(orderView.getUserIdentity());
				// orderView.setOrdersDetails(ordersDetails);
				orderViewResult.setContent(orderView);
				orderViewResult.setState(1);

				orderViewResult.setOrderState(OrderState.SUCCESS);

			} else {
				throw new OrderException(
						OrderState.USERRESOURCECOURSEPUBLICLOCKED, "");
			}
		} catch (OrderException e) {
			logger.error("----------------------------------addOrderView process  orderNo"
					+ orderNo + "   OrderException=" + e.toExecptionString());
			orderViewResult.setOrderState(e.getOrderState());
			orderViewResult.setOrderSateContent(e.getOrderSateContent());
			throw new OrderException(e.getOrderState(), e.getOrderSateContent());
		} finally {
			for (String lock : orderBuinessLocks) {
				redisLockHandler.unLock(lock);
			}
		}

		logger.info("----------------------------------addOrderView lock  resources   end "
				+ orderNo);
		Date endTime = new Date();
		logger.info("----------------------------------addOrderView end  orderNo="
				+ orderNo
				+ ",endtime="
				+ endTime
				+ ", count time="
				+ (endTime.getTime() - startTime.getTime()) + " millisecond");
		return orderViewResult;

	}

	private int calIntegrationDeductPrice() {
		int integrationDeductPrice = 0;
		return integrationDeductPrice;
	}

	/**
	 * 计算订单价格
	 * 
	 * @param orderView
	 * @return
	 * @throws OrderException
	 */
	private Order calApplyOrderPrice(OrderView orderView) throws OrderException {
		Order checkOrder = new Order();
		logger.info("----------------------------------start calApplyOrderPrice  orderNo="
				+ orderView.getOrder().getOrderNo());
		UserIdentity userIdentity = orderView.getUserIdentity();
		User user = userVerifyService.selectUser(userIdentity);
		List<OrdersDetail> ordersDetails = orderView.getOrdersDetails();
		Order order = orderView.getOrder();
		if (user == null) {
			throw new OrderException(OrderState.USERIDENTITYERROR, "");
		}
		userIdentity.setUserId(user.getId());
		int goodsPricesCount = 0;
		int goodsCount = 0;
		int goodsTotalCount = 0;
		int courseId = 0;
		int teacherId = 0;
		for (OrdersDetail ordersDetail : ordersDetails) {
			goodsCount++;

			goodsTotalCount += ordersDetail.getGoodsCount();
			int tempPrice = ordersDetail.getUnitPirce()
					* ordersDetail.getGoodsCount();
			if (tempPrice == ordersDetail.getCountPrice()) {
				goodsPricesCount += tempPrice;
			} else {
				throw new OrderException(OrderState.ORDERDETAILCOUNTPRICEERROR,
						ordersDetail.getCoursePublicId() + "");
			}
			int unitPirce = ordersDetail.getUnitPirce();

		}

		if (goodsCount != order.getGoodsCount()) {
			throw new OrderException(OrderState.ORDERGOODSCOUNTERROR,
					goodsCount + "");
		}
		if (goodsTotalCount != order.getGoodsTotalCount()) {
			throw new OrderException(OrderState.ORDERGOODSTOTALCOUNTERROR,
					goodsTotalCount + "");
		}

		if (goodsPricesCount != order.getOriginalPirce()) {
			throw new OrderException(OrderState.ORDERORGIORIGINALERROR,
					goodsPricesCount + "");
		}
		Integer couponId = order.getCashCouponId();
		int cashCouponPrice = 0;

		int integrationDeductPrice = calIntegrationDeductPrice();

		if (cashCouponPrice < 0) {

			throw new OrderException(OrderState.CASHCOUPONPRICEEROR,
					cashCouponPrice + "");
		}
		if (integrationDeductPrice < 0) {

			throw new OrderException(OrderState.INTERGRATIONPRICEERROR,
					integrationDeductPrice + "");
		}

		logger.info("----------------------------------end calApplyOrderPrice  orderNo="
				+ orderView.getOrder().getOrderNo());
		checkOrder.setDiscountPrice(0);
		checkOrder.setIntegrationDeductPrice(integrationDeductPrice);
		checkOrder.setCashCouponDeductPrice(cashCouponPrice);
		checkOrder.setDealPrice(0);
		checkOrder.setOriginalPirce(order.getOriginalPirce());
		return checkOrder;
	}

	@Override
	public OrderResult<OrderView> addApplyOrderAndLockOrder(OrderView orderView)
			throws OrderException {
		OrderResult<OrderView> orderResult = addOrderView(orderView);
		return orderResult;
	}

	@Override
	public OrderResult<OrderView> getOrderPrice(OrderView orderView)
			throws OrderException {
		OrderResult<OrderView> orderResult = new OrderResult<OrderView>();
		OrderView checkOrderView = new OrderView();
		Order checkOrder = calApplyOrderPrice(orderView);
		checkOrderView.setOrder(checkOrder);
		orderResult.setContent(checkOrderView);
		orderResult.setState(1);
		return orderResult;
	}

	@Override
	public OrderDetailClocks analysisOrderDetailClocks(
			List<OrdersDetail> applyOrdersDetails, int coursePublicPorcessType) {
		return null;
	}

	@Override
	public OrderResult<Integer> updateReleaseCouresePublicLockAndCloseOrderByOrderNo(
			String orderNo) throws OrderException {
		return null;
	}

	@Override
	public OrderResult<Integer> updateReleaseCouresePublicLockAndCloseOrder(
			OrderView orderView) {
		return null;
	}

	@Override
	public OrderResult<Integer> updateTimingAutoBatchReleaseCouresePublicLockAndCloseOrder() {
		return null;
	}

	@Override
	public OrderResult<OrdersDetail> selectOrdersDetail(String orderNo,
			Long userId, String ordersDetailNo) {
		// TODO Auto-generated method stub
		OrderResult<OrdersDetail> orderResult = new OrderResult<OrdersDetail>();

		String orderTableNum = getOrderNoTableNum(orderNo);
		String tableNum = getOrderDetailTableNum(userId);

		Map orderDetailsMap = new HashMap();
		orderDetailsMap.put("orderTableNum", orderTableNum);
		orderDetailsMap.put("tableNum", tableNum);
		orderDetailsMap.put("orderDetailNo", ordersDetailNo);
		OrdersDetail ordersDetail = ordersDetailMapper
				.selectModel(orderDetailsMap);

		if (ordersDetail != null) {
			orderResult.setState(1);
			orderResult.setContent(ordersDetail);
		}
		return orderResult;
	}

	@Override
	public OrderResult<OrdersDetail> selectOrdersDetail(String orderNo,
			String ordersDetailNo) {
		// TODO Auto-generated method stub
		OrderResult<OrdersDetail> orderResult = new OrderResult<OrdersDetail>();

		OrderResult<Order> orderModelResult = selectOrder(orderNo);
		if (orderModelResult.getState() == 1) {
			OrderResult<OrdersDetail> orderDedetailResult = selectOrdersDetail(
					orderNo, orderModelResult.getContent().getUserId(),
					ordersDetailNo);
			if (orderDedetailResult.getState() == 1) {
				orderResult = orderDedetailResult;
			} else {
				orderResult.setState(3);
			}
		} else {
			orderResult.setState(2);
		}
		return orderResult;
	}

	@Override
	public OrderResult<Integer> updateOrdersDetail(OrdersDetail ordersDetail) {
		// TODO Auto-generated method stub
		OrderResult<Integer> orderResult = new OrderResult<Integer>();
		ordersDetail.setModifyTime(new Date());
		orderResult.setState(ordersDetailMapper
				.updateByPrimaryKeySelective(ordersDetail));
		orderResult.setState(1);
		return orderResult;
	}

	@Override
	public OrderResult<OrderDetailClocks> updateUserReimburseAndReleaseCoursePulicsLocked(
			List<OrdersDetail> ordersDetails) throws UserReimburseException {
		return null;
	}

	public static String getOrderDetailTableNum(Long userId) {

		return getOrderDetailTableNumInt(userId) + "";
	}

	public static Integer getOrderDetailTableNumInt(Long userId) {
		String md5String = MD5Util.md5Hex(userId + "");
		int tableNum = Integer.parseInt(md5String.substring(0, 1), 16) % 2;
		return tableNum;
	}

	public static String getOrderNoTableNum(String orderNo) {
		String tableNum = orderNo.substring(0, 4);
		return tableNum;
	}

	public static String getOrderBadyTableNum(String orderNo) {
		String tableNum = orderNo.substring(0, 4);
		return tableNum;
	}

}
