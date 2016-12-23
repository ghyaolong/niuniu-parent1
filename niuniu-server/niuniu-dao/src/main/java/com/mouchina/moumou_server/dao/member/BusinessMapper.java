package com.mouchina.moumou_server.dao.member;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.member.Business;

public interface BusinessMapper  extends BaseMapper<Business,Long> {
	int updateByUserId(Business business);
}