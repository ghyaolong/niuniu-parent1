package com.mouchina.moumou_server.entity.advert;

import java.io.Serializable;
import java.util.Date;

/**
 * Banner实体类
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class BannerConfig implements Serializable {

	private Long id; //主键
	private String picUrl; //banner图片url
	private String secondLevelUrl; //二级链接url
	private Integer picIndex; //图片序号
	private Integer bannerType; //banner类型    1 首页banner
	private Integer flag; //有效标志  0 无效  1 有效
	private Date createTime; //创建时间
	private Date modifyTime; //修改时间
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPicUrl() {
		return picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}
	public String getSecondLevelUrl() {
		return secondLevelUrl;
	}
	public void setSecondLevelUrl(String secondLevelUrl) {
		this.secondLevelUrl = secondLevelUrl;
	}
	public Integer getPicIndex() {
		return picIndex;
	}
	public void setPicIndex(Integer picIndex) {
		this.picIndex = picIndex;
	}
	public Integer getBannerType() {
		return bannerType;
	}
	public void setBannerType(Integer bannerType) {
		this.bannerType = bannerType;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
}
