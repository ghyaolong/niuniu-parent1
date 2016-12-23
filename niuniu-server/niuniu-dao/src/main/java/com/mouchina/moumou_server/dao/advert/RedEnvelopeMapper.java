package com.mouchina.moumou_server.dao.advert;

import java.util.List;
import java.util.Map;

import com.mouchina.base.framework.BaseMapper;
import com.mouchina.moumou_server.entity.advert.RedEnvelope;
import com.mouchina.moumou_server.entity.advert.RedEnvelopeTop;
import com.mouchina.moumou_server.entity.advert.UserSearchSO;
import com.mouchina.moumou_server.entity.vo.marketing.HomePageAdvertDo;
import com.mouchina.moumou_server.entity.vo.marketing.MarketDetailVo;
import com.mouchina.moumou_server.entity.vo.user.particular.ConsumeParticularVo;

/**
 * 
 * @author Administrator
 *
 */
public interface RedEnvelopeMapper extends BaseMapper<RedEnvelope, Long> {

	public List<RedEnvelopeTop> selectListPageRedEnvelopeTop(UserSearchSO search);

	public List<RedEnvelopeTop> selectListPageRedEnvelopeTopNew(Map<String, Object> map);

	public RedEnvelopeTop selectRedEnvelopeTopNewMe(Long userId);

	/**
	 * 查询红包排行前50(存储过程)
	 * @return
	 */
	public List<RedEnvelopeTop> selectTopRed50Procedure();
	/**
	 * 查询待解冻广告列表
	 * 
	 * @param limit
	 *            行数
	 * @return
	 */
	public List<RedEnvelope> searchWaitUnFreezeAdvertList(int limit);

	/**
	 * 校验用户是否领取过改广告
	 * 
	 * <pre>
	 * 排除已作废的(status=-1), 只包含冻结的和已领取(status=0、1)
	 * </pre>
	 * 
	 * @param envelope
	 *            包含用户ID&广告ID
	 * @return 大于1 已领取、0 未领取
	 */
	public int verifyUserReceivedAdvert(RedEnvelope envelope);

	/**
	 * 查询全服前50人【3.0版本】
	 * 
	 * @return
	 */
	public List<RedEnvelopeTop> selectRedEnvelopeTop3();

	/**
	 * 查询自己的排名【3.0版本】
	 * 
	 * @return
	 */
	public RedEnvelopeTop selectRedEnvelopeOneself(Long userId);

	/**
	 * 查询该红包已经领取的人数
	 * 
	 * @param advertId
	 * @return
	 */
	public Integer selectFetchRedEnvelopeCount(Long advertId);

	/**
	 * 查询营销数据明细
	 * 
	 * @param map
	 * @return
	 */
	public List<MarketDetailVo> selectMarketDetail(Map<String, Object> map);
	/**
	 * 营销数据优惠券明细【特殊需求】
	 * @param map
	 * @return
	 */
	public List<MarketDetailVo> selectBusinessCouponMarketDetail(Map<String, Object> map);
	/**
	 * 查询首页广告最新的广告的所有时间
	 * @param userId	发布广告的id
	 * @return
	 */
	public List<HomePageAdvertDo> selectRedEnvAllDate(Long userId);
	/**
	 * 消费明细
	 * @param map
	 * @return
	 */
	public List<ConsumeParticularVo> selectConsumeParticular(Map<String, Object> map);
	/**
	 * 收入明细
	 * @param map
	 * @return
	 */
	public List<RedEnvelope> selectEarnParticular(Map<String, Object> map);
	/**
	 * 收入详情【3.0版本】
	 * @param map
	 * @return
	 */
	public List<RedEnvelope> selectUserIncomeDetails(Map<String, Object> map);
	/**
	 * 查询用户抢到的优惠券
	 * @param map
	 * @return
	 */
	public RedEnvelope selectRedEnvelopeByMap(Map<String, Object> map);
	/**
	 * 查询首页福袋余额
	 * @return
	 */
	public Integer selectHomeLuckyBagBalance(Map<String, Object> map);	
	/**
	 * 公益广告捐款排行榜
	 * @param map
	 * @return
	 */
	public List<RedEnvelope> selectPublicWelfareRanking(Map<String, Object> map);
	/**
	 * 查询幸运转轮中奖人名单
	 * @param map
	 * @return
	 */
	public List<RedEnvelope> selectLuckyWheelList(Map<String, Object> map);
}