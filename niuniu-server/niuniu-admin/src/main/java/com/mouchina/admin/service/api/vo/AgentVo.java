package com.mouchina.admin.service.api.vo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AgentVo implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_agent.id
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_agent.user_id
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    private Long userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_agent.state
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    private Byte state;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_agent.parent_id
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    private Long parentId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_agent.create_time
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_agent.modify_time
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date modifyTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_agent.agent_level
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    private Integer agentLevel;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_agent.identity_card
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    private String identityCard;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_agent.identity_card_image
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    private String identityCardImage;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_agent.available_amount
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    private Integer availableAmount;

    
    private String phone;
    private String userName;
    
    
    
    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}



	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table user_agent
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_agent.id
     *
     * @return the value of user_agent.id
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_agent.id
     *
     * @param id the value for user_agent.id
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_agent.user_id
     *
     * @return the value of user_agent.user_id
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_agent.user_id
     *
     * @param userId the value for user_agent.user_id
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_agent.state
     *
     * @return the value of user_agent.state
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    public Byte getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_agent.state
     *
     * @param state the value for user_agent.state
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    public void setState(Byte state) {
        this.state = state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_agent.parent_id
     *
     * @return the value of user_agent.parent_id
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_agent.parent_id
     *
     * @param parentId the value for user_agent.parent_id
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_agent.create_time
     *
     * @return the value of user_agent.create_time
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_agent.create_time
     *
     * @param createTime the value for user_agent.create_time
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_agent.modify_time
     *
     * @return the value of user_agent.modify_time
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_agent.modify_time
     *
     * @param modifyTime the value for user_agent.modify_time
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_agent.agent_level
     *
     * @return the value of user_agent.agent_level
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    public Integer getAgentLevel() {
        return agentLevel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_agent.agent_level
     *
     * @param agentLevel the value for user_agent.agent_level
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    public void setAgentLevel(Integer agentLevel) {
        this.agentLevel = agentLevel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_agent.identity_card
     *
     * @return the value of user_agent.identity_card
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    public String getIdentityCard() {
        return identityCard;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_agent.identity_card
     *
     * @param identityCard the value for user_agent.identity_card
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    public void setIdentityCard(String identityCard) {
        this.identityCard = identityCard == null ? null : identityCard.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_agent.identity_card_image
     *
     * @return the value of user_agent.identity_card_image
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    public String getIdentityCardImage() {
        return identityCardImage;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_agent.identity_card_image
     *
     * @param identityCardImage the value for user_agent.identity_card_image
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    public void setIdentityCardImage(String identityCardImage) {
        this.identityCardImage = identityCardImage == null ? null : identityCardImage.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_agent.available_amount
     *
     * @return the value of user_agent.available_amount
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    public Integer getAvailableAmount() {
        return availableAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_agent.available_amount
     *
     * @param availableAmount the value for user_agent.available_amount
     *
     * @mbggenerated Mon Feb 01 16:24:57 CST 2016
     */
    public void setAvailableAmount(Integer availableAmount) {
        this.availableAmount = availableAmount;
    }
}