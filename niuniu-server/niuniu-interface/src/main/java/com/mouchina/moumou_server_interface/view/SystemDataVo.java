package com.mouchina.moumou_server_interface.view;

import java.io.Serializable;

/**
 * Created by douzy on 16/2/23.
 */
public class SystemDataVo implements Serializable {
    private Integer id;

    private String name;
    private Integer value;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
    
}
