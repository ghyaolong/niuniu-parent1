package com.mouchina.moumou_server.entity.luckybag;

public class ActivityConfig {
    private Long id;

    private Long advertId;

    private String scope;

    private Integer rank;

    private String probability;

    private String numMax;

    private String prize;

    private String prizePic;

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
}