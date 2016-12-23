package com.mouchina.web.service.api.vo;

import java.io.Serializable;

/**
 * Created by douzy on 16/2/22.
 */
public class SystemVo implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3542248527027914949L;
	private String key;
    private String value;

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

   
}
