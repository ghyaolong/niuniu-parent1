package com.mouchina.moumou_server_dubbo.provider.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.redis.RedisHelper;
import com.mouchina.base.redis.RedisLockHandler;
import com.mouchina.moumou_server.dao.member.UserPartMapper;
import com.mouchina.moumou_server.dao.order.WithdrawalHistoryOrderMapper;
import com.mouchina.moumou_server.entity.member.UserAssets;
import com.mouchina.moumou_server.entity.member.UserPart;
import com.mouchina.moumou_server.entity.order.WithdrawalHistoryOrder;
import com.mouchina.moumou_server.entity.pay.PayOrder;
import com.mouchina.moumou_server_dubbo.provider.advert.AdvertServiceSupport;
import com.mouchina.moumou_server_interface.member.UserAssetsService;
import com.mouchina.moumou_server_interface.member.UserVerifyService;
import com.mouchina.moumou_server_interface.order.WithdrawalsService;
import com.mouchina.moumou_server_interface.view.OrderShowView;
import com.mouchina.moumou_server_interface.view.WelfareResult;
import com.mouchina.moumou_server_interface.view.WithdrawalView;

/**
 * Created by douzy on 16/2/18.
 */
public class WithdrawalsServiceSupport implements WithdrawalsService {
	@Resource
	private WithdrawalHistoryOrderMapper withdrawalHistoryOrderMapper;
	@Resource
	private UserAssetsService userAssetsService;
	@Resource
	UserPartMapper userPartMapper;
	@Resource
	UserVerifyService userVerifyService;

	@Resource
	RedisHelper redisHelper;

	@Resource
	RedisLockHandler redisLockHandler;

	private static String USER_WITHDRAWAL_LOCK = "User_Withdrawal_Lock_";

	private static final Logger logger = LoggerFactory
			.getLogger(WithdrawalsServiceSupport.class);
	
	@Override
	public WelfareResult<WithdrawalHistoryOrder> addWithdrawalHistoryOrder(
			WithdrawalHistoryOrder withdrawalHistoryOrder) {
		WelfareResult<WithdrawalHistoryOrder> welfareResult = new WelfareResult<WithdrawalHistoryOrder>();

		List<String> advertAddLocks = new ArrayList<String>();
		try {

			String advertLock = USER_WITHDRAWAL_LOCK
					+ withdrawalHistoryOrder.getUserId();
			advertAddLocks.add(advertLock);

			if (redisLockHandler.tryLock(advertLock)) {
				UserAssets userAssets = userAssetsService
						.selectUserAssets(withdrawalHistoryOrder.getUserId());
				if (userAssets != null) {

//					Integer balance = withdrawalHistoryOrder.getType() == 1 ? userAssets
//							.getRedEnvelopeBalance() : userAssets
//							.getCashBalance();
					Integer balance = userAssets.getRedEnvelopeBalance();//用户余额
					if (withdrawalHistoryOrder.getPrice() <= balance) {
						withdrawalHistoryOrderMapper
								.insertSelective(withdrawalHistoryOrder);
						welfareResult.setContent(withdrawalHistoryOrder);
logger.error("add --------------------------------修改用户余额!");
						userAssets.setRedEnvelopeBalance(userAssets
								.getRedEnvelopeBalance()
								- withdrawalHistoryOrder.getPrice());

						// userAssets.setPriceCount(userAssets.getPriceCount() -
						// welfareResult.getContent().getPrice());
						Map partMap = new HashMap();
						partMap.put("mapprerValue", userAssets.getUserId());
						UserPart userPart = userPartMapper.selectModel(partMap);

						userAssets.setTableNum(userPart.getNum());

						userAssetsService.updateUserAssets(userAssets);
						welfareResult.setState(1);
					} else {
						welfareResult.setState(-1);
					}
				}
			}
		} catch (Exception e) {

			logger.error(e.getMessage());
		} finally {
			for (String lock : advertAddLocks) {
				redisLockHandler.unLock(lock);
			}
		}
		return welfareResult;
	}

	@Override
	public WelfareResult<WithdrawalHistoryOrder> selectWithdrawalHistoryOrder(
			Long withdrawalHistoryOrderId) {
		WelfareResult<WithdrawalHistoryOrder> welfareResult = new WelfareResult<WithdrawalHistoryOrder>();

		WithdrawalHistoryOrder withdrawalHistoryOrder = withdrawalHistoryOrderMapper
				.selectByPrimaryKey(withdrawalHistoryOrderId);
		if (withdrawalHistoryOrder != null) {
			welfareResult.setContent(withdrawalHistoryOrder);
			welfareResult.setState(1);
		}
		return welfareResult;
	}

	@Override
	public WelfareResult<Integer> updateWithdrawalHistoryOrder(
			WithdrawalHistoryOrder withdrawalHistoryOrder) {
		WelfareResult<Integer> welfareResult = new WelfareResult<Integer>();
		welfareResult.setState(withdrawalHistoryOrderMapper
				.updateByPrimaryKeySelective(withdrawalHistoryOrder));
		return welfareResult;
	}

	@Override
	public WelfareResult<Integer> deleteWithdrawalHistoryOrder(
			Long withdrawalHistoryOrderId) {
		WelfareResult<Integer> welfareResult = new WelfareResult<Integer>();
		welfareResult.setState(withdrawalHistoryOrderMapper
				.deleteByPrimaryKey(withdrawalHistoryOrderId));
		return welfareResult;
	}

	@Override
	public WelfareResult<ListRange<WithdrawalView>> selectListRangeWithdrawalHistory(
			Map map) {
		WelfareResult<ListRange<WithdrawalView>> welfareResult = new WelfareResult<ListRange<WithdrawalView>>();
		ListRange<WithdrawalView> listRange = new ListRange<WithdrawalView>();
		int count = withdrawalHistoryOrderMapper.selectCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);

		List<WithdrawalHistoryOrder> withdrawalHistoryOrders = withdrawalHistoryOrderMapper
				.selectPageList(map);
		List<WithdrawalView> withdrawalViews = new ArrayList<>();
		for (WithdrawalHistoryOrder withdrawalHistoryOrder : withdrawalHistoryOrders) {
			WithdrawalView withdrawalView = new WithdrawalView();
			withdrawalView.setWithdrawalHistoryOrder(withdrawalHistoryOrder);
			withdrawalView.setUser(userVerifyService.selectUserByUserId(
					Long.valueOf(withdrawalHistoryOrder.getUserId()))
					.getContent());
			withdrawalViews.add(withdrawalView);
		}
		listRange.setPage(page);
		listRange.setData(withdrawalViews);
		welfareResult.setContent(listRange);
		return welfareResult;
	}

	@Override
	public WelfareResult<ListRange<WithdrawalView>> selectListRangeWithdrawalHistoryByPager(
			Map map) {
		WelfareResult<ListRange<WithdrawalView>> welfareResult = new WelfareResult<ListRange<WithdrawalView>>();
		ListRange<WithdrawalView> listRange = new ListRange<WithdrawalView>();
		int count = withdrawalHistoryOrderMapper.selectCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		List<WithdrawalView> withdrawalViews = new ArrayList<>();
		List<WithdrawalHistoryOrder> withdrawalHistoryOrderList = withdrawalHistoryOrderMapper
				.selectPageList(map);
		for (WithdrawalHistoryOrder withdrawalHistoryOrder : withdrawalHistoryOrderList) {
			// Teacher teacher =
			// teacherService.selectTeacher(withdrawalHistoryOrder.getTeacherId());
			WithdrawalView withdrawalView = new WithdrawalView();
			withdrawalView.setWithdrawalHistoryOrder(withdrawalHistoryOrder);
			// withdrawalView.setTeacher(teacher);
			withdrawalViews.add(withdrawalView);
		}
		listRange.setData(withdrawalViews);
		welfareResult.setContent(listRange);
		return welfareResult;
	}

	@Override
	public WelfareResult<WithdrawalHistoryOrder> selectWithdrawalOrderModel(
			Map map) {
		WithdrawalHistoryOrder withdrawalHistoryOrder = withdrawalHistoryOrderMapper
				.selectModel(map);
		WelfareResult<WithdrawalHistoryOrder> withdrawalHistoryOrderWelfareResult = new WelfareResult<WithdrawalHistoryOrder>();
		withdrawalHistoryOrderWelfareResult
				.setState(withdrawalHistoryOrder == null ? 0 : 1);
		withdrawalHistoryOrderWelfareResult.setContent(withdrawalHistoryOrder);
		return withdrawalHistoryOrderWelfareResult;
	}

	/**
	 * 查询当前月老师申请提现次数
	 * 
	 * @param map
	 * @return
	 */
	@Override
	public Integer selectApplyWithdrawalOrderCount(Map map) {
		return withdrawalHistoryOrderMapper
				.selectApplyWithdrawalOrderCount(map);
	}
}
