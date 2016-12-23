package com.mouchina.moumou_server_dubbo.provider.member;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.moumou_server.dao.member.OrderBillMapper;
import com.mouchina.moumou_server.entity.member.OrderBill;
import com.mouchina.moumou_server_interface.member.OrderBillService;
import com.mouchina.moumou_server_interface.view.BillResult;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by douzy on 16/2/15.
 */
public class OrderBillServiceSupport implements OrderBillService {

    @Resource
    private OrderBillMapper orderBillMapper;

    @Override
    public BillResult<ListRange<OrderBill>> selectListBusinessPage(Map map) {
        // TODO Auto-generated method stub
        BillResult<ListRange<OrderBill>> businessResult = new BillResult<ListRange<OrderBill>>();
        ListRange<OrderBill> listRange = new ListRange<OrderBill>();
        int count = orderBillMapper.selectCount(map);
        Page page = (Page) map.get("page");
        page.setCount(count);
        listRange.setPage(page);
        listRange.setData(orderBillMapper.selectPageList(map));
        businessResult.setContent(listRange);
        businessResult.setState(1);
        return businessResult;
    }

    @Override
    public BillResult<OrderBill> addOrderBill(OrderBill orderBill) {
        // TODO Auto-generated method stub
        BillResult<OrderBill> orderBillResult = new BillResult<OrderBill>();

        int result = orderBillMapper.insertSelective(orderBill);
        orderBillResult.setContent(orderBill);
        orderBillResult.setState(result);

        return orderBillResult;
    }
}

