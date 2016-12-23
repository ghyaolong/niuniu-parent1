package com.mouchina.moumou_server_dubbo.provider.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.utils.DateUtils;
import com.mouchina.moumou_server.dao.order.UserLuckyBagDetailsMapper;
import com.mouchina.moumou_server.entity.order.UserLuckyBagDetails;
import com.mouchina.moumou_server.entity.vo.UserLuckyBagDetailsVo;
import com.mouchina.moumou_server_interface.order.UserLuckyBagDetailsService;

public class UserLuckyBagDetailsServiceSupport implements UserLuckyBagDetailsService {

	@Resource
	private UserLuckyBagDetailsMapper userLuckyBagDetailsMapper;
	
	@Override
	public ListRange<UserLuckyBagDetails> selectListUserLuckyBagDetails(Map map) {
		if (!map.containsKey("tableNum")) {

			String year = map.get("year") == null ? null : map.get("year")
					.toString();

			String tableNum = year == null ? DateUtils
					.getDateStringYYYY(new Date()) : year;
			map.put("tableNum", tableNum);
		}
		ListRange<UserLuckyBagDetails> listRange = new ListRange<UserLuckyBagDetails>();
		int count = userLuckyBagDetailsMapper.selectCount(map);
		Page page = (Page) map.get("page");
		page.setCount(count);
		listRange.setPage(page);
		listRange.setData(userLuckyBagDetailsMapper.selectPageList(map));
		return listRange;
	}

	@Override
	public List<UserLuckyBagDetailsVo> exportListUserLuckyBagDetails(Map map) {
		if (!map.containsKey("tableNum")) {

			String year = map.get("year") == null ? null : map.get("year")
					.toString();

			String tableNum = year == null ? DateUtils
					.getDateStringYYYY(new Date()) : year;
			map.put("tableNum", tableNum);
		}
		List<UserLuckyBagDetailsVo> list = new ArrayList<UserLuckyBagDetailsVo>();
		list = userLuckyBagDetailsMapper.exportPageList(map);
		return list;
	}

}
