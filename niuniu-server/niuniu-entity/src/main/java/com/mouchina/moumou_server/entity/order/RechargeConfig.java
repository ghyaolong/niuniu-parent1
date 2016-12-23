package com.mouchina.moumou_server.entity.order;

import java.io.Serializable;

public class RechargeConfig implements Serializable{
    private Long id;

    private Integer exchangeCoins;

    private Integer additionalCoins;

    private Byte hotFlag;

    private Integer rechargeMoney;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getExchangeCoins() {
        return exchangeCoins;
    }

    public void setExchangeCoins(Integer exchangeCoins) {
        this.exchangeCoins = exchangeCoins;
    }

    public Integer getAdditionalCoins() {
        return additionalCoins;
    }

    public void setAdditionalCoins(Integer additionalCoins) {
        this.additionalCoins = additionalCoins;
    }

    public Byte getHotFlag() {
        return hotFlag;
    }

    public void setHotFlag(Byte hotFlag) {
        this.hotFlag = hotFlag;
    }

    public Integer getRechargeMoney() {
        return rechargeMoney;
    }

    public void setRechargeMoney(Integer rechargeMoney) {
        this.rechargeMoney = rechargeMoney;
    }
}