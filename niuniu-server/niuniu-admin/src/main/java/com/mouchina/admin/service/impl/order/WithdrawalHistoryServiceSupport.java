package com.mouchina.admin.service.impl.order;

import com.mouchina.admin.service.api.order.WithdrawalHistoryService;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.order.WithdrawalHistoryOrder;
import com.mouchina.moumou_server_interface.order.WithdrawalsService;
import com.mouchina.moumou_server_interface.view.WithdrawalView;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by douzy on 15/10/15.
 */
@Service
public class WithdrawalHistoryServiceSupport implements WithdrawalHistoryService {
    @Resource
    WithdrawalsService withdrawalService;

    @Override
    public ListRange<WithdrawalView> selectListRangeWithbrawal(Map map) {
        return withdrawalService.selectListRangeWithdrawalHistoryByPager(map).getContent();
    }

    @Override
    public WithdrawalHistoryOrder selectWithdrawalHistoryOrder(Long withId)
    {
        return withdrawalService.selectWithdrawalHistoryOrder(withId).getContent();
    }
    @Override
    public Integer updateWithdrawalHistoryOrder(WithdrawalHistoryOrder withdrawalHistoryOrder) {
       return withdrawalService.updateWithdrawalHistoryOrder(withdrawalHistoryOrder).getContent();
    }
    @Override
    public WithdrawalHistoryOrder selectWithdrawalOrderModel(Map map) {
        return withdrawalService.selectWithdrawalOrderModel(map).getContent();
    }
}
