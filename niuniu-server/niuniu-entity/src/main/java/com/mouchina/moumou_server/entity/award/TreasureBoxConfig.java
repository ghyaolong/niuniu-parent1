package com.mouchina.moumou_server.entity.award;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class TreasureBoxConfig implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8221792901495211770L;

	private Long id;

    private Byte type;

    private Integer value;

    private Integer hitRate;
    
    @DateTimeFormat(pattern="HH:mm:ss")
    @JsonFormat(pattern="HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @DateTimeFormat(pattern="HH:mm:ss")
    @JsonFormat(pattern="HH:mm:ss",timezone = "GMT+8")
    private Date openTime;

    private Byte state;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
	
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    public Date getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Date openTime) {
        this.openTime = openTime;
    }

    public Byte getState() {
        return state;
    }

    public void setState(Byte state) {
        this.state = state;
    }
}