package com.mouchina.niuniu.job;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.container.Main;
import com.mouchina.base.redis.RedisHelper;
import com.mouchina.base.redis.RedisLockHandler;
import com.mouchina.moumou_server.entity.advert.RedEnvelope;
import com.mouchina.moumou_server_interface.advert.AdvertService;

/**
 * 任务：广告解冻
 * @author hpf
 *
 */
@Component
public final class AdvertStatisticsUnfreezeJob extends ABaseJob{
	
	@Resource
	AdvertService advertService;
	public void execute(){
		List<RedEnvelope> list = advertService.searchWaitUnFreezeAdvertList(500);
		if(list == null || list.size() <= 0){
			log.error("暂无待解冻的广告数据");
			return;
		}
		long beginTime = System.currentTimeMillis();
		
		log.info(String.format("[任务：解冻广告] 时间：%s 执行开始...", beginTime));
		for (RedEnvelope item : list) {
			
			try{
				advertService.changeAdvertUnFreeze(item);
			}catch (Exception ex) {
				log.error(String.format("[任务：解冻广告] 用户(%s)广告(%s)解冻失败，信息：%s", new Object[]{
						item.getUserId(),item.getAdvertId(),ex.getMessage()
				}),ex);
				
				try {
					advertService.changeUnFreezeCountAdd(item);
				} catch (Exception e) {
					log.error(String.format("[任务：解冻广告] 用户(%s)广告(%s)解冻次数递增失败，信息：%s", new Object[]{
							item.getUserId(),item.getAdvertId(),e.getMessage()
					}),e);
				}
			}
		}
		
		long endTime = System.currentTimeMillis();
		log.info(String.format("[任务：解冻广告] 时间：%s 执行结束，耗时：%s 秒", endTime,(endTime-beginTime)/1000));
	}
}
