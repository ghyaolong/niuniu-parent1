package com.mouchina.admin.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.mouchina.admin.dao.StaticUserRoleMapper;
import com.mouchina.admin.dao.UserMapper;
import com.mouchina.admin.service.api.permission.LoginService;
import com.mouchina.base.utils.MD5Util;
import com.mouchina.base.utils.StringUtil;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.permission.StaticPermission;
import com.mouchina.moumou_server_interface.member.UserIdentity;
import com.mouchina.moumou_server_interface.member.UserInfo;
import com.mouchina.moumou_server_interface.view.UserResult;

/**
 * Created by douzy on 15/11/10.
 */
public class MyUserDetailService implements UserDetailsService {
    @Resource
    UserMapper userMapper;
    @Resource
    StaticUserRoleMapper staticUserRoleMapper;
    @Resource(name="loginService")
    LoginService  loginService ;

    //登陆验证时，通过username获取用户的所有权限信息，
    //并返回User放到spring的全局缓存SecurityContextHolder中，以供授权器使用
    public UserDetails loadUserByUsername(String uphone) throws UsernameNotFoundException {

        UserDetails userDetails = null;

        UserIdentity userIdentity = new UserIdentity();
        userIdentity.setPhone(uphone);
        User user = selectUser(userIdentity);

        if(user==null) {
            throw new UsernameNotFoundException("该用户不存在!");
        }


        List<SimpleGrantedAuthority> autthorities = new ArrayList<SimpleGrantedAuthority>();

        Map map=new HashMap();
        map.put("userPhone",uphone);
        List<StaticPermission> staticPermissions=loginService.getLoginUserPermissionByPhone(map);

        for(StaticPermission staticPermission:staticPermissions){
            autthorities.add(new SimpleGrantedAuthority(staticPermission.getPermissionCode()));
        }
//        List<User> roles =  user.getRoles();
//        System.out.println(roles.size());
//        for (User etRole : roles) {
//            autthorities.add(new GrantedAuthorityImpl(etRole.getRoleCode()));
//        }
//        return autthorities ;
//
//        Collection<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
//
        if (user != null) {
            userDetails = new org.springframework.security.core.userdetails.User(uphone,
                    user.getPassWord(), true, true, true, true, autthorities);
        }
        return userDetails;
    }

    public UserResult<UserInfo> processUserLoginInWeb(UserIdentity userIdentity) {

        UserResult<UserInfo> userResult = new UserResult<UserInfo>();
        UserInfo userInfo = new UserInfo();
        // TODO Auto-generated method stub
        int loginResult = 0;
        if (userIdentity != null) {
            User user = selectUser(userIdentity);
            if (user != null) {
                if (user.getPassWord().equals( MD5Util.md5Hex(userIdentity.getPwd()))) {
                    userInfo.setUser(user);
                    loginResult = 1;
                } else
                    loginResult = -1;//密码错误
            }
        }

        userResult.setState(loginResult);
        userResult.setUserInfo(userInfo);
        return userResult;
    }
    public User selectUser(UserIdentity userIdentity) {
        User user = null;
        Map userMap = new HashMap();

        if (userIdentity != null) {
            if (StringUtil.isNotEmpty(userIdentity.getPhone())) {
                userMap.put("phone", userIdentity.getPhone());

                Integer tableNum = userPartTableNum(userIdentity.getPhone());
                userMap.put("tableNum", tableNum);

            } else if (StringUtil.isNotEmpty(userIdentity.getToken())
                    && userIdentity.getTableNum() >= 0) {
                userMap.put("token", userIdentity.getToken());
                userMap.put("tableNum", userIdentity.getTableNum());

            }
        }
        user = userMapper.selectModel(userMap);

        return user;
    }
    public int userPartTableNum(final String phone) {
    	int num = 0;
		String md5String = MD5Util.md5Octal(phone);
		num = Integer.parseInt(md5String.substring(0, 1), 8);
		return num;
    }
}
