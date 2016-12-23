package com.mouchina.admin.service.impl.tencentPay;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.mouchina.admin.base.common.CommonEnumUtil;
import com.mouchina.admin.service.api.order.WithdrawalHistoryService;
import com.mouchina.admin.service.api.tencentPay.TencentWithdrawalsService;
import com.mouchina.moumou_server.entity.member.UserAssets;
import com.mouchina.moumou_server.entity.order.WithdrawalHistoryOrder;
import com.mouchina.moumou_server_interface.member.UserAssetsService;
import com.tencent.common.Configure;
import com.tencent.common.XMLParser;
import com.tencent.protocol.pay_web_protocol.BusinessPayReqData;
import com.tencent.service.ScanBusinessPayService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by douzy on 16/2/27.
 */
@Service
public class TencentWithdrawalsServiceSupport implements TencentWithdrawalsService {

    @Resource
    WithdrawalHistoryService withdrawalHistoryService;
    @Resource
    UserAssetsService userAssetsService;

    static Logger logger = LogManager.getLogger();

    @Override
    public String tencentWithdrawalsApi(Map map) throws Exception {
        String result ="FAIL";
        logger.info("====微信提现 start====");

        String withdrawalsNo = map.get("withdrawalsNo").toString();

        logger.info("提现单号:" + withdrawalsNo);

        String price = map.get("price").toString();

        logger.info("提现金额:" + price);

        if (!StringUtils.isBlank(withdrawalsNo)
                && !StringUtils.isBlank(price)) {
            int withdraFee = new BigDecimal(price).multiply(new BigDecimal(100)).intValue();
            logger.info("==参数校验通过==");

            Map withMap = new HashMap();
            withMap.put("withdrawlHistoryOrderNo", withdrawalsNo);
            WithdrawalHistoryOrder withdrawalHistoryOrder = withdrawalHistoryService.selectWithdrawalOrderModel(withMap);
            if (withdrawalHistoryOrder == null || withdrawalHistoryOrder.getState() == CommonEnumUtil.Withdrawals.SUCCESS.getState()) {
                logger.info("==没有该笔提现订单或该订单已提现==");
                return "FAIL,没有该笔提现订单或该订单已提现";
            }

            //查找用户资产信息
            UserAssets userAssets = userAssetsService.selectUserAssets(withdrawalHistoryOrder.getUserId());
            if (userAssets == null)
                return "FAIL,无法找到该用户信息";
            String openId = userAssets.getOpenId();
            if (!StringUtils.isBlank(openId)) {
                return "FAIL,无法获取用户OpenId.";
            }

            InetAddress netAddress = getInetAddress();
            if (null == netAddress) {
                return null;
            }
            String ip = netAddress.getHostAddress();
            BusinessPayReqData businessPayReqData = new BusinessPayReqData(Configure.getAppid(), Configure.getMchid(), "", withdrawalHistoryOrder.getWithdrawlHistoryOrderNo(), userAssets.getOpenId(), "FORCE_CHECK", withdrawalHistoryOrder.getAccountName(), withdraFee, "哞哞用户提现", ip);
            ScanBusinessPayService scanBusinessPayService = new ScanBusinessPayService();

            withdrawalHistoryOrder.setState((byte)4);
            withdrawalHistoryService.updateWithdrawalHistoryOrder(withdrawalHistoryOrder);
            logger.info("=====提现状态已更新-提现中=====");
            String responseString = scanBusinessPayService.request(businessPayReqData);

            logger.info("提现结果(微信返回XML):" + responseString);
            System.out.print(responseString);
            Map<String,Object> resultMap= XMLParser.getMapFromXML(responseString);
            String resul=resultMap.get("return_code").toString();
            String result_code=resultMap.get("result_code").toString();
            logger.info("提现POST结果(XML解析后):" + resul);
            logger.info("提现业务结果(XML解析后):" + result_code);

            if(resul.equals("SUCCESS")
                    &&result_code.equals("SUCCESS")) {
                String partnerTradeNo = resultMap.get("partner_trade_no").toString();
                String paymentNo = resultMap.get("payment_no").toString();
                logger.info("提现单号(XML解析后):" + partnerTradeNo);
                logger.info("微信单号(XML解析后):" + paymentNo);
                withdrawalHistoryOrder.setState((byte) 2);
                withdrawalHistoryOrder.setFinishTime(new Date());
                withdrawalHistoryService.updateWithdrawalHistoryOrder(withdrawalHistoryOrder);
                result = "SUCCESS";
                logger.info("提现成功");
            }else
            {

                withdrawalHistoryOrder.setState((byte)3);
                withdrawalHistoryService.updateWithdrawalHistoryOrder(withdrawalHistoryOrder);
                result =resultMap.get("err_code_des").toString();
                logger.info("提现失败msg:" + result);
            }
//            //余额提现
//            if(withdrawalHistoryOrder.getType()==0) {
//                //余额小于提现金额
//                if(userAssets.getCashBalance()<withdraFee) {
//                    logger.info("**********当前提现金额错误！可提现:" + userAssets.getCashBalance() + " , 当前:" + withdraFee + "***********");
//                    return "FAIL,余额不足.";
//                }
//            }


        }

        logger.info("====微信提现 end====");
        return result;
    }
    public static InetAddress getInetAddress(){

        try{
            return InetAddress.getLocalHost();
        }catch(UnknownHostException e){
            System.out.println("unknown host!");
        }
        return null;

    }
}
