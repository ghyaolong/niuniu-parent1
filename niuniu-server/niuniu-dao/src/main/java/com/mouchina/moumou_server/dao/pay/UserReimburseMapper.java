package com.mouchina.moumou_server.dao.pay;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.pay.UserReimburse;

import java.util.Map;

public interface UserReimburseMapper extends BaseMapper<UserReimburse,Long> {
	  Integer  deleteByUserReimburseNo(Map map);
}