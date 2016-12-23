package com.mouchina.web.service.api.member;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.order.WithdrawalHistoryOrder;
import com.mouchina.moumou_server_interface.view.UserAssetsView;
import com.mouchina.moumou_server_interface.view.WelfareResult;

import java.util.Map;

/**
 * Created by douzy on 16/2/18.
 */
public interface UserAssetsAdminService {
    ListRange<UserAssetsView> selectUserAssets(Map<String, Object> map);
    Integer selectApplyWithdrawalOrderCount(Map map);
    /**
     * 添加提现记录
     * @param userId
     * @param price
     * @param account
     * @param accountName
     * @param type
     * @param channel
     * @return
     */
    WelfareResult<WithdrawalHistoryOrder> applyUserWithdeawal(Long userId,Integer price,String account,String accountName,Integer type,Byte channel) ;
}
