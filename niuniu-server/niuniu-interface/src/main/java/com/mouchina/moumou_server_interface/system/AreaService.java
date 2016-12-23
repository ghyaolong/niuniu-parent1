package com.mouchina.moumou_server_interface.system;

import java.util.List;
import java.util.Map;

import com.mouchina.moumou_server.entity.system.AreaData;

/**
 * 区域service
 * @author Administrator
 *
 */
public interface AreaService {

	/**
	 * 根据区域code查询区域信息
	 * @param areaCode
	 * @return
	 */
	AreaData selectAreaByCode(String areaCode);
	
	/**
	 * 根据市(辖区、直辖市)code查询区域信息
	 * @param areaCode
	 * @return
	 */
	AreaData selectCityByCode(String areaCode);
	
	/**
	 * 根据省份code查询区域信息
	 * @param areaCode
	 * @return
	 */
	AreaData selectProvinceByCode(String areaCode);
	
	/**
	 * 根据市(辖区、直辖市)code查询区域信息
	 * @param areaCode
	 * @return
	 */
	List<AreaData> selectAreaListByCode(Map map);
	
	/**
	 * 根据省份code查询市(辖区、直辖市)区域信息
	 * @param areaCode
	 * @return
	 */
	List<AreaData> selectCityListByCode(Map map);
	
	/**
	 * 查询省份区域信息
	 * 
	 * @return
	 */
	List<AreaData> selectProvinceListByCode();
	
}
