package com.mouchina.web.service.api.vo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class AdvertStatisticsVo implements Serializable {

	  /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column advert_statistics.id
     *
     * @mbggenerated Mon Feb 01 15:47:43 CST 2016
     */
	@JsonIgnore
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column advert_statistics.advert_id
     *
     * @mbggenerated Mon Feb 01 15:47:43 CST 2016
     */
	@JsonIgnore
    private Long advertId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column advert_statistics.send_red_envelope_amount
     *
     * @mbggenerated Mon Feb 01 15:47:43 CST 2016
     */
    private Integer sendRedEnvelopeAmount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column advert_statistics.send_red_envelope_count
     *
     * @mbggenerated Mon Feb 01 15:47:43 CST 2016
     */
    private Integer sendRedEnvelopeCount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column advert_statistics.create_time
     *
     * @mbggenerated Mon Feb 01 15:47:43 CST 2016
     */
    @JsonIgnore
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
	 *广告冻结数
	 */
	private Integer freezeEnvelopeCount;
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table advert_statistics
     *
     * @mbggenerated Mon Feb 01 15:47:43 CST 2016
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column advert_statistics.id
     *
     * @return the value of advert_statistics.id
     *
     * @mbggenerated Mon Feb 01 15:47:43 CST 2016
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column advert_statistics.id
     *
     * @param id the value for advert_statistics.id
     *
     * @mbggenerated Mon Feb 01 15:47:43 CST 2016
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column advert_statistics.advert_id
     *
     * @return the value of advert_statistics.advert_id
     *
     * @mbggenerated Mon Feb 01 15:47:43 CST 2016
     */
    public Long getAdvertId() {
        return advertId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column advert_statistics.advert_id
     *
     * @param advertId the value for advert_statistics.advert_id
     *
     * @mbggenerated Mon Feb 01 15:47:43 CST 2016
     */
    public void setAdvertId(Long advertId) {
        this.advertId = advertId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column advert_statistics.send_red_envelope_amount
     *
     * @return the value of advert_statistics.send_red_envelope_amount
     *
     * @mbggenerated Mon Feb 01 15:47:43 CST 2016
     */
    public Integer getSendRedEnvelopeAmount() {
        return sendRedEnvelopeAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column advert_statistics.send_red_envelope_amount
     *
     * @param sendRedEnvelopeAmount the value for advert_statistics.send_red_envelope_amount
     *
     * @mbggenerated Mon Feb 01 15:47:43 CST 2016
     */
    public void setSendRedEnvelopeAmount(Integer sendRedEnvelopeAmount) {
        this.sendRedEnvelopeAmount = sendRedEnvelopeAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column advert_statistics.send_red_envelope_count
     *
     * @return the value of advert_statistics.send_red_envelope_count
     *
     * @mbggenerated Mon Feb 01 15:47:43 CST 2016
     */
    public Integer getSendRedEnvelopeCount() {
        return sendRedEnvelopeCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column advert_statistics.send_red_envelope_count
     *
     * @param sendRedEnvelopeCount the value for advert_statistics.send_red_envelope_count
     *
     * @mbggenerated Mon Feb 01 15:47:43 CST 2016
     */
    public void setSendRedEnvelopeCount(Integer sendRedEnvelopeCount) {
        this.sendRedEnvelopeCount = sendRedEnvelopeCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column advert_statistics.create_time
     *
     * @return the value of advert_statistics.create_time
     *
     * @mbggenerated Mon Feb 01 15:47:43 CST 2016
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column advert_statistics.create_time
     *
     * @param createTime the value for advert_statistics.create_time
     *
     * @mbggenerated Mon Feb 01 15:47:43 CST 2016
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

	public Integer getFreezeEnvelopeCount() {
		return freezeEnvelopeCount;
	}

	public void setFreezeEnvelopeCount(Integer freezeEnvelopeCount) {
		this.freezeEnvelopeCount = freezeEnvelopeCount;
	}
}
