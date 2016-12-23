package com.mouchina.admin.service.impl.alipay;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipaySubmit;
import com.mouchina.admin.base.common.CommonEnumUtil;
import com.mouchina.admin.base.common.util;
import com.mouchina.admin.base.config.Env;
import com.mouchina.admin.service.api.alipay.AlipayWithdrawalService;
import com.mouchina.admin.service.api.order.UserRemiburseAdminService;
import com.mouchina.admin.service.api.order.WithdrawalHistoryService;
import com.mouchina.base.utils.DateUtils;
import com.mouchina.moumou_server.entity.order.WithdrawalHistoryOrder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by douzy on 15/10/19.
 */
@Service
public class AlipayWithdrawalServiceSupport implements AlipayWithdrawalService {
    @Resource
    private UserRemiburseAdminService userRemiburseAdminService;
    @Resource
    private WithdrawalHistoryService withdrawalHistoryService;
    @Resource
    private Env env;
    static Logger logger = LogManager.getLogger();

    @Override
    public Integer   updateWithdrawalHistoryOrder(WithdrawalHistoryOrder withdrawalHistoryOrder) {
        return withdrawalHistoryService.updateWithdrawalHistoryOrder(withdrawalHistoryOrder);
    }

    /**
     *提现 回调
     * @param batchNo 批次号
     * @param success_details 成功串
     * @param fail_details 失败串
     * @return
     * @throws java.text.ParseException
     */
    @Override
    public boolean aliNotify(String batchNo,String success_details,String fail_details) throws ParseException {

        Map map=new HashMap();
        map.put("batchNo",batchNo);
        WithdrawalHistoryOrder withdrawalHistoryOrder=withdrawalHistoryService.selectWithdrawalOrderModel(map);//    batchNo
        if(withdrawalHistoryOrder==null||withdrawalHistoryOrder.getState()!=4) {//存在提现记录 且 状态为提现中
//            response.getWriter().println("fail");
            logger.info("无法通过该批次号找到提现数据或该提现已经操作: 批次号:" + batchNo + " 状态:" + withdrawalHistoryOrder.getState());
            return false;
        }
        //请在这里加上商户的业务逻辑程序代码
        if(!StringUtils.isBlank(success_details)) {
            String[] successArr=success_details.split("\\^");
            //成功
            if(successArr.length>0&&successArr.length==8) {//支付宝返回参数 固定长度8
                String payNo = successArr[0], account = successArr[1],
                        accountName = successArr[2], price = successArr[3],
                        successStr = successArr[4], successReason = successArr[5],
                        thirdPartyPayNo = successArr[6], finshTime = successArr[7];
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
                Date date=simpleDateFormat.parse(finshTime);
                logger.info("*********************成功参数*********************");
                logger.info("流水号(内):"+payNo);
                logger.info("收款方账号:"+account);
                logger.info("收款账号姓名:"+accountName);
                logger.info("付款金额:"+price);
                logger.info("成功标识:"+successStr);
                logger.info("成功原因:"+successReason);
                logger.info("流水号(支付宝):"+thirdPartyPayNo);
                logger.info("完成时间:"+finshTime);
                logger.info("*********************成功参数*********************");
                withdrawalHistoryOrder.setThirdPartyPayNo(thirdPartyPayNo);
                withdrawalHistoryOrder.setState((byte) 2);
                withdrawalHistoryOrder.setFinishTime(date);
                withdrawalHistoryService.updateWithdrawalHistoryOrder(withdrawalHistoryOrder);
            }
        }
        //失败
        if(!StringUtils.isBlank(fail_details)) {
            String[] failArr = fail_details.split("\\^");
            if (failArr.length > 0 && failArr.length == 8) {//支付宝返回参数 固定长度8
                String payNo = failArr[0], account = failArr[1],
                        accountName = failArr[2], price = failArr[3],
                        failStr = failArr[4], failReason = failArr[5],
                        thirdPartyPayNo = failArr[6], finshTime = failArr[7];

                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
                Date date=simpleDateFormat.parse(finshTime);
                logger.info("*********************失败参数*********************");
                logger.info("流水号(内):"+payNo);
                logger.info("收款方账号:"+account);
                logger.info("收款账号姓名:"+accountName);
                logger.info("付款金额:"+price);
                logger.info("失败标识:"+failStr);
                logger.info("失败原因:"+failReason);
                logger.info("流水号(支付宝):"+thirdPartyPayNo);
                logger.info("完成时间:"+finshTime);
                logger.info("*********************失败参数*********************");

                withdrawalHistoryOrder.setThirdPartyPayNo(thirdPartyPayNo);
                withdrawalHistoryOrder.setState((byte) 3);
                withdrawalHistoryOrder.setFinishTime(date);

                withdrawalHistoryService.updateWithdrawalHistoryOrder(withdrawalHistoryOrder);
            }
        }
        return true;
    }
    /**
     * 批量付款
     * @param map
     * @return
     */
    @Override
    public String buildForm(Map map) throws Exception {

        logger.info("=================支付宝批量付款入口==============");

        //服务器异步通知页面路径
        String notify_url =env.getProp("withdrawal.alipay.notify.url");// "http://test7.eachbaby.com/admin/welfare/withdrawal/ali_notify.html";
        //需http://格式的完整路径，不允许加?id=123这类自定义参数
        logger.info("notify:" + notify_url);
        //付款账号
        String email = AlipayConfig.seller_email;
        //必填

        //付款账户名
        String account_name = AlipayConfig.seller_user;
        //必填，个人支付宝账号是真实姓名公司支付宝账号是公司名称
        //付款当天日期
        String pay_date = DateUtils.getDateStringYMD(new Date());
        //必填，格式：年[4位]月[2位]日[2位]，如：20100801

        //批次号
        String batch_no = userRemiburseAdminService.userReimburseNoGenerator();
        //必填，格式：当天日期[8位]+序列号[3至16位]，如：201008010000001
        logger.info("批次号：" + batch_no);
        //付款总金额
        //必填，格式：流水号1^收款方帐号1^真实姓名^付款金额1^备注说明1|流水号2^收款方帐号2^真实姓名^付款金额2^备注说明2....
        Long withId = Long.valueOf(map.get("wid").toString());
        WithdrawalHistoryOrder withdrawalHistoryOrder = withdrawalHistoryService.selectWithdrawalHistoryOrder(withId);
        String sHtmlText = null;
//        UserReimburse userReimburse = userRemiburseAdminService.selectUserReimburse(map.get("reimburseNo").toString());
        logger.info("当前付款状态:" + withdrawalHistoryOrder.getState());
        //存在该笔提现数据  且 没有操作过提现
        if (withdrawalHistoryOrder != null && withdrawalHistoryOrder.getState() != 2) {


            //订单状态为退款中 续用原退款批次号 防止重复退款   退款异常 退款失败  新生成批次号
            if(!StringUtils.isBlank(withdrawalHistoryOrder.getBatchNo())&&withdrawalHistoryOrder.getState()== CommonEnumUtil.Reimburse.ISREFOUND.getState()) {
                String batchNo = withdrawalHistoryOrder.getBatchNo();
                String sdate = batchNo.substring(0, 8);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
                Date sDate = simpleDateFormat.parse(sdate);
                if (util.daysBetween(sDate, new Date()) == 0) {
                    batch_no = withdrawalHistoryOrder.getBatchNo();
                    logger.info("批次号已生成，续用原批次号:" + batch_no);
                }
            }
            String payNo = withdrawalHistoryOrder.getWithdrawlHistoryOrderNo();
            withdrawalHistoryOrder.setBatchNo(batch_no);
            withdrawalHistoryOrder.setState((byte) 4);
//            userReimburse.setBatchNo(batch_no);
//            userReimburse.setState((byte)4);//退款中
//            userReimburse.setTableNum(getOrderNoTableNum(userReimburse.getReimburseNo()));
            updateWithdrawalHistoryOrder(withdrawalHistoryOrder);  //更新付款批次号

            String batch_fee =util.changeF2Y(withdrawalHistoryOrder.getPrice().toString()) ;

            logger.info("提现金额：" + batch_fee);

            logger.info("流水号：" + payNo);
//            Map searchMap = new HashMap();
//            String tableNum = getOrderNoTableNum(orderView.getOrder().getOrderNo());
//            searchMap.put("tableNum", tableNum);
//            searchMap.put("payState", 2);
//            searchMap.put("payNo", payNo);
//            searchMap.put("orderNo", orderView.getOrder().getOrderNo());
//            PayOrder payOrder = payOrderAdminService.selectPayOrderModel(searchMap);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(payNo);
            stringBuffer.append("^");
            stringBuffer.append(withdrawalHistoryOrder.getAccount());
            stringBuffer.append("^");
            stringBuffer.append(withdrawalHistoryOrder.getAccountName());
            stringBuffer.append("^");
            stringBuffer.append(batch_fee);
            stringBuffer.append("^");
            stringBuffer.append("哞哞福袋提现");
//            String reimburseCause= StringUtils.isBlank(map.get("reimburseCause").toString())?"协商退款":map.get("reimburseCause").toString();
//            stringBuffer.append(reimburseCause);
            //退款详细数据
            String detail_data = stringBuffer.toString();
            logger.info("付款详细数据:" + detail_data);
            //退款笔数
            String batch_num = "1";
            //必填，参数detail_data的值中，“#”字符出现的数量加1，最大支持1000笔（即“#”字符出现的数量999个）

            //////////////////////////////////////////////////////////////////////////////////

            //把请求参数打包成数组
            Map<String, String> sParaTemp = new HashMap<String, String>();
            sParaTemp.put("service", "batch_trans_notify");
            sParaTemp.put("partner", AlipayConfig.partner);
            sParaTemp.put("_input_charset", AlipayConfig.input_charset);
            sParaTemp.put("notify_url", notify_url);
            sParaTemp.put("email", email);
            sParaTemp.put("account_name", account_name);
            sParaTemp.put("pay_date", pay_date);
            sParaTemp.put("batch_no", batch_no);
            sParaTemp.put("batch_fee", batch_fee);
            sParaTemp.put("batch_num", batch_num);
            sParaTemp.put("detail_data", detail_data);
            //建立请求
            sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "get", "确认");

            logger.info("=================支付宝批量付款入口==============");
        } else {
            logger.info("付款异常");
            sHtmlText = "付款异常。";
        }
        return sHtmlText;
    }
}
