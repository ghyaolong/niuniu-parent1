package com.mouchina.admin.service.api.member;

import com.mouchina.base.common.page.ListRange;

import com.mouchina.moumou_server.entity.member.User;

import com.mouchina.moumou_server_interface.member.UserIdentity;
import com.mouchina.moumou_server_interface.member.UserInfo;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;

public interface UserAdminService
{
    User addUser(User user);

    Integer checkUser(String phone);

    Integer deleteUser(String userId);

    UserInfo user(String phone);

    Integer updateUser(User user);

    ListRange<User> selectListRangeUsers(Map map);

    User selectUserByUserId(Long userId);

    UserInfo processUserLoginInWeb(UserIdentity userIdentity);
    User selectUser(UserIdentity userIdentity);
}
