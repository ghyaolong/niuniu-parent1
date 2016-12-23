package com.mouchina.moumou_server_dubbo.provider.system;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.mouchina.moumou_server.dao.system.AreaDataMapper;
import com.mouchina.moumou_server.entity.system.AreaData;
import com.mouchina.moumou_server_interface.system.AreaService;

/**
 * 区域信息service实现类
 * @author Administrator
 *
 */
public class AreaServiceSupport implements AreaService {

	@Resource
	private AreaDataMapper areaDataMapper;
	
	@Override
	public AreaData selectAreaByCode(String areaCode) {
		return areaDataMapper.selectAreaByCode(areaCode);
	}

	@Override
	public AreaData selectCityByCode(String areaCode) {
		return areaDataMapper.selectCityByCode(areaCode);
	}

	@Override
	public AreaData selectProvinceByCode(String areaCode) {
		return areaDataMapper.selectProvinceByCode(areaCode);
	}

	@Override
	public List<AreaData> selectAreaListByCode(Map map) {
		return areaDataMapper.selectAreaListByCode(map);
	}

	@Override
	public List<AreaData> selectCityListByCode(Map map) {
		return areaDataMapper.selectCityListByCode(map);
	}

	@Override
	public List<AreaData> selectProvinceListByCode() {
		return areaDataMapper.selectProvinceListByCode();
	}

}
