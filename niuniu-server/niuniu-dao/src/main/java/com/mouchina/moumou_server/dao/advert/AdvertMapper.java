package com.mouchina.moumou_server.dao.advert;

import java.util.List;
import java.util.Map;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.advert.Advert;
import com.mouchina.moumou_server.entity.advert.BannerConfig;
import com.mouchina.moumou_server.entity.vo.PublicWelFareAdvertVo;
import com.mouchina.moumou_server.entity.vo.marketing.AdvertVo;
import com.mouchina.moumou_server.entity.vo.marketing.CouponAdvertVo;
import com.mouchina.moumou_server.entity.vo.marketing.HomePageAdvertDo;
import com.mouchina.moumou_server.entity.vo.marketing.RelayLuckBagAdvertDo;

public interface AdvertMapper extends BaseMapper<Advert, Long> {
	
	public Map<String, Object> getAdvertSumInfo();

	@SuppressWarnings("rawtypes")
	public int selectAdvertBidTop(Map map);

	/**
	 * 根据主键查询一条广告
	 * 
	 * @param id
	 *            广告id
	 * @return
	 */
	public Advert selectAdvertByPrimaryKey(Long id);

	/**
	 * 查询首页广告列表
	 * 
	 * @param map
	 * @return
	 */
	public List<Advert> selectHomePageList(Map<String, Object> map);

	public int selectHomeCount(Map<String, Object> map);
	
	/**
	 * 根据广告类型查询该类型记录条数
	 */
	public int selectCountByType(Map<String, Object> map);

	public Advert selectByAdvertId(Long id);

	public int selectViewTimesCount(Map<String, Object> map);

	/**
	 * 首页福袋营销
	 * 
	 * @param userId
	 * @return
	 */
	public List<HomePageAdvertDo> homePageAdvert(Map<String,Object> map);

	/**
	 * 接力福袋营销
	 * 
	 * @param userId
	 */
	public List<RelayLuckBagAdvertDo> relayLuckBagAdvert(Map<String,Object> map);

	/**
	 * 普通福袋营销
	 * @param map
	 */
	public List<AdvertVo> selectGeneralAdvert(Map<String,Object> map);

	/**
	 * 优惠券营销
	 * @param map
	 */
	public List<CouponAdvertVo> couponAdvert(Map<String, Object> map);
	
	/**
	 * 查询Banner总条数
	 * @param map
	 * @return
	 */
	public int selectBannerCount(Map<String, Object> map);
	
	/**
	 * 查询Banner数据
	 * @param map
	 * @return
	 */
	public List<BannerConfig> selectListBanner(Map<String, Object> map);
	
	/**
	 * 添加Banner数据
	 * @param bannerConfig
	 * @return
	 */
	public int insertBannerSelective(BannerConfig bannerConfig);
	
	/**
	 * 更新Banner数据
	 * @param bannerConfig
	 * @return
	 */
	public int updateBannerByPrimaryKey(BannerConfig bannerConfig);
	/**
	 * 查询所有公益广告
	 * @param map
	 * @return
	 */
	public List<Advert> selectAllPublicWelFare(Map<String,Object> map);
	/**
	 * 公益广告列表含（捐赠金额）
	 * @param map
	 * @return
	 */
	public List<PublicWelFareAdvertVo> selectPublicWelFareVoList(Map<String,Object> map);
	
	/**
	 * 获取运营活动信息
	 * @param map
	 * @return
	 */
	public List<Advert> selectOperateActivity(Map<String, Object> map);
	/**
	 * 模糊查询历史福袋
	 * @param map
	 * @return
	 */
	public List<Advert> selectListRangeAdvertByLike(Map<String, Object> map);
	/**
	 * 模糊查询总条数
	 * @param map
	 * @return
	 */
	public int selectCountByLike(Map<String,Object> map);
	/**
	 * 分页查询运营活动
	 * @param map
	 * @return
	 */
	public List<Advert> selectListOperateTheActivity(Map<String, Object> map);
	/**
	 * 查询最近一条XX活动
	 * @param map
	 * @return
	 */
	public Advert selectActivity(Map<String, Object> map);
}