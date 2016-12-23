package com.mouchina.moumou_server_dubbo.provider.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.utils.DateUtils;
import com.mouchina.moumou_server.dao.order.UserTransactionDetailsMapper;
import com.mouchina.moumou_server.entity.order.UserTransactionDetails;
import com.mouchina.moumou_server.entity.vo.UserTransactionDetailsVo;
import com.mouchina.moumou_server_interface.order.UserTransactionDetailsService;

public class UserTransactionDetailsServiceSupport implements UserTransactionDetailsService {

	@Resource
	private UserTransactionDetailsMapper userTransactionDetailsMapper;
	
	@Override
	public ListRange<UserTransactionDetails> selectListUserTransactionDetails(Map map) {
		if (!map.containsKey("tableNum")) {

			String year = map.get("year") == null ? null : map.get("year")
					.toString();

			String tableNum = year == null ? DateUtils
					.getDateStringYYYY(new Date()) : year;
			map.put("tableNum", tableNum);
		}
		ListRange<UserTransactionDetails> listRange = new ListRange<UserTransactionDetails>();
		int count = userTransactionDetailsMapper.selectCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		listRange.setData(userTransactionDetailsMapper.selectPageList(map));
		return listRange;
	}

	@Override
	public List<UserTransactionDetailsVo> exportListUserTransactionDetails(Map map) {
		if (!map.containsKey("tableNum")) {

			String year = map.get("year") == null ? null : map.get("year")
					.toString();

			String tableNum = year == null ? DateUtils
					.getDateStringYYYY(new Date()) : year;
			map.put("tableNum", tableNum);
		}
		List<UserTransactionDetailsVo> list = new ArrayList<UserTransactionDetailsVo>();
		list = userTransactionDetailsMapper.exportPageList(map);
		return list;
	}

}
