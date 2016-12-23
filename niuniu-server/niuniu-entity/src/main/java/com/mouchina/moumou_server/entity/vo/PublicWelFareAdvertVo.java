package com.mouchina.moumou_server.entity.vo;

import com.mouchina.moumou_server.entity.advert.Advert;

/**
 * 
 * @author Administrator 公益广告实体
 */
public class PublicWelFareAdvertVo extends Advert {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3172624912787033166L;
	/**
	 * 已经领取红包金额
	 */
	private Integer sendRedEnvelopeAmount;
	/**
	 * 已经领取红包数
	 */
	private Integer sendRedEnvelopeCount;

	public Integer getSendRedEnvelopeAmount() {
		return sendRedEnvelopeAmount;
	}

	public void setSendRedEnvelopeAmount(Integer sendRedEnvelopeAmount) {
		this.sendRedEnvelopeAmount = sendRedEnvelopeAmount;
	}

	public Integer getSendRedEnvelopeCount() {
		return sendRedEnvelopeCount;
	}

	public void setSendRedEnvelopeCount(Integer sendRedEnvelopeCount) {
		this.sendRedEnvelopeCount = sendRedEnvelopeCount;
	}

}
