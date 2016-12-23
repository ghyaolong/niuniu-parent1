package com.mouchina.admin.service.api.invite;

import java.util.Map;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.social.UserInvite;
import com.mouchina.moumou_server_interface.view.Result;

public interface IUserInviteService {
	Result<ListRange<UserInvite>> queryInviteList(Map<String,Object> param);
}
