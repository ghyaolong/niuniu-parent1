package com.mouchina.moumou_server_interface.member;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.member.OrderBill;
import com.mouchina.moumou_server_interface.view.BillResult;

import java.util.Map;

/**
 * Created by douzy on 16/2/15.
 */
public interface OrderBillService {
    BillResult<ListRange<OrderBill>> selectListBusinessPage(Map map);
    BillResult<OrderBill> addOrderBill(OrderBill orderBill);
}
