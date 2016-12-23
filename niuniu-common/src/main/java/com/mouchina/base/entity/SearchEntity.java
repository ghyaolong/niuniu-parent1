package com.mouchina.base.entity;

import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;

public class SearchEntity implements IEntity {
	private static final long serialVersionUID = 2282780541293751689L;
	
	
	/**
	 * 查询是否加锁
	 */
	@JsonIgnore
	private boolean queryLock;
	
	/**
	 * 查询是否校验结果
	 */
	private boolean verifyNull;
	
	/**
	 * ID
	 */
	private Integer id;
	/**
	 * 查询 key
	 */
	private String key;
	/**
	 * 查询 value
	 */
	private String value;
	
	/**
	 * 查询 开始日期
	 */
	private Date beginDate;
	/**
	 * 查询 结束日期
	 */
	private Date endDate;
	
	/**
	 * 状态
	 */
	private long state;
	/**
	 * 总条数
	 */
	private long totalRecord;
	/**
	 * 当前页数
	 */
	private long currentPage = 1;
	
	/**
	 * @author 侯斌飞
	 * @version 0.1 2015-07-01 09:22:17 分页信息：数量 限制
	 */
	private long limit = 10;
	
	private long start = 0;
	
	private long totalPage;
	/**
	 * @param totalRecord the totalRecord to set
	 */
	public void setTotalRecord(long totalRecord) {
		this.totalRecord = totalRecord;
	}


	/**
	 * @param currentPage the currentPage to set
	 */
	public void setCurrentPage(Object currentPage) {
		if(currentPage instanceof Number){
			this.currentPage = ((Number)currentPage).longValue();
		}else if (currentPage instanceof String){
			this.currentPage = Long.valueOf((String)currentPage);
		}
		
		if(this.currentPage == 0){
			this.currentPage = 1;
		}
	}

	/**
	 * @author 侯斌飞
	 * @version 0.1 2015-07-01 09:22:17
	 * @return the start
	 */
	public long getStart() {
		this.start = (this.getCurrentPage()-1) * this.getLimit();
		return this.start;
	}
	
	/**
	 * 是否可以整除
	 * 
	 * @return true 是，false 否
	 */
	private boolean isDivisible(){
		return (this.getTotalRecord() % this.getLimit()) > 0 ?false:true;
	}

	/**
	 * @author 侯斌飞
	 * @version 0.1 2015-07-01 09:22:17
	 * @return the limit
	 */
	public long getLimit() {
		return limit;
	}

	/**
	 * @author 侯斌飞
	 * @version 0.1 2015-07-01 09:22:17
	 * @param limit
	 *            the limit to set
	 */
	public void setLimit(long limit) {
		this.limit = limit;
	}

	public long getTotalRecord() {
		return totalRecord;
	}

	public long getTotalPage() {
		long shang= this.getTotalRecord() / this.getLimit();
		this.totalPage = isDivisible()?shang:shang+1;
		return this.totalPage;
	}

	public long getCurrentPage() {
		return currentPage;
	}


	public String getKey() {
		return key;
	}


	public void setKey(String key) {
		this.key = key;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	

	/**
	 * 查询是否加锁
	 * @return the queryLock
	 */
	public boolean isQueryLock() {
		return queryLock;
	}


	/**
	 * 设置：查询是否加锁
	 * @param queryLock the queryLock to set
	 */
	public void setQueryLock(boolean queryLock) {
		this.queryLock = queryLock;
	}

	/**
	 * @return the state
	 */
	public long getState() {
		return state;
	}


	/**
	 * @param state the state to set
	 */
	public void setState(long state) {
		this.state = state;
	}


	/**
	 * @return the verifyNull
	 */
	public boolean isVerifyNull() {
		return verifyNull;
	}


	/**
	 * @param verifyNull the verifyNull to set
	 */
	public void setVerifyNull(boolean verifyNull) {
		this.verifyNull = verifyNull;
	}
	
	public Date getBeginDate() {
		return beginDate;
	}


	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}


	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
