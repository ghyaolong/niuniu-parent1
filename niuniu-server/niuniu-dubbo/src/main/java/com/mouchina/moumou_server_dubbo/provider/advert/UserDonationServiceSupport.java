package com.mouchina.moumou_server_dubbo.provider.advert;

import java.util.Map;

import javax.annotation.Resource;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.moumou_server.dao.advert.UserDonationRecordMapper;
import com.mouchina.moumou_server.entity.advert.UserDonationRecord;
import com.mouchina.moumou_server_interface.advert.UserDonationService;
import com.mouchina.moumou_server_interface.view.Result;

public class UserDonationServiceSupport implements UserDonationService {
	@Resource
	UserDonationRecordMapper userDonationRecordMapper;
	@Override
	public Result<ListRange<UserDonationRecord>> selectListDonationRecordPage(Map<String, Object> map) {
		// TODO Auto-generated method stub
		Result<ListRange<UserDonationRecord>> advertResult = new Result<ListRange<UserDonationRecord>>();
		ListRange<UserDonationRecord> listRange = new ListRange<UserDonationRecord>();
		int count = userDonationRecordMapper.selectCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		listRange.setData(userDonationRecordMapper.selectPageList(map));
		advertResult.setContent(listRange);
		advertResult.setState(1);
		return advertResult;
	}

}
