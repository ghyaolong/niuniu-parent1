package com.mouchina.base.common.page;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author larry 2013-08-11 desc：分页数据组装
 */
public class ListRange<T> implements Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private List<T> data;
	private Page page;
	private Map facets;
	private Map allFacets;

	public Map getAllFacets() {
		return allFacets;
	}

	public void setAllFacets(Map allFacets) {
		this.allFacets = allFacets;
	}

	public Map getFacets() {
		return facets;
	}

	public void setFacets(Map facets) {
		this.facets = facets;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
}
