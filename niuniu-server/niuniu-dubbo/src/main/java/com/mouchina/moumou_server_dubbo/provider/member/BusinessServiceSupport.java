package com.mouchina.moumou_server_dubbo.provider.member;

import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.moumou_server.dao.member.BusinessMapper;
import com.mouchina.moumou_server.dao.member.BusinessTempMapper;
import com.mouchina.moumou_server.dao.system.SystemMessageMapper;
import com.mouchina.moumou_server.entity.member.Business;
import com.mouchina.moumou_server.entity.member.BusinessTemp;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserAssets;
import com.mouchina.moumou_server.entity.system.SystemMessage;
import com.mouchina.moumou_server_interface.member.BusinessService;
import com.mouchina.moumou_server_interface.member.UserAssetsService;
import com.mouchina.moumou_server_interface.member.UserVerifyService;
import com.mouchina.moumou_server_interface.view.UserResult;

public class BusinessServiceSupport implements BusinessService {

	private static final Logger logger = LoggerFactory.getLogger(BusinessServiceSupport.class);
	@Resource
	private BusinessMapper businessMapper;
	@Resource
	private UserAssetsService userAssetsService;
	@Resource
	private UserVerifyService userVerifyService;
	@Resource
	private BusinessTempMapper businessTempMapper;
	@Resource
	private BusinessService businessService;
	@Resource
	private SystemMessageMapper systemMessageMapper;

	@Override
	public UserResult<Business> selectBusiness(Long businessId) {
		// TODO Auto-generated method stub

		UserResult<Business> userResult = new UserResult<Business>();

		Business business = businessMapper.selectByPrimaryKey(businessId);
		if (business != null) {
			userResult.setContent(business);
			userResult.setState(1);
		}

		return userResult;
	}

	@Override
	public UserResult<Business> selectBusiness(Map map) {
		// TODO Auto-generated method stub
		UserResult<Business> businessResult = new UserResult<Business>();

		Business business = businessMapper.selectModel(map);
		if (business != null) {
			businessResult.setContent(business);
			businessResult.setState(1);
		}

		return businessResult;
	}

	@Override
	public UserResult<Business> addBusiness(Business business) {
		// TODO Auto-generated method stub
		UserResult<Business> businessResult = new UserResult<Business>();
		business.setCreateTime(new Date());
		int result = businessMapper.insertSelective(business);
		businessResult.setContent(business);
		businessResult.setState(result);
		logger.info("-----------插入后获取的主键值 : " + business.getId());
		return businessResult;
	}

	@Override
	public UserResult<ListRange<Business>> selectListBusinessPage(Map map) {
		// TODO Auto-generated method stub
		UserResult<ListRange<Business>> businessResult = new UserResult<ListRange<Business>>();
		ListRange<Business> listRange = new ListRange<Business>();
		int count = businessMapper.selectCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		listRange.setData(businessMapper.selectPageList(map));
		businessResult.setContent(listRange);
		businessResult.setState(1);
		return businessResult;
	}

	@Override
	public UserResult<Integer> deleteBusiness(Long businessId) {
		// TODO Auto-generated method stub
		UserResult<Integer> businessResult = new UserResult<Integer>();

		int result = businessMapper.deleteByPrimaryKey(businessId);
		businessResult.setState(result);

		return businessResult;
	}

	@Override
	public UserResult<Integer> updateBusiness(Business business) {
		// TODO Auto-generated method stub
		UserResult<Integer> businessResult = new UserResult<Integer>();
		business.setModfiyTime(new Date());
		int result = businessMapper.updateByPrimaryKeySelective(business);
		businessResult.setState(result);

		return businessResult;
	}

	@Override
	public int updateUserAssetsForCertifiedAward(Business business, Integer money) {

		UserAssets userAssets = userAssetsService.selectUserAssets(business.getUserId());
		UserResult<User> businessUser = userVerifyService.selectUserByUserId(business.getUserId());
		userAssets.setTableNum(businessUser.getContent().getTableNum());
		userAssets.setCashBalance(userAssets.getCashBalance() + money);

		UserResult<UserAssets> resultAssets = userVerifyService.updateUserAssets(userAssets);

		if (resultAssets != null) {
			if (resultAssets.getState() != 1) {
				return 0;
			} else {
				return 1;
			}
		} else {
			return 0;
		}
	}

	@Override
	public UserResult<Business> updateMultipleAuthentication(Business business) {
		// TODO Auto-generated method stub
		UserResult<Business> businessResult = new UserResult<Business>();
		business.setCreateTime(new Date());
		business.setState((byte) 1);
		business.setModfiyTime(null);
		int result = businessMapper.updateByPrimaryKeySelective(business);
		businessResult.setContent(business);
		businessResult.setState(result);
		logger.info("-----------更新后获取的主键值 : " + business.getId());
		return businessResult;
	}

	@Override
	public UserResult<BusinessTemp> addBusinessInTemp(BusinessTemp businessTemp) {
		UserResult<BusinessTemp> businessResult = new UserResult<BusinessTemp>();
		businessTemp.setCreateTime(new Date());
		businessTemp.setModfiyTime(new Date());
		// 新增认证数据状态初始化1
		businessTemp.setState((byte) 1);
		int result = businessTempMapper.insertSelective(businessTemp);
		businessResult.setContent(businessTemp);
		businessResult.setState(result);
		logger.info("-----------插入后获取的主键值 : " + businessTemp.getId());
		return businessResult;
	}

	@Override
	public BusinessTemp selectBussinessInTemp(Long id) {
		// TODO Auto-generated method stub
		return businessTempMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateBussinessInTemp(BusinessTemp businessTemp) {
		// TODO Auto-generated method stub

		return businessTempMapper.updateBusinessTemp(businessTemp);
	}

	@Override
	public UserResult<ListRange<BusinessTemp>> selectListBusinessTempPage(Map map) {
		UserResult<ListRange<BusinessTemp>> businessResult = new UserResult<ListRange<BusinessTemp>>();
		ListRange<BusinessTemp> listRange = new ListRange<BusinessTemp>();
		int count = businessTempMapper.selectBussinessInTempCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		listRange.setData(businessTempMapper.selectPageListInTemp(map));
		businessResult.setContent(listRange);
		businessResult.setState(1);
		return businessResult;
	}

	@Override
	public int addAuditSuccess(UserResult<Business> businessResult, BusinessTemp businessTemp) {
		// TODO Auto-generated method stub
		int result = 0;
		businessTemp.setState((byte)2);
		Business business = new Business();
		BeanUtils.copyProperties(businessTemp, business);
		// business表中有数据 修改操作
		if (businessResult.getState() == 1) {
			// 修改正式表数据
			result = businessMapper.updateByUserId(business);
			if (result == 1) {
				// 修改中间表数据
				result = businessTempMapper.updateByPrimaryKeySelective(businessTemp);
			}
		} else {
			// 新增正式表数据
			result = businessMapper.insertSelective(business);
			if (result == 1) {
				// 修改中间表数据
				result = businessTempMapper.updateByPrimaryKeySelective(businessTemp);
			}
		}
		if (result == 1) {
			SystemMessage systemMessage = new SystemMessage();
			systemMessage.setCreateTime(new Date());
			systemMessage.setUserId(businessTemp.getUserId());
			if (businessTemp.getCertificationType() == 1) {
				systemMessage.setContent("恭喜您，已经通过了个人认证审核。");
			} else {
				systemMessage.setContent("恭喜您，已经通过了商户认证审核，快去创建您的主页吧。");
			}
			// 发送系统消息
			result = systemMessageMapper.insertSelective(systemMessage);
		}

		return result;
	}

	@Override
	public BusinessTemp selectBussinessTempByUserId(Long userId) {
		// TODO Auto-generated method stub
		return businessTempMapper.selectBussinessInTemp(userId);
	}
}
