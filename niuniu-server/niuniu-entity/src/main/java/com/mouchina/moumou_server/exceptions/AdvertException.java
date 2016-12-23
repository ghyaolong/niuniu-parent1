package com.mouchina.moumou_server.exceptions;

import com.mouchina.base.exceptions.BusinessException;
/**
 * 广告异常
 * @author hpf
 *
 */
public class AdvertException extends BusinessException {
	private static final long serialVersionUID = -4251011529778550089L;

	public AdvertException(boolean type, String message, Throwable ex) {
		super(type, message, ex);
	}

	public AdvertException(boolean type, String message) {
		super(type, message);
	}
	
	public AdvertException(boolean type, String message,String code) {
		super(type, message,code);
	}

	public AdvertException(boolean type, Throwable ex) {
		super(type, ex);
	}
}
