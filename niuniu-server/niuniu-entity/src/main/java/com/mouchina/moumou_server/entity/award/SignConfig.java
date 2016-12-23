package com.mouchina.moumou_server.entity.award;

import java.io.Serializable;

public class SignConfig implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6111812077955479516L;

	private Long id;
	/**
	 * 签到次数
	 */
    private Byte account;
    /**
     * 奖励类型 0:经验值 1：红包
     */
    private Byte type;
    /**
     * 奖励值
     */
    private Integer value;
    /**
     * 命中率
     */
    private Integer hitRate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getAccount() {
        return account;
    }

    public void setAccount(Byte account) {
        this.account = account;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

	public Integer getHitRate() {
		return hitRate;
	}

	public void setHitRate(Integer hitRate) {
		this.hitRate = hitRate;
	}
}