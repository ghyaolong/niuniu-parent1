package com.mouchina.admin.service.impl.tencentPay;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.mouchina.admin.base.common.CommonEnumUtil;
import com.mouchina.admin.service.api.order.OrderAdminService;
import com.mouchina.admin.service.api.order.PayOrderAdminService;
import com.mouchina.admin.service.api.order.UserRemiburseAdminService;
import com.mouchina.admin.service.api.tencentPay.TencentRemiburseService;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.pay.PayOrder;
import com.mouchina.moumou_server.entity.pay.UserReimburse;
import com.mouchina.moumou_server_interface.member.UserVerifyService;
import com.mouchina.moumou_server_interface.view.UserResult;
import com.tencent.common.XMLParser;
import com.tencent.protocol.refund_protocol.RefundReqData;
import com.tencent.service.RefundService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by douzy on 15/10/13.
 */
@Service
public class TencentRemiburseServiceSupport implements TencentRemiburseService {

    @Resource
    private UserRemiburseAdminService userRemiburseAdminService;
    @Resource
    private OrderAdminService orderAdminService;
    @Resource
    private PayOrderAdminService payOrderAdminService;
    @Resource
    private UserVerifyService userVerifyService;
    static Logger logger = LogManager.getLogger();
    public static String getOrderNoTableNum(String orderNo) {
        String tableNum = orderNo.substring(0, 4);
        return tableNum;
    }
    @Override
    public String tencentTrmiburseApi(Map map ) throws Exception {
        logger.info("==============微信退款==============");
        String result ="FAIL";
        String orderNo = map.get("orderNo").toString();

//        PayOrder payOrder= payOrderAdminService.selectPayOrderByOrderNo(map.get("orderNo").toString());
//        if(payOrder==null)
//            return "FAIL,该订单未支付";

        logger.info("退款订单号:"+orderNo);
        String price = map.get("price").toString();
        logger.info("退款金额:"+price);
        String reimburseNo = map.get("reimburseNo").toString();
        logger.info("退款单号:"+reimburseNo);
        if (!StringUtils.isBlank(orderNo)
                && !StringUtils.isBlank(price)
                &&!StringUtils.isBlank(reimburseNo)) {
            int refundFee = new BigDecimal(price).multiply(new BigDecimal(100)).intValue();
            logger.info("==参数校验通过==");
            UserReimburse userReimburse = userRemiburseAdminService.selectUserReimburse(reimburseNo);
            if (userReimburse == null||userReimburse.getState()== CommonEnumUtil.Reimburse.SUCCESS.getState())
                return "FAIL,没有该笔退款订单或该订单已退款";
            if(userReimburse.getPrice()>refundFee) {
                logger.info("**********当前退款金额错误！应退:"+userReimburse.getPrice()+" , 当前:"+refundFee+"***********");
                return "FAIL,退款金额错误.";
            }
            PayOrder payOrder = payOrderAdminService.selectPayOrderByOrderNo(orderNo);
            if (payOrder == null)
                return "FAIL,该笔订单未支付!";
            //String outTradeNo=payOrder.getOrderNo();
            UserResult<User> userUserResult = userVerifyService.selectUserByUserId(payOrder.getUserId());

            if (userUserResult.getState() <= 0)
                return "FAIL,用户信息获取失败!";

            String transactionID = payOrder.getThirdPartyPayNo();
            String deviceInfo = userUserResult.getContent().getMid();
            String outRefundNo = reimburseNo;
            logger.info("第三方交易号:" + transactionID);
            logger.info("用户终端号:" + deviceInfo);
            logger.info("退款单号:" + outRefundNo);
            int totalFee = payOrder.getPayPrice();//new BigDecimal().multiply(new BigDecimal(100)).intValue();

            logger.info("订单总金额(分):" + totalFee);
            logger.info("退款金额(分):" + refundFee);
            logger.info("=====对象构造=====");
            RefundReqData refundReqData = new RefundReqData(transactionID, "", deviceInfo, outRefundNo, totalFee, refundFee, "BJJ_ADMIN", "CNY");

            RefundService refundService = new RefundService();
            logger.info("=====对象构造=====");

            userReimburse.setState(CommonEnumUtil.Reimburse.ISREFOUND.getState());   //更新订单状态为退款中
            userReimburse.setTableNum(getOrderNoTableNum(userReimburse.getReimburseNo()));
            userRemiburseAdminService.updateReimburse(userReimburse);
            logger.info("=====订单状态已更新-退款中=====");
            String responseString = refundService.request(refundReqData);


            logger.info("退款结果(微信返回XML):" + responseString);
            Map<String,Object> resultMap= XMLParser.getMapFromXML(responseString);
            String resul=resultMap.get("return_code").toString();
            String result_code=resultMap.get("result_code").toString();
            logger.info("退款POST结果(XML解析后):" + resul);
            logger.info("退款业务结果(XML解析后):" + result_code);
            if(resul.equals("SUCCESS")
                    &&result_code.equals("SUCCESS")) {
                //微信退款单号
                String refId=resultMap.get("refund_id").toString();
                String refChannel=resultMap.get("refund_channel").toString();
                logger.info("退款单号(XML解析后):" + refId);
                logger.info("退款渠道(XML解析后):" + refChannel);
                String batchNo=userRemiburseAdminService.userReimburseNoGenerator();
                userReimburse.setBatchNo(batchNo);
                userReimburse.setPayNo(refId);
                userRemiburseAdminService.updateAgreeUserReimbursePrice(userReimburse);
                result = "SUCCESS";
            }else {
                userReimburse.setState(CommonEnumUtil.Reimburse.FAIL.getState());
                userReimburse.setTableNum(getOrderNoTableNum(userReimburse.getReimburseNo()));
                userRemiburseAdminService.updateStateReimburseError(userReimburse);
                result =resultMap.get("err_code_des").toString();
            }
            logger.info("==============微信退款==============");
        }

        return result;
    }
}
