package com.mouchina.moumou_server_dubbo.provider.award;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.mouchina.moumou_server.dao.award.TaskConfigMapper;
import com.mouchina.moumou_server.dao.award.TaskHistoryMapper;
import com.mouchina.moumou_server.dao.member.UserAssetsMapper;
import com.mouchina.moumou_server.dao.member.UserMapper;
import com.mouchina.moumou_server.dao.member.UserPartMapper;
import com.mouchina.moumou_server.entity.award.TaskConfig;
import com.mouchina.moumou_server.entity.award.TaskHistory;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserAssets;
import com.mouchina.moumou_server.entity.member.UserLevelConfig;
import com.mouchina.moumou_server.entity.vo.TaskHistoryVo;
import com.mouchina.moumou_server_dubbo.provider.social.UserInviteServiceSupport;
import com.mouchina.moumou_server_interface.award.TaskHistoryService;
import com.mouchina.moumou_server_interface.member.UserLevelConfigService;

@Service(value = "taskHistoryServiceSupport")
public class TaskHistoryServiceSupport implements TaskHistoryService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Resource
	private TaskHistoryMapper taskHistoryMapper;
	@Resource
	private TaskConfigMapper taskConfigMapper;
	@Resource
	private UserPartMapper userPartMapper;
	@Resource
	private UserAssetsMapper userAssetsMapper;
	@Resource
	private UserInviteServiceSupport userInviteServiceSupport;
	@Resource
	private UserMapper userMapper;
	@Autowired
	private UserLevelConfigService userLevelConfigService;

	@Override
	public Object insertTask(Long userId, Long taskConfigId, String isQuery) {
		
		logger.info("任务入口---------->用户id:[{}]---------任务id：[{}]---------是否查询[{}]", userId,taskConfigId,isQuery);
		try {
			if ("0".equals(isQuery)) {
				logger.info("invok select task method parameter userId: [{}] , isQuery : [{}]", userId, isQuery);
				// 查询所有的任务
				return this.selectTask(userId);
			} else if ("1".equals(isQuery)) {
				// 完成任务
				this.shareApp(userId, taskConfigId);
			} else if ("2".equals(isQuery)) {
				// 领取奖励
				return this.getAward(userId, taskConfigId);
			} else if ("3".equals(isQuery)) {
				// 完善用户信息
				this.selectUserInfo(userId, taskConfigId);
			} else if ("4".equals(isQuery)) {
				// 首次登陆
				this.loginTask(userId, taskConfigId);
			}
		} catch (ParseException e) {
			logger.error("invok insertTask method parameter userId: [{}] , taskConfigId : [{}],isQuery : [{}]", userId,
					taskConfigId, isQuery);
			e.printStackTrace();
		}

		return null;
	}

	public List<TaskHistoryVo> selectTask(Long userId) throws ParseException {
		List<TaskHistoryVo> taskHistoryVos = new ArrayList<TaskHistoryVo>();
		Date currentDate = new Date();
		
		if(userId == null) {
			logger.info("用户存在！");
			return taskHistoryVos;
		}

		List<TaskConfig> taskConfigs = taskConfigMapper.findAll();
		for (TaskConfig taskConfig : taskConfigs) {
			logger.info("task config id is : [{}] ", taskConfig.getId());

			Map<String, Object> taskHistoryMap = new HashMap<String, Object>();
			taskHistoryMap.put("userId", userId);
			taskHistoryMap.put("taskConfigId", taskConfig.getId());
			TaskHistory taskHistory = taskHistoryMapper.selectModel(taskHistoryMap);

			TaskHistoryVo taskHistoryVo = new TaskHistoryVo();
			taskHistoryVo.setUserId(userId);
			taskHistoryVo.setId(taskConfig.getId());
			taskHistoryVo.setGrowthPoint(taskConfig.getGrowthPoint());
			taskHistoryVo.setName(taskConfig.getName());
			taskHistoryVo.setUri(taskConfig.getUri());
			Date finishTaskDate = null;
			Boolean isInsert = false;
			// taskHistory.getFinishTaskDate();
			if (taskHistory != null) {
				taskHistoryVo.setIsFinish(taskHistory.getIsFinish());
				//如果曾经做过该任务，只需要累计
				//if(taskHistory.getCount() > 0) continue;
				
				finishTaskDate = taskHistory.getFinishTaskDate();
			} else {
				taskHistory = new TaskHistory();
				taskHistory.setIsFinish((byte) 2);
				taskHistoryVo.setIsFinish((byte) 2);
				finishTaskDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1970-01-01 19:00:00");
				isInsert = true;
			}

			// 初始化任务
			// 完成任务的时间在当前时间之前 完成任务的时间和当前不是一天 进行任务初始化
			if (taskHistory.getIsFinish() == 0 && finishTaskDate.before(currentDate) && !sdf.format(finishTaskDate).equals(sdf.format(currentDate)) && !this.selectIsOnceTask(taskConfig.getId())) {
				logger.info("重复性任务---------->userId：[{}]---------finishTaskDate：[{}]---------currentDate：[{}]-----------taskConfigId:[{}]", userId,finishTaskDate,currentDate,taskConfig.getId());
				
				taskHistory.setCount(0);
				taskHistory.setIsFinish((byte) 2);

				if (isInsert) {
					taskHistory.setUserId(userId);
					taskHistory.setTaskConfigId(taskConfig.getId());
					taskHistory.setFinishTaskDate(finishTaskDate);
					taskHistoryMapper.insert(taskHistory);
				} else {
					logger.info("逻辑控制参数----预期为:false-----isInsert：[{}]", isInsert);
					taskHistoryMapper.updateByPrimaryKey(taskHistory);
				}

				taskHistoryVo.setIsFinish((byte) 2);
			}

			taskHistoryVos.add(taskHistoryVo);
		}

		return taskHistoryVos;
	}

	/**
	 * 用户领取奖励 返回一个vo就OK
	 * 
	 * @param userId
	 * @param taskConfigId
	 */
	public TaskHistoryVo getAward(Long userId, Long taskConfigId) {
		TaskHistoryVo taskHistoryVo = new TaskHistoryVo();
		
		if(userId == null || taskConfigId == null) {
			logger.info("用户名和任务id为空，不能领取奖励");
			return taskHistoryVo;
		}
		
		TaskHistory taskHistory = this.getTaskHistory(userId, taskConfigId);
		if(taskHistory == null) {
			return taskHistoryVo;
		}
		Date finishTaskDate = taskHistory.getFinishTaskDate();
		Date currentDate = new Date();
		// 完成任务的时间在当前时间之前 完成任务的时间和当前不是一天 不能完成任务的
		/*if (finishTaskDate.before(currentDate) && !sdf.format(finishTaskDate).equals(sdf.format(currentDate))) {
			// 只有在跳过前端操作才会进入
			return null;
		}*/
		
		// 完成任务的时间在当前时间之前 完成任务的时间和当前不是一天 不能完成任务的
		if (!finishTaskDate.before(currentDate)) {
			// 只有在跳过前端操作才会进入
			return null;
		}

		if (this.selectIsOnceTask(taskConfigId)) {
			// 一次性任务
			
			// 如果不是2就表示任务完成
			if (1 == taskHistory.getIsFinish()) {
				Map<String, Object> taskConfigMap = new HashMap<String, Object>();
				taskConfigMap.put("id", taskConfigId);
				TaskConfig taskConfig = taskConfigMapper.selectModel(taskConfigMap);
				Integer awardValue = taskConfig.getGrowthPoint();
				this.updateUserAward(userId, (byte) 0, awardValue);

				// 一次性任务完成，任务不在获取，
				taskHistory.setCount(0);
				taskHistory.setIsFinish((byte) 0);
				taskHistory.setFinishTaskDate(new Date());
				taskHistoryMapper.updateByPrimaryKey(taskHistory);

				// 设置返回的vo
				taskHistoryVo.setId(taskConfigId);
				taskHistoryVo.setName(taskConfig.getName());
				taskHistoryVo.setIsFinish(taskHistory.getIsFinish());
				taskHistoryVo.setGrowthPoint(awardValue);
				taskHistoryVo.setUserId(userId);

				return taskHistoryVo;
			}

		} else {
			// 可重复的任务
			
			// 如果不是2就表示任务完成
			if (1 == taskHistory.getIsFinish()) {
				Map<String, Object> taskConfigMap = new HashMap<String, Object>();
				taskConfigMap.put("id", taskConfigId);
				TaskConfig taskConfig = taskConfigMapper.selectModel(taskConfigMap);
				Integer awardValue = taskConfig.getGrowthPoint();
				this.updateUserAward(userId, (byte) 0, awardValue);

				// 每日任务完成，初始化任务，以便今天以后的做任务
				taskHistory.setCount(0);
				taskHistory.setIsFinish((byte) 0);
				taskHistory.setFinishTaskDate(new Date());
				taskHistoryMapper.updateByPrimaryKey(taskHistory);

				// 设置返回的vo
				taskHistoryVo.setId(taskConfigId);
				taskHistoryVo.setName(taskConfig.getName());
				taskHistoryVo.setIsFinish(taskHistory.getIsFinish());
				taskHistoryVo.setGrowthPoint(awardValue);
				taskHistoryVo.setUserId(userId);

				return taskHistoryVo;
			}

		}

		return null;
	}

	/**
	 * 是否已经完成任务
	 * 
	 * @param userId
	 * @param taskConfigId
	 * @return false没有完成任务 true完成任务
	 */
	public TaskHistory getTaskHistory(Long userId, Long taskConfigId) {
		Map<String, Object> taskHistoryMap = new HashMap<String, Object>();
		taskHistoryMap.put("userId", userId);
		taskHistoryMap.put("taskConfigId", taskConfigId);
		return taskHistoryMapper.selectModel(taskHistoryMap);
	}

	/**
	 * 是否是一次性任务
	 * 
	 * @param taskConfigId
	 *            任务id
	 * @return true一次性任务 false可重复任务
	 */
	public Boolean selectIsOnceTask(Long taskConfigId) {
		TaskConfig taskConfig = taskConfigMapper.selectByPrimaryKey(taskConfigId);
		if(taskConfig == null) {
			logger.info("任务没有配置！");
			return null;
		}
		return taskConfig.getIsRepeat() == 0 ? true : false;

	}

	/**
	 * 更新财富值
	 * @param userId		用户id
	 * @param awardType		财富类型
	 * @param awardValue	财富值
	 */
	private void updateUserAward(Long userId,byte awardType,Integer awardValue) {
		Map<String,String> userPartMap = new HashMap<String,String>();
		userPartMap.put("mapprerValue", String.valueOf(userId));
		Integer tableIndex = userPartMapper.selectModel(userPartMap).getNum();
		
		//查询用户的财富
		Map<String,Object> userAssetsMap = new HashMap<String,Object>();
		userAssetsMap.put("userId", userId);
		userAssetsMap.put("tableNum", tableIndex);
		UserAssets userAssets = userAssetsMapper.selectModel(userAssetsMap);
		
		userAssets.setTableNum(tableIndex);
		if(awardType == 0) {
			//如果是经验	增加经验值
			Map<String,Object> userMap = new HashMap<String,Object>();
			userMap.put("id", userId);
			userMap.put("tableNum", tableIndex);
			User user = userMapper.selectModel(userMap);
			Integer userLevel = user.getLevel();
			Integer userGrowthValue = userAssets.getGrowthValue();
			
			Map<String,Object> userLevelConfigMap = new HashMap<String,Object>();
			userLevelConfigMap.put("level", userLevel);
			UserLevelConfig userLevelConfig = userLevelConfigService.selectUserLevelConfig(userLevelConfigMap);
			
			Integer levelGrowthPoint = userLevelConfig.getGrowthPoint();
			Integer currentGrowthPoint = userGrowthValue + awardValue;
			//用户升级
			if(currentGrowthPoint >= levelGrowthPoint) {
				Integer temGrowthPoint = currentGrowthPoint - levelGrowthPoint;
				userAssets.setGrowthValue(temGrowthPoint);
				user.setLevel(userLevel + 1);
				//更新用户等级
				userMapper.updateByPrimaryKey(user);
			} else {
				userAssets.setGrowthValue(userGrowthValue + awardValue);
			}
			
		} else if(awardType == 1) {
			//如果是红包	增加红包钱数
			userAssets.setRedEnvelopeBalance(userAssets.getRedEnvelopeBalance() + awardValue);
		}
		//更新财富值
		userAssetsMapper.updateByUserId(userAssets);
	}

	/**
	 * 分享app 前台直接调用
	 * 
	 * @param userId
	 * @param awardValue
	 * @throws ParseException
	 */
	public void shareApp(Long userId, Long taskConfigId) throws ParseException {
		if(userId == null || taskConfigId == null) {
			logger.info("没有用户id或者任务id，做你妹的任务！");
			return ;
		}
		Map<String, Object> taskHistoryMap = new HashMap<String, Object>();
		taskHistoryMap.put("userId", userId);
		taskHistoryMap.put("taskConfigId", taskConfigId);
		TaskHistory taskHistory = taskHistoryMapper.selectModel(taskHistoryMap);
		
		// 如果用户为null，就先给用户初始化一条数据
		if (taskHistory == null) {
			taskHistory = new TaskHistory();
			taskHistory.setCount(0);
			taskHistory.setUserId(userId);
			taskHistory.setTaskConfigId(taskConfigId);
			taskHistory.setIsFinish((byte) 2);
			taskHistory.setFinishTaskDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1970-01-01 19:00:00"));
			taskHistoryMapper.insert(taskHistory);
		}
		
		//是否是一次性任务
		Boolean isOnceTask = this.selectIsOnceTask(taskConfigId);
		if(isOnceTask && taskHistory.getIsFinish() == 0) {
			//一一次性任务
			return ;
		}
		
		//如果当前天已经做了任务，就不能再次做
		if(sdf.format(new Date()).equals(sdf.format(taskHistory.getFinishTaskDate()))) {
			return ;
		}

		Map<String, Object> taskConfigMap = new HashMap<String, Object>();
		taskConfigMap.put("id", taskConfigId);
		TaskConfig taskConfig = taskConfigMapper.selectModel(taskConfigMap);
		if(taskConfig == null) {
			logger.info("没有配置任务！");
			return ;
		}
		Integer taskCount = taskConfig.getCount();

		// 任务数是1 只要大于2就表示任务完成
		if (taskHistory.getCount() + 1 >= taskCount) {
			taskHistory.setUserId(userId);
			taskHistory.setIsFinish((byte) 1);
			taskHistory.setCount(taskHistory.getCount() + 1);
			taskHistory.setFinishTaskDate(new Date());
			taskHistoryMapper.updateByUidAndTaskConId(taskHistory);
		} else if (taskHistory.getCount() + 1 < taskCount) {
			taskHistory.setCount(taskHistory.getCount() + 1);
			taskHistoryMapper.updateByUidAndTaskConId(taskHistory);
		}
	}

	@Override
	public void selectUserInfo(Long userId, Long taskConfigId) {
		try {
			Map<String, String> userPartMap = new HashMap<String, String>();
			userPartMap.put("mapprerValue", String.valueOf(userId));
			// 获取用户tableNum
			Integer tableIndex = userPartMapper.selectModel(userPartMap).getNum();
			Map<String, Object> userMap = new HashMap<String, Object>();
			userMap.put("tableNum", tableIndex);
			userMap.put("id", userId);
			// 查询用户信息
			User user = userMapper.selectUserInfoNotNull(userMap);
			if (user != null) {
				this.shareApp(userId, taskConfigId);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 判断是不是首次登陆
	 * @param userId
	 * @param taskConfigId
	 * @throws ParseException
	 */
	private void loginTask(Long userId, Long taskConfigId) throws ParseException {
		Date lastLoginTime = getUser(userId).getLastLoginTime();// 获取最后登录时间
		if(lastLoginTime != null){
			Calendar c1 = Calendar.getInstance();
			c1.setTime(lastLoginTime);
			int year1 = c1.get(Calendar.YEAR);
			int month1 = c1.get(Calendar.MONTH) + 1;
			int day1 = c1.get(Calendar.DAY_OF_MONTH);

			Calendar c2 = Calendar.getInstance();
			c2.setTime(new Date());
			int currentYear = c2.get(Calendar.YEAR);
			int currentMonth = c2.get(Calendar.MONTH) + 1;
			int currentDay = c2.get(Calendar.DAY_OF_MONTH);
			//如果最后登录时间和当天不是同一天   修改任务展示状态可领取
			if (year1 == currentYear && month1 == currentMonth && day1 == currentDay) {
				this.shareApp(userId, taskConfigId);
			}
		}
	}

	/**
	 * 
	 * @param user_id
	 *            用户ID
	 */
	public User getUser(Long userId) {
		Map<String, String> userPartMap = new HashMap<String, String>();
		userPartMap.put("mapprerValue", String.valueOf(userId));
		Integer tableIndex = userPartMapper.selectModel(userPartMap).getNum();

		// 查询用户信息
		Map<String, Object> userMap = new HashMap<String, Object>();
		userMap.put("id", userId);
		userMap.put("tableNum", tableIndex);
		User user = userMapper.selectModel(userMap);

		user.setTableNum(tableIndex);
		return user;
	}

	/*
	 * 获得用户财富对象
	 * 
	 * @param user_id 用户ID
	 */
	public UserAssets getUserAsserts(Long userId) {

		Map<String, String> userPartMap = new HashMap<String, String>();
		userPartMap.put("mapprerValue", String.valueOf(userId));
		Integer tableIndex = userPartMapper.selectModel(userPartMap).getNum();

		// 查询用户的财富
		Map<String, Object> userAssetsMap = new HashMap<String, Object>();
		userAssetsMap.put("userId", userId);
		userAssetsMap.put("tableNum", tableIndex);
		UserAssets userAssets = userAssetsMapper.selectModel(userAssetsMap);

		userAssets.setTableNum(tableIndex);

		return userAssets;
	}

	@Override
	public void finishTask(Long userId, Long taskConfigId) {
		try {
			Map<String, Object> taskHistoryMap = new HashMap<String, Object>();
			taskHistoryMap.put("userId", userId);
			taskHistoryMap.put("taskConfigId", taskConfigId);
			TaskHistory taskHistory = taskHistoryMapper.selectModel(taskHistoryMap);
			// 如果用户为null，就先给用户初始化一条数据
			if (taskHistory == null) {
				taskHistory = new TaskHistory();
				taskHistory.setCount(0);
				taskHistory.setUserId(userId);
				taskHistory.setTaskConfigId(taskConfigId);
				taskHistory.setIsFinish((byte) 2);
				taskHistory.setFinishTaskDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1970-01-01 19:00:00"));
				taskHistoryMapper.insert(taskHistory);
			}

			Map<String, Object> taskConfigMap = new HashMap<String, Object>();
			taskConfigMap.put("id", taskConfigId);
			Integer taskCount = taskConfigMapper.selectModel(taskConfigMap).getCount();

			// 任务数是1 只要大于2就表示任务完成
			if (taskHistory.getCount() + 1 >= taskCount) {
				taskHistory.setUserId(userId);
				taskHistory.setIsFinish((byte) 1);
				taskHistory.setCount(taskHistory.getCount() + 1);
				taskHistory.setFinishTaskDate(new Date());
				taskHistoryMapper.updateByUidAndTaskConId(taskHistory);
			} else if (taskHistory.getCount() + 1 < taskCount) {
				taskHistory.setCount(taskHistory.getCount() + 1);
				taskHistoryMapper.updateByUidAndTaskConId(taskHistory);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}		
	}

}
