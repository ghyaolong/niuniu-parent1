package com.mouchina.web.service.impl.member;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.order.WithdrawalHistoryOrder;
import com.mouchina.moumou_server_interface.order.WithdrawalsService;
import com.mouchina.moumou_server_interface.view.WithdrawalView;
import com.mouchina.web.service.api.member.WithdrawalsApiService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by douzy on 16/2/18.
 */
@Service
public class WithdrawalsServiceSupport implements WithdrawalsApiService {
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
