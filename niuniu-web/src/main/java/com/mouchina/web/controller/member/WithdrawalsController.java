package com.mouchina.web.controller.member;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.redis.RedisLockHandler;
import com.mouchina.base.utils.MD5Util;
import com.mouchina.base.utils.NumberUtil;
import com.mouchina.base.utils.StringUtil;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserAssets;
import com.mouchina.moumou_server.entity.member.UserPart;
import com.mouchina.moumou_server.entity.order.WithDrawalConfig;
import com.mouchina.moumou_server.entity.order.WithdrawalHistoryOrder;
import com.mouchina.moumou_server_interface.award.TaskHistoryService;
import com.mouchina.moumou_server_interface.member.UserAssetsService;
import com.mouchina.moumou_server_interface.order.WithDrawalConfigService;
import com.mouchina.moumou_server_interface.order.WithdrawalsService;
import com.mouchina.moumou_server_interface.view.PayOrderException;
import com.mouchina.moumou_server_interface.view.WelfareResult;
import com.mouchina.moumou_server_interface.view.WithdrawalView;
import com.mouchina.web.base.config.Env;
import com.mouchina.web.base.utils.Constants;
import com.mouchina.web.service.api.member.UserAssetsAdminService;
import com.mouchina.web.service.api.member.UserWebService;
import com.mouchina.web.service.api.vo.WithdrawalHistoryOrderVo;

/**
 * Created by douzy on 16/2/18.
 */
@Controller
@RequestMapping("/withdrawals")
public class WithdrawalsController {
	@Resource
	WithdrawalsService withdrawalHistoryService;
	@Resource
	UserAssetsAdminService userAssetsAdminService;
	@Resource
	UserWebService userWebService;
	@Resource
	WithdrawalsService withdrawalsService;
	@Resource
	UserAssetsService userAssetsService;
	@Resource
	RedisLockHandler redisLockHandler;
	@Resource
	WithDrawalConfigService withDrawalConfigService;
	@Resource
	Env env;
	@Resource
	TaskHistoryService taskHistoryService;

	private static String WITHDRAWAL_USER_LOCK = "withdrawalLocks_user_lock_";

	private static Logger logger = LogManager.getLogger();

	/**
	 * 申请提现
	 * 
	 * @param _req
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/apply", method = { RequestMethod.GET,
			RequestMethod.POST })
	private String applyWithdrawal(HttpServletRequest request, Integer price,
			Byte channel, Integer priceType, ModelMap modelMap) {
		String loginKey = request.getParameter("loginKey");
		String accountName = request.getParameter("accountName");//用户昵称
		String account = request.getParameter("account");//提现账户

		logger.info("================applyWithdrawal loginKey=" + loginKey + ",accountName : " + accountName + ",account : " + account 
				+ "price : " + price + "channel : " + channel + "priceType : " + priceType + "end");
		User user = userWebService.getUserByLoginKey(loginKey.toString());
		if (user == null) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}

		if (price <= 0 || StringUtils.isEmpty(accountName)
				|| StringUtils.isEmpty(account)) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}

		List<String> withdrawalLocks = new ArrayList<String>();

		try {

			String withdrawalLock = WITHDRAWAL_USER_LOCK + user.getId();
			withdrawalLocks.add(withdrawalLock);

			if (redisLockHandler.tryLock(withdrawalLock)) {
				WelfareResult<WithdrawalHistoryOrder> welfareResult = userAssetsAdminService
						.applyUserWithdeawal(user.getId(), price, account,
								accountName.trim(), priceType, channel);

				if (welfareResult.getState() == 1) {
					modelMap.put("result", 1);
					logger.info("weixinRqeust startweixin account=" + account
							+ ",userId=" + user.getId() + ",channel=" + channel
							+ ",priceType=" + priceType + ",accountName="
							+ accountName + ",welfareResult state="
							+ welfareResult.getState()
							+ ", weixinwithdrawal id="
							+ welfareResult.getContent().getId());

//					if (channel == 2) {
//						int weixin = weixinwithdrawalRequst(welfareResult
//								.getContent());
//					}
					//发送异步请求（微信提现）
					int weixin = weixinwithdrawalRequst(welfareResult
							.getContent());
					if(-1 == (weixin)){
						modelMap.put("result", "0");
						modelMap.put("errorCode", Constants.ERROR_CODE_200504);
					}else{
						modelMap.put("result", "1");
						logger.info("提现成功，需要去完成任务-------用户id：[{}]",user.getId());
						taskHistoryService.insertTask(user.getId(), (long)7, "1");
					}
					
				} else {
					modelMap.put("result", "0");
					modelMap.put("errorCode", Constants.ERROR_CODE_200506);
				}
			}
		} catch (PayOrderException e) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_200503);
		} finally {
			for (String lock : withdrawalLocks) {
				redisLockHandler.unLock(lock);
			}
		}

		return "";
	}

	/**
	 * 是否符合提现条件
	 * @param request
	 * @param price
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/withDrawalCheck", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String withDrawalCheck(HttpServletRequest request,Integer price,
			HttpServletResponse response, ModelMap modelMap) throws IOException {

		String loginKey = request.getParameter("loginKey");
		
		if (StringUtil.isEmpty(request.getParameter("loginKey")) || price <= 0) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		
		User user = userWebService.getUserByLoginKey(loginKey);

		if (user != null) {
			UserAssets userAssets = userAssetsService.selectUserAssets(user.getId());
			if(userAssets != null){
				Integer userRedEnvelopeMoney = userAssets.getRedEnvelopeBalance();//用户当前余额
				if(userRedEnvelopeMoney < price){
					logger.info("用户 :" + user.getNickName() + "余额不足！");
					modelMap.put("result", "0");
					modelMap.put("errorCode", Constants.ERROR_CODE_200501);
				}else{
					List<WithDrawalConfig> withDrawalConfigList = withDrawalConfigService.selectByList(new HashMap(){{put("order", "id");}});//提现配置列表
					int maxWithDrawal = -1;//最大可提现金额，-1表示不限制
					double ratio = 0;//最大可以提现比例
					if(withDrawalConfigList != null && withDrawalConfigList.size() > 0){
						for(WithDrawalConfig config : withDrawalConfigList){
							//for循环修改最大可提现金额数值，若执行完for后值未变说明用户余额要么非常打超出了配置规则】或者非常小未进入配置规则，，不做提现限制
							
							int min = config.getMinAmount();
							int max = config.getMaxAmount();
							ratio = config.getMaxWithDrawalRatio();//可提取百分比 
							if(userRedEnvelopeMoney >= min && userRedEnvelopeMoney < max){
								maxWithDrawal = (int)(userRedEnvelopeMoney * ratio);
							}
							
							if(maxWithDrawal > -1){
								break;//跳出for循环
							}
						}
						if(maxWithDrawal == -1){
							modelMap.put("result", "1");
						}else{
							if(price <= maxWithDrawal){
								modelMap.put("result", "1");
							}else{
								modelMap.put("result", "0");
								modelMap.put("errorCode", Constants.ERROR_CODE_200505);
								modelMap.put("error_msg", "提现不能超过账户余额的百分之 :" + NumberUtil.formattedDecimalToPercentage(ratio));
							}
						}
						
					}else{
						modelMap.put("result", "1");//未配置提现规则则直接返回成功，允许用户提现可以提现的钱数
					}
				}
			}else{
				modelMap.put("result", "0");
				modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			}
		} else {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}

		return "";
	}
	
	@RequestMapping(value = "/list", method = { RequestMethod.GET,
			RequestMethod.POST })
	public String inviteList(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) throws IOException {

		String loginKey = request.getParameter("loginKey");

		int begin = 0;
		if (request.getParameter("begin") != null)
			begin = Integer.valueOf(request.getParameter("begin").toString())
					.intValue();

		User user = userWebService.getUserByLoginKey(loginKey);

		if (user != null) {
			modelMap.put("result", "1");
			Map map = new HashMap();
			map.put("page", new Page(begin, 10));
			map.put("order", "create_time desc");
			map.put("userId", user.getId());
			ListRange<WithdrawalView> listRange = withdrawalsService
					.selectListRangeWithdrawalHistory(map).getContent();
			ListRange<WithdrawalHistoryOrderVo> withdrawalHistoryOrderListVo = new ListRange<WithdrawalHistoryOrderVo>();
			withdrawalHistoryOrderListVo.setPage(listRange.getPage());
			List<WithdrawalHistoryOrderVo> listVo = new ArrayList<WithdrawalHistoryOrderVo>();
			for (WithdrawalView withdrawalView : listRange.getData()) {
				WithdrawalHistoryOrderVo withdrawalHistoryOrderVo = new WithdrawalHistoryOrderVo();
				BeanUtils.copyProperties(
						withdrawalView.getWithdrawalHistoryOrder(),
						withdrawalHistoryOrderVo);
				listVo.add(withdrawalHistoryOrderVo);
			}
			withdrawalHistoryOrderListVo.setData(listVo);

			modelMap.put("withdrawalList",
					withdrawalHistoryOrderListVo.getData());
			modelMap.put("page", withdrawalHistoryOrderListVo.getPage());

		} else {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}

		return "";
	}

	public static void main(String args[]) {

		// weixinwithdrawalRequst("20160324112800000001","oHdShw2tOfIAJcrteUMX1YiEaalQ",100);
		// String respon =
		// "<result_code><![CDATA[SUCCESS]]></result_code><partner_trade_no><![CDATA[10013574201505191526582441]]></partner_trade_no><payment_no><![CDATA[1000018301201505190181489473]]></payment_no><payment_time><![CDATA[2015-05-19 15：26：59]]></payment_time></xml>";
		// String payment_no =
		// respon.substring(respon.indexOf("<payment_no><![CDATA["));
		// payment_no = payment_no.substring(21,
		// payment_no.indexOf("]]></payment_no>"));
		//
		// String rep_partner_trade_no = respon.substring(respon
		// .indexOf("<partner_trade_no><![CDATA["));
		// rep_partner_trade_no = rep_partner_trade_no.substring(27,
		// rep_partner_trade_no.indexOf("]]></partner_trade_no>"));
		// System.out.println("payment_no="+payment_no);
		// System.out.println("rep_partner_trade_no="+rep_partner_trade_no);
	}

	/**
	 * 向微信发起提现异步请求
	 * @param withdrawalHistoryOrder
	 * @return 1代表成功 -1代表失败
	 */
	private int weixinwithdrawalRequst(
			WithdrawalHistoryOrder withdrawalHistoryOrder) {
		int result = 0;

		String mch_appid = env.getProp("weixin.mch_appid");// "wx11062cf326bb09bb";
		String mchid = env.getProp("weixin.mchid");// "1315715001";
		String nonce_str = new Date().getTime() + "";

		// String partner_trade_no = "201384989483";
		// String openid = "wx23230902039";
		String check_name = env.getProp("weixin.check_name");// "NO_CHECK";
		String desc = "mouchina";
		String spbill_create_ip = env.getProp("weixin.spbill_create_ip");// "117.33.106.44";
		String signkey = env.getProp("weixin.signkey");// "mmc201603201200xKxLx4o8PJ1afCtk9";

		String stringSign = "amount=" + withdrawalHistoryOrder.getPrice()
				+ "&check_name=" + check_name + "&desc=" + desc + "&mch_appid="
				+ mch_appid + "&mchid=" + mchid + "&nonce_str=" + nonce_str
				+ "&openid=" + withdrawalHistoryOrder.getAccount()
				+ "&partner_trade_no="
				+ withdrawalHistoryOrder.getWithdrawlHistoryOrderNo()
				+ "&spbill_create_ip=" + spbill_create_ip + "&key=" + signkey;
		String sign = MD5Util.md5Hex(stringSign).toLowerCase();

		Map<String, String> requestMap = new HashMap<String, String>();
		requestMap.put("mch_appid", mch_appid + "");
		requestMap.put("mchid", mchid + "");
		requestMap.put("nonce_str", nonce_str + "");
		requestMap.put("partner_trade_no",
				withdrawalHistoryOrder.getWithdrawlHistoryOrderNo());
		requestMap.put("openid", withdrawalHistoryOrder.getAccount());
		requestMap.put("amount", withdrawalHistoryOrder.getPrice() + "");
		requestMap.put("check_name", check_name);
		requestMap.put("desc", desc);
		requestMap.put("spbill_create_ip", spbill_create_ip);
		requestMap.put("sign", sign);

		
		int updateResult = -1;
		String requestXml = getXmlWeiXinInfo(requestMap);
		logger.info("weixinRqeust requestXml=" + requestXml);
		try {
			requestXml = new String(requestXml.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("---------n----requestXml=" + requestXml);
		String payment_no = "";
		String rep_partner_trade_no = "";
		try {
			String respon = wexin(requestXml);//获取微信提现结果响应信息
			
			logger.info("weixinRqeust respon=" + respon);
			/*根据微信回调是否成功修改withdrawalHistoryOrder的state字段 2:成功 3：失败*/
			if (respon
					.indexOf("<return_code><![CDATA[SUCCESS]]></return_code>") > 0
					&& respon
							.indexOf("<result_code><![CDATA[SUCCESS]]></result_code>") > 0) {
				
				payment_no = respon.substring(respon.indexOf("<payment_no><![CDATA["));
				payment_no = payment_no.substring(21,payment_no.indexOf("]]></payment_no>"));//第三方支付号
				rep_partner_trade_no = respon.substring(respon.indexOf("<partner_trade_no><![CDATA["));
				rep_partner_trade_no = rep_partner_trade_no.substring(27,rep_partner_trade_no.indexOf("]]></partner_trade_no>"));
				logger.info("weixinRqeust 返回 ------SUCCESS  respon=" + respon);
				result = 1;
				try {
					withdrawalHistoryOrder.setThirdPartyPayNo(payment_no);
					withdrawalHistoryOrder.setFinishTime(new Date());
					withdrawalHistoryOrder.setState((byte) 2);
					updateResult = withdrawalHistoryService
							.updateWithdrawalHistoryOrder(
									withdrawalHistoryOrder).getState();
				} catch (Exception exception) {
					logger.error("------rep_partner_trade_no="
							+ rep_partner_trade_no + ",payment_no="
							+ payment_no + ",price="
							+ withdrawalHistoryOrder.getPrice());
				}
				
				updateResult = result;
			} else {
				//微信提现消息返回失败
				result = -1;
				try {
					withdrawalHistoryOrder.setThirdPartyPayNo(payment_no);
					withdrawalHistoryOrder.setFinishTime(new Date());
					withdrawalHistoryOrder.setState((byte) 3);//提现失败
					updateResult = withdrawalHistoryService
							.updateWithdrawalHistoryOrder(
									withdrawalHistoryOrder).getState();
				} catch (Exception exception) {
					logger.error("------rep_partner_trade_no="
							+ rep_partner_trade_no + ",payment_no="
							+ payment_no + ",price="
							+ withdrawalHistoryOrder.getPrice());
				}
				//把前面扣除用户的钱给用户加上start
				UserAssets userAssets = userAssetsService.selectUserAssets(withdrawalHistoryOrder.getUserId());
				userAssets.setRedEnvelopeBalance(userAssets
						.getRedEnvelopeBalance()
						+ withdrawalHistoryOrder.getPrice());
				
				User user = userWebService.selectUserByUserId(userAssets.getUserId()).getContent();
				userAssets.setTableNum(user.getNum());
				userAssetsService.updateUserAssets(userAssets);
				//把前面扣除用户的钱给用户加上end
				
				logger.error("weixinRqeust FAIL  respon=" + respon);
			}
			updateResult = result;
			/*根据微信回调是否成功修改withdrawalHistoryOrder的state字段 2:成功 3：失败    end*/
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return updateResult;
	}

	private static String getXmlWeiXinInfo(Map<String, String> requestMap) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");

		for (Map.Entry<String, String> entry : requestMap.entrySet()) {
			sb.append("<" + entry.getKey() + ">" + entry.getValue() + "</"
					+ entry.getKey() + ">");
		}
		sb.append("</xml>");
		return sb.toString();
	}

	public String wexin(String requestXml) throws UnsupportedEncodingException,
			IllegalStateException, IOException {

		// 指定读取证书格式为PKCS12
		KeyStore keyStore = null;
		try {
			keyStore = KeyStore.getInstance("PKCS12");
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 读取本机存放的PKCS12证书文件
		FileInputStream instream = null;
		try {
			instream = new FileInputStream(new File(
					env.getProp("weixin.cert_path")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			// 指定PKCS12的密码(商户ID)
			try {
				keyStore.load(instream, env.getProp("weixin.mchid")
						.toCharArray());
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CertificateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			try {
				instream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		SSLContext sslcontext = null;
		try {
			sslcontext = SSLContexts
					.custom()
					.loadKeyMaterial(keyStore,
							env.getProp("weixin.mchid").toCharArray()).build();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 指定TLS版本
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslcontext, new String[] { "TLSv1" }, null,
				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		// 设置httpclient的SSLSocketFactory
		CloseableHttpClient httpclient = HttpClients.custom()
				.setSSLSocketFactory(sslsf).build();

		HttpPost httppost = new HttpPost(
				"https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers");
		StringEntity myEntity = new StringEntity(requestXml);
		httppost.addHeader("Content-Type", "text/xml");
		httppost.setEntity(myEntity);
		HttpResponse response = null;
		try {
			response = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpEntity resEntity = response.getEntity();
		InputStreamReader reader = new InputStreamReader(
				resEntity.getContent(), "utf-8");
		char[] buff = new char[1024];
		int length = 0;
		String responString = "";
		while ((length = reader.read(buff)) != -1) {
			responString += new String(buff, 0, length);
		}
		System.out.println("responString=" + responString);
		return responString;
	}
}
