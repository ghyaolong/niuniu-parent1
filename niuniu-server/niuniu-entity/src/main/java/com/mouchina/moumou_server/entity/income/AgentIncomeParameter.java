package com.mouchina.moumou_server.entity.income;

/**
 * 代理商收益参数实体类
 * @author Administrator
 *
 */
public class AgentIncomeParameter {

	private Double publishAdvertIncomeCounty; //区县代理商发布广告收益比例参数
	private Double publishAdvertIncomeCentre; //中心代理商发布广告收益比例参数
	private Double publishAdvertIncomeStarlevel; //星级代理商发布广告收益比例参数
	private Double areaAdvertIncomeCounty; //区县代理商区域广告收益比例参数
	private Double promotionCommissionAdvertIncomeCountyToCentre; //区县代理商区域广告收益比例参数
	private Double promotionCommissionAdvertIncomeCountyToStarlevel; //区县代理商推广星级代理商提成广告收益比例参数
	private Double promotionCommissionAdvertIncomeCentreToStarlevel; //中心代理商推广星级代理商提成广告收益比例参数
	private Double recommendAdvertIncomeCounty; //区县代理商推荐收益比例参数
	private Double recommendAdvertIncomeCentre; //中心代理商推荐收益比例参数
	private Double recommendAdvertIncomeStarlevel; //星级代理商推荐收益比例参数
	private Double publishAdvertDeduct; //发布广告系统扣除比例参数
	
	public Double getPublishAdvertIncomeCounty() {
		return publishAdvertIncomeCounty;
	}
	public void setPublishAdvertIncomeCounty(Double publishAdvertIncomeCounty) {
		this.publishAdvertIncomeCounty = publishAdvertIncomeCounty;
	}
	public Double getPublishAdvertIncomeCentre() {
		return publishAdvertIncomeCentre;
	}
	public void setPublishAdvertIncomeCentre(Double publishAdvertIncomeCentre) {
		this.publishAdvertIncomeCentre = publishAdvertIncomeCentre;
	}
	public Double getPublishAdvertIncomeStarlevel() {
		return publishAdvertIncomeStarlevel;
	}
	public void setPublishAdvertIncomeStarlevel(Double publishAdvertIncomeStarlevel) {
		this.publishAdvertIncomeStarlevel = publishAdvertIncomeStarlevel;
	}
	public Double getAreaAdvertIncomeCounty() {
		return areaAdvertIncomeCounty;
	}
	public void setAreaAdvertIncomeCounty(Double areaAdvertIncomeCounty) {
		this.areaAdvertIncomeCounty = areaAdvertIncomeCounty;
	}
	public Double getPromotionCommissionAdvertIncomeCountyToCentre() {
		return promotionCommissionAdvertIncomeCountyToCentre;
	}
	public void setPromotionCommissionAdvertIncomeCountyToCentre(Double promotionCommissionAdvertIncomeCountyToCentre) {
		this.promotionCommissionAdvertIncomeCountyToCentre = promotionCommissionAdvertIncomeCountyToCentre;
	}
	public Double getPromotionCommissionAdvertIncomeCountyToStarlevel() {
		return promotionCommissionAdvertIncomeCountyToStarlevel;
	}
	public void setPromotionCommissionAdvertIncomeCountyToStarlevel(
			Double promotionCommissionAdvertIncomeCountyToStarlevel) {
		this.promotionCommissionAdvertIncomeCountyToStarlevel = promotionCommissionAdvertIncomeCountyToStarlevel;
	}
	public Double getPromotionCommissionAdvertIncomeCentreToStarlevel() {
		return promotionCommissionAdvertIncomeCentreToStarlevel;
	}
	public void setPromotionCommissionAdvertIncomeCentreToStarlevel(
			Double promotionCommissionAdvertIncomeCentreToStarlevel) {
		this.promotionCommissionAdvertIncomeCentreToStarlevel = promotionCommissionAdvertIncomeCentreToStarlevel;
	}
	public Double getRecommendAdvertIncomeCounty() {
		return recommendAdvertIncomeCounty;
	}
	public void setRecommendAdvertIncomeCounty(Double recommendAdvertIncomeCounty) {
		this.recommendAdvertIncomeCounty = recommendAdvertIncomeCounty;
	}
	public Double getRecommendAdvertIncomeCentre() {
		return recommendAdvertIncomeCentre;
	}
	public void setRecommendAdvertIncomeCentre(Double recommendAdvertIncomeCentre) {
		this.recommendAdvertIncomeCentre = recommendAdvertIncomeCentre;
	}
	public Double getRecommendAdvertIncomeStarlevel() {
		return recommendAdvertIncomeStarlevel;
	}
	public void setRecommendAdvertIncomeStarlevel(Double recommendAdvertIncomeStarlevel) {
		this.recommendAdvertIncomeStarlevel = recommendAdvertIncomeStarlevel;
	}
	public Double getPublishAdvertDeduct() {
		return publishAdvertDeduct;
	}
	public void setPublishAdvertDeduct(Double publishAdvertDeduct) {
		this.publishAdvertDeduct = publishAdvertDeduct;
	}
	
}
