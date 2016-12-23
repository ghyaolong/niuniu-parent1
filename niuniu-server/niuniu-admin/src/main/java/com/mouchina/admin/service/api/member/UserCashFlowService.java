package com.mouchina.admin.service.api.member;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.pay.UserCashFlow;

import java.util.Map;

/**
 * Created by douzy on 15/9/28.
 */
public interface UserCashFlowService {
    public ListRange<UserCashFlow> getUserCashFlowByPage(Map map);
}
