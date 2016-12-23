package com.mouchina.moumou_server.dao.advert;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.advert.UserDonationRecord;

public interface UserDonationRecordMapper extends BaseMapper<UserDonationRecord, Long>{
    int deleteByPrimaryKey(Long id);

    int insert(UserDonationRecord record);

    int insertSelective(UserDonationRecord record);

    UserDonationRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserDonationRecord record);

    int updateByPrimaryKey(UserDonationRecord record);
}