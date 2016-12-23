package com.mouchina.moumou_server.entity.luckybag;

/**
 * 福袋枚举类
 * @author Administrator
 *
 */
public enum LuckyBagHelper {
	
	ADVERT_TYPE_0(0,"普通福袋"),
	ADVERT_TYPE_1(1,"优惠券福袋"),
	ADVERT_TYPE_2(2,"公告福袋"),
	ADVERT_TYPE_3(3,"圈子优惠券"),
	ADVERT_TYPE_4(4,"接力福袋"),
	ADVERT_TYPE_6(6,"定时广告"),
	ADVERT_TYPE_7(7,"运营活动"),
	ADVERT_TYPE_8(8,"定时活动广告(官方)"),
	ADVERT_TYPE_9(9,"说说"),
	
	ADVERT_STATE_1(1,"已审核"),
	ADVERT_STATE_3(3,"已完成"),
	
	ERROR_CODE_500002(500002,"当前用户已开启过此接力福袋"),
	ERROR_CODE_500006(500006,"当前用户余额不足"),
	ERROR_CODE_900100(900100,"拆接力福袋异常"),
	
	STATE_0(0,"待审核"),
	STATE_1(1,"已审核"),
	STATE_2(2,"暂停"),
	STATE_3(3,"已抢完"),
	STATE_4(4,"未通过"),
	STATE_5(5,"超时完成");
	
	private Integer marker;
	private String display;

	private LuckyBagHelper(Integer marker, String display) {
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
