package com.mouchina.moumou_server.exceptions;

import com.mouchina.base.exceptions.BusinessException;
/**
 * 红包异常
 * @author zhangkun
 *
 */
public class RedEnvelopeException extends BusinessException{
	private static final long serialVersionUID = -4251011529778550089L;
	
	public RedEnvelopeException(boolean type, String message, Throwable ex) {
		super(type, message, ex);
	}

	public RedEnvelopeException(boolean type, String message) {
		super(type, message);
	}
	
	public RedEnvelopeException(boolean type, String message,String code) {
		super(type, message,code);
	}

	public RedEnvelopeException(boolean type, Throwable ex) {
		super(type, ex);
	}
}
