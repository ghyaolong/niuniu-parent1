package com.mouchina.base.exceptions;
/**
 * App 异常
 * @author hpf
 *
 */
public class AppException extends AGeneralException {
	private static final long serialVersionUID = 6948341692366409728L;
	public AppException() {
		super();
	}

	public AppException(boolean type, String message, Throwable ex) {
		super(type, message, ex);
	}

	public AppException(boolean type, String message) {
		super(type, message);
	}

	public AppException(boolean type, Throwable ex) {
		super(type, ex);
	}
}
