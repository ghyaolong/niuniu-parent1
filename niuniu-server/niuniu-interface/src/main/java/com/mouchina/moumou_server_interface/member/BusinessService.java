package com.mouchina.moumou_server_interface.member;

import java.util.Map;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.member.Business;
import com.mouchina.moumou_server.entity.member.BusinessTemp;
import com.mouchina.moumou_server_interface.view.UserResult;

public interface BusinessService {

	UserResult<Business> selectBusiness(Long businessId);
	UserResult<Business> selectBusiness(Map<String,Object> map);

	UserResult<Business> addBusiness(Business business);

	UserResult<ListRange<Business>> selectListBusinessPage(Map<String,Object> map);

	UserResult<Integer> deleteBusiness(Long businessId);

	UserResult<Integer> updateBusiness(Business business);
	/**
	 * 奖励认证商户的User指定的钱数
	 * @param business 认证的商户对象
	 * @param money 赠与的钱数(传入分为单位)
	 * @return 0代表赠与失败，1代表赠与成功
	 */
	int updateUserAssetsForCertifiedAward(Business business,Integer money);
	
	/**
	 * 商户(个人)多次认证
	 * @param business
	 * @return
	 */
	UserResult<Business> updateMultipleAuthentication(Business business);
	
	/**
	 * 认证商户（个人）信息
	 * 向临时表中添加认证信息
	 * @param business
	 * @return
	 */
	UserResult<BusinessTemp> addBusinessInTemp(BusinessTemp businessTemp);
	/**
     * 查询临时表中商户（个人）信息
     * @param business
     * @return
     */
	BusinessTemp selectBussinessInTemp(Long userId);
    /**
     * 商户/个人 多次认证(3.0版本)
     * @param business
     * @return
     */
    int updateBussinessInTemp(BusinessTemp businessTemp);
    /**
     * 分页查询中间表数据（后台管理系统使用）
     * @param map
     * @return
     */
    UserResult<ListRange<BusinessTemp>> selectListBusinessTempPage(Map<String,Object> map);
   
    /**
     * 审核通过认证信息（后台管理系统使用）
     * @param businessResult
     * @param businessTemp
     * @param content
     * @return
     */
    int addAuditSuccess(UserResult<Business> businessResult,BusinessTemp businessTemp);
    
    BusinessTemp selectBussinessTempByUserId(Long userId);
}
