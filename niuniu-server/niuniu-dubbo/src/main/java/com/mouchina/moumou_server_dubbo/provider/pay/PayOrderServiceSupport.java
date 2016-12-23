package com.mouchina.moumou_server_dubbo.provider.pay;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.redis.RedisHelper;
import com.mouchina.base.redis.RedisLockHandler;
import com.mouchina.base.utils.DateUtils;
import com.mouchina.base.utils.MessageUtils;
import com.mouchina.base.utils.StringUtil;
import com.mouchina.base.utils.UUIDGenerator;
import com.mouchina.moumou_server.dao.pay.PayOrderMapper;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.order.Order;
import com.mouchina.moumou_server.entity.order.OrdersDetail;
import com.mouchina.moumou_server.entity.pay.PayOrder;
import com.mouchina.moumou_server.entity.pay.UserReimburse;
import com.mouchina.moumou_server_dubbo.provider.base.Env;
import com.mouchina.moumou_server_dubbo.provider.order.OrderServiceSupport;
import com.mouchina.moumou_server_interface.member.UserIdentity;
import com.mouchina.moumou_server_interface.member.UserVerifyService;
import com.mouchina.moumou_server_interface.order.OrderService;
import com.mouchina.moumou_server_interface.order.OrderView;
import com.mouchina.moumou_server_interface.pay.PayOrderService;
import com.mouchina.moumou_server_interface.pay.PayOrderView;
import com.mouchina.moumou_server_interface.pay.ReimburseService;
import com.mouchina.moumou_server_interface.view.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

/**
 * 类说明
 * 
 * @author larry 2015年6月19日下午9:10:46
 */

public class PayOrderServiceSupport implements PayOrderService {
	@Resource
	private OrderService orderService;
	@Resource
	private PayOrderMapper payOrderMapper;
	@Resource
	private RedisHelper redisHelper;
	@Resource
	private ReimburseService reimburseService;
	static Logger logger = LogManager.getLogger();
	@Resource
	private RedisLockHandler redisLockHandler;

	private static final String PAY_ORDER_LOCK_PRFIX = "pay_order_lock_prefix_";

	@Resource
	private UserVerifyService userVerifyService;

	@Resource
	private Env env;

	@Override
	public PayResult<PayOrder> addPayOrder(PayOrder payOrder) {
		// TODO Auto-generated method stub
		PayResult<PayOrder> payResult = new PayResult<PayOrder>();

		String payNo = UUIDGenerator.payOrderNoGenerator(redisHelper);
		String tableNum = getPayOrderNoTableNum(payNo);
		payOrder.setPayNo(payNo);
		payOrder.setTableNum(tableNum);
		payResult.setState(payOrderMapper.insertSelective(payOrder));
		payResult.setContent(payOrder);
		return payResult;

	}

	public static String getPayOrderNoTableNum(String payOrderNo) {
		String tableNum = payOrderNo.substring(0, 4);
		return tableNum;
	}

	@Override
	public PayResult<Integer> updatePayOrder(PayOrder payOrder) {
		// TODO Auto-generated method stub
		PayResult<Integer> payResult = new PayResult<Integer>();

		String tableNum = getPayOrderNoTableNum(payOrder.getPayNo());
		payOrder.setTableNum(tableNum);

		payOrder.setModifyTime(new Date());
		payResult
				.setState(payOrderMapper.updateByPrimaryKeySelective(payOrder));

		return payResult;

	}

	@Override
	public PayResult<Integer> deletePayOrder(String payNo) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		String tableNum = getPayOrderNoTableNum(payNo);
		map.put("payNo", payNo);
		map.put("tableNum", tableNum);
		PayResult<Integer> payResult = new PayResult<Integer>();
		payResult.setState(payOrderMapper.deleteByPayNo(map));
		return payResult;
	}

	@Override
	public PayResult<PayOrder> selectPayOrder(String payNo) {
		// TODO Auto-generated method stub
		Map map = new HashMap();
		String tableNum = getPayOrderNoTableNum(payNo);
		map.put("payNo", payNo);
		map.put("tableNum", tableNum);
		PayResult<PayOrder> payResult = new PayResult<PayOrder>();
		PayOrder payOrder = payOrderMapper.selectModel(map);
		if (payOrder != null) {
			payResult.setContent(payOrder);
			payResult.setState(1);
		}
		return payResult;
	}

	private int upateCheckPayOrder(PayOrder payOrder) throws PayOrderException {
		int result = 0;
		PayResult<PayOrder> paySelectResult = selectPayOrder(payOrder
				.getPayNo());
		if (paySelectResult.getState() != 1) {
			throw new PayOrderException(PayOrderState.PAYORDERNOEXITS,
					payOrder.getPayNo());
		}

		PayOrder dbPayOrder = paySelectResult.getContent();

		if (dbPayOrder.getPayPrice().intValue() != payOrder.getPayPrice()
				.intValue()) {
			logger.error("upateCheckPayOrder payNo=" + payOrder.getPayNo()
					+ ", dbpayorder payPrice="
					+ dbPayOrder.getPayPrice().intValue()
					+ ", payOrder payPrice="
					+ payOrder.getPayPrice().intValue());
			throw new PayOrderException(PayOrderState.PAYPRICEERROR,
					payOrder.getPayNo());
		}

		if (dbPayOrder.getPayState() != 1) {
			if (!payOrder.getThirdPartyPayNo().equals(
					dbPayOrder.getThirdPartyPayNo())) {

				Map payMap = new HashMap();
				payMap.put("userId", dbPayOrder.getUserId());
				String tableNum = getPayOrderNoTableNum(dbPayOrder.getPayNo());
				payMap.put("tableNum", tableNum);
				payMap.put("orderNo", dbPayOrder.getOrderNo());
				payMap.put("thirdPartyPayNo", payOrder.getThirdPartyPayNo());
				int count = payOrderMapper.selectCount(payMap);
				if (count == 0) {
					logger.info("----------------------------------upateCheckPayOrder  repetnewPayorderProcess userId="
							+ dbPayOrder.getUserId()
							+ " thirdPartyPayNo= "
							+ payOrder.getThirdPartyPayNo());
					PayOrder newPayOrder = new PayOrder();
					BeanUtils.copyProperties(dbPayOrder, newPayOrder);
					newPayOrder.setFinshiTime(new Date());
					newPayOrder.setModifyTime(new Date());
					newPayOrder.setId(null);
					newPayOrder.setPayState((byte) 8);
					newPayOrder.setThirdPartyPayNo(payOrder
							.getThirdPartyPayNo());
					addPayOrder(newPayOrder);

					UserResult<UserIdentity> userResultUserIdentity = userVerifyService
							.selectUserPartByUserId(dbPayOrder.getUserId() + "");

					logger.info("----------------------------------upateCheckPayOrder  repetnewPayorderProcess userId="
							+ dbPayOrder.getUserId()
							+ " thirdPartyPayNo= "
							+ payOrder.getThirdPartyPayNo()
							+ ",newPayOrderpayNo="
							+ newPayOrder.getPayNo()
							+ ",orderNo=" + newPayOrder.getOrderNo());

					result = 1;

				} else {
					logger.info("----------------------------------upateCheckPayOrder  repetnewPayorderIgnore userId="
							+ dbPayOrder.getUserId()
							+ " thirdPartyPayNo= "
							+ payOrder.getThirdPartyPayNo());
					throw new PayOrderException(PayOrderState.PAYNOFINISHED,
							payOrder.getPayNo() + " "
									+ dbPayOrder.getPayState());
				}
			} else {
				throw new PayOrderException(PayOrderState.PAYNOFINISHED,
						payOrder.getPayNo() + " " + dbPayOrder.getPayState());
			}

		} else if (dbPayOrder.getPayState() == 1) {
			OrderResult<Order> orderResult = orderService
					.selectOrder(dbPayOrder.getOrderNo());

			Map payMap = new HashMap();
			payMap.put("userId", dbPayOrder.getUserId());
			String tableNum = getPayOrderNoTableNum(dbPayOrder.getPayNo());
			payMap.put("tableNum", tableNum);
			payMap.put("orderNo", dbPayOrder.getOrderNo());
			payMap.put("payState", 2);
			int count = payOrderMapper.selectCount(payMap);
			if (count > 0) {

				dbPayOrder.setTableNum(tableNum);
				dbPayOrder.setPayState((byte) 8);
				updatePayOrder(dbPayOrder);

				UserResult<UserIdentity> userResultUserIdentity = userVerifyService
						.selectUserPartByUserId(dbPayOrder.getUserId() + "");
				result = 1;
			}

		}

		return result;
	}

	/**
	 * 
	 * @param payOrderView
	 * @return result 1终止处理 0继续执行
	 * @throws PayOrderException
	 */
	private int updateCheckPayOrderViewBefore(PayOrderView payOrderView)
			throws PayOrderException {

		int result = 0;
		PayOrder payOrder = payOrderView.getPayOrder();
		PayResult<PayOrder> payResult = selectPayOrder(payOrder.getPayNo());
		if (payResult.getState() == 1) {
			payOrder.setOrderNo(payResult.getContent().getOrderNo());
			payOrderView.setPayOrder(payOrder);
		} else {
			throw new PayOrderException(PayOrderState.PAYORDERNOEXITS,
					payOrder.getPayNo() + " ");
		}
		logger.info("----------------------------------updateCheckPayOrder  start  payNo= "
				+ payOrder.getPayNo());
		result = upateCheckPayOrder(payOrder);
		return result;
	}

	@Override
	public PayResult<Integer> updatePayOrderCheckAndUnlock(
			PayOrderView payOrderView) {
		// TODO Auto-generated method stub

		PayResult<Integer> payResult = new PayResult<Integer>();

		PayOrder payOrder = payOrderView.getPayOrder();

		PayResult<PayOrder> payOrderResult = selectPayOrder(payOrderView
				.getPayOrder().getPayNo());
		if (payOrderResult.getState() != 1) {
			throw new PayOrderException(PayOrderState.PAYORDERNOEXITS,
					payOrder.getPayNo() + " " + payOrder.getOrderNo());
		}

		Date startTime = new Date();
		logger.info("----------------------------------updatePayOrderCheckAndUnlock start  payNo= "
				+ payOrder.getPayNo()
				+ ",ThirdPartyPayId="
				+ payOrder.getThirdPartyPayNo());
		List<String> payOrderBuinessLocks = new ArrayList<String>();

		try {

			String payOrderLock = PAY_ORDER_LOCK_PRFIX + payOrder.getPayNo();
			payOrderBuinessLocks.add(payOrderLock);
			String orderNoLock = OrderServiceSupport.ORDER_NO_LOCK_PREFIX
					+ payOrder.getOrderNo();
			payOrderBuinessLocks.add(orderNoLock);
			if (redisLockHandler.tryLock(payOrderLock)
					&& redisLockHandler.tryLock(orderNoLock)) {
				logger.info("----------------------------------updatePayOrderCheckAndUnlock lock suceess  payNo= "
						+ payOrder.getPayNo());
				if (updateCheckPayOrderViewBefore(payOrderView) == 0) {

					orderService.updateOrderAndUnlockOrder(
							payOrder.getOrderNo(), payOrder.getPayNo());

					int payState = 5;

					logger.info("----------------------------------updatePayOrderCheckAndUnlock  updateCheckPayOrderView start process   payNo= "
							+ payOrder.getPayNo());

					PayResult<PayOrder> paySelectResult = selectPayOrder(payOrder
							.getPayNo());
					PayOrder updatePayOrder = new PayOrder();

					updatePayOrder.setId(paySelectResult.getContent().getId());
					updatePayOrder.setPayNo(payOrder.getPayNo());
					updatePayOrder.setFinshiTime(new Date());
					updatePayOrder.setPayState((byte) payState);
					updatePayOrder.setThirdPartyPayNo(payOrder
							.getThirdPartyPayNo());

					logger.info("----------------------------------updatePayOrderCheckAndUnlock  upateCheckPayOrder start process   payNo= "
							+ payOrder.getPayNo());
					updatePayOrder(updatePayOrder);

					logger.info("----------------------------------updatePayOrderCheckAndUnlock sms send start orderNo"
							+ payOrder.getPayNo());
					try {
					} catch (Exception e) {
						logger.error("----------------------------------updatePayOrderCheckAndUnlock sms send error");
					}

					payResult.setPayOrderState(PayOrderState.SUCCESS);
					payResult.setState(1);
				} else {
					logger.info("----------------------------------updatePayOrderRepetProcesFinished payNo="
							+ payOrder.getPayNo());
				}
			} else {
				logger.info("----------------------------------updatePayOrderCheckAndUnlock lock fail  payNo= "
						+ payOrder.getPayNo());
				throw new PayOrderException(PayOrderState.PAYNOLOCKED, "");
			}

		} catch (PayOrderException e) {
			logger.error("----------------------------------updatePayOrderCheckAndUnlock process  payNo"
					+ payOrder.getPayNo()
					+ "   PayOrderException="
					+ e.toExecptionString());
			payResult.setPayOrderState(e.getPayOrderState());
			payResult.setPayOrderSateContent(e.getPayOrderSateContent());
			throw new PayOrderException(e.getPayOrderState(),
					e.getPayOrderSateContent());
		} finally {
			for (String lock : payOrderBuinessLocks) {
				redisLockHandler.unLock(lock);
			}
		}

		Date endTime = new Date();
		logger.info("----------------------------------updatePayOrderCheckAndUnlock  end  payNo= "
				+ payOrder.getPayNo()
				+ ",ThirdPartyPayId="
				+ payOrder.getThirdPartyPayNo()
				+ ", count time="
				+ (endTime.getTime() - startTime.getTime()) + " millisecond");

		return payResult;

	}

	@Override
	public PayResult<Integer> updatePayOrderCheck(PayOrderView payOrderView) {
		// TODO Auto-generated method stub

		PayResult<Integer> payResult = new PayResult<Integer>();

		PayOrder payOrder = payOrderView.getPayOrder();

		PayResult<PayOrder> payOrderResult = selectPayOrder(payOrderView
				.getPayOrder().getPayNo());
		if (payOrderResult.getState() != 1) {
			throw new PayOrderException(PayOrderState.PAYORDERNOEXITS,
					payOrder.getPayNo() + " " + payOrder.getOrderNo());
		}

		Date startTime = new Date();
		logger.info("----------------------------------updatePayOrderCheckAndUnlock start  payNo= "
				+ payOrder.getPayNo()
				+ ",ThirdPartyPayId="
				+ payOrder.getThirdPartyPayNo());
		List<String> payOrderBuinessLocks = new ArrayList<String>();

		try {

			String payOrderLock = PAY_ORDER_LOCK_PRFIX + payOrder.getPayNo();
			payOrderBuinessLocks.add(payOrderLock);
			String orderNoLock = OrderServiceSupport.ORDER_NO_LOCK_PREFIX
					+ payOrder.getOrderNo();
			payOrderBuinessLocks.add(orderNoLock);
			if (redisLockHandler.tryLock(payOrderLock)
					&& redisLockHandler.tryLock(orderNoLock)) {
				logger.info("----------------------------------updatePayOrderCheckAndUnlock lock suceess  payNo= "
						+ payOrder.getPayNo());
				if (updateCheckPayOrderViewBefore(payOrderView) == 0) {

					logger.info("----------------------------------updatePayOrderCheckAndUnlock  updateCheckPayOrderView end process   payNo= "
							+ payOrder.getPayNo());
					OrderResult<OrderDetailClocks> orderAndUnlockOrderResult = orderService
							.updateOrderAndUnlockOrder(payOrder.getOrderNo(),
									payOrder.getPayNo());
					int payState = 2;
					if (orderAndUnlockOrderResult.getContent().getLockIdsSet()
							.size() == 0) {
						payState = 5;
					}
					logger.info("----------------------------------updatePayOrderCheckAndUnlock  updateCheckPayOrderView start process   payNo= "
							+ payOrder.getPayNo());

					PayResult<PayOrder> paySelectResult = selectPayOrder(payOrder
							.getPayNo());
					PayOrder updatePayOrder = new PayOrder();

					updatePayOrder.setId(paySelectResult.getContent().getId());
					updatePayOrder.setPayNo(payOrder.getPayNo());
					updatePayOrder.setFinshiTime(new Date());
					updatePayOrder.setPayState((byte) payState);
					updatePayOrder.setThirdPartyPayNo(payOrder
							.getThirdPartyPayNo());
					logger.info("----------------------------------updatePayOrderCheckAndUnlock  upateCheckPayOrder start process   payNo= "
							+ payOrder.getPayNo());
					updatePayOrder(updatePayOrder);

					logger.info("----------------------------------updatePayOrderCheckAndUnlock  upateCheckPayOrder end process   payNo= "
							+ payOrder.getPayNo());

					logger.info("----------------------------------updatePayOrderCheckAndUnlock sms send start orderNo"
							+ payOrder.getPayNo());
					try {
					} catch (Exception e) {
						logger.error("----------------------------------updatePayOrderCheckAndUnlock sms send error");
					}
					logger.info("----------------------------------updatePayOrderCheckAndUnlock sms send end  orderNo"
							+ payOrder.getPayNo());
					logger.info("----------------------------------updatePayOrderCheckAndUnlock solr syn start  orderNo"
							+ payOrder.getPayNo());

					logger.info("----------------------------------updatePayOrderCheckAndUnlock solr syn start  end"
							+ payOrder.getPayNo());
					payResult.setPayOrderState(PayOrderState.SUCCESS);
					payResult.setState(1);
				} else {
					logger.info("----------------------------------updatePayOrderRepetProcesFinished payNo="
							+ payOrder.getPayNo());
				}
			} else {
				logger.info("----------------------------------updatePayOrderCheckAndUnlock lock fail  payNo= "
						+ payOrder.getPayNo());
				throw new PayOrderException(PayOrderState.PAYNOLOCKED, "");
			}

		} catch (PayOrderException e) {
			logger.error("----------------------------------updatePayOrderCheckAndUnlock process  payNo"
					+ payOrder.getPayNo()
					+ "   PayOrderException="
					+ e.toExecptionString());
			payResult.setPayOrderState(e.getPayOrderState());
			payResult.setPayOrderSateContent(e.getPayOrderSateContent());
			throw new PayOrderException(e.getPayOrderState(),
					e.getPayOrderSateContent());
		} finally {
			for (String lock : payOrderBuinessLocks) {
				redisLockHandler.unLock(lock);
			}
		}

		Date endTime = new Date();
		logger.info("----------------------------------updatePayOrderCheckAndUnlock  end  payNo= "
				+ payOrder.getPayNo()
				+ ",ThirdPartyPayId="
				+ payOrder.getThirdPartyPayNo()
				+ ", count time="
				+ (endTime.getTime() - startTime.getTime()) + " millisecond");

		return payResult;

	}

	@Override
	public PayResult<ListRange<PayOrderView>> selectListRangePayOrderViewPage(
			Map map) {
		// TODO Auto-generated method stub
		return null;

	}

	@Override
	public PayResult<ListRange<PayOrder>> selectListRangePayOrderPage(Map map) {
		if (!map.containsKey("tableNum")) {

			String year = map.get("year") == null ? null : map.get("year")
					.toString();

			String tableNum = year == null ? DateUtils
					.getDateStringYYYY(new Date()) : year;
			map.put("tableNum", tableNum);
		}
		PayResult<ListRange<PayOrder>> payResult = new PayResult<ListRange<PayOrder>>();
		ListRange<PayOrder> listRange = new ListRange<PayOrder>();
		int count = payOrderMapper.selectCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		listRange.setData(payOrderMapper.selectPageList(map));
		payResult.setContent(listRange);
		payResult.setState(1);
		return payResult;

	}

	@Override
	public PayResult<String> addRePay(PayOrder payOrder) {
		// TODO Auto-generated method stub
		PayResult<String> payResult = new PayResult<String>();
		String orderNoLock = OrderServiceSupport.ORDER_NO_LOCK_PREFIX
				+ payOrder.getOrderNo();
		try {

			if (redisLockHandler.tryLock(orderNoLock)) {
				PayOrder newPayOrder = new PayOrder();
				BeanUtils.copyProperties(payOrder, newPayOrder);
				newPayOrder.setId(null);
				newPayOrder.setCreateTime(null);
				newPayOrder.setModifyTime(null);
				newPayOrder.setPayState(null);
				String payNo = UUIDGenerator.payOrderNoGenerator(redisHelper);
				String tableNum = getPayOrderNoTableNum(payNo);
				newPayOrder.setPayNo(payNo);
				newPayOrder.setTableNum(tableNum);
				payOrderMapper.insertSelective(newPayOrder);
				payResult.setState(1);
				payResult.setContent(payNo);
			} else {

				payResult.setState(-1);
			}
		} catch (Exception e) {

			logger.error("addRePay  orderNo=" + payOrder.getOrderNo() + ",e="
					+ payOrder);
		} finally {
			redisLockHandler.unLock(orderNoLock);
		}
		return payResult;

	}

	private void reimburseRequestAfterMessage(UserReimburse userReimburse,
			OrderDetailClocks orderDetailClocks) {

		int courseCount = 0;
		String orderDetailNo = null;
		Order order = null;
		for (Map.Entry<Integer, List<OrdersDetail>> entry : orderDetailClocks
				.getTeacherOrdersDetailDealMap().entrySet()) {

			courseCount += entry.getValue().size();
			if (order == null) {
				OrderResult<Order> orderResult = orderService.selectOrder(entry
						.getValue().get(0).getOrderNo());
				order = orderResult.getContent();
			}

			if (StringUtil.isEmpty(orderDetailNo)) {
				orderDetailNo = entry.getValue().get(0).getOrderDetailNo();
			}
			String smsTeacherKey = "teacher.sms.order.cancel";

		}
		UserResult<User> userResult = userVerifyService
				.selectUserByUserId(order.getUserId());
		String smsUserKey = "customer.sms.order.cancel";
		if (userResult.getState() == 1) {

			String smsContent = null;

		}
	}

	@Override
	public PayResult<PayOrder> selectLatestPayOrderByOrderNo(String orderNo) {

		PayResult<PayOrder> payResult = new PayResult<PayOrder>();
		Map map = new HashMap();
		map.put("orderNo", orderNo);
		String tableNum = OrderServiceSupport.getOrderNoTableNum(orderNo);
		map.put("tableNum", tableNum);
		Page page = new Page(0, 1);
		page.setBegin(0);
		map.put("page", page);
		map.put("order", "create_time desc");
		PayResult<ListRange<PayOrder>> payResults = selectListRangePayOrderPage(map);
		if (payResults.getState() == 1
				&& payResults.getContent().getData().size() > 0) {
			payResult.setState(1);
			payResult.setContent(payResults.getContent().getData().get(0));
		}
		return payResult;
	}

	@Override
	public PayResult<Integer> reimburseQueryPayOrder(String orderNo,
			String orderDetailNos) {
		List<OrdersDetail> ordersDetails = new ArrayList<OrdersDetail>();
		return cehckReimburseQueryPayOrder(orderNo, orderDetailNos,
				ordersDetails);
	}

	private PayResult<Integer> cehckReimburseQueryPayOrder(String orderNo,
			String orderDetailNos, List<OrdersDetail> ordersDetails) {
		boolean flag = true;
		int reimbursePrice = 0;
		PayResult<Integer> payResult = new PayResult<Integer>();
		OrderResult<Order> orderModelResult = orderService.selectOrder(orderNo);

		if (orderModelResult.getState() == 1) {

			for (String orderDetailNo : orderDetailNos.split(",")) {
				OrderResult<OrdersDetail> orderDetailResult = orderService
						.selectOrdersDetail(orderNo, orderModelResult
								.getContent().getUserId(), orderDetailNo);

				if (orderDetailResult.getState() == 1) {
					if (orderDetailResult.getContent().getOrderState() == 1) {
						ordersDetails.add(orderDetailResult.getContent());
						int calPrice = calReimburse(
								orderDetailResult.getContent(),
								orderModelResult.getContent());

						if (calPrice > 0) {
							reimbursePrice += calPrice;
						} else {
							flag = false;
							payResult.setPayOrderSateContent(orderDetailResult
									.getContent().getOrderDetailNo());
							payResult.setState(-1);
							break;
						}

					} else {
						flag = false;
						payResult.setState(-3);
						break;

					}

				} else {
					flag = false;
					payResult.setState(orderDetailResult.getState());
					break;
				}
			}

			reimbursePrice = reimbursePrice
					- orderModelResult.getContent().getCashCouponDeductPrice()
					- orderModelResult.getContent().getIntegrationDeductPrice();

			if (orderModelResult.getContent().getDealPrice() < (reimbursePrice + orderModelResult
					.getContent().getReimbursePrice()) && reimbursePrice > 0) {
				flag = false;
				payResult.setState(-2);
				payResult.setContent(reimbursePrice);
			}

		} else {
			flag = false;
			payResult.setState(2);
		}
		if (flag && reimbursePrice > 0) {
			payResult.setState(1);
			payResult.setContent(reimbursePrice);

		}

		return payResult;
	}

	private int calReimburse(OrdersDetail ordersDetail, Order order) {
		int price = 0;
		long nowTime = new Date().getTime();
		long startTime = ordersDetail.getStartTime().getTime();
		if (startTime > (nowTime + 3600000 * 24)) {
			price = ordersDetail.getOrderCountPrice();

		} else if (startTime > (nowTime + 3600000)) {
			price = (int) (ordersDetail.getOrderCountPrice() * 0.5);
		} else if (startTime > nowTime) {
			price = (int) (ordersDetail.getOrderCountPrice() * 0.2);
		} else {
			if (ordersDetail.getOrderState().intValue() == 1) {
				price = ordersDetail.getOrderCountPrice();
			}

		}
		return price;
	}

	@Override
	public PayResult<Integer> updateReimburseRequestPayOrder(String orderNo,
			String orderDetailNos, Integer reimbursePrice) {
		List<OrdersDetail> ordersDetails = new ArrayList<OrdersDetail>();
		PayResult<Integer> payResult = cehckReimburseQueryPayOrder(orderNo,
				orderDetailNos, ordersDetails);
		long userId = 0;
		if (payResult.getState() == 1) {
			if (payResult.getContent().intValue() == reimbursePrice) {
				OrderResult<Order> orderModelResult = orderService
						.selectOrder(orderNo);
				Order order = orderModelResult.getContent();
				userId = order.getUserId();

				for (String orderDetailNo : orderDetailNos.split(",")) {
					OrdersDetail ordersDetail = new OrdersDetail();
					ordersDetail.setOrderDetailNo(orderDetailNo);
					ordersDetail.setOrderNo(orderNo);
					// orderFulfillService.updateReimburseRequestOrdersDetail(
					// ordersDetail, (byte) 6, orderModelResult
					// .getContent().getUserId());
				}

				order.setReimbursePrice(order.getReimbursePrice()
						+ reimbursePrice);
				orderService.updateOrderByOrderNo(order);

				// update orderService

				payResult.setContent(reimbursePrice);
			} else {
				payResult.setState(4);
			}
		} else if (payResult.getState() == -2) {
			if (payResult.getContent().intValue() == reimbursePrice.intValue()) {
				OrderResult<Order> orderModelResult = orderService
						.selectOrder(orderNo);
				Order order = orderModelResult.getContent();
				userId = order.getUserId();
				OrderResult<OrderView> orderView = orderService
						.selectOrderView(orderNo);
				for (OrdersDetail ordersDetail : orderView.getContent()
						.getOrdersDetails()) {
					if (ordersDetail.getOrderState() < 6) {
						ordersDetail.setOrderNo(orderNo);
						// orderFulfillService.updateReimburseRequestOrdersDetail(
						// ordersDetail, (byte) 6, orderModelResult
						// .getContent().getUserId());
					}
				}
				order.setReimbursePrice(order.getReimbursePrice()
						+ reimbursePrice);
				orderService.updateOrderByOrderNo(order);
				payResult.setState(1);
				payResult.setContent(reimbursePrice);
			}
		}
		if (payResult.getState() == 1) {
			UserReimburse userReimburse = new UserReimburse();
			userReimburse.setPrice(reimbursePrice);
			userReimburse.setOrderNo(orderNo);
			userReimburse.setOrderDetailNos(orderDetailNos);
			userReimburse.setUserId(userId);
			reimburseService.addUserReimburse(userReimburse);
			OrderResult<OrderDetailClocks> orderResult = orderService
					.updateUserReimburseAndReleaseCoursePulicsLocked(ordersDetails);
			// reimburseService.reimburseReduceAssets(userReimburse,
			// orderResult.getContent());
			// courseSolrService
			// .synSolrCourselistReadCommitedByCoursePublicNumsAndTeacherIds(
			// orderResult.getContent()
			// .getCoursePublicNumAndTeachers(), DateUtils
			// .checkOptionRetrunDate("next", new Date(),
			// 1));
			try {

				reimburseRequestAfterMessage(userReimburse,
						orderResult.getContent());
			} catch (Exception e) {
				logger.error("-------------------------sendReimbureseMessage Exception e="
						+ e.getMessage());
			}
		}
		return payResult;
	}

	@Override
	public PayOrder selectPayOrderModel(Map map) {
		return payOrderMapper.selectModel(map);
	}

}
