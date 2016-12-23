package com.mouchina.admin.service.impl.order;

import com.mouchina.admin.service.api.order.OrderAdminService;
import com.mouchina.admin.service.api.order.PayOrderAdminService;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.pay.PayOrder;
import com.mouchina.moumou_server_interface.pay.PayOrderService;
import com.mouchina.moumou_server_interface.view.PayResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class PayOrderAdminServiceSupport implements PayOrderAdminService {

	@Resource
	private PayOrderService payOrderService;
	@Resource
	private OrderAdminService orderAdminService;

	@Override
	public PayOrder selectPayOrder(String payNo) {
		// TODO Auto-generated method stub
		PayResult<PayOrder> payResult = payOrderService.selectPayOrder(payNo);
		return payResult.getContent();
	}

	@Override
	public PayOrder selectPayOrderByOrderNo(String orderNo) {
		// TODO Auto-generated method stub
		PayResult<PayOrder> payResult = payOrderService
				.selectLatestPayOrderByOrderNo(orderNo);
		return payResult.getContent();
	}

	@Override
	public ListRange<PayOrder> selectListPayOrderByOrderNo(Map map) {

		// TODO Auto-generated method stub
		PayResult<ListRange<PayOrder>> payResult = payOrderService
				.selectListRangePayOrderPage(map);

		return payResult.getContent();
	}

	@Override
	public Integer updatePayOrder(PayOrder payOrder) {
		// TODO Auto-generated method stub
		
		return null;
	}
	 @Override
	public PayResult<Integer> reimburseQueryPayOrder(String orderNo,String orderDetailNos) {
		 return payOrderService.reimburseQueryPayOrder(orderNo, orderDetailNos);
	 }
	@Override
	public PayResult<Integer> updateReimburseRequestPayOrder(String orderNo,String orderDetailNos,Integer  reimbursePrice) {
		return payOrderService.updateReimburseRequestPayOrder(orderNo, orderDetailNos, reimbursePrice);
	}
	@Override
	public PayOrder selectPayOrderModel(Map map) {
		return payOrderService.selectPayOrderModel(map);
	}
}
