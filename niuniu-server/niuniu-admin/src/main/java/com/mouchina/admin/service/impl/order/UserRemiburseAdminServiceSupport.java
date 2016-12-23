package com.mouchina.admin.service.impl.order;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.mouchina.admin.service.api.order.UserRemiburseAdminService;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.order.OrdersDetail;
import com.mouchina.moumou_server.entity.pay.UserReimburse;
import com.mouchina.moumou_server_interface.member.UserIdentity;
import com.mouchina.moumou_server_interface.member.UserVerifyService;
import com.mouchina.moumou_server_interface.order.OrderService;
import com.mouchina.moumou_server_interface.order.UserReimburseView;
import com.mouchina.moumou_server_interface.pay.ReimburseService;
import com.mouchina.moumou_server_interface.view.OrderResult;
import com.mouchina.moumou_server_interface.view.PayResult;
import com.mouchina.moumou_server_interface.view.UserResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserRemiburseAdminServiceSupport
    implements UserRemiburseAdminService
{
    @Resource
    private ReimburseService reimburseService;
    @Resource
    private OrderService orderService;
    @Resource
    private UserVerifyService userVerifyService;
    @Override
    public UserReimburse selectUserReimburse( String reimburseNo )
    {
        UserReimburse userReimburse = null;
        PayResult<UserReimburse> payResult = reimburseService.selectUserReimburseByReimburseNo( reimburseNo );

        if ( payResult.getState(  ) == 1 )
        {
            userReimburse = payResult.getContent(  );
        }

        return userReimburse;
    }
//     @Override
//     public String userReimburseNoGenerator()
//     {
//         return reimburseService.
//     }
    @Override
    public UserReimburse addUserReimburse( UserReimburse userReimburse )
    {
        // TODO Auto-generated method stub
        return reimburseService.addUserReimburse( userReimburse ).getContent(  );
    }

    @Override
    public Integer updateUserReimburse( UserReimburse userReimburse )
    {
        // TODO Auto-generated method stub
        return reimburseService.updateAgreeUserUserReimburse( userReimburse ).getState(  );
    }

    @Override
    public ListRange<UserReimburseView> selectListUserReimbursePage( Map map )
    {

        if(map.get("phone")!=null)
        {
            UserIdentity userIdentity=new UserIdentity();
            userIdentity.setPhone(map.get("phone").toString());
            User user=userVerifyService.selectUser(userIdentity);
            map.put("userId", user.getId());
        }

        PayResult<ListRange<UserReimburse>> userReimburseListRange = reimburseService.selectListRangeUserReimbursePage(map);

        ListRange<UserReimburse> userReimbursesRange=userReimburseListRange.getContent();

        ListRange<UserReimburseView> userReimburseViewListRange=new ListRange<UserReimburseView>();

        List<UserReimburseView> userReimburseViewList=new ArrayList<UserReimburseView>();

        for(UserReimburse userReimburse:userReimbursesRange.getData()) {
            UserReimburseView userReimburseView = new UserReimburseView();
            UserResult<User> userUserResult = userVerifyService.selectUserByUserId(userReimburse.getUserId());

            userReimburseView.setUser(userUserResult.getContent());
            userReimburseView.setUserReimburse(userReimburse);
            userReimburseViewList.add(userReimburseView);
        }
        userReimburseViewListRange.setData(userReimburseViewList);
        userReimburseViewListRange.setPage(userReimbursesRange.getPage());

        // TODO Auto-generated method stub
        return userReimburseViewListRange;
    }

    @Override
    public Integer updateAgreeUserReimbursePrice( UserReimburse userReimburse )
    {
        // TODO Auto-generated method stub
        return reimburseService.updateAgreeUserUserReimburse( userReimburse ).getState(  );
    }
   @Override
   public String userReimburseNoGenerator() {
    return reimburseService.userReimburseNoGenerator();
   }

    /**
     * 退款异常  状态同步
     * @param userReimburse
     * @return
     */
   @Override
   public void updateStateReimburseError(UserReimburse userReimburse) {
       if (userReimburse != null) {
           reimburseService.updateReimburse(userReimburse);  //更新退款表状态
           if (!StringUtils.isBlank(userReimburse.getOrderDetailNos())) {//更新订单明细状态
               String[] orderDetailArr = userReimburse.getOrderDetailNos().split(",");
               for (String detailNo : orderDetailArr) {
                   OrderResult<OrdersDetail> ordersDetailOrderResult = orderService.selectOrdersDetail(userReimburse.getOrderNo(), userReimburse.getUserId(), detailNo);
                   if (ordersDetailOrderResult.getState() == 1) {
                       OrdersDetail ordersDetail = ordersDetailOrderResult.getContent();
                       ordersDetail.setOrderState((byte) 10);
                       orderService.updateOrdersDetail(ordersDetail);
                   }

               }
           }
       }
   }
    @Override
    public Integer updateReimburse(UserReimburse userReimburse) {
        return reimburseService.updateReimburse(userReimburse).getContent();
    }
    @Override
    public UserReimburse getReimburseModel(Map map) {
        return reimburseService.getReimburseModel(map).getContent();
    }
}
