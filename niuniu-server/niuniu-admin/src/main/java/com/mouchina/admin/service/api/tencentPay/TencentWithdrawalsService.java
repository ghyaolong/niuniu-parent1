package com.mouchina.admin.service.api.tencentPay;

import java.util.Map;

/**
 * Created by douzy on 16/2/27.
 */
public interface TencentWithdrawalsService {
    /**
     * 微信提现
     * @param map
     * @return
     */
    String tencentWithdrawalsApi(Map map) throws Exception;
}
