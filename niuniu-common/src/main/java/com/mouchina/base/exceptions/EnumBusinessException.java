package com.mouchina.base.exceptions;

import com.mouchina.base.enums.IEnum;

/**
 * 业务异常
 * @author hpf
 *
 */
public class EnumBusinessException extends AEnumException {
	private static final long serialVersionUID = 7654164305125280659L;

	public EnumBusinessException(boolean type, IEnum ienum, String extendMessage) {
		super(type, ienum, extendMessage);
	}

	public EnumBusinessException(boolean type, IEnum ienum, Throwable ex) {
		super(type, ienum, ex);
	}

	public EnumBusinessException(boolean type, IEnum ienum) {
		super(type, ienum);
	}

	public EnumBusinessException(boolean type, String message, Throwable ex) {
		super(type, message, ex);
	}

	public EnumBusinessException(boolean type, String message) {
		super(type, message);
	}

	public EnumBusinessException(boolean type, Throwable ex) {
		super(type, ex);
	}
}
