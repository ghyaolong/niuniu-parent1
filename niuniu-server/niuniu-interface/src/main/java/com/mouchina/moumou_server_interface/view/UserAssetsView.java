package com.mouchina.moumou_server_interface.view;

import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserAssets;

import java.io.Serializable;

/**
 * Created by douzy on 16/2/18.
 */
public class UserAssetsView implements Serializable {
    private User user;
    private UserAssets userAssets;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserAssets getUserAssets() {
        return userAssets;
    }

    public void setUserAssets(UserAssets userAssets) {
        this.userAssets = userAssets;
    }
}
