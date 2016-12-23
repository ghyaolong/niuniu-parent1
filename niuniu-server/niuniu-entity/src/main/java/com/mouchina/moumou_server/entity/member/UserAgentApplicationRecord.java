package com.mouchina.moumou_server.entity.member;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户申请代理商记录信息实体类
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class UserAgentApplicationRecord implements Serializable{
	
    private Long id; //主键

    private Long userId; //用户ID

    private String applicationName; //申请人姓名

    private String phoneNumber; //电话号码
    
    private Integer agentLevel; //代理商等级  2 区县代理商   3 中心代理商   4 星级代理商

    private String province; //省份

    private String city; //市(直辖市)

    private String area; //区县

    private Integer visitState; //回访状态   0 未回访   1 已回访

    private Date createTime; //创建时间

    private Date modifyTime; //修改时间

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

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName == null ? null : applicationName.trim();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber == null ? null : phoneNumber.trim();
    }

    public Integer getAgentLevel() {
		return agentLevel;
	}

	public void setAgentLevel(Integer agentLevel) {
		this.agentLevel = agentLevel;
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

    public Integer getVisitState() {
        return visitState;
    }

    public void setVisitState(Integer visitState) {
        this.visitState = visitState;
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