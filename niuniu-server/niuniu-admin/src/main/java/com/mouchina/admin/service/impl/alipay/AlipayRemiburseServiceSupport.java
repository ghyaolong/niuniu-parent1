package com.mouchina.admin.service.impl.alipay;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipaySubmit;
import com.mouchina.admin.base.common.CommonEnumUtil;
import com.mouchina.admin.base.common.util;
import com.mouchina.admin.base.config.Env;
import com.mouchina.admin.service.api.alipay.AlipayRemiburseService;
import com.mouchina.admin.service.api.order.OrderAdminService;
import com.mouchina.admin.service.api.order.PayOrderAdminService;
import com.mouchina.admin.service.api.order.UserRemiburseAdminService;
import com.mouchina.base.utils.DateUtils;
import com.mouchina.moumou_server.entity.pay.PayOrder;
import com.mouchina.moumou_server.entity.pay.UserReimburse;
import com.mouchina.moumou_server_interface.order.OrderView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by douzy on 15/10/9.
 */
@Service
public class AlipayRemiburseServiceSupport implements AlipayRemiburseService {

    @Resource
    private UserRemiburseAdminService userRemiburseAdminService;
    @Resource
    private OrderAdminService orderAdminService;
    @Resource
    private PayOrderAdminService payOrderAdminService;
    @Resource
    private Env env;
    static Logger logger = LogManager.getLogger();
    public static String getOrderNoTableNum(String orderNo) {
        String tableNum = orderNo.substring(0, 4);
        return tableNum;
    }
    @Override
    public  void alipayNotify(String batch_no,String result_details) {
        if(!StringUtils.isBlank(result_details)) {
            String[] resultArr=result_details.split("\\^");

            String payNo=resultArr[0];   // 交易号
            String payPrice=resultArr[1];  //交易金额
            String payResult=resultArr[2]; //交易结果
            logger.info("退款交易号:"+payNo);
            logger.info("退款交易金额:"+payPrice);
            logger.info("退款交易结果:"+payResult);
            Map searchMap=new HashMap();
            searchMap.put("batchNo",batch_no);
            searchMap.put("tableNum",getOrderNoTableNum(batch_no));
            UserReimburse userReimburseModel=userRemiburseAdminService.getReimburseModel(searchMap);

            byte st=userReimburseModel.getState();
            //已退款 或退款中
            if(userReimburseModel==null||
                    st== CommonEnumUtil.Reimburse.SUCCESS.getState()) {
                return;
            }
            int pp = new BigDecimal(payPrice).multiply(new BigDecimal(100)).intValue();

            logger.info("应退金额:"+userReimburseModel.getPrice() +"实退金额:"+pp+"("+payPrice+")");
            userReimburseModel.setPayNo(payNo);
            byte state=CommonEnumUtil.Reimburse.FAIL.getState();//默认退款失败
            //退款成功
            if(payResult.equals("SUCCESS")) {
                if (userReimburseModel.getPrice() == pp) {
                    state = CommonEnumUtil.Reimburse.SUCCESS.getState();//退款成功
                    userRemiburseAdminService.updateAgreeUserReimbursePrice(userReimburseModel);
                } else {
                    state = CommonEnumUtil.Reimburse.ERROR.getState();//退款异常
                }
            }
            if(state!=2) {        //异常状态
                userReimburseModel.setState(state);
                userRemiburseAdminService.updateStateReimburseError(userReimburseModel);
//                String[] orderDetailArr=userReimburseModel.getOrderDetailNos().split(",");
//                for(String orderDetail :orderDetailArr) {
//
//                }
            }
            logger.info("退款结果:"+(state==CommonEnumUtil.Reimburse.SUCCESS.getState()?"退款成功":state==CommonEnumUtil.Reimburse.FAIL.getState()?"退款失败":"退款异常"));
//            userReimburseModel.setState(state);
//            userReimburseModel.setTableNum(getOrderNoTableNum(userReimburseModel.getReimburseNo()));
//            int re= userRemiburseAdminService.updateReimburse(userReimburseModel);
            //logger.info("DB更新结果:"+re);
        }
    }
    @Override
    public String buildFormHtml(Map map) throws ParseException {

        logger.info("=================支付宝退款入口==============");

        //服务器异步通知页面路径
        String notify_url =env.getProp("reimburse.alipay.notify.url");// "http://test7.eachbaby.com/admin/order/userremiburse/ali_notify.html";
        //需http://格式的完整路径，不允许加?id=123这类自定义参数
        logger.info("notify:"+notify_url);
        //卖家支付宝帐户
        String seller_email = AlipayConfig.seller_email;
        //必填
        //退款当天日期
        String refund_date = DateUtils.getNowDateString();
        logger.info("refund_date:"+refund_date);
        //必填，格式：年[4位]-月[2位]-日[2位] 小时[2位 24小时制]:分[2位]:秒[2位]，如：2007-10-01 13:13:13

        String sHtmlText = null;
        UserReimburse userReimburse = userRemiburseAdminService.selectUserReimburse(map.get("reimburseNo").toString());
        logger.info("退款状态:"+userReimburse.getState());
        String batch_no = userRemiburseAdminService.userReimburseNoGenerator();
        //存在该笔退款数据  且 没有操作过退款
        if (userReimburse != null&&userReimburse.getState()!=CommonEnumUtil.Reimburse.SUCCESS.getState()) {
            //订单状态为退款中 续用原退款批次号 防止重复退款   退款异常 退款失败  新生成批次号
             if(!StringUtils.isBlank(userReimburse.getBatchNo())&&userReimburse.getState()==CommonEnumUtil.Reimburse.ISREFOUND.getState()) {
                 String batchNo = userReimburse.getBatchNo();
                 String sdate = batchNo.substring(0, 8);
                 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
                 Date sDate = simpleDateFormat.parse(sdate);
                 if (util.daysBetween(sDate, new Date()) == 0) {
                     batch_no = userReimburse.getBatchNo();
                     logger.info("批次号已生成，续用原批次号:" + batch_no);
                 }
             }
            //批次号
            logger.info("退款批次号:"+batch_no);
            userReimburse.setBatchNo(batch_no);
            userReimburse.setState(CommonEnumUtil.Reimburse.ISREFOUND.getState());//退款中
            userReimburse.setTableNum(getOrderNoTableNum(userReimburse.getReimburseNo()));
            userRemiburseAdminService.updateReimburse(userReimburse);  //更新退款批次号
            logger.info("订单号:"+userReimburse.getOrderNo());
            logger.info("退款单号:"+userReimburse.getReimburseNo());
            OrderView orderView = orderAdminService.selectOrderView(userReimburse.getOrderNo());
            String payNo = orderView.getOrder().getPayNo();
            logger.info("支付单号:"+payNo);

            Map searchMap = new HashMap();
            String tableNum = getOrderNoTableNum(orderView.getOrder().getOrderNo());
            searchMap.put("tableNum", tableNum);
            searchMap.put("payState", 2);
            searchMap.put("payNo", payNo);
            searchMap.put("orderNo", orderView.getOrder().getOrderNo());
            PayOrder payOrder = payOrderAdminService.selectPayOrderModel(searchMap);
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(payOrder.getThirdPartyPayNo());
            stringBuffer.append("^");
            stringBuffer.append(map.get("price"));
            stringBuffer.append("^");
            String reimburseCause= StringUtils.isBlank(map.get("reimburseCause").toString())?"协商退款":map.get("reimburseCause").toString();
            stringBuffer.append(reimburseCause);
            //退款详细数据
            String detail_data = stringBuffer.toString();
            logger.info("退款详细数据:"+detail_data);
            //退款笔数
            String batch_num = "1";
            //必填，参数detail_data的值中，“#”字符出现的数量加1，最大支持1000笔（即“#”字符出现的数量999个）

            //////////////////////////////////////////////////////////////////////////////////

            //把请求参数打包成数组
            Map<String, String> sParaTemp = new HashMap<String, String>();
            sParaTemp.put("service", "refund_fastpay_by_platform_pwd");
            sParaTemp.put("partner", AlipayConfig.partner);
            sParaTemp.put("_input_charset", AlipayConfig.input_charset);
            sParaTemp.put("notify_url", notify_url);
            sParaTemp.put("seller_email", seller_email);
            sParaTemp.put("refund_date", refund_date);
            sParaTemp.put("batch_no", batch_no);
            sParaTemp.put("batch_num", batch_num);
            sParaTemp.put("detail_data", detail_data);

            //建立请求
            sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "get", "确认");

            logger.info("=================支付宝退款入口==============");
        }else {
            logger.info("退款异常 没有该笔订单 或 该笔订单已经退款");
            sHtmlText="退款异常,请确认该笔订单时候存在且没有操作过退款。";
        }
        return sHtmlText;
    }
}
