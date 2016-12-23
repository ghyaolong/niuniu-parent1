package com.mouchina.admin.service.impl.member;

import com.mouchina.admin.service.api.member.UserCashFlowService;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.pay.UserCashFlow;
import com.mouchina.moumou_server_interface.pay.CashFlowService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by douzy on 16/1/27.
 */
@Service
public class UserCashFlowServiceSupport implements UserCashFlowService {

    @Resource
    CashFlowService cashFlowService;
    @Override
    public ListRange<UserCashFlow> getUserCashFlowByPage(Map map) {
        return cashFlowService.selectListUserCashFlowPage(map).getContent();
    }
}
