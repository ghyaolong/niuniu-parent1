package com.mouchina.web.service.impl.income;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.income.UserIncome;
import com.mouchina.moumou_server.entity.income.UserIncomeSum;
import com.mouchina.moumou_server_interface.income.UserIncomeService;
import com.mouchina.moumou_server_interface.view.IncomeResult;
import com.mouchina.web.service.api.income.UserIncomeWebService;

@Service
public class UserIncomeWebServiceSupport implements UserIncomeWebService {

	@Resource
	UserIncomeService userIncomeService;

	@Override
	public IncomeResult<ListRange<UserIncomeSum>> selectUserIncomeSumByDay(Map map) {
		return userIncomeService.selectListUserIncomeSumPage(map);
	}

	@Override
	public IncomeResult<ListRange<UserIncome>> selectUserIncome(Map map) {
		return userIncomeService.selectListUserIncomePage(map);
	}

	@Override
	public IncomeResult<UserIncomeSum> selectUserIncomeSum(long userIncomeSumId) {
		return userIncomeService.selectUserIncomeSum(userIncomeSumId);
	}

	@Override
	public int selectUserIncomeSumCount(Map map) {
		return userIncomeService.selectUserIncomeSumCount(map);
	}

}
