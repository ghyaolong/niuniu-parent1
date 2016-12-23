package com.mouchina.moumou_server.dao.order;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.order.WithdrawalHistoryOrder;

import java.util.Map;

/**
 * 提现明细
 * 
 * @author Administrator
 *
 */
public interface WithdrawalHistoryOrderMapper extends BaseMapper<WithdrawalHistoryOrder, Long> {

	public Integer selectApplyWithdrawalOrderCount(Map<String, Object> map);

}