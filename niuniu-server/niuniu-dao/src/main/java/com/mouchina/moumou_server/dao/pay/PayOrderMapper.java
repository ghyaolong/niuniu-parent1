package com.mouchina.moumou_server.dao.pay;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.pay.PayOrder;

import java.util.Map;

public interface PayOrderMapper  extends BaseMapper<PayOrder,Long> {
  Integer deleteByPayNo(Map map);
}