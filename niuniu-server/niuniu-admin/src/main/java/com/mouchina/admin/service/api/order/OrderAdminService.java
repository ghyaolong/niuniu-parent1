package com.mouchina.admin.service.api.order;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.order.Order;
import com.mouchina.moumou_server_interface.order.OrderView;

import java.util.Map;

/**
 *类说明
 *@author larry
 * 2015年7月8日下午7:49:30
 */
public interface OrderAdminService
{
    Integer updateTimingAutoBatchReleaseCouresePublicLockAndCloseOrder();

    ListRange<Order> selectListRangeOrders(Map map);

    Order selectOrder(String orderNo);

    OrderView selectOrderView(String orderNo);

    Integer updateOrder(Order order);

    OrderView updateOrderView(OrderView orderView);

    ListRange<OrderView> selectListRangeOrdersView(Map map);

}
