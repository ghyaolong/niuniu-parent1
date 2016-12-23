package com.mouchina.admin.service.api.advert;

import com.mouchina.moumou_server.entity.advert.Advert;
import com.mouchina.moumou_server_interface.view.AdvertResult;

public interface AdvertAdminService {

	
	AdvertResult<Integer> updateAdvertExpired(Advert advert);
	
	
	AdvertResult<Integer>  updateTimingAdvertExpire();
	
}
