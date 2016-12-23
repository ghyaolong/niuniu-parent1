package com.mouchina.admin.controller.order;

import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipayNotify;
import com.alipay.util.AlipaySubmit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by douzy on 16/2/4.
 */
@Controller
@RequestMapping( "/order/test_pay" )
public class TestPayController {
    static Logger logger = LogManager.getLogger();
    @RequestMapping("/ali")
    private String aliTestPay() {
        return "/order/payorder/test_pay";
    }
    @RequestMapping("/alipayapi")
    private String alipayApi(HttpServletRequest request,ModelMap modelMap) throws UnsupportedEncodingException {
        ////////////////////////////////////请求参数//////////////////////////////////////

        //服务器异步通知页面路径
        String notify_url = "http://localhost:8082/order/test_pay/ali_notify.html";
        //需http://格式的完整路径，不允许加?id=123这类自定义参数

        //支付类型
        String payment_type = "1";
        //必填，不能修改
        //页面跳转同步通知页面路径
        String return_url = "http://商户网关地址/create_direct_pay_by_user-JAVA-UTF-8/return_url.jsp";
        //需http://格式的完整路径，不能加?id=123这类自定义参数，不能写成http://localhost/

        //商户订单号
        String out_trade_no = new String(request.getParameter("WIDout_trade_no").getBytes("ISO-8859-1"),"UTF-8");
        //商户网站订单系统中唯一订单号，必填

        //订单名称
        String subject = new String(request.getParameter("WIDsubject").getBytes("ISO-8859-1"),"UTF-8");
        //必填

        //付款金额
        String total_fee = new String(request.getParameter("WIDtotal_fee").getBytes("ISO-8859-1"),"UTF-8");
        //必填

        //订单描述

        String body = new String(request.getParameter("WIDbody").getBytes("ISO-8859-1"),"UTF-8");
        //商品展示地址
        String show_url = new String(request.getParameter("WIDshow_url").getBytes("ISO-8859-1"),"UTF-8");
        //需以http://开头的完整路径，例如：http://www.商户网址.com/myorder.html

        //防钓鱼时间戳
        String anti_phishing_key = "";
        //若要使用请调用类文件submit中的query_timestamp函数

        //客户端的IP地址
        String exter_invoke_ip = "";
        //非局域网的外网IP地址，如：221.0.0.1
        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("service", "create_direct_pay_by_user");
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("seller_email", AlipayConfig.seller_email);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
        sParaTemp.put("payment_type", payment_type);
        sParaTemp.put("notify_url", notify_url);
        sParaTemp.put("return_url", return_url);
        sParaTemp.put("out_trade_no", out_trade_no);
        sParaTemp.put("subject", subject);
        sParaTemp.put("total_fee", total_fee);
        sParaTemp.put("body", body);
        sParaTemp.put("show_url", show_url);
        sParaTemp.put("anti_phishing_key", anti_phishing_key);
        sParaTemp.put("exter_invoke_ip", exter_invoke_ip);

        //建立请求
        String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "get", "确认");
        modelMap.put("aliHtml",sHtmlText);
        return "order/payorder/alipayApi";
    }
    @RequestMapping(value = "/ali_notify",method = RequestMethod.POST)
    private void aliPayNotify(HttpServletRequest request,ModelMap modelMap,HttpServletResponse response) throws IOException {

        logger.info("===================test 支付宝付款回调==============");
        //获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<String,String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        //批量付款数据中转账成功的详细信息

        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
        logger.info("==============out_trade_no:"+out_trade_no);
        //支付宝交易号

        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
        logger.info("==============trade_no:"+trade_no);
        //交易状态
        String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
        logger.info("==============trade_status:"+trade_status);
        if(AlipayNotify.verify(params)){//验证成功
            logger.info("==============参数校验成功  开始业务处理==============");
            //////////////////////////////////////////////////////////////////////////////////////////
            //请在这里加上商户的业务逻辑程序代码
//            alipayRemiburseService.alipayNotify(batch_no,result_details);
            //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
            //判断是否在商户网站中已经做过了这次通知返回的处理
            //如果没有做过处理，那么执行商户的业务程序
            //如果有做过处理，那么不执行商户的业务程序

            response.getWriter().println("success");    //请不要修改或删除
            logger.info("success");
            //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
            logger.info("==============参数校验成功  业务处理结束==============");
            //////////////////////////////////////////////////////////////////////////////////////////
        }else {//验证失败
            logger.info("==============参数校验失败  返回fail==============");
            response.getWriter().println("fail");
            logger.info("fail");
        }
        logger.info("===================支付宝付款回调==============");
        //return "order/userremiburse/list";
    }
}
