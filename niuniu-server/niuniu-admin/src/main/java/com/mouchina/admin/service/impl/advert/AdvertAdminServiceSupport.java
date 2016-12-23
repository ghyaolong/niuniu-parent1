package com.mouchina.admin.service.impl.advert;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mouchina.admin.service.api.advert.AdvertAdminService;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.utils.DateUtils;
import com.mouchina.moumou_server.entity.advert.Advert;
import com.mouchina.moumou_server_interface.advert.AdvertService;
import com.mouchina.moumou_server_interface.view.AdvertResult;

@Service
public class AdvertAdminServiceSupport implements AdvertAdminService {

	
    @Resource
    AdvertService advertService;
    
	@Override
	public AdvertResult<Integer> updateAdvertExpired(Advert advert) {
		// TODO Auto-generated method stub
		return advertService.updateAdvertExpired(advert);
	}

	
	
	@Override
	public AdvertResult<Integer> updateTimingAdvertExpire() {
		// TODO Auto-generated method stub
		
		AdvertResult<Integer> advertResult=new AdvertResult<Integer>();
		int count=0;
		
		
		
		Map map = new HashMap();
//		Date now = new Date(new Date().getTime()-3600*48*1000-5*1000);
		Date now = new Date(new Date().getTime()-60*1000);
		map.put("order", "start_time asc");
		map.put("endTimelt", now);
		map.put("state", 1);
		int pageSize=500;
		map.put("page", new Page(0,pageSize));
		List<Advert> adverts=advertService.selectListAdvert(map);
		
		for (Advert advert : adverts) {
			AdvertResult<Integer> advResult= advertService.updateAdvertExpired(advert);
			if(advResult.getState()==1){
				
				count++;
			}
		}
		
		advertResult.setContent(count);
		return advertResult;
	}

}
