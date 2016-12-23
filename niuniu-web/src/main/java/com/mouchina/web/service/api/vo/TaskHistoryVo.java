package com.mouchina.web.service.api.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TaskHistoryVo {
	
	private Object obj;
	private String result;

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		if(!(obj instanceof Collection)) {
			List<Object> objs = new ArrayList<Object>();
			objs.add(obj);
			obj = objs;
		}
		this.obj = obj;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
