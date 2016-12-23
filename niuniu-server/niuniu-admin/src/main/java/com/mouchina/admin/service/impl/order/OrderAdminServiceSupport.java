package com.mouchina.admin.service.impl.order;

import com.mouchina.admin.service.api.order.OrderAdminService;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.order.Order;
import com.mouchina.moumou_server_interface.order.OrderService;
import com.mouchina.moumou_server_interface.order.OrderView;
import com.mouchina.moumou_server_interface.view.OrderResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 类说明
 * 
 * @author larry 2015年7月8日下午7:50:29
 */
@Service
public class OrderAdminServiceSupport implements OrderAdminService {

	@Resource
	private OrderService orderService;

	@Override
	public Integer updateTimingAutoBatchReleaseCouresePublicLockAndCloseOrder() {
		OrderResult<Integer> orderResult = new OrderResult<Integer>();
		orderService
				.updateTimingAutoBatchReleaseCouresePublicLockAndCloseOrder();
		return 1;

	}

	@Override
	public ListRange<Order> selectListRangeOrders(Map map) {
		return orderService.selectlistRangeOrdersPage(map).getContent();
	}

	@Override
	public Order selectOrder(String orderNo) {
		return orderService.selectOrder(orderNo).getContent();
	}

	@Override
	public OrderView selectOrderView(String orderNo) {
		return orderService.selectAdminOrderView(orderNo).getContent();
	}
	@Override
	public Integer updateOrder(Order order) {
		OrderResult<Integer> orderResult=	orderService.updateOrderByOrderNo(order);
		return orderResult.getState();
	}

	@Override
	public OrderView updateOrderView(OrderView orderView) {
		return null;
	}
	@Override
	public ListRange<OrderView> selectListRangeOrdersView(Map map) {
		return orderService.listRangeOrdersPage(map).getContent();
	}
}
