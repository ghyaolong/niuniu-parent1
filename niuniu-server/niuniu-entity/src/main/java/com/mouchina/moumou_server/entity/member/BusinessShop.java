package com.mouchina.moumou_server.entity.member;

import java.io.Serializable;
import java.util.Date;
/**
 * 商家店铺实体
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class BusinessShop implements Serializable {
	
	private Long id; //主键
	
	private Long businessId; //商户认证信息ID
	
	private Long userId; //用户ID
	
	private Date createTime;//创建时间
	
	private Date modifyTime;//修改时间
	
	private Integer state; //店铺状态  0待审核  1审核通过  2审核未通过
	
	private String shopSign; //店铺门头图片
	
	private String firstPic; //产品简介及链接地址
	
	private String secondPic; //产品简介及链接地址
	
	private String thirdPic; //产品简介及链接地址
	
	private String fourPic; //产品简介及链接地址
	
	private String fivePic; //产品简介及链接地址
	
	private String sixPic; //公司的承诺及广告语
	
	private String about; //店铺简介
	
	private String remark; //备注
	
	private String nickName; //用户昵称
	
	private String phone; //用户手机号

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getShopSign() {
		return shopSign;
	}

	public void setShopSign(String shopSign) {
		this.shopSign = shopSign;
	}

	public String getFirstPic() {
		return firstPic;
	}

	public void setFirstPic(String firstPic) {
		this.firstPic = firstPic;
	}

	public String getSecondPic() {
		return secondPic;
	}

	public void setSecondPic(String secondPic) {
		this.secondPic = secondPic;
	}

	public String getThirdPic() {
		return thirdPic;
	}

	public void setThirdPic(String thirdPic) {
		this.thirdPic = thirdPic;
	}

	public String getFourPic() {
		return fourPic;
	}

	public void setFourPic(String fourPic) {
		this.fourPic = fourPic;
	}

	public String getFivePic() {
		return fivePic;
	}

	public void setFivePic(String fivePic) {
		this.fivePic = fivePic;
	}

	public String getSixPic() {
		return sixPic;
	}

	public void setSixPic(String sixPic) {
		this.sixPic = sixPic;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
