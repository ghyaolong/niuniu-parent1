package com.mouchina.moumou_server.dao.system;

import java.util.List;
import java.util.Map;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.system.AreaData;

public interface AreaDataMapper extends BaseMapper<AreaData, Long> {

	AreaData selectAreaByCode(String areaCode);
	
	AreaData selectCityByCode(String areaCode);
	
	AreaData selectProvinceByCode(String areaCode);
	
	List<AreaData> selectAreaListByCode(Map map);
	
	List<AreaData> selectCityListByCode(Map map);
	
	List<AreaData> selectProvinceListByCode();
	
}
