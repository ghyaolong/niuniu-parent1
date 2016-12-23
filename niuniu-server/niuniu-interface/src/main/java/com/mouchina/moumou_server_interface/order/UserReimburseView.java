package com.mouchina.moumou_server_interface.order;

import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.pay.UserReimburse;

import java.io.Serializable;

/**
 * Created by douzy on 15/9/1.
 */
public class UserReimburseView implements Serializable {
    private UserReimburse userReimburse;
    private User user;

    public UserReimburse getUserReimburse() {
        return userReimburse;
    }

    public void setUserReimburse(UserReimburse userReimburse) {
        this.userReimburse = userReimburse;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
