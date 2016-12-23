package com.mouchina.moumou_server_dubbo.provider.order;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.mouchina.moumou_server.dao.order.WithDrawalConfigMapper;
import com.mouchina.moumou_server.entity.order.WithDrawalConfig;
import com.mouchina.moumou_server_interface.order.WithDrawalConfigService;

public class WithDrawalConfigServiceSupport implements WithDrawalConfigService {
	@Resource
	WithDrawalConfigMapper withDrawalConfigMapper;
	@Override
	public List<WithDrawalConfig> selectByList(Map map) {
		// TODO Auto-generated method stub
		return withDrawalConfigMapper.selectList(map);
	}

}
