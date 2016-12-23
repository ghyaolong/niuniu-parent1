package com.mouchina.web.controller.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.utils.StringUtil;
import com.mouchina.moumou_server.entity.member.OrderBill;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserAssets;
import com.mouchina.moumou_server.entity.order.Order;
import com.mouchina.moumou_server.entity.pay.PayOrder;
import com.mouchina.moumou_server_interface.award.TaskHistoryService;
import com.mouchina.moumou_server_interface.member.OrderBillService;
import com.mouchina.moumou_server_interface.member.UserAssetsService;
import com.mouchina.moumou_server_interface.member.UserIdentity;
import com.mouchina.moumou_server_interface.member.UserVerifyService;
import com.mouchina.moumou_server_interface.order.OrderQueryService;
import com.mouchina.moumou_server_interface.order.OrderService;
import com.mouchina.moumou_server_interface.order.OrderView;
import com.mouchina.moumou_server_interface.pay.PayOrderService;
import com.mouchina.moumou_server_interface.view.BillResult;
import com.mouchina.moumou_server_interface.view.OrderException;
import com.mouchina.moumou_server_interface.view.OrderProcessState;
import com.mouchina.moumou_server_interface.view.OrderResult;
import com.mouchina.moumou_server_interface.view.OrderShowView;
import com.mouchina.moumou_server_interface.view.PayResult;
import com.mouchina.moumou_server_interface.view.UserResult;
import com.mouchina.web.base.config.Env;
import com.mouchina.web.base.utils.Constants;
import com.mouchina.web.service.api.member.UserWebService;
import com.mouchina.web.service.api.vo.OrderViewVo;

/**
 * Created by douzy on 16/2/4.
 */
@Controller
@RequestMapping("/order")
public class OrderController {
	private Logger logger = Logger.getLogger(getClass());
	@Resource
	private OrderService orderService;
	@Resource
	private OrderQueryService orderQueryService;
	@Resource
	private UserVerifyService userVerifyService;
	@Resource
	private PayOrderService payOrderService;
	@Resource
	private OrderBillService orderBillService;
	@Resource
	UserWebService userWebService;
	@Resource
	private Env env;
	@Resource
	private UserAssetsService userAssetsService;
	@Resource
	private TaskHistoryService taskHistoryService;
	/**
	 * 查询订单集合
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String getOrderList(HttpServletRequest request, ModelMap modelMap) {
		String loginKey = request.getParameter("loginKey");

		int begin = 0;
		if (request.getParameter("begin") != null)
			begin = Integer.valueOf(request.getParameter("begin").toString())
					.intValue();
		User user = userWebService.getUserByLoginKey(loginKey);

		if (user == null) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
			return "";
		}
		Page pager = new Page(begin, 10);
		Map map = new HashMap();
		map.put("page", pager);
		map.put("userId", user.getId());
		map.put("order", "create_time desc");
		OrderResult<ListRange<OrderShowView>> orderResult = orderQueryService
				.listRangeOrderShowViewPage(map);

		if (orderResult != null) {
			ListRange<OrderShowView> orderViewListRange = orderResult
					.getContent();
			if (orderViewListRange != null) {

				List<OrderViewVo> orderWriteViewList = new ArrayList<OrderViewVo>();

				if (orderResult.getContent() != null
						&& orderResult.getContent().getData() != null) {
					for (OrderShowView orderShowView : orderResult.getContent()
							.getData()) {
						OrderViewVo orderViewVo = new OrderViewVo();
						orderViewVo.setOrderNo(orderShowView.getOrder()
								.getOrderNo());
						orderViewVo.setCreateTime(orderShowView.getOrder()
								.getCreateTime());
						orderViewVo.setOrderState(orderShowView.getOrder()
								.getOrderState());
						if (orderShowView.getPayOrder() != null) {
							orderViewVo.setPayChannel(Integer
									.valueOf(orderShowView.getPayOrder()
											.getPayChannel()));
							orderViewVo.setThirdPartyPayNo(orderShowView
									.getPayOrder().getThirdPartyPayNo());
							
						}
						orderViewVo.setPayPirce(orderShowView.getOrder()
								.getDealPrice());
						orderViewVo.setOrderType(orderShowView.getOrder()
								.getOrderType());
						orderViewVo.setFinshTime(orderShowView.getOrder()
								.getFinshiTime());

						orderViewVo.setPaySource(Integer.valueOf(orderShowView
								.getOrder().getPaySource()));
						orderWriteViewList.add(orderViewVo);
					}
				}

				modelMap.put("result", 1);
				modelMap.put("orderList", orderWriteViewList);
				modelMap.put("page", orderResult.getContent().getPage());
			}
		}

		return "";
	}

	/**
	 * 下单(添加order payOrder)
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/apply", method = RequestMethod.POST)
	public String orderApply(HttpServletRequest request, ModelMap modelMap) {
		String loginKey = request.getParameter("loginKey");
		String title = request.getParameter("title");
		String paySource = request.getParameter("paySource");//订单来源 (1 ios,2 android)
		String payPrice = request.getParameter("payPrice");
		String payChannel = request.getParameter("payChannel");//支付渠道 (1支付宝 2微信)
		String sign = request.getParameter("sign");

		System.out.println("---------------------loginKey=" + loginKey
				+ ",payPirce=" + payPrice + ",paySource=" + paySource
				+ ",payChannel=" + payChannel + ",title=" + title);
		if (StringUtils.isEmpty(loginKey) || StringUtils.isEmpty(payPrice)
				|| StringUtils.isEmpty(paySource)
				|| StringUtils.isEmpty(payChannel)
				|| StringUtils.isEmpty(title)) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}

		User user = userWebService.getUserByLoginKey(loginKey);

		if (user == null) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		byte pChannel = Byte.valueOf(payChannel);//支付渠道

		byte pSource = Byte.valueOf(paySource);//订单来源

		Integer pprice = Integer.valueOf(payPrice);//订单金额

		if (pprice < 1) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		try {
			OrderView orderView = new OrderView();

			PayOrder payOrder = new PayOrder();
			payOrder.setPayChannel(pSource);//订单来源
			payOrder.setPayPrice(pprice);//订单价格
			payOrder.setPayWay(pChannel);//'支付渠道  1 支付宝 2微信 3 余额 4管理员'
			orderView.setPayOrder(payOrder);
			
			Order order = new Order();
			order.setPaySource(pSource);
			order.setOriginalPirce(pprice);//原始总金额(分为单位)
			order.setGoodsTotalCount(1);//商品总数量
			order.setDealPrice(pprice);//成交总金额(分为单位)

			order.setTitle(title);
			order.setUserId(user.getId());
			order.setUserName(user.getNickName());
			orderView.setOrder(order);
			

			UserIdentity userIdentity = new UserIdentity();
			userIdentity.setToken(user.getToken());
			userIdentity.setTableNum(user.getTableNum());
			// userIdentity
			orderView.setUserIdentity(userIdentity);
			OrderResult<OrderView> orderViewOrderResult = orderService
					.addApplyOrderAndLockOrder(orderView);

			if (orderViewOrderResult.getState() == 1
					&& orderViewOrderResult.getContent() != null) {
				OrderView orderView1 = orderViewOrderResult.getContent();
				modelMap.put("result", 1);
				modelMap.put("payNo", orderView1.getPayOrder().getPayNo());
				modelMap.put("orderNo", orderView1.getOrder().getOrderNo());
				modelMap.put(
						"notifyUrl",
						(pChannel == 1 ? env.getProp("alipay.notify.url") : env
								.getProp("weixin.notify.url")));
			} else {
				modelMap.put("result", 0);
				modelMap.put("errorCode", Constants.ERROR_ERROR);
			}

		} catch (OrderException e) {

		}
		return "";
	}

	
	/**
	 * 下单充值(添加order payOrder)
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/reCharge", method = {RequestMethod.POST,RequestMethod.GET})
	public String orderRecharge(HttpServletRequest request, ModelMap modelMap) {
		String loginKey = request.getParameter("loginKey");
		String title = request.getParameter("title");//支付标题
		String paySource = request.getParameter("paySource");//订单来源 (1 ios,2 android)
		String payPrice = request.getParameter("payPrice");//分
		String payChannel = request.getParameter("payChannel");//支付渠道 (1支付宝 2微信)
		String rechargeConfigId = request.getParameter("rechargeConfigId");//充值套餐id(关联providerId)
		
		Integer provideId = rechargeConfigId != null && !"".equals(rechargeConfigId) ? Integer.parseInt(rechargeConfigId) : 0;//关联充值套餐表id
		if (StringUtils.isEmpty(loginKey) || StringUtils.isEmpty(payPrice)
				|| StringUtils.isEmpty(paySource)
				|| StringUtils.isEmpty(payChannel)
				|| StringUtils.isEmpty(title)) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}

		User user = userWebService.getUserByLoginKey(loginKey);

		if (user == null) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		byte pChannel = Byte.valueOf(payChannel);//支付渠道
		byte pSource = Byte.valueOf(paySource);//订单来源1ios2android
		Integer pprice = Integer.valueOf(payPrice);//订单金额

		if (pprice < 1) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		try {
			OrderView orderView = new OrderView();

			PayOrder payOrder = new PayOrder();
			payOrder.setPayChannel(pSource);//订单来源1ios2android
			payOrder.setPayPrice(pprice);//订单价格
			payOrder.setPayWay(pChannel);//'支付渠道  1 支付宝 2微信 3 余额 4管理员'
			payOrder.setProvideId(provideId);//关联充值套餐表id
			
			orderView.setPayOrder(payOrder);
			
			Order order = new Order();
			order.setPaySource(pSource);
			order.setOriginalPirce(pprice);//原始总金额(分为单位)
			order.setGoodsTotalCount(1);//商品总数量
			order.setDealPrice(pprice);//成交总金额(分为单位)

			order.setTitle(title);
			order.setUserId(user.getId());
			order.setUserName(user.getNickName());
			
			order.setProviderId(provideId);
			orderView.setOrder(order);
			

			UserIdentity userIdentity = new UserIdentity();
			userIdentity.setToken(user.getToken());
			userIdentity.setTableNum(user.getTableNum());
			// userIdentity
			orderView.setUserIdentity(userIdentity);
			OrderResult<OrderView> orderViewOrderResult = orderService
					.addApplyOrderAndLockOrder(orderView);

			if (orderViewOrderResult.getState() == 1
					&& orderViewOrderResult.getContent() != null) {
				OrderView orderView1 = orderViewOrderResult.getContent();
				modelMap.put("result", 1);
				modelMap.put("payNo", orderView1.getPayOrder().getPayNo());
				modelMap.put("orderNo", orderView1.getOrder().getOrderNo());
				modelMap.put(
						"notifyUrl",
						(pChannel == 1 ? env.getProp("alipay.notify.url") : env
								.getProp("weixin.notify.url")));
				taskHistoryService.insertTask(user.getId(), (long)8, "1");
			} else {
				modelMap.put("result", 0);
				modelMap.put("errorCode", Constants.ERROR_ERROR);
			}

		} catch (OrderException e) {

		}
		return "";
	}
	
	@RequestMapping(value = "/useBalanceExchangeCoins", method = {RequestMethod.POST,RequestMethod.GET})
	public String useBalanceExchangeCoins(HttpServletRequest request, ModelMap modelMap) {
		String loginKey = request.getParameter("loginKey");
		String title = request.getParameter("title");//支付标题
		String paySource = request.getParameter("paySource");//订单来源 (1 ios,2 android)
		String payPrice = request.getParameter("payPrice");//分
		String payChannel = request.getParameter("payChannel");//支付渠道 (3零钱)
		String rechargeConfigId = request.getParameter("rechargeConfigId");//充值套餐id(关联providerId)
		
		Long provideId = rechargeConfigId != null && !"".equals(rechargeConfigId) ? Long.parseLong(rechargeConfigId) : 0;//关联充值套餐表id
		if (StringUtils.isEmpty(loginKey) || StringUtils.isEmpty(payPrice)
				|| StringUtils.isEmpty(paySource)
				|| StringUtils.isEmpty(payChannel)
				|| StringUtils.isEmpty(title)) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}

		User user = userWebService.getUserByLoginKey(loginKey);

		if (user == null) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		byte pChannel = Byte.valueOf(payChannel);//支付渠道
		byte pSource = Byte.valueOf(paySource);//订单来源1ios2android
		Integer pprice = Integer.valueOf(payPrice);//订单金额

		if (pprice < 1) {
			modelMap.put("result", "0");
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		try {
			OrderView orderView = new OrderView();

			PayOrder payOrder = new PayOrder();
			payOrder.setPayChannel(pSource);//订单来源1ios2android
			payOrder.setPayPrice(pprice);//订单价格
			payOrder.setPayWay(pChannel);//'支付渠道  1 支付宝 2微信 3 余额 4管理员'
			payOrder.setProvideId(provideId.intValue());//关联充值套餐表id
//			payOrder.setPayState((byte) 5);
			orderView.setPayOrder(payOrder);
			
			Order order = new Order();
			order.setPaySource(pSource);
			order.setOriginalPirce(pprice);//原始总金额(分为单位)
			order.setGoodsTotalCount(1);//商品总数量
			order.setDealPrice(pprice);//成交总金额(分为单位)
//			order.setOrderState(OrderProcessState.ORDERPAYSUCCESS.getCode());
			order.setOrderState(OrderProcessState.ORDERAPPLYSUCCESS.getCode());
			
			order.setTitle(title);
			order.setUserId(user.getId());
			order.setUserName(user.getNickName());
			
			order.setProviderId(provideId.intValue());
			orderView.setOrder(order);
			

			UserIdentity userIdentity = new UserIdentity();
			userIdentity.setToken(user.getToken());
			userIdentity.setTableNum(user.getTableNum());
			// userIdentity
			orderView.setUserIdentity(userIdentity);
			OrderResult<OrderView> orderViewOrderResult = orderService
					.addApplyOrderAndLockOrder(orderView);

			if (orderViewOrderResult.getState() == 1
					&& orderViewOrderResult.getContent() != null) {
				UserAssets userAssets = userVerifyService.getUserAssetsByUser(user);
				Boolean flag = orderService.updateBalanceToCoins(userAssets, provideId, false);
				if(flag){
					Date succDate = new Date();
					Order applyedOrder = orderViewOrderResult.getContent().getOrder();//前面保存成功的order,state为下单成功100
					applyedOrder.setOrderState(OrderProcessState.ORDERPAYSUCCESS.getCode());//状态改为成功200
					applyedOrder.setModifyTime(succDate);
					applyedOrder.setFinshiTime(succDate);
					orderService.updateOrderByOrderNo(applyedOrder);
					
					PayOrder applyedPayOrder = orderViewOrderResult.getContent().getPayOrder();
					applyedPayOrder.setPayState((byte)5);
					applyedPayOrder.setModifyTime(succDate);
					applyedPayOrder.setFinshiTime(succDate);
					payOrderService.updatePayOrder(applyedPayOrder);
					modelMap.put("result", 1);
				}else{
					modelMap.put("result", 0);
					modelMap.put("errorCode", Constants.ERROR_CODE_200507);
				}
			} else {
				modelMap.put("result", 0);
				modelMap.put("errorCode", Constants.ERROR_CODE_200506);
			}

		} catch (OrderException e) {

		}
		return "";
	}
	
	/**
	 * 获取支付订单号继续支付
	 * 
	 * @return
	 */
	@RequestMapping(value = "/repay", method = RequestMethod.POST)
	public String orderRepay(HttpServletRequest request, ModelMap modelMap) {
		String loginKey = request.getParameter("login_key");
		String orderNo = request.getParameter("orderNo");
		String paySource = request.getParameter("paySource");
		String payChannel = request.getParameter("payChannel");
		
		PayResult<PayOrder> payOrderResult = payOrderService
				.selectLatestPayOrderByOrderNo(orderNo);
		if (payOrderResult != null) {
			PayOrder payOrder = payOrderResult.getContent();
			if (payOrder != null) {
				payOrder.setPayWay(Integer.valueOf(payChannel).byteValue());
				PayResult<String> payResult = payOrderService
						.addRePay(payOrder);
				if (payResult != null) {
					if (payResult.getState() == 1) {
						String newPayNo = payResult.getContent();
						Byte _payType = payOrder.getPayChannel();
						if (!StringUtils.isEmpty(payChannel)) {
							_payType = Byte.valueOf(payChannel);
						}
						if (newPayNo != null) {
							modelMap.put("payNo", newPayNo);
							modelMap.put(
									"notifyUrl",
									(_payType == 1 ? env
											.getProp("alipay_notify_url") : env
											.getProp("weixinpay_notify_url")));
						}
					}
				}
			}
		}
		return "";
	}
	
	/**
	 * 发票申请列表
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/bill", method = RequestMethod.GET)
	public String orderBill(HttpServletRequest request, ModelMap modelMap) {
		String loginKey = request.getParameter("login_key");
		String begin = request.getParameter("begin");

		int start = 0;
		int page = 1;
		int size = 10;
		if (!StringUtils.isEmpty(begin)) {
			page = new Integer(begin);
		}
		if (page >= 1) {
			start = size * (page - 1);
			Page pager = new Page(start, size);
			Map map = new HashMap();
			map.put("page", pager);
			BillResult<ListRange<OrderBill>> orderBillBillResult = orderBillService
					.selectListBusinessPage(map);
			if (orderBillBillResult.getState() == 1) {
				ListRange<OrderBill> orderBillListRange = orderBillBillResult
						.getContent();
				if (orderBillListRange != null) {
					modelMap.put("result", 1);
					modelMap.put("orderBillList", orderBillListRange.getData());
					modelMap.put("page", orderBillListRange.getPage());
				}
			}
		}
		return "";
	}

	/**
	 * 发票申请
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/bill/submit", method = RequestMethod.GET)
	public String orderBillSubmit(HttpServletRequest request, ModelMap modelMap) {

		String loginKey = request.getParameter("loginKey");
		String title = request.getParameter("title");
		String companyName = request.getParameter("companyName");
		String billType = request.getParameter("billType");
		String address = request.getParameter("address");
		String mail = request.getParameter("mail");
		String tel = request.getParameter("tel");
		String amount = request.getParameter("amount");
		String bak = request.getParameter("bak");

		if (StringUtils.isBlank(title)) {
			modelMap.put("result", 0);
			modelMap.put("errorCode", Constants.ERROR_ERROR);
			return "";
		}
		if (StringUtils.isBlank(companyName)) {
			modelMap.put("result", 0);
			modelMap.put("errorCode", Constants.ERROR_ERROR);
			return "";
		}
		if (StringUtils.isBlank(billType)) {
			modelMap.put("result", 0);
			modelMap.put("errorCode", Constants.ERROR_ERROR);
			return "";
		}
		if (StringUtils.isBlank(address)) {
			modelMap.put("result", 0);
			modelMap.put("errorCode", Constants.ERROR_ERROR);
			return "";
		}
		if (StringUtils.isBlank(mail)) {
			modelMap.put("result", 0);
			modelMap.put("errorCode", Constants.ERROR_ERROR);
			return "";
		}
		if (StringUtils.isBlank(tel)) {
			modelMap.put("result", 0);
			modelMap.put("errorCode", Constants.ERROR_ERROR);
			return "";
		}
		if (StringUtils.isBlank(amount)) {
			modelMap.put("result", 0);
			modelMap.put("errorCode", Constants.ERROR_ERROR);
			return "";
		}

		OrderBill orderBill = new OrderBill();
		orderBill.setTitle(title);
		orderBill.setCompanyName(companyName);
		orderBill.setBillType(Byte.valueOf(billType));
		orderBill.setAddress(address);
		orderBill.setMail(mail);
		orderBill.setTel(tel);
		orderBill.setAmount(Integer.valueOf(amount));
		orderBill.setBak(bak);
		orderBill.setCreateTime(new Date());
		orderBill.setState((byte) 0);

		BillResult<OrderBill> orderBillBillResult = orderBillService
				.addOrderBill(orderBill);
		if (orderBillBillResult.getState() == 1) {
			modelMap.put("result", 1);
			modelMap.put("id", orderBillBillResult.getContent().getId());
		}

		return "";
	}
	/**
	 * 修改用户余额接口
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/deductUserRedEnvelopeBalance", method = { RequestMethod.GET, RequestMethod.POST })
	public String deductUserRedEnvelopeBalance(HttpServletRequest request, ModelMap modelMap) {
		String loginKey = request.getParameter("loginKey");
		String price =request.getParameter("price");
		if (StringUtil.isEmpty(loginKey) || StringUtil.isEmpty(price)) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		Integer priceNum =Integer.valueOf(price); //输入的金额
		User user = userWebService.getUserByLoginKey(loginKey);
		
		if(user != null){
			UserAssets assets =userAssetsService.selectUserAssets(user.getId()); //查询用户的财富
			Integer redEnvelopeBalance =assets.getRedEnvelopeBalance(); //用户的余额
			if(redEnvelopeBalance < priceNum){
				modelMap.put("result", Constants.ERROR_ERROR);
				modelMap.put("errorCode", Constants.ERROR_CODE_500006);
			}else{
				Integer money =redEnvelopeBalance - priceNum; 
				assets.setRedEnvelopeBalance(money);
				UserResult<Integer> userResult =userAssetsService.updateUserAssets(assets);
				modelMap.put("result", Constants.ERROR_OK);
			}
			
		}else{
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		
		return "";
	}

}
