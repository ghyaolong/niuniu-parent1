package com.mouchina.moumou_server.dao.member;

import java.util.List;
import java.util.Map;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.member.BusinessShop;

public interface BusinessShopMapper extends BaseMapper<BusinessShop, Long> {

	//查询商铺临时表信息
	@SuppressWarnings("rawtypes")
	BusinessShop selectModelTemp(Map map);
	
	BusinessShop selectByPrimaryKeyTemp(Long id);
	
	BusinessShop selectByBusinessIdTemp(Long id);
	
	int updateByBusinessId(BusinessShop businessShop);
	
	int updateByBusinessIdTemp(BusinessShop businessShop);
	
	int insertSelectiveTemp(BusinessShop businessShop);
	
	int selectCountTemp(Map<String, Object> map);
	
	List<BusinessShop> selectPageListTemp(Map<String, Object> map);
	
	int updateByPrimaryKeySelectiveTemp(BusinessShop businessShop);
	
}
