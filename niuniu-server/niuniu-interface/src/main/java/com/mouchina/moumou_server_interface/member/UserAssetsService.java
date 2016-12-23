package com.mouchina.moumou_server_interface.member;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.member.UserAssets;
import com.mouchina.moumou_server_interface.view.UserAssetsView;
import com.mouchina.moumou_server_interface.view.UserResult;

import java.util.Map;

/**
 * Created by douzy on 16/2/18.
 */
public interface UserAssetsService {
	/**
	 * 更新老师资产(除提现密码)
	 * 
	 * @param userAssets
	 * @return
	 */
	public UserResult<Integer> updateUserAssets(UserAssets userAssets);

	/**
	 * 更新老提现密码
	 * 
	 * @param teacherId
	 * @return
	 */
	public UserResult<Integer> updateUserAssetsWithdrawPassword(Long userId, String withdrawPassword);

	/**
	 * 验证老师提现密码
	 * 
	 * @param teacherId
	 * @return 1 验证成功 0 验证失败
	 */
	public UserResult<Integer> checkUserAssetsWithdrawPassword(Long userId, String withdrawPassword);

	public UserAssets selectUserAssets(Long userId);

	/**
	 * 老师资产列表
	 * 
	 * @param map
	 * @return
	 */
	public UserResult<ListRange<UserAssetsView>> selectUserAssets(Map<String, Object> map);

	/**
	 * 分页总数
	 * 
	 * @param map
	 * @return
	 */
	public int selectUserAssetsCount(Map<String, Object> map);

	/**
	 * 添加用户财富
	 * @param userAssets
	 * @return
	 */
	public Integer addUserAssets(UserAssets userAssets);
	
}
