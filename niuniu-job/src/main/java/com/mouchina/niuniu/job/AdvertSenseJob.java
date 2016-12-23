package com.mouchina.niuniu.job;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import com.mouchina.moumou_server.entity.advert.Advert;
import com.mouchina.moumou_server.entity.advert.AdvertHelper;
import com.mouchina.moumou_server_interface.advert.AdvertService;

/**
 * 定时广告投放job
 * @author Administrator
 *
 */
@Component
public class AdvertSenseJob extends ABaseJob {

	@Resource
	private AdvertService advertService;
	
	public void execute() {
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("state", AdvertHelper.STATE_0.getMarker());
			map.put("advertTypes", "" + AdvertHelper.ADVERT_TYPE_2.getMarker() + ","
					+ AdvertHelper.ADVERT_TYPE_6.getMarker() + ","
					+ AdvertHelper.ADVERT_TYPE_8.getMarker());
			map.put("startTimelt", new Date());
			List<Advert> adverts = advertService.selectListAdvert(map);
			if(adverts != null && adverts.size() > 0){
				long beginTime = System.currentTimeMillis();
				log.info(String.format("[任务：定时广告投放] 时间：%s 执行开始...", beginTime));
				for(Advert advert : adverts){
					advert.setState((byte)AdvertHelper.STATE_1.getMarker().intValue());
					advertService.updateAdvert(advert);
				}
				long endTime = System.currentTimeMillis();
				log.info(String.format("[任务：定时广告投放] 时间：%s 执行结束，耗时：%s 秒", endTime, (endTime-beginTime)/1000));
			}
		} catch (Exception e) {
			log.error("定时广告投放job错误信息:" + ExceptionUtils.getStackTrace(e));
		}
	}
	
}
