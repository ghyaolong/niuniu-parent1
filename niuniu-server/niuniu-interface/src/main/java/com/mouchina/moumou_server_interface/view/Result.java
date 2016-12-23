package com.mouchina.moumou_server_interface.view;

import java.io.Serializable;

/**
 * 返回结果集的处理
 * 
 * @author Administrator
 *
 * @param <T>
 */
public class Result<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5022011415026317862L;
	/***
	 * state 1表示成功 0表示失败 2 异常
	 */
	private int state;
	/**
	 * 返回对象内容
	 */
	private T content;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public T getContent() {
		return content;
	}

	public void setContent(T content) {
		this.content = content;
	}

}
