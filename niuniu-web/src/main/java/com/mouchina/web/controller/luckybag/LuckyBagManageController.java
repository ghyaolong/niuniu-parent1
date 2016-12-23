package com.mouchina.web.controller.luckybag;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.base.utils.StringUtil;
import com.mouchina.moumou_server.entity.activityConfig.ActivityConfig;
import com.mouchina.moumou_server.entity.advert.Advert;
import com.mouchina.moumou_server.entity.advert.AdvertHelper;
import com.mouchina.moumou_server.entity.advert.BusinessCoupon;
import com.mouchina.moumou_server.entity.advert.RedEnvelope;
import com.mouchina.moumou_server.entity.luckybag.LuckyBagHelper;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.member.UserActivityDeploy;
import com.mouchina.moumou_server.entity.member.UserAssets;
import com.mouchina.moumou_server.entity.social.UserComment;
import com.mouchina.moumou_server.entity.social.UserCommentHelper;
import com.mouchina.moumou_server_interface.activityConfig.ActivityConfigService;
import com.mouchina.moumou_server_interface.advert.AdvertService;
import com.mouchina.moumou_server_interface.advert.BusinessCouponService;
import com.mouchina.moumou_server_interface.member.UserAssetsService;
import com.mouchina.moumou_server_interface.redEnvelope.RedEnvelopeService;
import com.mouchina.moumou_server_interface.social.UserCommentService;
import com.mouchina.moumou_server_interface.view.AdvertResult;
import com.mouchina.moumou_server_interface.view.SocialResult;
import com.mouchina.moumou_server_interface.view.UserResult;
import com.mouchina.web.base.framework.BaseController;
import com.mouchina.web.base.utils.Constants;
import com.mouchina.web.service.api.advert.AdvertWebService;
import com.mouchina.web.service.api.member.UserWebService;
import com.mouchina.web.service.api.social.SocialWebService;
import com.mouchina.web.service.api.vo.AdvertVo;
import com.mouchina.web.service.api.vo.UserCommentVo;
import com.mouchina.web.service.api.vo.UserRedEnvelope;

/**
 * 用户福袋管理controller
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/luckyBagManage")
public class LuckyBagManageController extends BaseController {

	@Autowired
	private UserWebService userWebService;
	@Autowired
	private AdvertWebService advertWebService;
	@Autowired
	private ActivityConfigService activityConfigService;
	@Autowired
	private UserCommentService userCommentService;
	@Autowired
	private BusinessCouponService businessCouponService;
	@Autowired
	AdvertService advertService;
	@Autowired
	RedEnvelopeService redEnvelopeService;
	@Autowired
	SocialWebService socialWebService;
	@Autowired
	UserAssetsService userAssetsService;

	/** 查询用户福袋(只包含普通福袋、优惠券福袋、公告福袋、定时福袋)信息 **/
	@RequestMapping(value = "/query", method = RequestMethod.GET)
	public String queryLuckyBag(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		String loginKey = request.getParameter("loginKey");
		response.addHeader("Access-Control-Allow-Origin", "*");
		if (StringUtil.isEmpty(loginKey)) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		User user = userWebService.getUserByLoginKey(loginKey);
		if (user != null) {
			Integer begin = 0;
			Map<String, Object> map = new HashMap<>();
			String beginStr = request.getParameter("begin");
			if (beginStr != null && !beginStr.isEmpty())
				begin = Integer.valueOf(beginStr);
			map.put("userId", user.getId());
			map.put("advertTypes",
					LuckyBagHelper.ADVERT_TYPE_0.getMarker() + "," + LuckyBagHelper.ADVERT_TYPE_1.getMarker() + ","
							+ LuckyBagHelper.ADVERT_TYPE_2.getMarker() + ","
							+ LuckyBagHelper.ADVERT_TYPE_6.getMarker());
			map.put("order", "create_time desc");
			map.put("page", new Page(begin, 10));
			ListRange<Advert> advertsList = advertWebService.selectListRangeAdvert(map); // 默认返回按创建时间排序
			if (advertsList == null || advertsList.getData().size() <= 0) {
				modelMap.put("result", Constants.ERROR_ERROR);
				modelMap.put("errorCode", Constants.ERROR_CODE_200001);
				return "";
			} else {
				List<AdvertVo> advertVoList = new ArrayList<AdvertVo>();
				for (Advert advert : advertsList.getData()) {
					AdvertVo advertVo = new AdvertVo();
					BeanUtils.copyProperties(advert, advertVo);
					/* 当前福袋为定时福袋时，计算倒计时 */
					if (advertVo.getAdvertType().intValue() == AdvertHelper.ADVERT_TYPE_6.getMarker().intValue()) {
						long now = new Date().getTime();
						long start = advertVo.getStartTime().getTime();
						long countDown = (start - now) / 1000;
						countDown = countDown < 0 ? 0 : countDown;
						advertVo.setCountDownTime(countDown);
					}
					/* 查询优惠券信息 */
					long relationId = advertVo.getRelationId() == null ? 0 : advertVo.getRelationId();
					if (relationId > 0) {
						BusinessCoupon businessCoupon = businessCouponService.selectBusinessCouponById(relationId);
						if (null != businessCoupon) {
							int couponType = businessCoupon.getCouponType();
							advertVo.setCouponType(couponType);
							switch (couponType) {
							case 1:
								advertVo.setCouponContent(String.valueOf(businessCoupon.getMoney()));
								break;
							case 2:
								advertVo.setCouponContent(String.valueOf(businessCoupon.getDiscount()));
								break;
							case 3:
								advertVo.setCouponContent(String.valueOf(businessCoupon.getPhysicalVolume()));
								break;
							default:
								break;
							}
						}
					}
					// 查询点赞数据
					map.clear();
					map.put("sourceId", advertVo.getId());
					map.put("state", UserCommentHelper.STATE_1.getMarker());
					map.put("commentType", UserCommentHelper.COMMENT_TYPE_2.getMarker());
					map.put("order", "create_time");
					List<UserComment> list = userCommentService.selectListUserComment(map).getContent();
					List<UserCommentVo> likes = new ArrayList<>();
					for (UserComment comment : list) {
						UserCommentVo commentVo = new UserCommentVo();
						commentVo.setCommentId(comment.getId());
						commentVo.setPraisedUserId(comment.getUserId());
						commentVo.setPraisedNickName(comment.getNickName());
						likes.add(commentVo);
					}
					advertVo.setLikes(likes);
					// 查询评论数据
					map.clear();
					map.put("sourceId", advertVo.getId());
					map.put("state", UserCommentHelper.STATE_1.getMarker());
					map.put("commentType", UserCommentHelper.COMMENT_TYPE_1.getMarker());
					map.put("order", "create_time");
					List<UserComment> userComments = userCommentService.selectListUserComment(map).getContent();
					List<UserCommentVo> comments = new ArrayList<>();
					for (UserComment comment : userComments) {
						UserCommentVo commentVo = new UserCommentVo();
						commentVo.setCommentId(comment.getId());
						commentVo.setCommentUserId(comment.getUserId());
						commentVo.setCommentNickName(comment.getNickName());
						commentVo.setCommentedUserId(comment.getParentId());
						commentVo.setCommentedNickName(comment.getParentNickName());
						commentVo.setContent(comment.getContent());
						comments.add(commentVo);
					}
					advertVo.setComments(comments);
					advertVoList.add(advertVo);
				}
				modelMap.put("result", Constants.ERROR_OK);
				modelMap.put("advertList", advertVoList);
				modelMap.put("page", advertsList.getPage());
			}
		} else {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		return "";
	}

	/** 根据广告关键字或者商家名字搜索福袋信息 **/
	@RequestMapping(value = "/searchHistory", method = RequestMethod.GET)
	public String searchLuckyBag(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		String loginKey = request.getParameter("loginKey");
		String advertContent = request.getParameter("advertContent");// 关键字
		String labelType = request.getParameter("labelType");// 标签
		if (StringUtil.isEmpty(loginKey)) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		User user = userWebService.getUserByLoginKey(loginKey);
		if (user != null) {
			Integer begin = 0;
			Map<String, Object> map = new HashMap<>();
			String beginStr = request.getParameter("begin");
			if (beginStr != null && !beginStr.isEmpty()) {
				begin = Integer.valueOf(beginStr);
			}

			if (!StringUtil.isEmpty(advertContent)) {
				map.put("advertContent", advertContent);
			}

			if (!StringUtil.isEmpty(labelType)) {
				map.put("labelType", labelType);
			}
			map.put("userId", user.getId());
			map.put("advertTypes",
					LuckyBagHelper.ADVERT_TYPE_0.getMarker() + "," + LuckyBagHelper.ADVERT_TYPE_1.getMarker() + ","
							+ LuckyBagHelper.ADVERT_TYPE_2.getMarker() + "," + LuckyBagHelper.ADVERT_TYPE_6.getMarker()
							+ "," + LuckyBagHelper.ADVERT_TYPE_8.getMarker());
			map.put("order", "create_time DESC");
			map.put("page", new Page(begin, 10));
			map.put("states", LuckyBagHelper.STATE_1.getMarker() + "," + LuckyBagHelper.STATE_2.getMarker() + ","
					+ LuckyBagHelper.STATE_3.getMarker() + "," + LuckyBagHelper.STATE_5.getMarker());
			/* 模糊查询 */
			ListRange<Advert> advertsList = advertService.selectListRangeAdvertByLike(map); // 默认返回按创建时间排序
			if (advertsList == null || advertsList.getData().size() <= 0) {
				modelMap.put("result", Constants.ERROR_ERROR);
				modelMap.put("errorCode", Constants.ERROR_CODE_200001);
				return "";
			} else {
				/* 查询优惠券信息 */
				List<AdvertVo> advertVoList = new ArrayList<AdvertVo>();
				AdvertVo advertVo;
				for (Advert advert : advertsList.getData()) {
					advertVo = new AdvertVo();
					BeanUtils.copyProperties(advert, advertVo);
					if (advert.getAdvertType() == 1) {
						BusinessCoupon businessCoupon = businessCouponService
								.selectBusinessCouponContent(advert.getRelationId());
						advertVo.setCouponType(businessCoupon.getCouponType());// 优惠券类型
						advertVo.setCouponContent(businessCoupon.getCouponContent());// 优惠券内容
						if (businessCoupon.getMoney() != null) {
							advertVo.setTempContent(businessCoupon.getMoney().toString());
						} else if (businessCoupon.getDiscount() != null) {
							advertVo.setTempContent(businessCoupon.getDiscount().toString());
						} else if (businessCoupon.getPhysicalVolume() != null) {
							advertVo.setTempContent(businessCoupon.getPhysicalVolume());
						}
					}
					int viewTime = advertService.selectAdvertStatisticsByAdvertId(advert.getId()).getContent()
							.getViewTimes().intValue();

					advertVo.setViewTimes(viewTime);// 显示人气
					/* 查询昵称 */
					UserResult<User> userResult = userWebService.selectUserByUserId(advert.getUserId());
					advertVo.setNickName(userResult.getContent().getNickName());// 用户昵称
					advertVo.setAvatar(userResult.getContent().getAvatar());// 用户头像
					advertVo.setSex(userResult.getContent().getSex());//用户性别
					advertVo.setProvince(userResult.getContent().getProvince());
					advertVo.setArea(userResult.getContent().getArea());
					advertVo.setCity(userResult.getContent().getCity());
					advertVoList.add(advertVo);
				}
				modelMap.put("result", Constants.ERROR_OK);
				modelMap.put("advertList", advertVoList);
				modelMap.put("page", advertsList.getPage());
			}
		} else {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		return "";
	}

	/** 运营活动列表查询信息 **/
	@RequestMapping(value = "/queryActivityList", method = RequestMethod.GET)
	public String queryActivityList(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		String loginKey = request.getParameter("loginKey");
		if (StringUtil.isEmpty(loginKey)) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		User user = userWebService.getUserByLoginKey(loginKey);
		if (user != null) {
			Integer begin = 0;
			Map<String, Object> map = new HashMap<>();
			String beginStr = request.getParameter("begin");
			if (beginStr != null && !beginStr.isEmpty()) {
				begin = Integer.valueOf(beginStr);
			}
			map.put("userId", user.getId());
			map.put("advertType", LuckyBagHelper.ADVERT_TYPE_6.getMarker());
			map.put("order", "create_time desc");
			map.put("page", new Page(begin, 9999999));
			ListRange<Advert> advertsList = advertWebService.selectListRangeAdvert(map); // 默认返回按创建时间排序
			if (advertsList == null || advertsList.getData().size() <= 0) {
				modelMap.put("result", Constants.ERROR_ERROR);
				modelMap.put("errorCode", Constants.ERROR_CODE_200001);
				return "";
			} else {

				List<HashMap<String, Object>> listActivityConfigMap = new ArrayList<HashMap<String, Object>>();
				for (Advert advert : advertsList.getData()) {
					AdvertVo advertVo = new AdvertVo();
					BeanUtils.copyProperties(advert, advertVo);
					HashMap<String, Object> activityMap = new HashMap<String, Object>();

					Long advertId = advert.getId();

					Map<String, Object> activityConfigMap = new HashMap<String, Object>();
					activityConfigMap.put("advertId", advertId);
					activityConfigMap.put("rank", 0);
					List<ActivityConfig> listActivityConfig = activityConfigService
							.selectListActivityConfigPage(activityConfigMap).getContent().getData();

					String imgUrl = "";
					String prize = "";
					if (listActivityConfig.size() > 0) {
						imgUrl = listActivityConfig.get(0).getPrizePic();
						prize = listActivityConfig.get(0).getPrize();
					}
					activityMap.put("title", advert.getAdvertName());// 活动标题
					activityMap.put("content", advert.getAdvertContent());
					activityMap.put("image", imgUrl);// 活动图片
					activityMap.put("startTime", advert.getStartTime());// 活动开始时间
					activityMap.put("endTime", advert.getEndTime());// 活动结束时间
					activityMap.put("prize", prize);// 奖品
					activityMap.put("getPrizeWay", "奖品我们将快递给您");// 奖品领取方式
					listActivityConfigMap.add(activityMap);
				}

				ListRange<HashMap<String, Object>> listRange = new ListRange<HashMap<String, Object>>();
				listRange.setData(listActivityConfigMap);
				SocialResult<ListRange<HashMap<String, Object>>> userCommentMapListResult = new SocialResult<ListRange<HashMap<String, Object>>>();
				userCommentMapListResult.setContent(listRange);
				modelMap.put("operatingActivityList", listActivityConfigMap);
				modelMap.put("result", Constants.ERROR_OK);
			}
		} else {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		return "";
	}

	/**
	 * 幸运转轮所有中奖人信息
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/LuckyWheel/success", method = RequestMethod.GET)
	public String luckyWheelSuccess(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		String loginKey = request.getParameter("loginKey");
		String advertId = request.getParameter("advertId");
		if (StringUtil.isEmpty(loginKey) || StringUtil.isEmpty(advertId)) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		User user = userWebService.getUserByLoginKey(loginKey);
		if (user != null) {
			/* 查询所有中奖人信息 */
			Map<String, Object> map = new HashMap<>();
			map.put("advertId", Long.parseLong(advertId));
			map.put("order", "create_time desc");
			//map.put("page", new Page(begin, 20));
			map.put("originalAmouts", 1+","+2+","+3+","+4+","+5);
			List<RedEnvelope> winList = redEnvelopeService.selectLuckyWheelSuccess(map);
			List<UserRedEnvelope> prizeList = new ArrayList<>();
			if (winList.size() > 0) {
				UserRedEnvelope userRedEnvelope;
				for (RedEnvelope redEnvelope : winList) {
					int rank = redEnvelope.getOriginalAmout();// 奖品等级
					Date awardTime = redEnvelope.getAwardTime();// 中奖时间
					if (-1024 != rank || null != awardTime) {
						userRedEnvelope = new UserRedEnvelope();
						Map<String, Object> acMap = new HashMap<>();
						acMap.put("advertId", Long.parseLong(advertId));
						acMap.put("rank", rank);
						List<ActivityConfig> activityConfigsList = activityConfigService
								.selectListActivityConfigs(acMap);
						userRedEnvelope.setPrizeName(activityConfigsList.get(0).getPrize());// 存放奖品名称
						userRedEnvelope.setCount(rank);// 存放奖品等级
						userRedEnvelope.setUserAvatar(redEnvelope.getUserAvatar());// 存放用户头像
						userRedEnvelope.setUserNickName(redEnvelope.getUserNickName());// 存放用户昵称
						userRedEnvelope.setPrizeDate(awardTime);// 存放中奖时间
						prizeList.add(userRedEnvelope);
					}
				}
			}
			/* 查询幸运转轮信息 */
			Advert advert = advertWebService.selectAdvert(Long.parseLong(advertId));
			map.put("userId", user.getId());
			map.put("startTime", advert.getStartTime());
			Calendar calendar = new GregorianCalendar();
			calendar.setTime(advert.getEndTime());
			calendar.add(calendar.DATE, 1);// 把日期往后增加一天.整数往后推,负数往前移动
			map.put("endTime", calendar.getTime());
			int inviteedCount = socialWebService.selectUserInviteBetweenTimeAndTime(map);

			Map<String, Object> uadMap = new HashMap<>();
			uadMap.put("userId", user.getId());
			uadMap.put("advertId", Long.parseLong(advertId));
			/* 更新用户抽奖次数 */
			UserActivityDeploy userActivityDeploy = socialWebService.selectUserActivityDeploy(map);
			if (userActivityDeploy == null) {
				userActivityDeploy = new UserActivityDeploy();
				/* 查询为空初始化数据 */
				userActivityDeploy.setAdvertId(Long.parseLong(advertId));
				userActivityDeploy.setCount(2);
				userActivityDeploy.setCreateTime(new Date());
				userActivityDeploy.setUserId(user.getId());
				socialWebService.addUserActivityDeploy(userActivityDeploy);
			} else if (userActivityDeploy.getCount() < 2 && userActivityDeploy.getCreateTime().before(new Date())) {
				/* 创建时间在当前时间之前刷新 */
				userActivityDeploy.setCount(2);
				userActivityDeploy.setCreateTime(new Date());
				socialWebService.updateUserActivityDeploy(userActivityDeploy);
			} else {
				/* 抽奖次数大于2次不初始化 */
				userActivityDeploy.setCreateTime(new Date());
				socialWebService.updateUserActivityDeploy(userActivityDeploy);
			}
			modelMap.put("drawCount", userActivityDeploy.getCount());// 存放用户可抽奖次数
			modelMap.put("inviteedCount", inviteedCount);// 存放邀请用户人数
			modelMap.put("prizeList", 1);
			modelMap.put("prizeList", prizeList);
		} else {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		return "";
	}

	/**
	 * 幸运转轮我的中奖信息
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/LuckyWheel/mine", method = RequestMethod.GET)
	public String luckyWheelMine(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		String loginKey = request.getParameter("loginKey");
		String advertId = request.getParameter("advertId");
		if (StringUtil.isEmpty(loginKey) || StringUtil.isEmpty(advertId)) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		User user = userWebService.getUserByLoginKey(loginKey);
		if (user != null) {
			int begin = 0;
			/* 查询我的中奖信息 */
			Map<String, Object> map = new HashMap<>();
			map.put("advertId", Integer.parseInt(advertId));
			map.put("userId", user.getId());
			map.put("order", "create_time desc");
			map.put("page", new Page(begin, 20));
			List<RedEnvelope> winList = redEnvelopeService.selectLuckyWheelSuccess(map);
			List<UserRedEnvelope> prizeList = new ArrayList<>();
			if (winList.size() > 0) {
				UserRedEnvelope userRedEnvelope;
				for (RedEnvelope redEnvelope : winList) {
					int rank = redEnvelope.getOriginalAmout();// 奖品等级
					Date awardTime = redEnvelope.getAwardTime();// 中奖时间
					if (-1024 != rank || null != awardTime) {
						userRedEnvelope = new UserRedEnvelope();
						Map<String, Object> acMap = new HashMap<>();
						acMap.put("advertId", Integer.parseInt(advertId));
						acMap.put("rank", rank);
						List<ActivityConfig> activityConfigsList = activityConfigService
								.selectListActivityConfigs(acMap);
						userRedEnvelope.setPrizeName(activityConfigsList.get(0).getPrize());// 存放奖品名称
						userRedEnvelope.setCount(rank);// 存放奖品等级
						userRedEnvelope.setPrizeUrl(activityConfigsList.get(0).getPrizePic());// 存放奖品图片
						userRedEnvelope.setPrizeDate(awardTime);// 存放中奖时间
						prizeList.add(userRedEnvelope);
					}
				}
			}
			modelMap.put("prizeList", prizeList);
		} else {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		return "";
	}

	/**
	 * 幸运转轮保存中奖人信息
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/LuckyWheel/save", method = RequestMethod.GET)
	public String luckyWheelSave(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		String loginKey = request.getParameter("loginKey");
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");
		if (StringUtil.isEmpty(loginKey)) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		User user = userWebService.getUserByLoginKey(loginKey);
		if (user != null) {
			user.setAddress(address);// 保存用户邮寄地址
			user.setName(name);
			user.setBindingMobile(phone);
			int result = userWebService.updateUser(user);
			modelMap.put("prizeList", result);
		} else {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		return "";
	}

	/**
	 * 幸运转轮抽奖
	 * 
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = "/LuckyWheel/lottery ", method = RequestMethod.GET)
	public String luckyWheelLottery(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
		response.addHeader("Access-Control-Allow-Origin", "*");
		String loginKey = request.getParameter("loginKey");
		String advertId = request.getParameter("advertId");
		if (StringUtil.isEmpty(loginKey) || StringUtil.isEmpty(advertId)) {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100001);
			return "";
		}
		User user = userWebService.getUserByLoginKey(loginKey);
		if (user != null) {
			Map<String, Object> map = new HashMap<>();
			map.put("userId", user.getId());
			map.put("advertId", Long.parseLong(advertId));
			/* 查询活动次数表确认可参与次数 */
			UserActivityDeploy userActivityDeploy = socialWebService.selectUserActivityDeploy(map);
			if (userActivityDeploy.getCount() > 0) {
				/* 转轮抽奖 */
				AdvertResult<RedEnvelope> advertResult = advertService.addLuckyWheel(user.getId(),
						Long.parseLong(advertId));
				if (advertResult.getContent().getAmout().intValue() == 0) {
					/* 修改抽奖次数 */
					userActivityDeploy.setCount(userActivityDeploy.getCount() - 1);
					socialWebService.updateUserActivityDeploy(userActivityDeploy);
					/* -1024表示没中奖 */
					if (advertResult.getContent().getOriginalAmout() != -1024) {
						Map<String, Object> ActivityConfigMap = new HashMap<>();
						ActivityConfigMap.put("advertId", Long.parseLong(advertId));
						ActivityConfigMap.put("rank", advertResult.getContent().getOriginalAmout());
						List<ActivityConfig> list = activityConfigService.selectListActivityConfigs(ActivityConfigMap);
						if (list.size() > 0 || list != null) {
							ActivityConfig activityConfig = list.get(0);
							/* 五等奖给红包 */
							if (activityConfig.getRank() == 5) {
								UserAssets userAssets = userAssetsService.selectUserAssets(user.getId());
								userAssets.setRedEnvelopeBalance(userAssets.getRedEnvelopeBalance().intValue() + 20);// 五等奖发红包0.2元
								userAssetsService.updateUserAssets(userAssets);
							}
							modelMap.put("rank", activityConfig.getRank());// 奖品等级
							modelMap.put("prize", activityConfig.getPrize());// 奖品名称
							modelMap.put("prizePic", activityConfig.getPrizePic());// 奖品图片
						}
					} else {
						modelMap.put("rank", 0);// 没中奖
					}
					modelMap.put("result", Constants.ERROR_OK);
				}else{
					modelMap.put("result", Constants.ERROR_ERROR);
					modelMap.put("errorCode", Constants.ERROR_CODE_600008);
				}
			} else {
				modelMap.put("result", Constants.ERROR_ERROR);
				modelMap.put("errorCode", Constants.ERROR_CODE_600009);
			}
		} else {
			modelMap.put("result", Constants.ERROR_ERROR);
			modelMap.put("errorCode", Constants.ERROR_CODE_100101);
		}
		return "";
	}
}
