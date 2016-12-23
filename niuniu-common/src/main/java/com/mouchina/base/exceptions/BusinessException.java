package com.mouchina.base.exceptions;
/**
 * 业务异常
 * @author hpf
 *
 */
public class BusinessException extends AGeneralException {
	private static final long serialVersionUID = 7654164305125280659L;

	public BusinessException() {
		super();
	}

	public BusinessException(boolean type, String message, Throwable ex) {
		super(type, message, ex);
	}

	public BusinessException(boolean type, String message) {
		super(type, message);
	}
	
	public BusinessException(boolean type, String message,String code) {
		super(type, message,code);
	}

	public BusinessException(boolean type, Throwable ex) {
		super(type, ex);
	}
}
