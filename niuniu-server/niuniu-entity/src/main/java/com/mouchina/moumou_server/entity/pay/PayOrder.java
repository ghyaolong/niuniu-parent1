package com.mouchina.moumou_server.entity.pay;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class PayOrder implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order_2015.id
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order_2015.pay_price
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    private Integer payPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order_2015.pay_count
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    private Integer paySumPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order_2015.exchange_price
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    private Integer exchangePrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order_2015.use_balance_price
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    private Integer useBalancePrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order_2015.pay_no
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    private String payNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order_2015.order_no
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    private String orderNo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order_2015.user_id
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    private Long userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order_2015.provide_id
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    private Integer provideId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order_2015.pay_channel
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    private Byte payChannel;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order_2015.pay_way
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    private Byte payWay;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order_2015.create_time
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order_2015.modify_time
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date modifyTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order_2015.finshi_time
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date finshiTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order_2015.pay_sate
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    private Byte payState;


	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column pay_order_2015.third_party_pay_id
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    private String thirdPartyPayNo;

    private String tableNum;
    public String getTableNum() {
		return tableNum;
	}

	public void setTableNum(String tableNum) {
		this.tableNum = tableNum;
	}

	/**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table pay_order_2015
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order_2015.id
     *
     * @return the value of pay_order_2015.id
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order_2015.id
     *
     * @param id the value for pay_order_2015.id
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order_2015.pay_price
     *
     * @return the value of pay_order_2015.pay_price
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    public Integer getPayPrice() {
        return payPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order_2015.pay_price
     *
     * @param payPrice the value for pay_order_2015.pay_price
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    public void setPayPrice(Integer payPrice) {
        this.payPrice = payPrice;
    }


    public Byte getPayWay() {
		return payWay;
	}

	public void setPayWay(Byte payWay) {
		this.payWay = payWay;
	}

	public Byte getPayState() {
		return payState;
	}

	public void setPayState(Byte payState) {
		this.payState = payState;
	}
   
    public Integer getPaySumPrice() {
		return paySumPrice;
	}

	public void setPaySumPrice(Integer paySumPrice) {
		this.paySumPrice = paySumPrice;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order_2015.exchange_price
     *
     * @return the value of pay_order_2015.exchange_price
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    public Integer getExchangePrice() {
        return exchangePrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order_2015.exchange_price
     *
     * @param exchangePrice the value for pay_order_2015.exchange_price
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    public void setExchangePrice(Integer exchangePrice) {
        this.exchangePrice = exchangePrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order_2015.use_balance_price
     *
     * @return the value of pay_order_2015.use_balance_price
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    public Integer getUseBalancePrice() {
        return useBalancePrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order_2015.use_balance_price
     *
     * @param useBalancePrice the value for pay_order_2015.use_balance_price
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    public void setUseBalancePrice(Integer useBalancePrice) {
        this.useBalancePrice = useBalancePrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order_2015.pay_no
     *
     * @return the value of pay_order_2015.pay_no
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    public String getPayNo() {
        return payNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order_2015.pay_no
     *
     * @param payNo the value for pay_order_2015.pay_no
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    public void setPayNo(String payNo) {
        this.payNo = payNo == null ? null : payNo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order_2015.order_no
     *
     * @return the value of pay_order_2015.order_no
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    public String getOrderNo() {
        return orderNo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order_2015.order_no
     *
     * @param orderNo the value for pay_order_2015.order_no
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

  

    public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order_2015.provide_id
     *
     * @return the value of pay_order_2015.provide_id
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    public Integer getProvideId() {
        return provideId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order_2015.provide_id
     *
     * @param provideId the value for pay_order_2015.provide_id
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    public void setProvideId(Integer provideId) {
        this.provideId = provideId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order_2015.pay_channel
     *
     * @return the value of pay_order_2015.pay_channel
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    public Byte getPayChannel() {
        return payChannel;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order_2015.pay_channel
     *
     * @param payChannel the value for pay_order_2015.pay_channel
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    public void setPayChannel(Byte payChannel) {
        this.payChannel = payChannel;
    }

  

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order_2015.create_time
     *
     * @return the value of pay_order_2015.create_time
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order_2015.create_time
     *
     * @param createTime the value for pay_order_2015.create_time
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order_2015.modify_time
     *
     * @return the value of pay_order_2015.modify_time
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order_2015.modify_time
     *
     * @param modifyTime the value for pay_order_2015.modify_time
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column pay_order_2015.finshi_time
     *
     * @return the value of pay_order_2015.finshi_time
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    public Date getFinshiTime() {
        return finshiTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column pay_order_2015.finshi_time
     *
     * @param finshiTime the value for pay_order_2015.finshi_time
     *
     * @mbggenerated Mon Jun 15 13:14:47 CST 2015
     */
    public void setFinshiTime(Date finshiTime) {
        this.finshiTime = finshiTime;
    }

	public String getThirdPartyPayNo() {
		return thirdPartyPayNo;
	}

	public void setThirdPartyPayNo(String thirdPartyPayNo) {
		this.thirdPartyPayNo = thirdPartyPayNo;
	}

  
  
}