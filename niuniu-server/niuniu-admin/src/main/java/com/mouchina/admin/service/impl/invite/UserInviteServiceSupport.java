package com.mouchina.admin.service.impl.invite;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mouchina.admin.service.api.invite.IUserInviteService;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.social.UserInvite;
import com.mouchina.moumou_server_interface.social.UserInviteService;
import com.mouchina.moumou_server_interface.view.Result;

@Service
public class UserInviteServiceSupport implements IUserInviteService{
	@Resource
	private UserInviteService inviteService;
	@Override
	public Result<ListRange<UserInvite>> queryInviteList(Map<String, Object> param) {
		return inviteService.selectListUserInvitePage(param);
	}
}
