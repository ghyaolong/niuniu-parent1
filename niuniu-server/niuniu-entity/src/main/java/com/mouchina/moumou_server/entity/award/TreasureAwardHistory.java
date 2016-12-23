package com.mouchina.moumou_server.entity.award;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class TreasureAwardHistory implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -1350357127020308268L;

	private Long id;

    private Long treasureBoxConfigId;

    private Long userId;
    
    /**
     * 倒计时
     */
    private Long countDown;
    /**
     * 是否可以点击
     */
    private String isClick;

    @DateTimeFormat(pattern="HH:mm:ss")
    private Date createTime;
    /**
     * 领取宝箱的开始时间
     */
    private Date startTime;
    /**
     * 领取宝箱的结束时间
     */
    private Date endTime;
    private String result;
    /**
     * 奖励类型
     */
    private Byte type;
    /**
     * 奖励值
     */
    private Integer value;
    /**
     * 宝箱开启的时间点
     */
    private String boxTimePoint;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTreasureBoxConfigId() {
        return treasureBoxConfigId;
    }

    public void setTreasureBoxConfigId(Long treasureBoxConfigId) {
        this.treasureBoxConfigId = treasureBoxConfigId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public Long getCountDown() {
		return countDown;
	}

	public void setCountDown(Long countDown) {
		this.countDown = countDown;
	}

	public String getIsClick() {
		return isClick;
	}

	public void setIsClick(String isClick) {
		this.isClick = isClick;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
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

	public String getBoxTimePoint() {
		return boxTimePoint;
	}

	public void setBoxTimePoint(String boxTimePoint) {
		this.boxTimePoint = boxTimePoint;
	}
}