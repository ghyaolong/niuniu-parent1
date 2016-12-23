package com.mouchina.moumou_server.entity.income;

/**
 * 代理商收益统计枚举类
 * @author Administrator
 *
 */
public enum AgentIncomeStatisticsEnum {
	
	INCOME_TYPE_0(0,"无收益"),
	INCOME_TYPE_1(1,"一级商家收益"),
	INCOME_TYPE_2(2,"二级商家收益"),
	INCOME_TYPE_3(3,"三级商家收益"),
	INCOME_TYPE_4(4,"区域收益"),
	INCOME_TYPE_5(5,"推荐区县收益"),
	INCOME_TYPE_6(6,"推广中心收益"),
	INCOME_TYPE_7(7,"推广星级收益"),
	
	AGENT_LEVEL_2(2,"区县代理商"),
	AGENT_LEVEL_3(3,"中心代理商"),
	AGENT_LEVEL_4(4,"星级代理商"),
	
	VALID_SIGN(1,"有效");
	
	private Integer marker;
	
	private String display;

	private AgentIncomeStatisticsEnum(Integer marker, String display) {
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
