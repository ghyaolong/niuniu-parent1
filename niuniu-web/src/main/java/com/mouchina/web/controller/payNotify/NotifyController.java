package com.mouchina.web.controller.payNotify;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mouchina.moumou_server.entity.pay.PayOrder;
import com.mouchina.moumou_server_interface.award.TaskConfigService;
import com.mouchina.moumou_server_interface.award.TaskHistoryService;
import com.mouchina.moumou_server_interface.member.UserAssetsService;
import com.mouchina.moumou_server_interface.pay.PayOrderService;
import com.mouchina.moumou_server_interface.pay.PayOrderView;
import com.mouchina.moumou_server_interface.view.PayOrderException;
import com.mouchina.moumou_server_interface.view.PayOrderState;
import com.mouchina.moumou_server_interface.view.PayResult;
import com.mouchina.moumou_server_interface.view.WxPayResult;
import com.mouchina.web.pay.alipay.util.AlipayNotify;
import com.mouchina.web.pay.tenpay.ResponseHandler;

/**
 * Created by douzy on 16/2/15.
 */
@Controller
@RequestMapping(value = "/notify")
public class NotifyController {
	private static final Logger logger = LoggerFactory
			.getLogger(NotifyController.class);

	@Autowired
	private PayOrderService payOrderService;
	@Resource
	private UserAssetsService userAssetsService;
	

	/**
	 * SYSTEMERROR(0),
	 * SUCCESS(1),PAYORDERNOEXITS(2),ORDERNOEXITS(3),PAYNOLOCKED(
	 * 5),PAYNOFINISHED
	 * (6),ORDERNOFINISHED(7),CASHCOUPONINVALID(8),CASHCOUPONNOEXITS(9) 0系统错误
	 * 1成功 2支付订单不存在 3订单号不存在 5支付订单被锁 6支付已经完成 7订单已经完成 8优惠券无效 9优惠券不存在
	 */

	@RequestMapping(value = "/alipay_notify")
	public void aliPayNotify(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, String> params = new HashMap<>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
//			if(!"sign_type".equals(name))
				params.put(name, valueStr);
				
				logger.info("====> aliPayNotify  key="+name+",value="+valueStr);
		
		}
		// 商户订单号
		String out_trade_no = new String(request.getParameter("out_trade_no")
				.getBytes("ISO-8859-1"), "UTF-8");
		// 支付宝交易号
		String trade_no = new String(request.getParameter("trade_no").getBytes(
				"ISO-8859-1"), "UTF-8");
		// 交易状态
		String trade_status = new String(request.getParameter("trade_status")
				.getBytes("ISO-8859-1"), "UTF-8");

		String total_fee = new String(request.getParameter("total_fee")
				.getBytes("ISO-8859-1"), "UTF-8");

		boolean verifyFlag =AlipayNotify.verify(params);

		System.out.println("====> aliPayNotify  out_trade_no : " + out_trade_no
				+ " ,trade_no:" + trade_no + ",total_fee=" + total_fee
				+ ",verifyFlag:" + verifyFlag + "<====");
		if (verifyFlag) {
			System.out.println("==================================================");
			System.out.println("====> aliPayNotify  trade_status : " + trade_status
					+ " <====");

			if (trade_status.equals("TRADE_FINISHED")
					|| trade_status.equals("TRADE_SUCCESS")) {
				// ------------------------------
				// 处理业务开始
				// ------------------------------
				PayOrderView payOrderView = new PayOrderView();
				PayOrder payOrder = new PayOrder();
				payOrder.setPayNo(out_trade_no);
				payOrder.setThirdPartyPayNo(trade_no);
				int payPrice = new BigDecimal(total_fee).multiply(
						new BigDecimal(100)).intValue();
				payOrder.setPayPrice(payPrice);
				payOrderView.setPayOrder(payOrder);

				try {
					PayResult<Integer> payResult = payOrderService
							.updatePayOrderCheckAndUnlock(payOrderView);
					// logger.debug("updatePayOrderCheckAndUnlock getPayOrderState ="
					// + payResult.getPayOrderState().toString() +
					// ",getPayOrderSateContent=" +
					// payResult.getPayOrderSateContent());

					if (payResult != null) {
						System.out
								.println("updatePayOrderCheckAndUnlock getPayOrderState ="
										+ payResult.getPayOrderState()
										+ ",getPayOrderSateContent="
										+ payResult.getPayOrderSateContent());
						if (payResult.getPayOrderState() == PayOrderState.SUCCESS) {
							System.out.println("==> aliPayNotify SUCCESS <==");
							response.getWriter().println("success");
							return;
						} else {
							if (payResult.getPayOrderState() == PayOrderState.PAYNOFINISHED
									|| payResult.getPayOrderState() == PayOrderState.ORDERNOFINISHED) {
								System.out.println("==> aliPayNotify FAIL PAYNOFINISHED or ORDERNOFINISHED  <==");
								response.getWriter().println("success");
								return;
							} else if (payResult.getPayOrderState() == PayOrderState.PAYPRICEERROR) {
								System.out.println("==> aliPayNotify FAIL payprice error  <==");
								response.getWriter().println("success");
								return;
							} else {
								response.getWriter().println("fail");
								return;
							}
						}
					} else {
						System.out.println("==> payResult null <==");
						response.getWriter().println("fail");
						return;
					}
				} catch (PayOrderException e) {
					System.out.println("----------------------------------updatePayOrderCheckAndUnlock process  payNo "
							+ payOrder.getPayNo()
							+ "   OrderException="
							+ e.toExecptionString());
					if (e.getPayOrderState() == PayOrderState.PAYNOFINISHED
							|| e.getPayOrderState() == PayOrderState.ORDERNOFINISHED) {
						System.out.println("==> aliPayNotify FAIL PAYNOFINISHED or ORDERNOFINISHED  <==");
						response.getWriter().println("success");
						return;
					} else if (e.getPayOrderState() == PayOrderState.PAYPRICEERROR) {
						logger.error("==> aliPayNotify FAIL payprice error  <==");
						response.getWriter().println("success");
						return;
					} else {
						response.getWriter().println("fail");
						return;
					}
				}
				// ------------------------------
				// 处理业务完毕
				// ------------------------------
			} else {
				response.getWriter().println("success");
				return;
			}
		} else {
			System.out.println("=================================================");
			System.out.println("====> aliPayNotify error : verify params <====");
			System.out.println("=================================================");
			response.getWriter().println("fail");
			return;
		}
	}

	// V3
	@RequestMapping(value = "/weixinpay_notify")
	public void weixinPayNotify(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String, String> resultMap = parseXml(request);
		WxPayResult wxPayResult = new WxPayResult();
		wxPayResult.setAppid(resultMap.get("appid"));
		wxPayResult.setAttach(resultMap.get("attach"));
		wxPayResult.setBank_type(resultMap.get("bank_type"));
		wxPayResult.setCash_fee(resultMap.get("cash_fee"));
		wxPayResult.setCash_fee_type(resultMap.get("cash_fee_type"));
		wxPayResult.setCoupon_count(resultMap.get("coupon_count"));
		wxPayResult.setCoupon_fee(resultMap.get("coupon_fee"));
		wxPayResult.setDevice_info(resultMap.get("device_info"));
		wxPayResult.setErr_code(resultMap.get("err_code"));
		wxPayResult.setErr_code_des(resultMap.get("err_code_des"));
		wxPayResult.setFee_type(resultMap.get("fee_type"));
		wxPayResult.setIs_subscribe(resultMap.get("is_subscribe"));
		wxPayResult.setMch_id(resultMap.get("mch_id"));
		wxPayResult.setNonce_str(resultMap.get("nonce_str"));
		wxPayResult.setOpenid(resultMap.get("openid"));
		wxPayResult.setOut_trade_no(resultMap.get("out_trade_no"));
		wxPayResult.setResult_code(resultMap.get("result_code"));
		wxPayResult.setSign(resultMap.get("sign"));
		wxPayResult.setTime_end(resultMap.get("time_end"));
		wxPayResult.setTotal_fee(resultMap.get("total_fee"));
		wxPayResult.setTrade_type(resultMap.get("trade_type"));
		wxPayResult.setTransaction_id(resultMap.get("transaction_id"));

		if (logger.isInfoEnabled()) {
			logger.info("==================================================");
			logger.info("====> weixinPayNotify wxPayResult="
					+ wxPayResult.toString() + "<====");
			logger.info("==================================================");
		}

		String successXml = "<xml>"
				+ "<return_code><![CDATA[SUCCESS]]></return_code>"
				+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
		String failXml = "<xml>"
				+ "<return_code><![CDATA[FAIL]]></return_code>"
				+ "<return_msg><![CDATA[FAIL]]></return_msg>" + "</xml> ";

		ResponseHandler resHandler = new ResponseHandler(request, response);
		if ("SUCCESS".equals(wxPayResult.getResult_code())) {
			// ------------------------------
			// 处理业务开始
			// ------------------------------
			PayOrderView payOrderView = new PayOrderView();
			PayOrder payOrder = new PayOrder();
			payOrder.setPayNo(wxPayResult.getOut_trade_no());
			payOrder.setPayPrice(Integer.valueOf(wxPayResult.getTotal_fee()));
			payOrder.setThirdPartyPayNo(wxPayResult.getTransaction_id());
			payOrderView.setPayOrder(payOrder);
			try {
				PayResult<Integer> payResult = payOrderService
						.updatePayOrderCheckAndUnlock(payOrderView);

				if (payResult != null) {
					logger.info("updatePayOrderCheckAndUnlock getPayOrderState ="
							+ payResult.getPayOrderState()
							+ ",getPayOrderSateContent="
							+ payResult.getPayOrderSateContent());
					if (payResult.getPayOrderState() == PayOrderState.SUCCESS) {
						logger.info("==> weixinPay SUCCESS <==");
						resHandler.sendToCFT(successXml);
						return;
					} else {
						if (payResult.getPayOrderState() == PayOrderState.PAYNOFINISHED
								|| payResult.getPayOrderState() == PayOrderState.ORDERNOFINISHED) {
							logger.error("==> weixinPay FAIL PAYNOFINISHED or ORDERNOFINISHED  <==");
							resHandler.sendToCFT(successXml);
							return;
						} else if (payResult.getPayOrderState() == PayOrderState.PAYPRICEERROR) {
							logger.error("==> weixinPay FAIL payprice error  <==");
							response.getWriter().println("success");
							return;
						} else {
							resHandler.sendToCFT(failXml);
							return;
						}
					}
				} else {
					logger.error("==> payResult null <==");
					response.getWriter().println(successXml);
					return;
				}

			} catch (PayOrderException e) {
				logger.error("----------------------------------updatePayOrderCheckAndUnlock process  payNo "
						+ payOrder.getPayNo()
						+ "   OrderException="
						+ e.toExecptionString());
				if (e.getPayOrderState() == PayOrderState.PAYNOFINISHED
						|| e.getPayOrderState() == PayOrderState.ORDERNOFINISHED) {
					logger.error("==> weixinPay FAIL PAYNOFINISHED or ORDERNOFINISHED  <==");
					resHandler.sendToCFT(successXml);
					return;
				} else if (e.getPayOrderState() == PayOrderState.PAYPRICEERROR) {
					logger.error("==> weixinPay FAIL payprice error  <==");
					response.getWriter().println("success");
					return;
				} else {
					response.getWriter().println(failXml);
					return;
				}
			}
			// ------------------------------
			// 处理业务完毕
			// ------------------------------
		} else {
			response.getWriter().println(failXml);
			return;
		}
	}

	@RequestMapping(value = "/weixinpay_notify3")
	public void weixinPayNotify3(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String, String> resultMap = parseXml(request);
		WxPayResult wxPayResult = new WxPayResult();
		wxPayResult.setAppid(resultMap.get("appid"));
		wxPayResult.setAttach(resultMap.get("attach"));
		wxPayResult.setBank_type(resultMap.get("bank_type"));
		wxPayResult.setCash_fee(resultMap.get("cash_fee"));
		wxPayResult.setCash_fee_type(resultMap.get("cash_fee_type"));
		wxPayResult.setCoupon_count(resultMap.get("coupon_count"));
		wxPayResult.setCoupon_fee(resultMap.get("coupon_fee"));
		wxPayResult.setDevice_info(resultMap.get("device_info"));
		wxPayResult.setErr_code(resultMap.get("err_code"));
		wxPayResult.setErr_code_des(resultMap.get("err_code_des"));
		wxPayResult.setFee_type(resultMap.get("fee_type"));
		wxPayResult.setIs_subscribe(resultMap.get("is_subscribe"));
		wxPayResult.setMch_id(resultMap.get("mch_id"));
		wxPayResult.setNonce_str(resultMap.get("nonce_str"));
		wxPayResult.setOpenid(resultMap.get("openid"));
		wxPayResult.setOut_trade_no(resultMap.get("out_trade_no"));
		wxPayResult.setResult_code(resultMap.get("result_code"));
		wxPayResult.setSign(resultMap.get("sign"));
		wxPayResult.setTime_end(resultMap.get("time_end"));
		wxPayResult.setTotal_fee(resultMap.get("total_fee"));
		wxPayResult.setTrade_type(resultMap.get("trade_type"));
		wxPayResult.setTransaction_id(resultMap.get("transaction_id"));

		if (logger.isInfoEnabled()) {
			logger.info("==================================================");
			logger.info("====> weixinPayNotify wxPayResult="
					+ wxPayResult.toString() + "<====");
			logger.info("==================================================");
		}

		String successXml = "<xml>"
				+ "<return_code><![CDATA[SUCCESS]]></return_code>"
				+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
		String failXml = "<xml>"
				+ "<return_code><![CDATA[FAIL]]></return_code>"
				+ "<return_msg><![CDATA[FAIL]]></return_msg>" + "</xml> ";

		ResponseHandler resHandler = new ResponseHandler(request, response);
		if ("SUCCESS".equals(wxPayResult.getResult_code())) {
			// ------------------------------
			// 处理业务开始
			// ------------------------------
			PayOrderView payOrderView = new PayOrderView();
			PayOrder payOrder = new PayOrder();
			payOrder.setPayNo(wxPayResult.getOut_trade_no());
			payOrder.setPayPrice(Integer.valueOf(wxPayResult.getTotal_fee()));
			payOrder.setThirdPartyPayNo(wxPayResult.getTransaction_id());
			payOrderView.setPayOrder(payOrder);
			try {
				PayResult<Integer> payResult = payOrderService
						.updatePayOrderCheckAndUnlock(payOrderView);

				if (payResult != null) {
					logger.info("updatePayOrderCheckAndUnlock getPayOrderState ="
							+ payResult.getPayOrderState()
							+ ",getPayOrderSateContent="
							+ payResult.getPayOrderSateContent());
					if (payResult.getPayOrderState() == PayOrderState.SUCCESS) {
						logger.info("==> weixinPay SUCCESS <==");
						resHandler.sendToCFT(successXml);
						return;
					} else {
						if (payResult.getPayOrderState() == PayOrderState.PAYNOFINISHED
								|| payResult.getPayOrderState() == PayOrderState.ORDERNOFINISHED) {
							logger.error("==> weixinPay FAIL PAYNOFINISHED or ORDERNOFINISHED  <==");
							resHandler.sendToCFT(successXml);
							return;
						} else if (payResult.getPayOrderState() == PayOrderState.PAYPRICEERROR) {
							logger.error("==> weixinPay FAIL payprice error  <==");
							response.getWriter().println("success");
							return;
						} else {
							resHandler.sendToCFT(failXml);
							return;
						}
					}
				} else {
					logger.error("==> payResult null <==");
					response.getWriter().println(successXml);
					return;
				}

			} catch (PayOrderException e) {
				logger.error("----------------------------------updatePayOrderCheckAndUnlock process  payNo "
						+ payOrder.getPayNo()
						+ "   OrderException="
						+ e.toExecptionString());
				if (e.getPayOrderState() == PayOrderState.PAYNOFINISHED
						|| e.getPayOrderState() == PayOrderState.ORDERNOFINISHED) {
					logger.error("==> weixinPay FAIL PAYNOFINISHED or ORDERNOFINISHED  <==");
					resHandler.sendToCFT(successXml);
					return;
				} else if (e.getPayOrderState() == PayOrderState.PAYPRICEERROR) {
					logger.error("==> weixinPay FAIL payprice error  <==");
					response.getWriter().println("success");
					return;
				} else {
					response.getWriter().println(failXml);
					return;
				}
			}
			// ------------------------------
			// 处理业务完毕
			// ------------------------------
			
		} else {
			response.getWriter().println(failXml);
			return;
		}
	}
	
	private static Map<String, String> parseXml(HttpServletRequest request)
			throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		InputStream inputStream = request.getInputStream();
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		Element root = document.getRootElement();
		List<Element> elementList = root.elements();
		for (Element e : elementList) {
			map.put(e.getName(), e.getText());
		}
		inputStream.close();
		return map;
	}
}