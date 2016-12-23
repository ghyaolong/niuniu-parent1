package com.mouchina.moumou_server.dao.order;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.order.Order;

import java.util.List;
import java.util.Map;

public interface OrderMapper extends BaseMapper<Order, Long> {
	Integer deleteByOrderNo(Map map);

	List<Order> selectWherePageList(Map map);

	Integer selectWhereCount(Map map);

	Integer updateByOrderSelective(Order order);

	Order selectByOrderNo(Order order);
}