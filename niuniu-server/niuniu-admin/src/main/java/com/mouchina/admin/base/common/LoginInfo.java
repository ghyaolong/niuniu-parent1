package com.mouchina.admin.base.common;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Created by douzy on 15/12/9.
 */
public class LoginInfo {

    /**
     * 获取登录对象
     * @return
     */
    public static UserDetails getLoginUser() {
        UserDetails userDetails= (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails;
    }

    /**
     * 获取登录用户名
     * @return
     */
    public static String getLoginUserPhone() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

}
