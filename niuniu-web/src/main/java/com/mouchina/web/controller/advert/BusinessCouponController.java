package com.mouchina.web.controller.advert;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mouchina.base.utils.StringUtil;
import com.mouchina.moumou_server.entity.advert.BusinessCoupon;
import com.mouchina.moumou_server.entity.advert.RedEnvelope;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.vo.business.coupon.MyBusinessCouponVo;
import com.mouchina.moumou_server.entity.vo.user.particular.UserBusinessCouponParticularVo;
import com.mouchina.moumou_server_interface.advert.AdvertService;
import com.mouchina.moumou_server_interface.advert.BusinessCouponService;
import com.mouchina.moumou_server_interface.view.AdvertResult;
import com.mouchina.web.service.api.member.UserWebService;
import com.mouchina.web.service.api.vo.BaseResultVo;

/**
 * 优惠券
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value = "/businessCoupon")
public class BusinessCouponController {

	@Autowired
	private BusinessCouponService businessCouponService;
	@Autowired
	private UserWebService userWebService;
	@Autowired
	private AdvertService advertService;

	/**
	 * 商家添加优惠券
	 * 
	 * @param businessCoupon
	 * @return
	 */
	@RequestMapping(value = "/addBusinessCoupon")
	public @ResponseBody BaseResultVo<BusinessCoupon> addBusinessCoupon(@RequestBody BusinessCoupon businessCoupon) {
		BaseResultVo<BusinessCoupon> baseResultVo = new BaseResultVo<BusinessCoupon>();

		// 1现金券 2折扣券 3实物券
		if (businessCoupon.getCouponType() == 1) {
			if (businessCoupon.getMoney() == null) {
				baseResultVo.setResult("0");
				baseResultVo.setErrorMsg("缺少优惠券金额");
				return baseResultVo;
			}
		} else if (businessCoupon.getCouponType() == 2) {
			if (businessCoupon.getDiscount() == null) {
				baseResultVo.setResult("0");
				baseResultVo.setErrorMsg("缺少折扣券折扣");
				return baseResultVo;
			}
		} else if (businessCoupon.getCouponType() == 3) {
			if (businessCoupon.getPhysicalVolume() == null) {
				baseResultVo.setResult("0");
				baseResultVo.setErrorMsg("缺少实物券名称");
				return baseResultVo;
			}
		}

		businessCoupon.setIsThrowIn(0);
		businessCoupon.setCreateTime(new Date());
		BusinessCoupon bc = businessCouponService.addBusinessCoupon(businessCoupon);
		baseResultVo.setResult("1");
		baseResultVo.setData(bc);
		return baseResultVo;
	}

	/**
	 * 删除优惠券【这是逻辑删除，并非物理删除】
	 * 
	 * @param id
	 *            优惠券id
	 * @return
	 */
	@RequestMapping(value = "/deleteBusinessCoupon")
	public @ResponseBody BaseResultVo<Integer> deleteBusinessCoupon(Long id) {
		businessCouponService.deleteBusinessCoupon(id);
		BaseResultVo<Integer> baseResultVo = new BaseResultVo<>();
		baseResultVo.setResult("1");
		return baseResultVo;
	}

	/**
	 * 更新商户创建的优惠券
	 * 
	 * @param id
	 *            优惠券id
	 * @return
	 */
	@RequestMapping(value = "/updateBusinessCoupon")
	public @ResponseBody BaseResultVo<BusinessCoupon> updateBusinessCoupon(@RequestBody BusinessCoupon businessCoupon) {
		BaseResultVo<BusinessCoupon> baseResultVo = new BaseResultVo<BusinessCoupon>();
		BusinessCoupon bc = businessCouponService.updateBusinessCoupon(businessCoupon);
		if (bc == null) {
			baseResultVo.setResult("0");
			baseResultVo.setErrorMsg("优惠券不存在!");
			return baseResultVo;
		}

		baseResultVo.setResult("1");
		baseResultVo.setData(bc);
		return baseResultVo;
	}

	/**
	 * 选择优惠券
	 * 
	 * @param userId
	 */
	@RequestMapping(value = "/selectBusinessCoupon")
	public @ResponseBody BaseResultVo<List<BusinessCoupon>> selectBusinessCoupon(String loginKey, Integer offset) {
		BaseResultVo<List<BusinessCoupon>> baseResultVo = new BaseResultVo<>();
		
		User user = userWebService.getUserByLoginKey(loginKey);
		if(user == null) {
			baseResultVo.setResult("0");
			baseResultVo.setErrorMsg("loginKey不对，你管理你妹啊！");
			return baseResultVo;
		}
		Long userId = user.getId();
		
		if (offset == null || offset <= 0) offset = 1;
		Map<String, Object> businessCouponMap = new HashMap<String, Object>();
		businessCouponMap.put("userId", userId);
		businessCouponMap.put("offset", (offset - 1) * 10);
		businessCouponMap.put("pageSize", 10);
		List<BusinessCoupon> businessCoupons = businessCouponService.selectBusinessCoupon(businessCouponMap);
		if (businessCoupons == null || businessCoupons.size() <= 0) {
			baseResultVo.setResult("0");
			baseResultVo.setErrorCode(10000);
			baseResultVo.setErrorMsg("没有创建优惠券!");
			baseResultVo.setData(businessCoupons);
			return baseResultVo;
		}
		baseResultVo.setErrorCode(10001);
		baseResultVo.setResult("1");
		baseResultVo.setData(businessCoupons);
		baseResultVo.setOffset(offset);
		return baseResultVo;
	}

	/**
	 * 优惠券管理
	 * 
	 * @param loginKey
	 * @param begin
	 * @return
	 */
	@RequestMapping(value = "/selectBusinessCouponManage")
	public @ResponseBody BaseResultVo<List<BusinessCoupon>> selectBusinessCouponManage(String loginKey, Integer begin) {
		BaseResultVo<List<BusinessCoupon>> baseResultVo = new BaseResultVo<>();
		
		User user = userWebService.getUserByLoginKey(loginKey);
		if(user == null) {
			baseResultVo.setResult("0");
			baseResultVo.setErrorMsg("loginKey不对，你管理你妹啊！");
			return baseResultVo;
		}
		Long userId = user.getId();

		Map<String, Object> businessCouponMap = new HashMap<String, Object>();
		businessCouponMap.put("userId", userId);
		businessCouponMap.put("offset", begin);
		businessCouponMap.put("pageSize", 10);
		List<BusinessCoupon> businessCoupons = businessCouponService.selectBusinessCouponManage(businessCouponMap);
		if (businessCoupons == null || businessCoupons.size() <= 0) {
			baseResultVo.setResult("0");
			baseResultVo.setErrorCode(10000);
			baseResultVo.setErrorMsg("没有创建优惠券!");
			baseResultVo.setData(businessCoupons);
			return baseResultVo;
		}
		baseResultVo.setErrorCode(10001);
		baseResultVo.setResult("1");
		baseResultVo.setData(businessCoupons);
		return baseResultVo;
	}

	/**
	 * 用户领取优惠券
	 * 
	 * @param userId
	 * @param advertId
	 * @return
	 */
	@RequestMapping(value = "/robBusinessCoupon")
	public @ResponseBody BaseResultVo<RedEnvelope> robBusinessCoupon(String loginKey, Long advertId) {
		BaseResultVo<RedEnvelope> baseResultVo = new BaseResultVo<>();
		
		User user = userWebService.getUserByLoginKey(loginKey);
		if(user == null) {
			baseResultVo.setResult("0");
			baseResultVo.setErrorMsg("loginKey不对，你管理你妹啊！");
			return baseResultVo;
		}
		Long userId = user.getId();
		
		AdvertResult<RedEnvelope> advertResult = businessCouponService.addRobBusinessCoupon(userId, advertId);

		if (advertResult.getState() == 1) {
			baseResultVo.setResult("0");
			baseResultVo.setErrorCode(10001);
			baseResultVo.setErrorMsg("您还不是他的粉丝，成为粉丝后可领取优惠券!");
		} else if (advertResult.getState() == 2) {
			baseResultVo.setResult("0");
			baseResultVo.setErrorCode(10002);
			baseResultVo.setErrorMsg("该用户已经领取过优惠券，不能再次领取!");
		} else if(advertResult.getState() == 5) {
			baseResultVo.setResult("0");
			baseResultVo.setErrorCode(10005);
			baseResultVo.setErrorMsg("优惠券已经领取完，不能领取!");
		} else {
			baseResultVo.setResult("1");
			baseResultVo.setData(advertResult.getContent());
		}

		return baseResultVo;
	}

	/**
	 * 查询用户抢到的所有优惠券【我的优惠券】
	 * 
	 * @param userId
	 *            用户id
	 * @param advertType
	 *            广告类型
	 */
	@RequestMapping(value = "/selectUserBusinessCoupon")
	public @ResponseBody BaseResultVo<List<MyBusinessCouponVo>> selectUserBusinessCoupon(String loginKey, Integer begin) {
		BaseResultVo<List<MyBusinessCouponVo>> baseResultVo = new BaseResultVo<>();

		User user = userWebService.getUserByLoginKey(loginKey);
		if (user == null) {
			baseResultVo.setResult("0");
			baseResultVo.setErrorMsg("用户不存在! 请使用正确的账户登录!");
			return baseResultVo;
		}
		Long userId = user.getId();

		Map<String, Object> myBusinessCouponMap = this.setPage(begin);
		myBusinessCouponMap.put("userId", userId);

		List<MyBusinessCouponVo> myBusinessCouponVos = businessCouponService.selectUserBusinessCoupon(myBusinessCouponMap);

		/*
		 * Map<String,Object> businessCouponMap = new HashMap<String, Object>();
		 * businessCouponMap.put("id", advertId); Advert advert =
		 * advertService.selectByAdvertId(advertId); Double distance =
		 * advertWebService.calAdvertDistanceBylongiandlati(advert, longitude,
		 * latitude); myBusinessCouponVos.setDistance(distance);
		 */

		baseResultVo.setResult("1");
		baseResultVo.setData(myBusinessCouponVos);
		return baseResultVo;
	}

	/**
	 * 核销优惠券
	 * 
	 * @param loginKey
	 *            用户唯一标示
	 * @param advertId
	 *            广告id
	 * @return
	 */
	@RequestMapping(value = "/useUserBusinessCoupon")
	public @ResponseBody BaseResultVo<List<RedEnvelope>> useUserBusinessCoupon(String loginKey, Long advertId, String businessLoginKey) {
		BaseResultVo<List<RedEnvelope>> baseResultVo = new BaseResultVo<>();
		if (StringUtil.isEmpty(loginKey) || advertId == 0 || StringUtil.isEmpty(businessLoginKey)){
			baseResultVo.setResult("0");
			baseResultVo.setErrorMsg("信息不完整!");
			return baseResultVo;
		}
		User user = userWebService.getUserByLoginKey(loginKey);
		User businessUser = userWebService.getUserByLoginKey(businessLoginKey);
		baseResultVo.setResult("0");
		if (user != null) {
			Map<String, Object> map = new HashMap<>();
			map.put("userId", user.getId());
			map.put("advertId", advertId);
			AdvertResult<RedEnvelope> advertResult = advertService.selectRedEnvelopeByMap(map);
			RedEnvelope redEnvelope = advertResult.getContent();
			if (advertResult.getState() == 1 && redEnvelope.getType() == 1) {
				if(null == redEnvelope.getAwardTime()){
					if(null != businessUser){
						if(businessUser.getId().longValue() == redEnvelope.getPublisherId().longValue()){
							redEnvelope.setAwardTime(new Date());
							AdvertResult<Integer> advertResult2 = advertService.updateRedEnvelope(redEnvelope);
							if (advertResult2.getState() == 1) {
								baseResultVo.setResult("1");
								baseResultVo.setErrorMsg("核销成功!");
							} else {
								baseResultVo.setErrorMsg("核销失败!");
							}
						}else{
							baseResultVo.setErrorMsg("该商户无权核销！");
						}
					}else{
						baseResultVo.setErrorMsg("未查询到该商户信息！");
					}
				}else{
					baseResultVo.setErrorMsg("该优惠券已核销！");
				}
			} else {
				baseResultVo.setErrorMsg("未查询到该优惠券信息!");
			}
		} else {
			baseResultVo.setErrorMsg("未查询到该用户信息！");
		}

		return baseResultVo;
	}

	/**
	 * 查询用户优惠券详情【查询用户抢到优惠券时候使用的】
	 * 
	 * @param advertId
	 *            广告id
	 * @return
	 */
	@RequestMapping(value = "/selectUserBusinessCouponParticular")
	public @ResponseBody BaseResultVo<UserBusinessCouponParticularVo> selectUserBusinessCouponParticular(
			Long advertId) {
		BaseResultVo<UserBusinessCouponParticularVo> baseResultVo = new BaseResultVo<UserBusinessCouponParticularVo>();
		if (advertId == null) {
			baseResultVo.setResult("0");
			baseResultVo.setErrorMsg("所传参数为空!");
			return baseResultVo;
		}

		UserBusinessCouponParticularVo businessCouponParticularVo = businessCouponService
				.selectUserBusinessCouponParticular(advertId);
		baseResultVo.setResult("1");
		baseResultVo.setData(businessCouponParticularVo);
		return baseResultVo;
	}

	/**
	 * 删除用户的优惠券
	 * 
	 * @param userId
	 * @param advertId
	 * @return
	 */
	@RequestMapping(value = "/deleteUserBusinessCoupon")
	public @ResponseBody BaseResultVo<Integer> deleteUserBusinessCoupon(String loginKey, Long advertId) {
		BaseResultVo<Integer> baseResultVo = new BaseResultVo<Integer>();

		User user = userWebService.getUserByLoginKey(loginKey);
		if (user == null) {
			baseResultVo.setResult("0");
			baseResultVo.setErrorMsg("用户不存在! 请使用正确的账户登录!");
			return baseResultVo;
		}
		Long userId = user.getId();

		Integer value = businessCouponService.deleteUserBusinessCoupon(userId, advertId);
		if (value != 1) {
			baseResultVo.setResult("0");
			baseResultVo.setErrorMsg("不存在优惠券，无法删除!");
			return baseResultVo;
		}
		baseResultVo.setResult("1");
		baseResultVo.setErrorMsg("删除成功!");
		return baseResultVo;
	}

	/**
	 * 设置分页
	 * 
	 * @param offset
	 * @return
	 */
	private Map<String, Object> setPage(Integer offset) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("offset", offset);
		map.put("pagesize", 10);
		return map;
	}
}
