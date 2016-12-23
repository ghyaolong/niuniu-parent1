package com.mouchina.moumou_server.dao.advert;

import java.util.List;
import java.util.Map;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.advert.BusinessCoupon;
import com.mouchina.moumou_server.entity.vo.business.coupon.MyBusinessCouponVo;
import com.mouchina.moumou_server.entity.vo.user.particular.UserBusinessCouponParticularVo;

public interface BusinessCouponMapper extends BaseMapper<BusinessCoupon, Long> {
	public int deleteByPrimaryKey(Long id);

	public int insert(BusinessCoupon record);

	/**
	 * 添加一条记录之后返回主键
	 * 
	 * @param record
	 * @return
	 */
	public int insertReturnPrimaryKey(BusinessCoupon record);

	public int insertSelective(BusinessCoupon record);

	public BusinessCoupon selectByPrimaryKey(Long id);

	public int updateByPrimaryKeySelective(BusinessCoupon record);

	public int updateByPrimaryKey(BusinessCoupon record);
	
	/**
	 * 选择优惠券	根据创建优惠券商户的id查询所有的优惠券
	 * @param userId	商户的用户id
	 * @return
	 */
	public List<BusinessCoupon> selectBusinessCouponByUserId(Map<String, Object> map);
	/**
	 * 优惠券管理
	 * @param map
	 * @return
	 */
	public List<BusinessCoupon> selectBusinessCouponManage(Map<String, Object> map);

	/**
	 * 查询用户的优惠券详情
	 * 
	 * @param advertId
	 * @return
	 */
	public UserBusinessCouponParticularVo selectUserBusinessCouponParticular(Long advertId);
	/**
	 * 查询优惠券内容
	 * @param map
	 * @return
	 */
	public BusinessCoupon selectBusinessCouponContent(Map<String, Object> map);
	/**
	 * 查询用户抢到的所有优惠券
	 * 
	 * @param map
	 *            Long userId,List<Integer> advertType int state
	 * @return
	 */
	public List<MyBusinessCouponVo> selectUserBusinessCoupon(Map<String, Object> map);
	/**
	 * 删除用户的优惠券
	 * @param userId
	 * @param advertId
	 * @param state
	 * @return
	 */
	public Integer deleteUserBusinessCoupon(Long userId,Long advertId);
	/**
	 * 查询优惠券记录条数count*
	 */
	public int selectBusinessCouponCount(Map<String, Object> conditions);
}