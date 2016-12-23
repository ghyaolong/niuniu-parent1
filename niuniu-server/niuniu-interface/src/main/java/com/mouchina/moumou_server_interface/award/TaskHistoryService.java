package com.mouchina.moumou_server_interface.award;

public interface TaskHistoryService {
	
	/**
	 * 用户任务
	 * @param userId			用户id
	 * @param taskConfigId		任务id
	 * @param isQuery			是否查询【0查询当前状态		1完成任务	2领取奖励】
	 * @return
	 */
	public Object insertTask(Long userId,Long taskConfigId,String isQuery);
	/**
	 * 查询用户缺少的信息
	 * @param userId
	 * @param taskConfigId
	 */
	public void selectUserInfo(Long userId, Long taskConfigId);
	/**
	 * 完成任务
	 * @param userId
	 * @param taskConfigId
	 */
	public void finishTask(Long userId,Long taskConfigId);

}
