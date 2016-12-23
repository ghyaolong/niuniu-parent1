package com.mouchina.admin.service.api.vo;

public class BaseResultVo<T> {

	/**
	 * 请求是否成功
	 */
	private String result;
	/**
	 * 返回的错误码
	 */
	private Object errorCode;
	/**
	 * 返回的错误信息
	 */
	private String errorMsg;
	/**
	 * 当前的页数
	 */
	private Integer offset;
	/**
	 * 返回的数据
	 */
	private T data;

	public String getResult() {
		return result;
	}

	public BaseResultVo() {
		this.result = "1";
		this.errorCode = "";
		this.errorMsg = "";
		this.offset = 0;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Object getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Object errorCode) {
		if (errorCode instanceof Integer) {
			this.errorCode = String.valueOf(errorCode);
		} else if (errorCode instanceof String) {
			this.errorCode = errorCode;
		} else if (errorCode == null) {
			this.errorCode = "";
		}
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		if (errorMsg == null) {
			this.errorMsg = "";
		} else {
			this.errorMsg = errorMsg;
		}
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

}
