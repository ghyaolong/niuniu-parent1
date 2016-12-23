package com.mouchina.moumou_server.dao.order;

import java.util.List;
import java.util.Map;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.order.UserTransactionDetails;
import com.mouchina.moumou_server.entity.vo.UserTransactionDetailsVo;

public interface UserTransactionDetailsMapper extends BaseMapper<UserTransactionDetails, Long> {

	List<UserTransactionDetails> selectPageList(Map map);
	
	List<UserTransactionDetailsVo> exportPageList(Map map);
	
	int selectCount(Map map);
}
