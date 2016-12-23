package com.mouchina.moumou_server_dubbo.provider.particular;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mouchina.base.exception.BusinessException;
import com.mouchina.moumou_server.dao.advert.RedEnvelopeMapper;
import com.mouchina.moumou_server.entity.advert.RedEnvelope;
import com.mouchina.moumou_server.entity.vo.user.particular.ConsumeParticularVo;
import com.mouchina.moumou_server_interface.particular.UserParticularService;

/**
 * 用户明细
 * 
 * @author Administrator
 *
 */
@Service(value = "userParticularServiceSupport")
public class UserParticularServiceSupport implements UserParticularService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserParticularServiceSupport.class);
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
	
	@Autowired
	private RedEnvelopeMapper redEnvelopeMapper;

	@Override
	public List<RedEnvelope> selectEarnParticular(Map<String, Object> map) {
		//int count = advertService.selectRedEnvelopeCount(map);
		//List<RedEnvelope> redEnvelopes = advertService.selectRedEnvelopePageList(map);
		Integer count = redEnvelopeMapper.selectCount(map);
		List<RedEnvelope> redEnvelopes = redEnvelopeMapper.selectUserIncomeDetails(map);
		
		Integer length = redEnvelopes.size();
		//如果只有一条数据，就不用再次比较
		if(length <= 1) return redEnvelopes;
		
		for (int i = 0; i < length; i++) {
			if(redEnvelopes.get(i).getAwardTime() == null) {
				logger.error("用户获取奖励的时间为空，之后数据无法比较!");
//				throw new BusinessException("用户获取奖励的时间为空，之后数据无法比较!");
			}
			
			//只有在加载第一页数据的时候才会在顶部加上月份
			if((Integer)map.get("offset") <= 1 && i == 0 ) {
				//给首位加入月份
				String firstTime = sdf.format(redEnvelopes.get(0).getAwardTime());
				RedEnvelope re1 = new RedEnvelope();
				re1.setMonth(firstTime.split("-")[1] + "月");
				redEnvelopes.add(0, re1);
				continue;
			}
			
			//如果时间为null  就不用再次比较了
			if(redEnvelopes.get(i).getAwardTime() == null) continue;
			
			//如果最后一位，就不用再循环
			if(i+1 >= length) return redEnvelopes;
			
			RedEnvelope redEnvelopePrev = redEnvelopes.get(i);
			RedEnvelope redEnvelopeAfter = redEnvelopes.get(i + 1);
			String datePrev = sdf.format(redEnvelopePrev.getAwardTime());
			String dateAfter = sdf.format(redEnvelopeAfter.getAwardTime());
			
			// 如果月份不是一个，那么就出现了跨月份，需要插入月份
			if (!datePrev.equals(dateAfter)) {
				RedEnvelope re2 = new RedEnvelope();
				re2.setMonth(dateAfter.split("-")[1] + "月");
				redEnvelopes.add(i + 1, re2);
				continue;
			}
		}
		
		return redEnvelopes;
	}

	@Override
	public List<ConsumeParticularVo> selectConsumeParticular(Map<String, Object> map) {
		List<ConsumeParticularVo> consumeParticularVos = redEnvelopeMapper.selectConsumeParticular(map);

		Integer length = consumeParticularVos.size();
		//如果只有一条数据，就不用再次比较
		if(length <= 1) return consumeParticularVos;
		
		for (int i = 0; i < length; i++) {
			//只有在加载第一页数据的时候才会在顶部加上月份
			if((Integer)map.get("offset") <= 1 && i == 0 ) {
				//给首位加入月份
				String firstTime = sdf.format(consumeParticularVos.get(0).getTime());
				ConsumeParticularVo cpv1 = new ConsumeParticularVo();
				cpv1.setMonth(firstTime.split("-")[1] + "月");
				consumeParticularVos.add(0, cpv1);
				continue;
			}
			
			//如果时间为null  就不用再次比较了
			if(consumeParticularVos.get(i).getTime() == null) continue;
			
			//如果最后一位，就不用再循环
			if(i+1 >= length) return consumeParticularVos;
			
			ConsumeParticularVo ConsumeParticularPrev = consumeParticularVos.get(i);
			ConsumeParticularVo ConsumeParticularAfter = consumeParticularVos.get(i + 1);
			String datePrev = sdf.format(ConsumeParticularPrev.getTime());
			String dateAfter = sdf.format(ConsumeParticularAfter.getTime());
			
			// 如果月份不是一个，那么就出现了跨月份，需要插入月份
			if (!datePrev.equals(dateAfter)) {
				ConsumeParticularVo cpv2 = new ConsumeParticularVo();
				cpv2.setMonth(dateAfter.split("-")[1] + "月");
				consumeParticularVos.add(i + 1, cpv2);
				continue;
			}
		}
		
		return consumeParticularVos;
	}

}
