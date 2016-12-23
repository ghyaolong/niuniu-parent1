package com.mouchina.moumou_server_dubbo.provider.order;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.moumou_server.dao.order.RechargeConfigMapper;
import com.mouchina.moumou_server.entity.order.RechargeConfig;
import com.mouchina.moumou_server_interface.order.RechargeConfigService;

public class RechargeConfigServiceSupport implements RechargeConfigService {

	@Resource
	private RechargeConfigMapper rechargeConfigMapper;
	@Override
	public Integer updateRechargeConfigByMoney(Integer money) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ListRange<RechargeConfig> selectListRangeRechargePage(Map map) {
		ListRange<RechargeConfig> listRange = new ListRange<RechargeConfig>();

		int count = rechargeConfigMapper.selectCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		List<RechargeConfig> rechargeConfigs = rechargeConfigMapper.selectPageList(map);
		listRange.setData(rechargeConfigs);
		return listRange;
	}

	@Override
	public List<RechargeConfig> selectListRecharge(Map map) {
		// TODO Auto-generated method stub
		return rechargeConfigMapper.selectList(map);
	}
	
	public RechargeConfig selectByPK(long id) {
		// TODO Auto-generated method stub
		return rechargeConfigMapper.selectByPrimaryKey(id);
	}

}
