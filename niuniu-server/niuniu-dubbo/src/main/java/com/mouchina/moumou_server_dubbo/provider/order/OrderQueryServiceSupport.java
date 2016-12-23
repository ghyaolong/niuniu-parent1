package com.mouchina.moumou_server_dubbo.provider.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.utils.DateUtils;
import com.mouchina.base.utils.StringUtil;
import com.mouchina.moumou_server.dao.order.OrdersDetailMapper;
import com.mouchina.moumou_server.entity.order.Order;
import com.mouchina.moumou_server.entity.order.OrdersDetail;
import com.mouchina.moumou_server.entity.pay.PayOrder;
import com.mouchina.moumou_server_interface.member.UserIdentity;
import com.mouchina.moumou_server_interface.order.OrderQueryService;
import com.mouchina.moumou_server_interface.order.OrderService;
import com.mouchina.moumou_server_interface.view.OrderShowView;
import com.mouchina.moumou_server_interface.order.OrderView;
import com.mouchina.moumou_server_interface.view.OrdersDetailsView;
import com.mouchina.moumou_server_interface.pay.PayOrderService;
import com.mouchina.moumou_server_interface.view.OrderResult;
import com.mouchina.moumou_server_interface.view.PayResult;

/**
 * 类说明
 * 
 * @author larry 2015年7月7日下午5:10:40
 */

public class OrderQueryServiceSupport implements OrderQueryService {

	@Resource
	private OrderService orderService;
	@Resource
	private PayOrderService payOrderService;
	@Resource
	private OrdersDetailMapper ordersDetailMapper;

	@Override
	public OrderResult<ListRange<OrderShowView>> listRangeOrderShowViewPageByUser(
			Map map) {
		// TODO Auto-generated method stub
		return listRangeOrderShowViewPage(map);

	}

	@Override
	public OrderResult<ListRange<OrderShowView>> listRangeOrderShowViewPage(
			Map map) {
		// TODO Auto-generated method stub
		OrderResult<ListRange<OrderShowView>> orderShowViewResult = new OrderResult<ListRange<OrderShowView>>();

		ListRange<OrderShowView> listOrderShowViewRange = new ListRange<OrderShowView>();

		List<OrderShowView> orderShowViews = new ArrayList<OrderShowView>();
		OrderResult<ListRange<Order>> orderResult = orderService
				.selectlistRangeOrdersPage(map);

		ListRange<Order> listOrderViweRange = orderResult.getContent();
		if (listOrderViweRange != null && listOrderViweRange.getData() != null) {
			for (Order  order : listOrderViweRange.getData()) {

				OrderShowView orderShowView = new OrderShowView();
				orderShowView.setOrder(order);
				if (StringUtil.isNotEmpty(order.getPayNo())) {

					PayResult<PayOrder> payResult = payOrderService
							.selectPayOrder(order.getPayNo());
					if (payResult.getState() == 1) {
						orderShowView.setPayOrder(payResult.getContent());
					}
				}
				
				orderShowViews.add(orderShowView);
			}
			listOrderShowViewRange.setData(orderShowViews);
			listOrderShowViewRange.setPage(listOrderViweRange.getPage());

		}
		orderShowViewResult.setContent(listOrderShowViewRange);
		orderShowViewResult.setState(1);
		return orderShowViewResult;

	}

	@Override
	public OrderResult<List<OrdersDetailsView>> listOrderDetailViewByOrderNo(
			Map map) {
		String orderNo = map.get("orderNo").toString();
		String orderTableNum = OrderServiceSupport
				.getOrderBadyTableNum(orderNo);
		map.put("orderTableNum", orderTableNum);
		int tableNum = OrderServiceSupport.getOrderDetailTableNumInt(Long
				.valueOf(map.get("userId").toString()));
		map.put("tableNum", tableNum);
		OrderResult<List<OrdersDetailsView>> orderDetailsViewResult = new OrderResult<List<OrdersDetailsView>>();
		OrderResult<List<OrdersDetail>> orderResult = orderService
				.listOrderDetailsByOrderNo(map);

		List<OrdersDetailsView> ordersDetailsViews = selcetListOrdersDetailsView(orderResult
				.getContent());
		orderDetailsViewResult.setState(1);
		orderDetailsViewResult.setContent(ordersDetailsViews);
		return orderDetailsViewResult;

	}

	private List<OrdersDetailsView> selcetListOrdersDetailsView(
			List<OrdersDetail> ordersDetails) {

		List<OrdersDetailsView> ordersDetailsViews = new ArrayList<OrdersDetailsView>();

		for (OrdersDetail ordersDetail : ordersDetails) {

			OrdersDetailsView ordersDetailsView = new OrdersDetailsView();
			ordersDetailsView.setOrdersDetail(ordersDetail);
			ordersDetailsViews.add(ordersDetailsView);
		}

		return ordersDetailsViews;
	}

	@Override
	public OrderResult<Integer> selectCourseByBabyIdAndCourseIdNext(
			long userId, long babyId, int courseId) {
		OrderResult<Integer> orderResult = new OrderResult<Integer>();
		Map map = new HashMap();
		
		String tableNum = DateUtils.getDateStringYYYY(new Date());
		map.put("tableNum", tableNum);
		map.put("goodsId", courseId);
		map.put("babyIdLike", babyId+",");
		map.put("states", "1,2,3,4,9");
		
		orderResult.setState(1);
		return orderResult;
	}

	@Override
	public OrderResult<Integer> selectCourseByBabyIdAndCourseIdNowNum(
			long userId, long babyId, int courseId, Date time) {
		// TODO Auto-generated method stub
		OrderResult<Integer> orderResult = new OrderResult<Integer>();
		Map map = new HashMap();
		
		
		String tableNum = DateUtils.getDateStringYYYY(new Date());
		map.put("tableNum", tableNum);
		map.put("goodsId", courseId);
		map.put("babyIdLike", babyId+",");
		map.put("states", "1,2,3,4,9");
		map.put("createTimeBefore", time);
		
		orderResult.setState(1);
		return orderResult;
	}
}
