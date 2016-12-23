package com.mouchina.moumou_server.entity.member;

import java.io.Serializable;
import java.util.Date;

public class BusinessTemp implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

    private Long userId;

    private String businessName;

    private String businessAddress;

    private String businessTel;

    private String businessSite;

    private String businessQq;

    private String businessWeixin;

    private String businessWeibo;

    private String businessCredentialsUp;

    private String businessCredentialsDown;

    private String businessCredentialsFace;

    private Date createTime;

    private Date modfiyTime;

    private Byte state;

    private String descb;

    private String businessIndustry;

    private String businessLogo;

    private String detailAddress;

    private String businessAbbreviation;

    private String registrationNumber;

    private String applyName;

    private String identityCard;

    private Integer certificationType;

    private String avatar;

    private String phone;

    private String province;

    private String city;

    private String area;

    private String businessFullName;

    private String businessLicenseNum;

    private String coordinate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName == null ? null : businessName.trim();
    }

    public String getBusinessAddress() {
        return businessAddress;
    }

    public void setBusinessAddress(String businessAddress) {
        this.businessAddress = businessAddress == null ? null : businessAddress.trim();
    }

    public String getBusinessTel() {
        return businessTel;
    }

    public void setBusinessTel(String businessTel) {
        this.businessTel = businessTel == null ? null : businessTel.trim();
    }

    public String getBusinessSite() {
        return businessSite;
    }

    public void setBusinessSite(String businessSite) {
        this.businessSite = businessSite == null ? null : businessSite.trim();
    }

    public String getBusinessQq() {
        return businessQq;
    }

    public void setBusinessQq(String businessQq) {
        this.businessQq = businessQq == null ? null : businessQq.trim();
    }

    public String getBusinessWeixin() {
        return businessWeixin;
    }

    public void setBusinessWeixin(String businessWeixin) {
        this.businessWeixin = businessWeixin == null ? null : businessWeixin.trim();
    }

    public String getBusinessWeibo() {
        return businessWeibo;
    }

    public void setBusinessWeibo(String businessWeibo) {
        this.businessWeibo = businessWeibo == null ? null : businessWeibo.trim();
    }


	public String getBusinessCredentialsUp() {
		return businessCredentialsUp;
	}

	public void setBusinessCredentialsUp(String businessCredentialsUp) {
		this.businessCredentialsUp = businessCredentialsUp;
	}

	public String getBusinessCredentialsDown() {
        return businessCredentialsDown;
    }

    public void setBusinessCredentialsDown(String businessCredentialsDown) {
        this.businessCredentialsDown = businessCredentialsDown == null ? null : businessCredentialsDown.trim();
    }

    public String getBusinessCredentialsFace() {
        return businessCredentialsFace;
    }

    public void setBusinessCredentialsFace(String businessCredentialsFace) {
        this.businessCredentialsFace = businessCredentialsFace == null ? null : businessCredentialsFace.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModfiyTime() {
        return modfiyTime;
    }

    public void setModfiyTime(Date modfiyTime) {
        this.modfiyTime = modfiyTime;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }

    public String getDescb() {
        return descb;
    }

    public void setDescb(String descb) {
        this.descb = descb == null ? null : descb.trim();
    }

    public String getBusinessIndustry() {
        return businessIndustry;
    }

    public void setBusinessIndustry(String businessIndustry) {
        this.businessIndustry = businessIndustry == null ? null : businessIndustry.trim();
    }

    public String getBusinessLogo() {
        return businessLogo;
    }

    public void setBusinessLogo(String businessLogo) {
        this.businessLogo = businessLogo == null ? null : businessLogo.trim();
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress == null ? null : detailAddress.trim();
    }

    public String getBusinessAbbreviation() {
        return businessAbbreviation;
    }

    public void setBusinessAbbreviation(String businessAbbreviation) {
        this.businessAbbreviation = businessAbbreviation == null ? null : businessAbbreviation.trim();
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber == null ? null : registrationNumber.trim();
    }

    public String getApplyName() {
        return applyName;
    }

    public void setApplyName(String applyName) {
        this.applyName = applyName == null ? null : applyName.trim();
    }

    public String getIdentityCard() {
        return identityCard;
    }

    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard == null ? null : identityCard.trim();
    }

    public Integer getCertificationType() {
        return certificationType;
    }

    public void setCertificationType(Integer certificationType) {
        this.certificationType = certificationType;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar == null ? null : avatar.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    public String getBusinessFullName() {
        return businessFullName;
    }

    public void setBusinessFullName(String businessFullName) {
        this.businessFullName = businessFullName == null ? null : businessFullName.trim();
    }

    public String getBusinessLicenseNum() {
        return businessLicenseNum;
    }

    public void setBusinessLicenseNum(String businessLicenseNum) {
        this.businessLicenseNum = businessLicenseNum == null ? null : businessLicenseNum.trim();
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate == null ? null : coordinate.trim();
    }
}