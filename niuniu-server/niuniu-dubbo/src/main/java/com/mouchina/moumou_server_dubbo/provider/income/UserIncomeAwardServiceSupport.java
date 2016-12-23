package com.mouchina.moumou_server_dubbo.provider.income;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mouchina.moumou_server.dao.income.UserIncomeAwardMapper;
import com.mouchina.moumou_server.entity.income.UserIncomeAward;
import com.mouchina.moumou_server_interface.income.UserIncomeAwardService;
import com.mouchina.moumou_server_interface.view.UserResult;

public class UserIncomeAwardServiceSupport implements UserIncomeAwardService {
	
	private static final Logger logger = LoggerFactory
			.getLogger(UserIncomeAwardServiceSupport.class);

	@Resource
	private UserIncomeAwardMapper userIncomeAwardMapper;
	
	@Override
	public UserResult<UserIncomeAward> addIncomeAward(UserIncomeAward award) {
		UserResult<UserIncomeAward> incomeAwardResult = new UserResult<UserIncomeAward>();
		award.setCreateTime(new Date());
		int result = userIncomeAwardMapper.insertSelective(award);
		incomeAwardResult.setContent(award);
		incomeAwardResult.setState(result);
		logger.info("-----------插入后获取的主键值 : " + award.getId());
		return incomeAwardResult;
	}
	
	@Override
	public UserIncomeAward selectUserIncomeAward(Map map) {
		return userIncomeAwardMapper.selectModel(map);
	}


}
