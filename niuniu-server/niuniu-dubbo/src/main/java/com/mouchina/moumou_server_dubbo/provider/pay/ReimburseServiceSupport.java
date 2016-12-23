package com.mouchina.moumou_server_dubbo.provider.pay;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.redis.RedisHelper;
import com.mouchina.base.redis.RedisLockHandler;
import com.mouchina.base.utils.DateUtils;
import com.mouchina.base.utils.UUIDGenerator;
import com.mouchina.moumou_server.dao.pay.UserReimburseMapper;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.order.OrdersDetail;
import com.mouchina.moumou_server.entity.pay.UserCashFlow;
import com.mouchina.moumou_server.entity.pay.UserReimburse;
import com.mouchina.moumou_server_dubbo.provider.order.OrderServiceSupport;
import com.mouchina.moumou_server_interface.member.UserIdentity;
import com.mouchina.moumou_server_interface.member.UserVerifyService;
import com.mouchina.moumou_server_interface.order.OrderService;
import com.mouchina.moumou_server_interface.pay.CashFlowService;
import com.mouchina.moumou_server_interface.pay.PayOrderService;
import com.mouchina.moumou_server_interface.pay.ReimburseService;
import com.mouchina.moumou_server_interface.view.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import java.util.*;

public class ReimburseServiceSupport implements ReimburseService {

	@Resource
	private UserReimburseMapper userReimburseMapper;
	@Resource
	private RedisHelper redisHelper;
	static Logger logger = LogManager.getLogger();
	@Resource
	private OrderService orderService;
	@Resource
	private RedisLockHandler redisLockHandler;
	@Resource
	private UserVerifyService userVerifyService;
	@Resource
	private CashFlowService cashFlowService;
	@Resource
	private PayOrderService payOrderService;

	private static final String REIMBUERSE_LOCK_PREFIX = "reimbuerse_lock_prefix_";

	@Override
	public PayResult<UserReimburse> addUserReimburse(UserReimburse userReimburse) {
		PayResult<UserReimburse> payResult = new PayResult<UserReimburse>();
		String reimburseNo = UUIDGenerator.userReimburseNoGenerator(redisHelper);
		String tableNum = getUserReimburseNoTableNum(reimburseNo);
		userReimburse.setReimburseNo(reimburseNo);
		userReimburse.setTableNum(tableNum);
		payResult.setState(userReimburseMapper.insertSelective(userReimburse));
		payResult.setContent(userReimburse);
		return payResult;
	}

	@Override
	public PayResult<Integer> updateUserReimburse(UserReimburse userReimburse) {

		PayResult<Integer> payResult = new PayResult<Integer>();
		userReimburse.setModifyTime(new Date());
		String tableNum = getUserReimburseNoTableNum(userReimburse
				.getReimburseNo());
		userReimburse.setTableNum(tableNum);
		payResult.setState(userReimburseMapper
				.updateByPrimaryKeySelective(userReimburse));

		return payResult;
	}

	@Override
	public PayResult<UserReimburse> selectUserReimburseByReimburseNo(
			String reimburseNo) {
		PayResult<UserReimburse> payResult = new PayResult<UserReimburse>();
		Map map = new HashMap();

		String tableNum = getUserReimburseNoTableNum(reimburseNo);
		map.put("tableNum", tableNum);
		map.put("reimburseNo", reimburseNo);
		UserReimburse userReimburse = userReimburseMapper.selectModel(map);
		if (userReimburse != null) {
			payResult.setContent(userReimburse);
			payResult.setState(1);
		}

		return payResult;
	}

	@Override
	public PayResult<Integer> deleteUserReimburseByReimburseNo(
			String reimburseNo) {
		PayResult<Integer> payResult = new PayResult<Integer>();
		String tableNum = getUserReimburseNoTableNum(reimburseNo);
		Map map = new HashMap();
		map.put("tableNum", tableNum);
		map.put("reimburseNo", reimburseNo);
		payResult.setState(userReimburseMapper.deleteByUserReimburseNo(map));

		return payResult;
	}

	@Override
	public PayResult<ListRange<UserReimburse>> selectListRangeUserReimbursePage(
			Map map) {
		// TODO Auto-generated method stub
		if (!map.containsKey("tableNum")) {
			String year=map.get("year")==null?null:map.get("year").toString();
			String tableNum =year==null?DateUtils.getDateStringYYYY(new Date()):year;
			map.put("tableNum", tableNum);
		}
		PayResult<ListRange<UserReimburse>> payResult = new PayResult<ListRange<UserReimburse>>();
		ListRange<UserReimburse> listRange = new ListRange<UserReimburse>();
		int count = userReimburseMapper.selectCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		listRange.setData(userReimburseMapper.selectPageList(map));
		payResult.setContent(listRange);
		payResult.setState(1);
		return payResult;
	}

	@Override
	public PayResult<Integer> updateRefuseUserUserReimburse(
			UserReimburse userReimburse) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<OrdersDetail> checkUserReimburseBefore(
			UserReimburse userReimburse, int type)
			throws UserReimburseException {
		PayResult<UserReimburse> payResult = selectUserReimburseByReimburseNo(userReimburse
				.getReimburseNo());
		if (payResult.getState() != 1) {
			throw new UserReimburseException(
					UserReimburseState.REIMBURSENOEXITS, "");
		}
		if (!payResult.getContent().getOrderNo()
				.equals(userReimburse.getOrderNo())) {
			throw new UserReimburseException(UserReimburseState.ORDERNOEXITS,
					userReimburse.getOrderNo());
		}
		if (!payResult.getContent().getOrderDetailNos()
				.equals(userReimburse.getOrderDetailNos())) {
			throw new UserReimburseException(
					UserReimburseState.ORDERDETAILERROR,
					userReimburse.getOrderDetailNos());
		}
		if (payResult.getContent().getState().intValue() != 4) {
			throw new UserReimburseException(
					UserReimburseState.REIMBURSENOFINISHED, "");
		}
		if (!payResult.getContent().getPrice().equals(userReimburse.getPrice())) {
			throw new UserReimburseException(
					UserReimburseState.REIMBURSENOPRICEERROR, "");
		}
		List<OrdersDetail> ordersDetails = new ArrayList<OrdersDetail>();

		for (String orderDetailNo : userReimburse.getOrderDetailNos()
				.split(",")) {
			OrderResult<OrdersDetail> orderResult = orderService
					.selectOrdersDetail(userReimburse.getOrderNo(),
							userReimburse.getUserId(), orderDetailNo);
			if (orderResult.getState() == 0) {
				logger.info("----------------------------------updateAgreeUserUserReimburse 1  orderDetailNo= "
						+ orderDetailNo);
				throw new UserReimburseException(
						UserReimburseState.ORDERDETAILERROR, orderDetailNo);
			}
			if (type == 1) {
				if (orderResult.getContent().getOrderState().intValue() != 6) {
					logger.info("----------------------------------updateAgreeUserUserReimburse 2  orderDetailNo= "
							+ orderDetailNo);
					throw new UserReimburseException(
							UserReimburseState.ORDERDETAILERROR, orderDetailNo);
				}
			}
			if (type == 2) {
				if (orderResult.getContent().getOrderState().intValue() == 6) {

					logger.info("----------------------------------updateAgreeUserUserReimburse 3  orderDetailNo= "
							+ orderDetailNo);
					throw new UserReimburseException(
							UserReimburseState.ORDERDETAILERROR, orderDetailNo);
				}
			}
			OrdersDetail ordersDetail = orderResult.getContent();

			ordersDetail.setOrderTableNum(OrderServiceSupport
					.getOrderNoTableNum(ordersDetail.getOrderNo()));
			ordersDetail.setTableNum(OrderServiceSupport
					.getOrderDetailTableNum(userReimburse.getUserId()));
			ordersDetails.add(ordersDetail);
			
			logger.info("----------------------------------updateAgreeUserUserReimburse 4  reimburseNo= "
					+  userReimburse.getReimburseNo()+",ordersDetails size="+ordersDetails.size());
		}

		return ordersDetails;

	}

	@Override
	public ReimburseResult<Integer> updateAgreeUserUserReimburse(
			UserReimburse userReimburse) {
		ReimburseResult<Integer> reimburseResult = new ReimburseResult<Integer>();
		List<String> orderBuinessLocks = new ArrayList<String>();
		Date startTime = new Date();
		logger.info("----------------------------------updateAgreeUserUserReimburse start  reimburseNo= "
				+ userReimburse.getReimburseNo()
				+ ",startTime="
				+ startTime
				+ ",orderNo="
				+ userReimburse.getOrderNo()
				+ ",orderDetailNos="
				+ userReimburse.getOrderDetailNos());

		String orderLock = REIMBUERSE_LOCK_PREFIX
				+ userReimburse.getReimburseNo();
		orderBuinessLocks.add(orderLock);
		try {

			// lock resource
			if (redisLockHandler.tryLock(orderLock)) {

				// checkRemibuerse
				List<OrdersDetail> ordersDetails = checkUserReimburseBefore(
						userReimburse, 1);
				
				logger.info("----------------------------------updateAgreeUserUserReimburse 0  reimburseNo= "
						+  userReimburse.getReimburseNo()+",ordersDetails size="+ordersDetails.size());
				for (OrdersDetail ordersDetail : ordersDetails) {

					Map map = new HashMap();
					map.put("orderDetailNo", ordersDetail.getOrderDetailNo());
					map.put("orderNo", ordersDetail.getOrderNo());
//					OrderServiceSchedule orderServiceSchedule = orderFulfillService
//							.selectModelOrderServiceSchedule(map);
//					orderServiceSchedule.setState((byte) 7);
//					orderServiceSchedule.setOrderDetailNo(ordersDetail
//							.getOrderDetailNo());
//					orderServiceSchedule.setOrderNo(ordersDetail.getOrderNo());
//					orderServiceSchedule.setTableNum(OrderServiceSupport
//							.getOrderNoTableNum(ordersDetail.getOrderNo()));
//					orderFulfillService
//							.updateOrderServiceSchedule(orderServiceSchedule);
				}
				// OrderResult<OrderDetailClocks> orderResult = orderService
				// .updateUserReimburseAndReleaseCoursePulicsLocked(ordersDetails);
				//
				// if (orderResult.getState() == 1) {
				PayResult<UserReimburse> payResult = selectUserReimburseByReimburseNo(userReimburse
						.getReimburseNo());

				if (ordersDetails.size()>0  && payResult.getContent().getState() == 4) {
					userReimburse.setId(payResult.getContent().getId());
					userReimburse.setState((byte) 2);
					userReimburse.setFininshTime(new Date());
					updateUserReimburse(userReimburse);
				} else {
					throw new UserReimburseException(
							UserReimburseState.REIMBURSENOFINISHED,
							userReimburse.getReimburseNo());

				}
				// reimburseReduceAssets(userReimburse,
				// orderResult.getContent());

				// try {
				// sendReimbureseMessage(userReimburse,
				// orderResult.getContent());
				//
				// } catch (Exception e) {
				// logger.error("-------------------------sendReimbureseMessage Exception e="+e.getMessage());
				// }
				// }

				// update userReimburse
				reimburseResult.setState(1);

			} else {
				logger.info("----------------------------------updateAgreeUserUserReimburse lock fail  reimburseNo= "
						+ userReimburse.getReimburseNo());
				throw new UserReimburseException(
						UserReimburseState.REIMBURSENOLOCKED, "");
			}

		} catch (UserReimburseException e) {
			logger.error("----------------------------------updateAgreeUserUserReimburse process  reimburseNo="
					+ userReimburse.getReimburseNo()
					+ "   UserReimburseException=" + e.toExecptionString());
			reimburseResult.setUserReimburseState(e.getUserReimburseState());
			reimburseResult.setUserReimburseStateContent(e
					.getUserReimburseStateContent());
			throw new UserReimburseException(e.getUserReimburseState(),
					e.getUserReimburseStateContent());
		} finally {
			for (String lock : orderBuinessLocks) {
				redisLockHandler.unLock(lock);
			}
		}
		Date endTime = new Date();
		logger.info("----------------------------------updateAgreeUserUserReimburse end  reimburseNo="
				+ userReimburse.getReimburseNo()
				+ ",endtime="
				+ endTime
				+ ",orderNo="
				+ userReimburse.getOrderNo()
				+ ",orderDetailNos="
				+ userReimburse.getOrderDetailNos()
				+ ", count time="
				+ (endTime.getTime() - startTime.getTime()) + " millisecond");

		return reimburseResult;
	}

	private void sendReimbureseMessage(UserReimburse userReimburse,
			OrderDetailClocks orderDetailClocks) {
		//
		// String smsTeacherKey = "teacher.sms.order.cancel";
		// MessageResult<SmsTemplate> messageResult = smsTemplateService
		// .selectSmsTemplate(smsTeacherKey);
		// int teacherCount = 0;
		//
		// for (Map.Entry<Integer, List<OrdersDetail>> entry : orderDetailClocks
		// .getTeacherOrdersDetailDealMap().entrySet()) {
		// TeacherAssets teacherAssets = teacherService
		// .selectTeacherAssets(entry.getKey());
		//
		// String courserContent = null;
		// for (OrdersDetail ordersDetail : entry.getValue()) {
		// if (courserContent == null)
		// courserContent = DateUtils.getPayOrderMDHMDate(
		// ordersDetail.getStartTime(),
		// ordersDetail.getEndTime());
		//
		// Map map = new HashMap();
		// map.put("cashFlowType", 1);
		// map.put("teacherId", ordersDetail.getTeacherId());
		// map.put("triggerNo", ordersDetail.getOrderDetailNo());
		// PayResult<TeacherCashFlow> payResult = cashFlowService
		// .selectTeacherCashFlow(map);
		// if (payResult.getState() == 1) {
		// teacherCount += payResult.getContent().getPrice();
		//
		// }
		// }
		//
		// if(entry.getValue().size()>1){
		// courserContent+="等";
		// }
		// }

		// String smsUserKey = "customer.sms.order.cancel";
		// MessageResult<SmsTemplate> messageResult = smsTemplateService
		// .selectSmsTemplate(smsUserKey);
		// String smsContent = MessageFormat.format(messageResult
		// .getContent().getTemplateValue(), new Object[] {
		// userReimburse.getPrice()/100.00, userReimburse.getOrderDetailNos()});
		// if (StringUtil.isNotEmpty(smsContent)) {
		// payOrderService.sendBaseMessage(smsContent,
		// userReimburse.getUserId());
		// }

	}

//	@Override
//	public void reimburseReduceAssets(UserReimburse userReimburse,
//			OrderDetailClocks orderDetailClocks) throws UserReimburseException {
//		int userCountSum = 0;
//
//		String triggerNos = "";
//
//		for (Map.Entry<Integer, List<OrdersDetail>> entry : orderDetailClocks
//				.getTeacherOrdersDetailDealMap().entrySet()) {
//			TeacherAssets teacherAssets = teacherService
//					.selectTeacherAssets(entry.getKey());
//			int teacherCount = 0;
//			for (OrdersDetail ordersDetail : entry.getValue()) {
//				Map map = new HashMap();
//				map.put("cashFlowType", 1);
//				map.put("teacherId", ordersDetail.getTeacherId());
//				map.put("triggerNo", ordersDetail.getOrderDetailNo());
//				PayResult<TeacherCashFlow> payResult = cashFlowService
//						.selectTeacherCashFlow(map);
//				if (payResult.getState() == 1) {
//					userCountSum += payResult.getContent().getSumPrice();
//
//					TeacherCashFlow teacherCashFlow = new TeacherCashFlow();
//					BeanUtils.copyProperties(payResult.getContent(),
//							teacherCashFlow);
//					teacherCashFlow.setId(null);
//					teacherCashFlow.setCashFlowType((byte) 2);
//					teacherCashFlow.setCashFlowTitle(teacherCashFlow
//							.getCashFlowTitle());
//
//					int freezeBalance = teacherAssets.getFreezeBalance()
//							- payResult.getContent().getPrice();
//					teacherCashFlow.setFreezeBalance(freezeBalance);
//					teacherCashFlow.setActiveBalance(teacherAssets
//							.getFreezeBalance());
//					teacherCashFlow.setCreateTime(null);
//					teacherAssets.setFreezeBalance(freezeBalance);
//					PayResult<TeacherCashFlow> payResultTeacherCashFlow = cashFlowService
//							.addTeacherCashFlow(teacherCashFlow);
//					triggerNos += payResultTeacherCashFlow.getContent()
//							.getCashFlowNo();
//					teacherCount += payResult.getContent().getPrice();
//
//				} else {
//					throw new UserReimburseException(
//							UserReimburseState.REIMBURSENOEXITS,
//							ordersDetail.getOrderDetailNo());
//				}
//			}
////			teacherAssets.setOrderFinish(teacherAssets.getOrderFinish() - 1);
//			teacherService.updateTeacherAssets(teacherAssets);
//		}
//
//		if (userReimburse.getPrice() <= userCountSum) {
//			UserResult<User> userResult = userVerifyService
//					.selectUserByUserId(userReimburse.getUserId());
//			UserIdentity userIdentity = new UserIdentity();
//			userIdentity.setPhone(userResult.getContent().getPhone());
//			// UserAssets userAssets = userExtService.selectUserAssets(
//			// userIdentity).getContent();
//			// userAssets.setBalance(balance)
//			UserCashFlow userCashFlow = new UserCashFlow();
//			userCashFlow.setUserId(userReimburse.getUserId());
//			userCashFlow.setPrice(userReimburse.getPrice());
//			userCashFlow.setCashFlowTitle("退款");
//			userCashFlow.setTriggerNo(triggerNos);
//			cashFlowService.addUserCashFlow(userCashFlow);
//		} else {
//			throw new UserReimburseException(
//					UserReimburseState.REIMBURSENOPRICEERROR, "");
//		}
//
//	}

	public static String getUserReimburseNoTableNum(String userReimburseNo) {
		String tableNum = userReimburseNo.substring(0, 4);
		return tableNum;
	}

	@Override
	public String userReimburseNoGenerator() {
		return UUIDGenerator.userReimburseNoGenerator(redisHelper);
	}
	@Override
	public PayResult<Integer> updateReimburse(UserReimburse userReimburse) {
		PayResult<Integer> payResult = new PayResult<>();
		Integer result = userReimburseMapper.updateByPrimaryKeySelective(userReimburse);
		payResult.setState(result);
		payResult.setContent(result);
		return payResult;
	}

	@Override
	public PayResult<UserReimburse> getReimburseModel(Map map) {
		PayResult<UserReimburse> userReimbursePayResult = new PayResult<>();
		UserReimburse userReimburse = userReimburseMapper.selectModel(map);
		userReimbursePayResult.setState(1);
		userReimbursePayResult.setContent(userReimburse);
		return userReimbursePayResult;
	}
}
