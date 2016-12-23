package com.mouchina.web.service.api.member;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.order.WithdrawalHistoryOrder;
import com.mouchina.moumou_server_interface.view.WithdrawalView;

import java.util.Map;

/**
 * Created by douzy on 16/2/18.
 */
public interface WithdrawalsApiService {

    ListRange<WithdrawalView> selectListRangeWithbrawal(Map map);
    WithdrawalHistoryOrder selectWithdrawalHistoryOrder(Long withId);
    Integer updateWithdrawalHistoryOrder(WithdrawalHistoryOrder withdrawalHistoryOrder);
    WithdrawalHistoryOrder selectWithdrawalOrderModel(Map map);
}
