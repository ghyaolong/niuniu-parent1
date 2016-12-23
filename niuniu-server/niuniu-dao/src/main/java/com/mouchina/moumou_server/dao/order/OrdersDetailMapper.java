package com.mouchina.moumou_server.dao.order;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.order.OrdersDetail;

import java.util.List;
import java.util.Map;

public interface OrdersDetailMapper   extends BaseMapper<OrdersDetail,Long>{
	List<OrdersDetail> selectlistOrderDetailsByTableNum(Map map);
	Integer selectListOrderDetailsByTableNumCount(Map map);
	Integer deleteOrderDetailsByOrderNo(Map map);
}