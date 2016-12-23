package com.mouchina.admin.controller.advert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mouchina.admin.service.api.vo.BaseResultVo;
import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.common.page.Page;
import com.mouchina.moumou_server.entity.advert.BusinessCoupon;
import com.mouchina.moumou_server_interface.advert.BusinessCouponService;

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

	@RequestMapping(value = "/reviewBusinessCoupon", method = RequestMethod.GET)
	public String reviewBusinessCoupon() {
		
		return "advert/businessCoupon/list";
	}
	
	/**
	 * 查询商户的所有优惠券
	 * @param userId
	 */
	@RequestMapping("/selectBusinessCoupon")
	public String selectBusinessCoupon(Integer begin, Long userId, ModelMap modelMap) {
		Map<String,Object> businessCouponMap = new HashMap<String,Object>();
		if (begin == null || begin < 0) {
			begin = 0;
		}
		Page page = new Page(begin, 10);
		businessCouponMap.put("userId", userId);
		businessCouponMap.put("page", page);
		ListRange<BusinessCoupon> listRange = businessCouponService.businessCouponList(businessCouponMap);
		modelMap.put("businessCoupons", listRange);
		return "";
	}

	/**
	 * 商家创建优惠券的审核【不是发布广告的审核】
	 * 
	 * @param id
	 *            优惠券id
	 * @return
	 */
	@RequestMapping(value = "/reviewBusinessCoupon", method = RequestMethod.POST)
	public @ResponseBody BaseResultVo<BusinessCoupon> reviewBusinessCoupon(BusinessCoupon businessCoupon) {
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
	 * 修改审核状态
	 */
	@RequestMapping("/updateState")
	public String updateState(Long id, Integer state, ModelMap modelMap){
		int result = 0;
		if (id != null && state != null) {
			result = businessCouponService.updateState(id, state);
		}
		modelMap.put("result", result);
		if (result > 0) {
			modelMap.put("msg", "修改成功");
		} else {
			modelMap.put("msg", "修改失败");
		}
		return "";
	}

}
