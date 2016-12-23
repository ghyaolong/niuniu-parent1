package com.mouchina.base.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户分页保存数据
 * @author 侯斌飞
 * @param <T> 要返回的 实体 <T extends BaseEntity>
 */
public class ReturnEntity<T extends BaseEntity> extends SearchEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1206204610430575594L;
	
	/**
	 * 分页数据集合
	 */
	private List<T> list = new ArrayList<T>();
	
	public ReturnEntity(List<T> data){
		this.setList(data);
	}
	public ReturnEntity(){}
	
	/**
	 * 设置分页信息
	 * @param searchEntity
	 */
	public void settingPager(SearchEntity searchEntity){
		this.setLimit(searchEntity.getLimit());
		this.setCurrentPage(searchEntity.getCurrentPage());
	}
	
	/**
	 * get 分页数据集合
	 * @return
	 */
	public List<T> getList() {
		return list;
	}
	
	/**
	 * set 分页数据集合
	 * @param list 数据集合
	 */
	public void setList(List<T> list) {
		this.list = list;
	}
}
