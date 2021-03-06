package com.mouchina.moumou_server.entity.order;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

public class WithdrawalHistoryOrder implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column withdrawal_history_order.id
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column withdrawal_history_order.withdrawl_history_order_no
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    private String withdrawlHistoryOrderNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column withdrawal_history_order.batch_no
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    private String batchNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column withdrawal_history_order.price
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    private Integer price;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column withdrawal_history_order.account
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    private String account;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column withdrawal_history_order.withdrawal_channel
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    private Byte withdrawalChannel;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column withdrawal_history_order.third_party_pay_no
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    private String thirdPartyPayNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column withdrawal_history_order.create_time
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column withdrawal_history_order.modify_time
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column withdrawal_history_order.finish_time
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date finishTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column withdrawal_history_order.state
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    private Byte state;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column withdrawal_history_order.freeze_balance
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    private Integer freezeBalance;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column withdrawal_history_order.user_id
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    private Long userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column withdrawal_history_order.account_name
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    private String accountName;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column withdrawal_history_order.type
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    private Byte type;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table withdrawal_history_order
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column withdrawal_history_order.id
     *
     * @return the value of withdrawal_history_order.id
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column withdrawal_history_order.id
     *
     * @param id the value for withdrawal_history_order.id
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column withdrawal_history_order.withdrawl_history_order_no
     *
     * @return the value of withdrawal_history_order.withdrawl_history_order_no
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    public String getWithdrawlHistoryOrderNo() {
        return withdrawlHistoryOrderNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column withdrawal_history_order.withdrawl_history_order_no
     *
     * @param withdrawlHistoryOrderNo the value for withdrawal_history_order.withdrawl_history_order_no
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    public void setWithdrawlHistoryOrderNo(String withdrawlHistoryOrderNo) {
        this.withdrawlHistoryOrderNo = withdrawlHistoryOrderNo == null ? null : withdrawlHistoryOrderNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column withdrawal_history_order.batch_no
     *
     * @return the value of withdrawal_history_order.batch_no
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    public String getBatchNo() {
        return batchNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column withdrawal_history_order.batch_no
     *
     * @param batchNo the value for withdrawal_history_order.batch_no
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo == null ? null : batchNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column withdrawal_history_order.price
     *
     * @return the value of withdrawal_history_order.price
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column withdrawal_history_order.price
     *
     * @param price the value for withdrawal_history_order.price
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column withdrawal_history_order.account
     *
     * @return the value of withdrawal_history_order.account
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    public String getAccount() {
        return account;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column withdrawal_history_order.account
     *
     * @param account the value for withdrawal_history_order.account
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column withdrawal_history_order.withdrawal_channel
     *
     * @return the value of withdrawal_history_order.withdrawal_channel
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    public Byte getWithdrawalChannel() {
        return withdrawalChannel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column withdrawal_history_order.withdrawal_channel
     *
     * @param withdrawalChannel the value for withdrawal_history_order.withdrawal_channel
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    public void setWithdrawalChannel(Byte withdrawalChannel) {
        this.withdrawalChannel = withdrawalChannel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column withdrawal_history_order.third_party_pay_no
     *
     * @return the value of withdrawal_history_order.third_party_pay_no
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    public String getThirdPartyPayNo() {
        return thirdPartyPayNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column withdrawal_history_order.third_party_pay_no
     *
     * @param thirdPartyPayNo the value for withdrawal_history_order.third_party_pay_no
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    public void setThirdPartyPayNo(String thirdPartyPayNo) {
        this.thirdPartyPayNo = thirdPartyPayNo == null ? null : thirdPartyPayNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column withdrawal_history_order.create_time
     *
     * @return the value of withdrawal_history_order.create_time
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column withdrawal_history_order.create_time
     *
     * @param createTime the value for withdrawal_history_order.create_time
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column withdrawal_history_order.modify_time
     *
     * @return the value of withdrawal_history_order.modify_time
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column withdrawal_history_order.modify_time
     *
     * @param modifyTime the value for withdrawal_history_order.modify_time
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column withdrawal_history_order.finish_time
     *
     * @return the value of withdrawal_history_order.finish_time
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    public Date getFinishTime() {
        return finishTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column withdrawal_history_order.finish_time
     *
     * @param finishTime the value for withdrawal_history_order.finish_time
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column withdrawal_history_order.state
     *
     * @return the value of withdrawal_history_order.state
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    public Byte getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column withdrawal_history_order.state
     *
     * @param state the value for withdrawal_history_order.state
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    public void setState(Byte state) {
        this.state = state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column withdrawal_history_order.freeze_balance
     *
     * @return the value of withdrawal_history_order.freeze_balance
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    public Integer getFreezeBalance() {
        return freezeBalance;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column withdrawal_history_order.freeze_balance
     *
     * @param freezeBalance the value for withdrawal_history_order.freeze_balance
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    public void setFreezeBalance(Integer freezeBalance) {
        this.freezeBalance = freezeBalance;
    }

   

    public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column withdrawal_history_order.account_name
     *
     * @return the value of withdrawal_history_order.account_name
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column withdrawal_history_order.account_name
     *
     * @param accountName the value for withdrawal_history_order.account_name
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName == null ? null : accountName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column withdrawal_history_order.type
     *
     * @return the value of withdrawal_history_order.type
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    public Byte getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column withdrawal_history_order.type
     *
     * @param type the value for withdrawal_history_order.type
     *
     * @mbggenerated Thu Feb 18 11:12:40 CST 2016
     */
    public void setType(Byte type) {
        this.type = type;
    }
}