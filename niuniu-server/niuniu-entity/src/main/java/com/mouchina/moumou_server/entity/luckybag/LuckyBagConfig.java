package com.mouchina.moumou_server.entity.luckybag;

import java.util.Date;

public class LuckyBagConfig {
	private Long id;

	private Date publishTime;

	private Double money;

	private Integer luckyBagNum;

	private Integer area;

	private String copywrite;

	private String pictureUri;

	private Long userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Integer getLuckyBagNum() {
		return luckyBagNum;
	}

	public void setLuckyBagNum(Integer luckyBagNum) {
		this.luckyBagNum = luckyBagNum;
	}

	public Integer getArea() {
		return area;
	}

	public void setArea(Integer area) {
		this.area = area;
	}

	public String getCopywrite() {
		return copywrite;
	}

	public void setCopywrite(String copywrite) {
		this.copywrite = copywrite == null ? null : copywrite.trim();
	}

	public String getPictureUri() {
		return pictureUri;
	}

	public void setPictureUri(String pictureUri) {
		this.pictureUri = pictureUri == null ? null : pictureUri.trim();
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}