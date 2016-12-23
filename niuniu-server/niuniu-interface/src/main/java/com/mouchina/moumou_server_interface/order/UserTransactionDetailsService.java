package com.mouchina.moumou_server_interface.order;

import java.util.List;
import java.util.Map;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.order.UserTransactionDetails;
import com.mouchina.moumou_server.entity.vo.UserTransactionDetailsVo;

/**
 * 用户交易明细服务
 * @author Administrator
 *
 */
public interface UserTransactionDetailsService {

	/**
	 * 统计用户交易明细信息(管理系统使用)
	 * @param map
	 * @return
	 */
	ListRange<UserTransactionDetails> selectListUserTransactionDetails(Map map);
	
	/**
	 * 导出用户交易明细信息(管理系统使用)
	 * @param map
	 * @return
	 */
	List<UserTransactionDetailsVo> exportListUserTransactionDetails(Map map);
	
}
