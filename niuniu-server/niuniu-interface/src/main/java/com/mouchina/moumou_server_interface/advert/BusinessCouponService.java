package com.mouchina.moumou_server_interface.advert;

import java.util.List;
import java.util.Map;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.advert.Advert;
import com.mouchina.moumou_server.entity.advert.BusinessCoupon;
import com.mouchina.moumou_server.entity.advert.RedEnvelope;
import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.vo.business.coupon.MyBusinessCouponVo;
import com.mouchina.moumou_server.entity.vo.user.particular.UserBusinessCouponParticularVo;
import com.mouchina.moumou_server_interface.view.AdvertResult;

public interface BusinessCouponService {

	/**
	 * 添加优惠券
	 */
	public BusinessCoupon addBusinessCoupon(BusinessCoupon businessCoupon);

	/**
	 * 删除优惠券【这是逻辑删除，并非物理删除】
	 */
	public Integer deleteBusinessCoupon(Long id);

	/**
	 * 更新商户创建的优惠券信息
	 */
	public BusinessCoupon updateBusinessCoupon(BusinessCoupon businessCoupon);

	/**
	 * app接口
	 * 选择优惠券【针对选择优惠券，只能显示审核通过和有效的优惠券】
	 * @param userId
	 */
	public List<BusinessCoupon> selectBusinessCoupon(Map<String,Object> map);
	
	/**
	 * 后台管理系统
	 * 选择优惠券
	 */
	public ListRange<BusinessCoupon> businessCouponList (Map<String,Object> map);
	
	/**
	 * 优惠券管理【针对优惠券管理，显示商家创建的所有优惠券，不管是什么状态都会显示】
	 * @param userId
	 */
	public List<BusinessCoupon> selectBusinessCouponManage(Map<String,Object> map);
	/**
	 * 发布商户优惠券【3.0版本】
	 * @param advert
	 * @param user
	 * @return
	 */
	public Advert addPublishBusinessCoupon(Advert advert,User user);
	/**
	 * 抢优惠券【针对圈子优惠券的接口】
	 * @param userId		用户id
	 * @param advertId		广告id
	 * @param type			优惠券类型
	 * @return
	 */
	public AdvertResult<RedEnvelope> addRobBusinessCoupon(Long userId,Long advertId);
	
	/**
	 * 查询用户抢到的所有优惠券【我的优惠券】
	 * @param userId		用户id
	 * @param advertType	广告类型
	 * @return
	 */
	public List<MyBusinessCouponVo> selectUserBusinessCoupon(Map<String,Object> map);
	
	/**
	 * 根据主键ID查询优惠券
	 * @param id 主键Id
	 * @return
	 */
	public BusinessCoupon selectBusinessCouponById(Long id);
	/**
	 * 查询用户的优惠券详情【抢红包】
	 * @param advertId	广告id
	 * @return
	 */
	public UserBusinessCouponParticularVo selectUserBusinessCouponParticular(Long advertId);
	/**
	 * 查询优惠券内容
	 * @param map
	 * @return
	 */
	public BusinessCoupon selectBusinessCouponContent(Long id);
	/**
	 * 删除用户的优惠券
	 */
	public Integer deleteUserBusinessCoupon(Long userId,Long advertId);
	/**
	 * 修改审核状态
	 */
	public int updateState(long id, int state);
}
