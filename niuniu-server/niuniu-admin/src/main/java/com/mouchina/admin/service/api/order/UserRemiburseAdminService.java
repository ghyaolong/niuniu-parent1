package com.mouchina.admin.service.api.order;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.pay.UserReimburse;
import com.mouchina.moumou_server_interface.order.UserReimburseView;

import java.util.Map;

public interface UserRemiburseAdminService
{
    UserReimburse selectUserReimburse(String reimburseNo);

    UserReimburse addUserReimburse(UserReimburse userReimburse);

    Integer updateUserReimburse(UserReimburse userReimburse);

    ListRange<UserReimburseView> selectListUserReimbursePage(Map map);

    Integer updateAgreeUserReimbursePrice(UserReimburse userReimburse);
    String userReimburseNoGenerator();
    void updateStateReimburseError(UserReimburse userReimburse);
    Integer updateReimburse(UserReimburse userReimburse);
    UserReimburse getReimburseModel(Map map);
}
