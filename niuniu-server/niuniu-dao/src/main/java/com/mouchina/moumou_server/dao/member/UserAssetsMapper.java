package com.mouchina.moumou_server.dao.member;


import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.member.UserAssets;


public interface UserAssetsMapper extends BaseMapper<UserAssets,java.lang.Long> {
    int updateByUserId(UserAssets userAssets);
}
