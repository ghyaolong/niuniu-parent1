package com.mouchina.admin.service.api.alipay;


import com.mouchina.moumou_server.entity.order.WithdrawalHistoryOrder;

import java.text.ParseException;
import java.util.Map;

/**
 * Created by douzy on 15/10/19.
 */
public interface AlipayWithdrawalService {
    boolean aliNotify(String batchNo, String success_details, String fail_details) throws ParseException;
    String buildForm(Map map) throws Exception;
     Integer   updateWithdrawalHistoryOrder(WithdrawalHistoryOrder withdrawalHistoryOrder);
}
