package com.mouchina.moumou_server_interface.order;

import java.util.List;
import java.util.Map;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.order.UserLuckyBagDetails;
import com.mouchina.moumou_server.entity.vo.UserLuckyBagDetailsVo;

/**
 * 用户福袋明细服务
 * @author Administrator
 *
 */
public interface UserLuckyBagDetailsService {

	/**
	 * 统计用户福袋明细信息(管理系统使用)
	 * @param map
	 * @return
	 */
	ListRange<UserLuckyBagDetails> selectListUserLuckyBagDetails(Map map);
	
	/**
	 * 导出用户福袋明细信息(管理系统使用)
	 * @param map
	 * @return
	 */
	List<UserLuckyBagDetailsVo> exportListUserLuckyBagDetails(Map map);
	
}
