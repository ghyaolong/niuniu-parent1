package com.mouchina.admin.service.impl.member;

import com.mouchina.admin.service.api.member.UserAdminService;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server_interface.member.UserIdentity;
import com.mouchina.moumou_server_interface.member.UserInfo;
import com.mouchina.moumou_server_interface.member.UserVerifyService;
import com.mouchina.moumou_server_interface.view.UserResult;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class UserAdminServiceSupport
    implements UserAdminService
{
    @Resource
    private UserVerifyService userVerifyService;


    @Override
    public User addUser( User user )
    {
        // TODO Auto-generated method stub
        UserInfo userInfo = new UserInfo(  );
        userInfo.setUser( user );

        UserResult<User> userResult = userVerifyService.saveUserRegister( user );

        return userResult.getContent(  );
    }

    @Override
    public Integer checkUser( String phone )
    {
        int result = 0;

        // TODO Auto-generated method stub
        UserIdentity userIdentity = new UserIdentity(  );
        userIdentity.setPhone( phone );

        User user = userVerifyService.selectUser( userIdentity );

        if ( user != null )
        {
            result = 1;
        }

        return result;
    }

    @Override
    public Integer deleteUser( String userId )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer updateUser( User user )
    {
        // TODO Auto-generated method stub
        return userVerifyService.updateUser( user );
    }

    @Override
    public ListRange<User> selectListRangeUsers( Map map )
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserInfo user( String phone )
    {
        UserIdentity userIdentity = new UserIdentity(  );
        userIdentity.setPhone( phone );

        return userVerifyService.singleUserSearch( userIdentity );
    }

    @Override
    public User selectUserByUserId( Long userId )
    {
        User user = null;
        UserResult<User> userResult = userVerifyService.selectUserByUserId( userId );
        
        if ( userResult.getState(  ) == 1 )
        {
            user = userResult.getContent(  );
        }

        return user;
    }
    @Override
    public UserInfo processUserLoginInWeb(UserIdentity userIdentity) {
        UserResult<UserInfo> userInfoUserResult = userVerifyService.processUserLoginInWeb(userIdentity);
        if (userInfoUserResult.getState() != 1)
            return null;
        return userInfoUserResult.getUserInfo();
    }
    @Override
    public User selectUser(UserIdentity userIdentity){
        return userVerifyService.selectUser(userIdentity);
    }
}
