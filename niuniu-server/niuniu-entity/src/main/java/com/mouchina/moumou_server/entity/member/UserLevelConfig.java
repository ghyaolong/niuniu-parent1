package com.mouchina.moumou_server.entity.member;

import java.io.Serializable;

public class UserLevelConfig implements Serializable{
    private Long id;

    private Integer level;//用户等级

    private Double levelRatio;//等级系数

    private Integer growthPoint;//升级所需成长值

    private Integer homePageRedEnvelope;//可领取首页福袋个数

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Double getLevelRatio() {
        return levelRatio;
    }

    public void setLevelRatio(Double levelRatio) {
        this.levelRatio = levelRatio;
    }

    public Integer getGrowthPoint() {
        return growthPoint;
    }

    public void setGrowthPoint(Integer growthPoint) {
        this.growthPoint = growthPoint;
    }

    public Integer getHomePageRedEnvelope() {
        return homePageRedEnvelope;
    }

    public void setHomePageRedEnvelope(Integer homePageRedEnvelope) {
        this.homePageRedEnvelope = homePageRedEnvelope;
    }
}