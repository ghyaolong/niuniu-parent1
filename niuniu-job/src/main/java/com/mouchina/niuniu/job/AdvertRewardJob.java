package com.mouchina.niuniu.job;

import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.moumou_server.entity.advert.Advert;
import com.mouchina.moumou_server.entity.advert.AdvertStatistics;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserAssets;
import com.mouchina.moumou_server.entity.order.Order;
import com.mouchina.moumou_server.entity.pay.PayOrder;
import com.mouchina.moumou_server_interface.advert.AdvertService;
import com.mouchina.moumou_server_interface.member.UserAssetsService;
import com.mouchina.moumou_server_interface.member.UserIdentity;
import com.mouchina.moumou_server_interface.member.UserVerifyService;
import com.mouchina.moumou_server_interface.order.OrderService;
import com.mouchina.moumou_server_interface.order.OrderView;
import com.mouchina.moumou_server_interface.view.AdvertResult;
import com.mouchina.moumou_server_interface.view.OrderException;
import com.mouchina.moumou_server_interface.view.OrderResult;
import com.mouchina.moumou_server_interface.view.UserResult;

/**
 * 任务广告奖励
 * @author mouchina1005
 *
 */
@Component
public class AdvertRewardJob extends ABaseJob{
	
	private static final Logger logger = LoggerFactory.getLogger(AdvertRewardJob.class);
	@Resource
	AdvertService advertService;
	@Resource
	UserVerifyService userVerifyService;
	@Resource
	UserAssetsService userAssetsService;
	@Resource
	OrderService orderService;
	public void execute(){
		//取出离当前时间最近的500条广告明细
//		AdvertResult<ListRange<AdvertStatistics>> advertStatisticsResult = new AdvertResult<ListRange<AdvertStatistics>>();
//		ListRange<AdvertStatistics> listRange = new ListRange<AdvertStatistics>();
//		Page page = new Page();
//		page.setBegin(0);
//		page.setEnd(500);
////		page.setCount(500);
//		
//		Map sqlMap = new HashedMap();
//		sqlMap.put("isAward", 0);
//		sqlMap.put("order", "create_time desc");
//		sqlMap.put("page", page);
//		
//		advertStatisticsResult = advertService.selectListAdvertStatisticsPage(sqlMap);
//		if(advertStatisticsResult != null){
//			
//			
//			listRange = advertStatisticsResult.getContent();
//			
//			log.info("---本次返现广告明细数量---------------statistics Size : " + listRange.getData().size());
//			long beginTime = System.currentTimeMillis();
//			for(AdvertStatistics advertStatisVo : listRange.getData()){
//				log.info("当前奖励的广告明细广告Id : " + advertStatisVo.getAdvertId() + "状态标志位isAward : " + advertStatisVo.getIsAward());
//				//根据广告明细查出发布这条广告的用户
//				AdvertResult<Advert> advert= advertService.selectAdvert(advertStatisVo.getAdvertId());//当前广告
//				if(advert.getState() != 1){
//					log.error(String.format("广告(%s)统计数据不存在", advertStatisVo.getAdvertId()));
//					return;
//				}
//				UserResult<User> user = userVerifyService.selectUserByUserId(advert.getContent().getUserId());//发布这条广告的用户
//				try{
//					Integer advertStatisticsAmount = advertStatisVo.getSendRedEnvelopeAmount();//这条广告发布的总金额
//					Integer isAward = advertStatisVo.getIsAward();//是否返现标志位，0为没有返现、1代表已返现
//					
//					if(user == null){
//						log.error(String.format("用户(%s)统计数据不存在", advert.getContent().getUserId()));
//						return;
//					}
////					log.info("-------------------------------------------userId :" + user.getContent().getId() +  "<<<<<<<<<<<<<<tableNum : " + user.getContent().getTableNum());
////					if(advertStatisticsAmount.equals(advert.getContent().getRedEnvelopeAmount())){
//						//广告明细中的已领取总金额   等于   广告中的红包总金额代表此广告发布的福袋被抢完，否则不做处理
//						
//						Integer award = advertStatisticsAmount > 0 ? (int) (advertStatisticsAmount * 0.5) : 0;
//						UserAssets userAssets = userAssetsService.selectUserAssets(user.getContent().getId());
//						
//						if(userAssets == null){
//							log.error(String.format("<<<<<<<<<<<<<<<<<<非法数据<<<<<<<<<<<<<<<<<<<<<用户账户信息(%s)统计数据不存在", user.getContent().getId()));
//							return;
//						}
//						userAssets.setTableNum(user.getContent().getTableNum());
//						userAssets.setCashBalance(userAssets.getCashBalance() + award);
//						
//						userVerifyService.updateUserAssets(userAssets);
//						
//						//创建（虚拟一个奖励返现订单，以便用户在  钱袋-》资金明细-》充值中可以看到此次返现）
//						
//						
//						log.info("奖励用户 : " + user.getContent().getNickName() + "现金 " + award/100 + "元");
////					}
//						//将广告明细奖励状态字段改为1（即已经奖励过）
//						advertStatisVo.setIsAward(1);
//						advertService.updateAdvertStatistics(advertStatisVo);
//						
//						//保存此次奖励过程，目前是生成订单的形式------------------(以便在资金明细中的充值记录看到)
//						addAwardOrder("发布广告返现奖励", "4", award.toString(), "3", user.getContent());
//				}catch(Exception e){
//					log.error(String.format("返现过程出现异常 : %s", e.getMessage()));
//					return;
//				}
//				
//			}
//			long endTime = System.currentTimeMillis();
//			log.info(String.format("[本次任务：发广告返现],耗时：%s 秒", (endTime-beginTime)/1000));
//		}else{
//				log.info("没有符合返现条件的广告");
//				return;
//		}
	}
    /**
     * 添加奖励订单(这些订单不需要支付，只是为了更好地展现给用户收入明细)
     * @param title 订单title
     * @param paySource  payOrder中的payChannel : 支付渠道  1android 2ios             Order中的paySource : 订单来源(1 ios,2 android,3 weixin,4 web)
     * @param payPrice   payOrder中的payPrice : 支付金额                              Order中的originalPrice和dealPrice(原始总金额、成交总金额)
     * @param payChannel payOrder中的payWay : '支付方式  1 支付宝 2微信 3 余额 4管理员'
     * @param user 
     * @return
     */
	public int addAwardOrder(String title , String paySource , String payPrice , String payChannel , User user){
		System.out.println("---------------------userId=" + user.getId()
				+ ",payPirce=" + payPrice + ",paySource=" + paySource
				+ ",payChannel=" + payChannel + ",title=" + title);
		if (user == null || StringUtils.isEmpty(payPrice)
				|| StringUtils.isEmpty(paySource)
				|| StringUtils.isEmpty(payChannel)
				|| StringUtils.isEmpty(title)) {
			return 0;
		}
		
		byte pChannel = Byte.valueOf(payChannel);//payOrder中的payWay : '支付方式  1 支付宝 2微信 3 余额 4管理员'
		byte pSource = Byte.valueOf(paySource);//payOrder中的payChannel : 支付渠道  1android 2ios             Order中的paySource : 订单来源(1 ios,2 android,3 weixin,4 web)
		Integer pprice = Integer.valueOf(payPrice);//payOrder中的payPrice : 支付金额                              Order中的originalPrice和dealPrice(原始总金额、成交总金额)

		if (pprice < 1) {
			//订单金额小于1分
			return 0;
		}
		try {
			OrderView orderView = new OrderView();

			PayOrder payOrder = new PayOrder();
			payOrder.setPayChannel(pSource);
			payOrder.setPayPrice(pprice);
			payOrder.setPayWay(pChannel);//'支付方式  1 支付宝 2微信 3 余额 4管理员'

			Order order = new Order();
			order.setPaySource(pSource);
			order.setOriginalPirce(pprice);//原始总金额(分为单位)
			order.setGoodsTotalCount(1);//商品总数量
			order.setDealPrice(pprice);//成交总金额(分为单位)

			order.setTitle(title);
			order.setUserId(user.getId());
			order.setUserName(user.getNickName());
			orderView.setOrder(order);
			orderView.setPayOrder(payOrder);

			UserIdentity userIdentity = new UserIdentity();
			userIdentity.setToken(user.getToken());
			userIdentity.setTableNum(user.getTableNum());
			// userIdentity
			orderView.setUserIdentity(userIdentity);
			OrderResult<OrderView> orderViewOrderResult = orderService.addApplyOrderAndLockOrder(orderView);

			if(orderViewOrderResult != null){
				if(orderViewOrderResult.getState() != 0){
					return 1;
				}else{
					return 0;
				}
			}else{
				return 0;
			}
		} catch (OrderException e) {
			logger.error("给用户 : " + user.getNickName() + " 添加奖励订单时发生异常。");
		}
		return 0;
	}
	
}
