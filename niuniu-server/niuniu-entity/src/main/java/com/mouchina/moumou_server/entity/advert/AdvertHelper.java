package com.mouchina.moumou_server.entity.advert;

/**
 * 接力福袋帮助枚举类
 * @author Administrator
 *
 */
public enum AdvertHelper {

	ADVERT_TYPE_0(0, "普通福袋"),
	ADVERT_TYPE_1(1, "优惠券福袋"),
	ADVERT_TYPE_2(2, "公告福袋"),
	ADVERT_TYPE_3(3, "优惠券福袋(圈子)"),
	ADVERT_TYPE_4(4, "接力福袋(圈子)"),
	ADVERT_TYPE_5(5, "首页广告(首页)"),
	ADVERT_TYPE_6(6, "定时广告"),
	ADVERT_TYPE_7(7, "运营活动广告(官方)"),
	ADVERT_TYPE_8(8, "定时活动广告(官方)"),
	ADVERT_TYPE_9(9, "说说(圈子)"),
	ADVERT_TYPE_10(10, "公益广告"),
	ADVERT_TYPE_11(11, "幸运魔轮"),
			
	DRAW_STATE_0(0, "未领取"),
	DRAW_STATE_1(1, "已领取"),
	
	FOLLOW_STATE_0(0, "未关注"),
	FOLLOW_STATE_1(1, "已关注"),
	
	FOLLOW_TYPE_1(1, "收藏"),
	FOLLOW_TYPE_2(2, "粉丝(关注)"),
	
	RESULT_STATE_0(0, "查询无数据"),
	RESULT_STATE_1(1, "查询有数据"),
	RESULT_STATE_5(5, "查询异常"),
	
	RED_ENVELOPE_STATE_0(0, "已浏览福袋"),
	RED_ENVELOPE_STATE_1(1, "已领取福袋"),
	
	RED_ENVELOPE_TYPE_0(0, "红包"),
	RED_ENVELOPE_TYPE_1(1, "优惠券"),
	RED_ENVELOPE_TYPE_2(2, "接力福袋"),
	RED_ENVELOPE_TYPE_3(3, "公益"),
	RED_ENVELOPE_TYPE_4(4, "首页广告福袋"),
	
	ERROR_CODE_500002(500002,"当前用户当天已领取此首页广告福袋"),
	ERROR_CODE_500005(500005,"当前用户当天领取首页广告福袋已达次数上限"),
	ERROR_CODE_900100(900100,"领取首页广告福袋异常"),
	
	STATE_0(0, "待审核"),
	STATE_1(1, "审核通过"),
	STATE_2(2, "暂停"),
	STATE_3(3, "已下线"),
	STATE_4(4, "未通过"),
	STATE_5(5, "超时完成"),
	
	DISTANCE_0(1000, "1公里"),
	DISTANCE_1(3000, "3公里"),
	DISTANCE_2(5000, "5公里"),
	DISTANCE_3(8000, "8公里"),
	DISTANCE_4(10000, "10公里");
	
	private Integer marker;
	
	private String display;

	private AdvertHelper(Integer marker, String display) {
		this.marker = marker;
		this.display = display;
	}

	public Integer getMarker() {
		return marker;
	}

	public void setMarker(Integer marker) {
		this.marker = marker;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}
	
}
