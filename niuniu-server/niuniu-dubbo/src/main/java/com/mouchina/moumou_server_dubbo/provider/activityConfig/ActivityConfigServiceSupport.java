package com.mouchina.moumou_server_dubbo.provider.activityConfig;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.moumou_server.dao.activityConfig.ActivityConfigMapper;
import com.mouchina.moumou_server.entity.activityConfig.ActivityConfig;
import com.mouchina.moumou_server_interface.activityConfig.ActivityConfigService;
import com.mouchina.moumou_server_interface.view.ActivityConfigResult;

@Service(value = "activityConfigServiceSupport")
public class ActivityConfigServiceSupport implements ActivityConfigService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ActivityConfigMapper activityConfigMapper;

	@Override
	public ActivityConfigResult<ActivityConfig> addActivityConfig(ActivityConfig activityConfig) {

		ActivityConfigResult<ActivityConfig> activityConfigResult = new ActivityConfigResult<ActivityConfig>();
		if (null != activityConfig) {
			activityConfigMapper.insertSelective(activityConfig);
			activityConfigResult.setContent(activityConfig);
			activityConfigResult.setState(1);
		}
		return activityConfigResult;
	}

	@Override
	public ActivityConfigResult<Integer> updateActivityConfig(ActivityConfig activityConfig) {
		ActivityConfigResult<Integer> activityConfigResult = new ActivityConfigResult<Integer>();
		int result = activityConfigMapper.updateByPrimaryKeySelective(activityConfig);
		activityConfigResult.setState(result);
		return activityConfigResult;

	}

	@Override
	public ActivityConfigResult<ListRange<ActivityConfig>> selectListActivityConfigPage(Map<String, Object> map) {
		logger.info("dubbo調用了：---------------》selectListActivityConfigPage");
		ActivityConfigResult<ListRange<ActivityConfig>> activityConfigResult = new ActivityConfigResult<ListRange<ActivityConfig>>();
		ListRange<ActivityConfig> listRange = new ListRange<ActivityConfig>();
		int count = activityConfigMapper.selectCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);

		List<ActivityConfig> list = activityConfigMapper.selectPageList(map);
		listRange.setData(list);
		activityConfigResult.setContent(listRange);
		activityConfigResult.setState(1);

		return activityConfigResult;
	}

	@Override
	public ActivityConfigResult<ActivityConfig> selectActivityConfig(Long activityConfigId) {

		ActivityConfigResult<ActivityConfig> activityConfigResult = new ActivityConfigResult<ActivityConfig>();
		ActivityConfig activityConfig = activityConfigMapper.selectByPrimaryKey(activityConfigId);
		activityConfigResult.setContent(activityConfig);
		activityConfigResult.setState(1);

		return activityConfigResult;
	}

	@Override
	public ActivityConfigResult<Integer> deleteActivityConfig(Long activityConfigId) {

		ActivityConfigResult<Integer> activityConfigResult = new ActivityConfigResult<Integer>();
		int result = activityConfigMapper.deleteByPrimaryKey(activityConfigId);

		activityConfigResult.setContent(result);
		activityConfigResult.setState(result);
		return activityConfigResult;
	}

	@Override
	public List<ActivityConfig> selectListActivityConfigs(Map<String, Object> map) {
		return activityConfigMapper.selectPageList(map);
	}

}
