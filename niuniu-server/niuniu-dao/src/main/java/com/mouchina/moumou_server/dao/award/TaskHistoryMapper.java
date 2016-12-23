package com.mouchina.moumou_server.dao.award;

import java.util.List;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.award.TaskHistory;
import com.mouchina.moumou_server.entity.vo.TaskHistoryVo;

public interface TaskHistoryMapper extends BaseMapper<TaskHistory, Long> {
	int deleteByPrimaryKey(Long id);

	int insert(TaskHistory record);

	int insertSelective(TaskHistory record);

	TaskHistory selectByPrimaryKey(Long id);

	int updateByPrimaryKeySelective(TaskHistory record);

	int updateByPrimaryKey(TaskHistory record);
	
	/**
	 * 根据userid和taskid更新TaskHistory
	 * @param record
	 */
	public void updateByUidAndTaskConId(TaskHistory record);

	/**
	 * 根据用户id查询任务历史记录
	 * 
	 * @param userId
	 * @return
	 */
	public List<TaskHistoryVo> selectTaskHistoryByUserId(Long userId);

}