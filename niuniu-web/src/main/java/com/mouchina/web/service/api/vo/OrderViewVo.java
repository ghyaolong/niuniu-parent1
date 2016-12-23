package com.mouchina.web.service.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * Created by douzy on 16/2/5.
 */
public class OrderViewVo implements Serializable {
	
	private String orderNo;
    /**
	 * 
	 */
	private static final long serialVersionUID = -8995529859564320950L;
	/**
     * 订单来源(1 ios,2 android)
     */
    private Integer paySource;
    /**
     * 订单状态 1 未支付 2支付完成3 支 付成功
     */
    private Integer orderState;
    /**
     * 金额
     */
   
    private Integer payPirce;
    /**
     * 支付渠道 1android 2ios
     */
    private Integer payChannel;
    /**
     * 创建时间
     */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 完成时间
     */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    
    private Date finshTime;
    /**
     * 订单类型1支付 2提现
     */
    private Integer orderType;
    /**
     * 第三方订单号
     */
    private String thirdPartyPayNo;

    public Integer getPaySource() {
        return paySource;
    }

    public void setPaySource(Integer paySource) {
        this.paySource = paySource;
    }

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    public Integer getPayPirce() {
        return payPirce;
    }

    public void setPayPirce(Integer payPirce) {
        this.payPirce = payPirce;
    }

    public Integer getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(Integer payChannel) {
        this.payChannel = payChannel;
    }

   

    public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getFinshTime() {
        return finshTime;
    }

    public void setFinshTime(Date finshTime) {
        this.finshTime = finshTime;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getThirdPartyPayNo() {
        return thirdPartyPayNo;
    }

    public void setThirdPartyPayNo(String thirdPartyPayNo) {
        this.thirdPartyPayNo = thirdPartyPayNo;
    }

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}


}
