package com.mouchina.moumou_server_interface.order;

import java.util.List;
import java.util.Map;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.order.RechargeConfig;

public interface RechargeConfigService{

	/**
	 * 根据充值金额更新充值配置信息
	 * @param money
	 * @return
	 */
	Integer updateRechargeConfigByMoney(Integer money);
	/**
	 * 根据主键查询记录
	 * @param id
	 * @return
	 */
	RechargeConfig selectByPK(long id);
	
	ListRange<RechargeConfig> selectListRangeRechargePage(Map map);
	
	public List<RechargeConfig> selectListRecharge(Map map);
	
}
