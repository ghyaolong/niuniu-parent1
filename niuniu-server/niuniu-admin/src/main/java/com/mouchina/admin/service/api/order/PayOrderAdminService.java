package com.mouchina.admin.service.api.order;

import com.mouchina.base.common.page.ListRange;

import com.mouchina.moumou_server.entity.pay.PayOrder;
import com.mouchina.moumou_server_interface.view.PayResult;

import java.util.Map;

public interface PayOrderAdminService
{
    PayOrder selectPayOrder(String payNo);

    PayOrder selectPayOrderByOrderNo(String orderNo);

    ListRange<PayOrder> selectListPayOrderByOrderNo(Map map);

    Integer updatePayOrder(PayOrder payOrder);
    PayResult<Integer> reimburseQueryPayOrder(String orderNo, String orderDetailNos);

    PayResult<Integer> updateReimburseRequestPayOrder(String orderNo, String orderDetailNos, Integer reimbursePrice);
    PayOrder selectPayOrderModel(Map map);
}
