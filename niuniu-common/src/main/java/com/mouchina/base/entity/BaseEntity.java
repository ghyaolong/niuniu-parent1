package com.mouchina.base.entity;

import java.util.Date;



public abstract class BaseEntity implements IEntity {
	private static final long serialVersionUID = -4969903814583440611L;
	
	/**
	 * Description 创建时间
	 * @author 侯斌飞 
	 * @version 0.1 
	 */
	private Date createTime;
	/**
	 * Description 创建人
	 * @author 侯斌飞 
	 * @version 0.1 
	 */
	/**
	 * Description get 创建时间
	 * @author 侯斌飞 
	 * @version 0.1 
	 * @return Date
	 */
	public Date getCreateTime(){
		return createTime;
	}
	
	/**
	 * Description set 创建时间
	 * @author 侯斌飞 
	 * @version 0.1 
	 * @param createTime
	 */
	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}
}
