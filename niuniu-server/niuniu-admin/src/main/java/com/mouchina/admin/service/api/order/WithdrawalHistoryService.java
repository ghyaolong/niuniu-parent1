package com.mouchina.admin.service.api.order;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.order.WithdrawalHistoryOrder;
import com.mouchina.moumou_server_interface.view.WithdrawalView;

import java.util.Map;

/**
 * Created by douzy on 15/10/15.
 */
public interface WithdrawalHistoryService {
     ListRange<WithdrawalView> selectListRangeWithbrawal(Map map);
     WithdrawalHistoryOrder selectWithdrawalHistoryOrder(Long withId);
     Integer updateWithdrawalHistoryOrder(WithdrawalHistoryOrder withdrawalHistoryOrder);
     WithdrawalHistoryOrder selectWithdrawalOrderModel(Map map);
}
