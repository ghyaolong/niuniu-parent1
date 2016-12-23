package com.mouchina.moumou_server_interface.order;

import java.util.List;
import java.util.Map;

import com.mouchina.moumou_server.entity.order.WithDrawalConfig;

public interface WithDrawalConfigService {

	List<WithDrawalConfig> selectByList(Map map);
}
