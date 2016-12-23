package com.mouchina.moumou_server.entity.activityConfig;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@SuppressWarnings("serial")
public class ActivityConfig implements Serializable{
    private Long id;

    private Long advertId;

    private String scope;

    private Integer rank;

    private String probability;

    private String numMax;

    private String prize;

    private String prizePic;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date showTime;//显示时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdvertId() {
        return advertId;
    }

    public void setAdvertId(Long advertId) {
        this.advertId = advertId;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope == null ? null : scope.trim();
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getProbability() {
        return probability;
    }

    public void setProbability(String probability) {
        this.probability = probability == null ? null : probability.trim();
    }

    public String getNumMax() {
        return numMax;
    }

    public void setNumMax(String numMax) {
        this.numMax = numMax == null ? null : numMax.trim();
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize == null ? null : prize.trim();
    }

    public String getPrizePic() {
        return prizePic;
    }

    public void setPrizePic(String prizePic) {
        this.prizePic = prizePic == null ? null : prizePic.trim();
    }

	public Date getShowTime() {
		return showTime;
	}

	public void setShowTime(Date showTime) {
		this.showTime = showTime;
	}

	@Override
	public String toString() {
		return "ActivityConfig [id=" + id + ", advertId=" + advertId + ", scope=" + scope + ", rank=" + rank
				+ ", probability=" + probability + ", numMax=" + numMax + ", prize=" + prize + ", prizePic=" + prizePic
				+ ", showTime=" + showTime + "]";
	}
    
    
}