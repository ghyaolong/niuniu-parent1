package com.mouchina.web.service.api.vo;

import java.io.Serializable;

/**
 * 代理商模板实体vo
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class AgentTemplateVo implements Serializable {

	private Long id; //主键
	
	private Integer agentLevel; //代理商级别
	
	private String privilege; //代理商特权
	
	private Integer agentAmount; //代理商费用
	

	public AgentTemplateVo() {
		super();
	}

	public AgentTemplateVo(Long id, Integer agentLevel, String privilege, Integer agentAmount) {
		super();
		this.id = id;
		this.agentLevel = agentLevel;
		this.privilege = privilege;
		this.agentAmount = agentAmount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getAgentLevel() {
		return agentLevel;
	}

	public void setAgentLevel(Integer agentLevel) {
		this.agentLevel = agentLevel;
	}

	public String getPrivilege() {
		return privilege;
	}

	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	public Integer getAgentAmount() {
		return agentAmount;
	}

	public void setAgentAmount(Integer agentAmount) {
		this.agentAmount = agentAmount;
	}
	
}
