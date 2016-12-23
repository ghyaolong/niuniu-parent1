package com.mouchina.moumou_server_interface.advert;

import java.util.Map;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.advert.UserDonationRecord;
import com.mouchina.moumou_server_interface.view.Result;

public interface UserDonationService {
	/**
	 * 根据条件map查询捐款记录userRecord
	 * @param map
	 * @return
	 */
	Result<ListRange<UserDonationRecord>> selectListDonationRecordPage(Map<String,Object> map);
}
