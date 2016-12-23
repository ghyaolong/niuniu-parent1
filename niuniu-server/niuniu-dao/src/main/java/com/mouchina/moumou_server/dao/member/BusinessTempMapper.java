package com.mouchina.moumou_server.dao.member;

import java.util.List;
import java.util.Map;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.member.BusinessTemp;

public interface BusinessTempMapper extends BaseMapper<BusinessTemp,Long> {
   /**
	 * 修改business表
	 * @param businessTemp
	 * @return
	 */
	 int updateBussiness(BusinessTemp businessTemp);
	/**
	 * 新增business表数据
	 * @param businessTemp
	 * @return
	 */
	int insertBusiness(BusinessTemp businessTemp);
	
	/**
     * 查询中间表中商户（个人）信息
     * @param business
     * @return
     */
	BusinessTemp selectBussinessInTemp(Long userId);
	/**
     * 查询中间表中商户（个人）信息总数
     * @param business
     * @return
     */
	int selectBussinessInTempCount(Map map);
	/**
	 * 分页查询中间表信息
	 * @param map
	 * @return
	 */
	List<BusinessTemp> selectPageListInTemp(Map map);
	
	int updateBusinessTemp(BusinessTemp businessTemp);
}