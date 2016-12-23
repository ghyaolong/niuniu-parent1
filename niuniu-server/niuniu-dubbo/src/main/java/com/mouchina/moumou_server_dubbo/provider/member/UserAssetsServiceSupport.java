package com.mouchina.moumou_server_dubbo.provider.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.utils.MD5Util;
import com.mouchina.base.utils.StringUtil;
import com.mouchina.moumou_server.dao.member.UserAssetsMapper;
import com.mouchina.moumou_server.dao.member.UserMapper;
import com.mouchina.moumou_server.dao.member.UserPartMapper;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserAssets;
import com.mouchina.moumou_server.entity.member.UserPart;
import com.mouchina.moumou_server_interface.member.UserAssetsService;
import com.mouchina.moumou_server_interface.view.UserAssetsView;
import com.mouchina.moumou_server_interface.view.UserResult;

/**
 * 用户财富业务
 * @author Administrator
 *
 */
public class UserAssetsServiceSupport implements UserAssetsService {

	@Resource
	private UserAssetsMapper userAssetsMapper;
	@Resource
	private UserMapper userMapper;
	@Resource
	private UserPartMapper userPartMapper;

	@Override
	public UserResult<Integer> updateUserAssets(UserAssets userAssets) {
		UserResult<Integer> userResult = new UserResult<Integer>();
		userAssets.setWithdrawPassword(null);
		userResult.setState(userAssetsMapper.updateByPrimaryKeySelective(userAssets));
		return userResult;
	}

	@Override
	public UserResult<Integer> updateUserAssetsWithdrawPassword(Long userId, String withdrawPassword) {
		UserResult<Integer> userResult = new UserResult<Integer>();
		UserAssets userAssets = new UserAssets();
		userAssets.setUserId(Long.valueOf(userId));
		userAssets.setWithdrawPassword(MD5Util.md5Hex(withdrawPassword));
		userResult.setState(userAssetsMapper.updateByUserId(userAssets));

		return userResult;
	}

	@Override
	public UserResult<Integer> checkUserAssetsWithdrawPassword(Long userId, String withdrawPassword) {

		UserResult<Integer> userResult = new UserResult<Integer>();
		UserAssets teacherAssets = selectUserAssets(userId);
		if (StringUtil.isNotEmpty(withdrawPassword)
				&& teacherAssets.getWithdrawPassword().equals(MD5Util.md5Hex(withdrawPassword))) {
			userResult.setState(1);
		}

		return userResult;
	}

	@Override
	public int selectUserAssetsCount(Map map) {
		return userAssetsMapper.selectCount(map);
	}

	@Override
	public UserResult<ListRange<UserAssetsView>> selectUserAssets(Map map) {
		int count = selectUserAssetsCount(map);
		UserResult<ListRange<UserAssetsView>> teacherResult = new UserResult<>();
		ListRange<UserAssetsView> teacherAssetsViewListRange = new ListRange<>();
		Page page = (Page) map.get("page");
		page.setCount(count);
		teacherAssetsViewListRange.setPage(page);
		List<UserAssetsView> teacherAssetsViews = new ArrayList<>();
		List<UserAssets> uAssetses = userAssetsMapper.selectPageList(map);
		for (UserAssets userAssets : uAssetses) {
			UserAssetsView userAssetsView = new UserAssetsView();
			userAssetsView.setUserAssets(userAssets);
			User user = userMapper.selectByPrimaryKey(userAssets.getUserId().intValue());
			if (user == null)
				continue;
			userAssetsView.setUser(user);
			teacherAssetsViews.add(userAssetsView);
			teacherAssetsViewListRange.setData(teacherAssetsViews);
		}
		teacherResult.setState(1);
		teacherResult.setContent(teacherAssetsViewListRange);
		return teacherResult;
	}

	@Override
	public UserAssets selectUserAssets(Long userId) {
		UserAssets userAssets = null;
		Map<String, Object> partMap = new HashMap<String, Object>();
		partMap.put("mapprerValue", userId);
		UserPart userPart = userPartMapper.selectModel(partMap);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userId", userId);
		map.put("tableNum", userPart.getNum());
		userAssets = userAssetsMapper.selectModel(map);
		userAssets.setTableNum(userPart.getNum());
		return userAssets;
	}

	@Override
	public Integer addUserAssets(UserAssets userAssets) {
		return userAssetsMapper.insert(userAssets);
	}
}
