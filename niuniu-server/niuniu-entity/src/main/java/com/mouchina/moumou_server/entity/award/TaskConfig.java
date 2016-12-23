package com.mouchina.moumou_server.entity.award;

import java.io.Serializable;

public class TaskConfig implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3357684994513355775L;

	private Long id;

    private Integer growthPoint;

    private String name;

    private Byte isRepeat;

    private String uri;

    private Integer count;

    private Integer status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getGrowthPoint() {
        return growthPoint;
    }

    public void setGrowthPoint(Integer growthPoint) {
        this.growthPoint = growthPoint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Byte getIsRepeat() {
        return isRepeat;
    }

    public void setIsRepeat(Byte isRepeat) {
        this.isRepeat = isRepeat;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri == null ? null : uri.trim();
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}