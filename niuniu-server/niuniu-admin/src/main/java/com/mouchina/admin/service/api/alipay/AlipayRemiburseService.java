package com.mouchina.admin.service.api.alipay;

import java.text.ParseException;
import java.util.Map;

/**
 * Created by douzy on 15/10/9.
 */
public interface AlipayRemiburseService {
    String buildFormHtml(Map map) throws ParseException;
    void alipayNotify(String batch_no, String result_details);
}
