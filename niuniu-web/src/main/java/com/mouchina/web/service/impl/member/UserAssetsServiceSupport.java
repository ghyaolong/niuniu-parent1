package com.mouchina.web.service.impl.member;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.member.UserAssets;
import com.mouchina.moumou_server.entity.order.WithdrawalHistoryOrder;
import com.mouchina.moumou_server_interface.member.UserAssetsService;
import com.mouchina.moumou_server_interface.order.WithdrawalsService;
import com.mouchina.moumou_server_interface.pay.ReimburseService;
import com.mouchina.moumou_server_interface.view.UserAssetsView;
import com.mouchina.moumou_server_interface.view.WelfareResult;
import com.mouchina.web.base.utils.Constants;
import com.mouchina.web.service.api.member.UserAssetsAdminService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by douzy on 16/2/18.
 */
@Service
public class UserAssetsServiceSupport implements UserAssetsAdminService {
	private final static Logger logger = LoggerFactory.getLogger(UserAssetsServiceSupport.class);
	@Resource
	UserAssetsService userAssetsService;
	@Resource
	WithdrawalsService withdrawalsService;
	@Resource
	ReimburseService reimburseService;

	@Override
	public ListRange<UserAssetsView> selectUserAssets(Map<String, Object> map) {
		return userAssetsService.selectUserAssets(map).getContent();
	}

	
	@Override
	public WelfareResult<WithdrawalHistoryOrder> applyUserWithdeawal(
			Long userId, Integer price, String account, String accountName,
			Integer type, Byte channel) {
		WelfareResult<WithdrawalHistoryOrder> welfareResult = new WelfareResult<WithdrawalHistoryOrder>();
		
		UserAssets userAssets = userAssetsService.selectUserAssets(userId);

		Map searchMap = new HashMap();
		searchMap.put("userId", userId);
		
		final Integer balance = userAssets.getRedEnvelopeBalance();//余额
		if (price > balance) {
			//提现余额不足
			welfareResult.setState(Constants.ERROR_CODE_200501);
			return welfareResult;
		}
		if(balance < 500){
			welfareResult.setState(Constants.ERROR_CODE_200502);
			return welfareResult;
		}

		WithdrawalHistoryOrder withdrawalHistoryOrder = new WithdrawalHistoryOrder();
		withdrawalHistoryOrder
				.setWithdrawlHistoryOrderNo(userReimburseNoGenerator());//提现单号
		withdrawalHistoryOrder.setCreateTime(new Date());
		withdrawalHistoryOrder.setState((byte) 1);//1 提交 2 完成  3 失败 4 提现中
		withdrawalHistoryOrder.setWithdrawalChannel(channel);// 提现渠道1默认支付宝2微信
		withdrawalHistoryOrder.setAccount(account);//微信账户，对应微信openId
		withdrawalHistoryOrder.setAccountName(accountName);//微信名
		withdrawalHistoryOrder.setPrice(price);
		withdrawalHistoryOrder.setUserId(userAssets.getUserId());
		withdrawalHistoryOrder.setFreezeBalance(balance - price);
		withdrawalHistoryOrder.setType(Byte.valueOf("1"));// 只支持 红包提现
		 WelfareResult<WithdrawalHistoryOrder> welfareResultAdd = withdrawalsService.addWithdrawalHistoryOrder(
				withdrawalHistoryOrder);
		welfareResult.setContent(welfareResultAdd.getContent());
		if (welfareResultAdd.getState() == -1) {
			
			welfareResult.setState(Constants.ERROR_CODE_200501);
		}else{
			//扣掉用户余额start
//			logger.error("not in add修改用户余额!");
//			userAssets.setRedEnvelopeBalance(balance - price);
//			userAssetsService.updateUserAssets(userAssets);
			//扣掉用户余额end
			welfareResult.setState(welfareResultAdd.getState());
		}

		return welfareResult;
	}

	public String userReimburseNoGenerator() {
		return reimburseService.userReimburseNoGenerator();
	}

	@Override
	public Integer selectApplyWithdrawalOrderCount(Map map) {
		return withdrawalsService.selectApplyWithdrawalOrderCount(map);
	}
}
