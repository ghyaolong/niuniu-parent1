package com.tencent.service;

import com.tencent.common.Configure;
import com.tencent.protocol.pay_web_protocol.BusinessPayReqData;

/**
 * Created by douzy on 16/3/12.
 */
public class ScanBusinessPayService extends BaseService {
    public ScanBusinessPayService() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        super(Configure.PAY_API_TRANSFERS);
    }

    /**
     * 请求支付查询服务
     * @param businessPayReqData 这个数据对象里面包含了API要求提交的各种数据字段
     * @return API返回的XML数据
     * @throws Exception
     */
    public String request(BusinessPayReqData businessPayReqData) throws Exception {

        //--------------------------------------------------------------------
        //发送HTTPS的Post请求到API地址
        //--------------------------------------------------------------------
        String responseString = sendPost(businessPayReqData);

        return responseString;
    }
}
