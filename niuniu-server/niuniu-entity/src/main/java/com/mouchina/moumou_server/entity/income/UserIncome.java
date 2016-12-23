package com.mouchina.moumou_server.entity.income;

import java.io.Serializable;
import java.util.Date;

public class UserIncome implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_income.id
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_income.user_id
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    private Long userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_income.envent_id
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    private Long enventId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_income.amout
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    private Integer amout;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_income.source_amount
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    private Integer sourceAmount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_income.state
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    private Byte state;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_income.envent_name
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    private String enventName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_income.create_time
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_income.event_user_id
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    private Long eventUserId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column user_income.income_type
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    private Byte incomeType;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table user_income
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    private static final long serialVersionUID = 1L;

    
    private Date incomeDate;
    
    public Date getIncomeDate() {
		return incomeDate;
	}


	public void setIncomeDate(Date incomeDate) {
		this.incomeDate = incomeDate;
	}


	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_income.id
     *
     * @return the value of user_income.id
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    public Long getId() {
        return id;
    }

   
    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_income.id
     *
     * @param id the value for user_income.id
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_income.user_id
     *
     * @return the value of user_income.user_id
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_income.user_id
     *
     * @param userId the value for user_income.user_id
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_income.envent_id
     *
     * @return the value of user_income.envent_id
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    public Long getEnventId() {
        return enventId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_income.envent_id
     *
     * @param enventId the value for user_income.envent_id
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    public void setEnventId(Long enventId) {
        this.enventId = enventId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_income.amout
     *
     * @return the value of user_income.amout
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    public Integer getAmout() {
        return amout;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_income.amout
     *
     * @param amout the value for user_income.amout
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    public void setAmout(Integer amout) {
        this.amout = amout;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_income.source_amount
     *
     * @return the value of user_income.source_amount
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    public Integer getSourceAmount() {
        return sourceAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_income.source_amount
     *
     * @param sourceAmount the value for user_income.source_amount
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    public void setSourceAmount(Integer sourceAmount) {
        this.sourceAmount = sourceAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_income.state
     *
     * @return the value of user_income.state
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    public Byte getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_income.state
     *
     * @param state the value for user_income.state
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    public void setState(Byte state) {
        this.state = state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_income.envent_name
     *
     * @return the value of user_income.envent_name
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    public String getEnventName() {
        return enventName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_income.envent_name
     *
     * @param enventName the value for user_income.envent_name
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    public void setEnventName(String enventName) {
        this.enventName = enventName == null ? null : enventName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_income.create_time
     *
     * @return the value of user_income.create_time
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_income.create_time
     *
     * @param createTime the value for user_income.create_time
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_income.event_user_id
     *
     * @return the value of user_income.event_user_id
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    public Long getEventUserId() {
        return eventUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_income.event_user_id
     *
     * @param eventUserId the value for user_income.event_user_id
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    public void setEventUserId(Long eventUserId) {
        this.eventUserId = eventUserId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column user_income.income_type
     *
     * @return the value of user_income.income_type
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    public Byte getIncomeType() {
        return incomeType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column user_income.income_type
     *
     * @param incomeType the value for user_income.income_type
     *
     * @mbggenerated Tue May 03 18:35:43 CST 2016
     */
    public void setIncomeType(Byte incomeType) {
        this.incomeType = incomeType;
    }
}