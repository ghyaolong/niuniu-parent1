package com.mouchina.admin.service.impl.income;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mouchina.admin.service.api.income.UserIncomeAdminService;
import com.mouchina.moumou_server_interface.advert.AdvertService;
import com.mouchina.moumou_server_interface.income.UserIncomeService;

@Service
public class UserIncomeAdminServiceSupport  implements UserIncomeAdminService{



	@Resource
	UserIncomeService  userIncomeService;
	
	@Resource
	AdvertService  advertService;

	@Override
	public int insertCalUserIncomeByDay(Date date) {
		// TODO Auto-generated method stub
		
		
		
		
		return userIncomeService.insertCalUserIncomeByDay(date);
	}



	


	

}
