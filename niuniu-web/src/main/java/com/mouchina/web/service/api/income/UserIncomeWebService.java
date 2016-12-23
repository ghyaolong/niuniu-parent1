package com.mouchina.web.service.api.income;

import java.util.Map;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.income.UserIncome;
import com.mouchina.moumou_server.entity.income.UserIncomeSum;
import com.mouchina.moumou_server_interface.view.IncomeResult;

public interface UserIncomeWebService {
	
	
	IncomeResult<ListRange<UserIncomeSum>> selectUserIncomeSumByDay(Map map);
	
	IncomeResult<ListRange<UserIncome>> selectUserIncome(Map map);
	IncomeResult<UserIncomeSum> selectUserIncomeSum(long userIncomeSumId);
	int selectUserIncomeSumCount(Map map);
}
