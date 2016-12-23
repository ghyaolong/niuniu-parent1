package com.mouchina.moumou_server.entity.social;

import java.io.Serializable;
import java.util.Date;

public class Feedback implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column feedback.id
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column feedback.title
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    private String title;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column feedback.content
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    private String content;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column feedback.app_version
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    private String appVersion;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column feedback.uadetail
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    private String uadetail;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column feedback.udid
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    private String udid;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column feedback.channel_id
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    private String channelId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column feedback.phone
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    private String phone;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column feedback.ip
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    private String ip;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column feedback.create_time
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    private Date createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column feedback.state
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    private Integer state;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table feedback
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column feedback.id
     *
     * @return the value of feedback.id
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column feedback.id
     *
     * @param id the value for feedback.id
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column feedback.title
     *
     * @return the value of feedback.title
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    public String getTitle() {
        return title;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column feedback.title
     *
     * @param title the value for feedback.title
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column feedback.content
     *
     * @return the value of feedback.content
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    public String getContent() {
        return content;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column feedback.content
     *
     * @param content the value for feedback.content
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column feedback.app_version
     *
     * @return the value of feedback.app_version
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    public String getAppVersion() {
        return appVersion;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column feedback.app_version
     *
     * @param appVersion the value for feedback.app_version
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion == null ? null : appVersion.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column feedback.uadetail
     *
     * @return the value of feedback.uadetail
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    public String getUadetail() {
        return uadetail;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column feedback.uadetail
     *
     * @param uadetail the value for feedback.uadetail
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    public void setUadetail(String uadetail) {
        this.uadetail = uadetail == null ? null : uadetail.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column feedback.udid
     *
     * @return the value of feedback.udid
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    public String getUdid() {
        return udid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column feedback.udid
     *
     * @param udid the value for feedback.udid
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    public void setUdid(String udid) {
        this.udid = udid == null ? null : udid.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column feedback.channel_id
     *
     * @return the value of feedback.channel_id
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    public String getChannelId() {
        return channelId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column feedback.channel_id
     *
     * @param channelId the value for feedback.channel_id
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    public void setChannelId(String channelId) {
        this.channelId = channelId == null ? null : channelId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column feedback.phone
     *
     * @return the value of feedback.phone
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    public String getPhone() {
        return phone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column feedback.phone
     *
     * @param phone the value for feedback.phone
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column feedback.ip
     *
     * @return the value of feedback.ip
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    public String getIp() {
        return ip;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column feedback.ip
     *
     * @param ip the value for feedback.ip
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column feedback.create_time
     *
     * @return the value of feedback.create_time
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column feedback.create_time
     *
     * @param createTime the value for feedback.create_time
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column feedback.state
     *
     * @return the value of feedback.state
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    public Integer getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column feedback.state
     *
     * @param state the value for feedback.state
     *
     * @mbggenerated Tue Feb 16 17:56:31 CST 2016
     */
    public void setState(Integer state) {
        this.state = state;
    }
}