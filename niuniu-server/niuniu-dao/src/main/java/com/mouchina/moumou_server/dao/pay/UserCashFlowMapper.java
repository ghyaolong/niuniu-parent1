package com.mouchina.moumou_server.dao.pay;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.pay.UserCashFlow;

import java.util.Map;


public interface UserCashFlowMapper extends  BaseMapper<UserCashFlow, Long>{
	   UserCashFlow  selectByCashFlowNo(Map map);
	   Integer  deleteByCashFlowNo(Map map);
}