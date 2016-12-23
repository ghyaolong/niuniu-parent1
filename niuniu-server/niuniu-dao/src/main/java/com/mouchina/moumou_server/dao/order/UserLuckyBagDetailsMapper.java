package com.mouchina.moumou_server.dao.order;

import java.util.List;
import java.util.Map;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.order.UserLuckyBagDetails;
import com.mouchina.moumou_server.entity.vo.UserLuckyBagDetailsVo;

public interface UserLuckyBagDetailsMapper extends BaseMapper<UserLuckyBagDetails, Long> {

	List<UserLuckyBagDetails> selectPageList(Map map);
	
	List<UserLuckyBagDetailsVo> exportPageList(Map map);
	
	int selectCount(Map map);
	
}
