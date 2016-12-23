package com.mouchina.moumou_server.exceptions;

import com.mouchina.base.enums.IEnum;
import com.mouchina.base.exceptions.EnumBusinessException;
/**
 * 用户异常
 * @author hpf
 *
 */
public class UserException extends EnumBusinessException {
	private static final long serialVersionUID = -4251011529778550089L;

	public UserException(boolean type, IEnum ienum, String extendMessage) {
		super(type, ienum, extendMessage);
	}

	public UserException(boolean type, IEnum ienum, Throwable ex) {
		super(type, ienum, ex);
	}

	public UserException(boolean type, IEnum ienum) {
		super(type, ienum);
	}

	public UserException(boolean type, String message) {
		super(type, message);
	}
	
}
