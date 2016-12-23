package com.mouchina.moumou_server.entity.order;

import java.io.Serializable;

public class WithDrawalConfig implements Serializable{
    private Long id;

    private Integer minAmount;

    private Integer maxAmount;

    private Double maxWithDrawalRatio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(Integer minAmount) {
        this.minAmount = minAmount;
    }

    public Integer getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Integer maxAmount) {
        this.maxAmount = maxAmount;
    }

    public Double getMaxWithDrawalRatio() {
        return maxWithDrawalRatio;
    }

    public void setMaxWithDrawalRatio(Double maxWithDrawalRatio) {
        this.maxWithDrawalRatio = maxWithDrawalRatio;
    }
}