package com.mouchina.moumou_server.entity.award;

import java.io.Serializable;
import java.util.Date;

/**
 * 签到的历史记录实体
 * @author Administrator
 *
 */
public class SignHistory implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2168245017498574790L;

	private Long id;

    private Long userId;

    private Byte count;

    private Date signTime;

    private String historyTime;
    /**
     * 历史奖励的类型，使用-号隔开
     */
    private String type;
    /**
     * 历史奖励值，使用-号隔开
     */
    private String value;
    /**
     * 数据库没有的字段，只作为前端判断是否已经有过签到
     */
    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Byte getCount() {
        return count;
    }

    public void setCount(Byte count) {
        this.count = count;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public String getHistoryTime() {
        return historyTime;
    }

    public void setHistoryTime(String historyTime) {
        this.historyTime = historyTime == null ? null : historyTime.trim();
    }

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
}