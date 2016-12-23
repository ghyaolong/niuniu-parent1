package com.mouchina.base.exceptions;

import com.mouchina.base.enums.IEnum;

public abstract class AEnumException extends AGeneralException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 13543750964158404L;

	public AEnumException(boolean type,IEnum ienum,Throwable ex){
		super(type, ex);
		this.inint(type, ienum);
	}
	
	public AEnumException(boolean type,IEnum ienum){
		super();
		this.inint(type, ienum);
	}
	
	public AEnumException(boolean type,IEnum ienum, String extendMessage){
		super();
		this.inint(type, ienum);
	
		if(extendMessage != null && !"".equals(extendMessage.trim())){
			this.setMessage(String.format(TEMPLATE, new Object[]{
					this.getMessage(), extendMessage
			}));
		}
	}
	
	public AEnumException(boolean type, String message, Throwable ex) {
		super(type, message, ex);
	}

	public AEnumException(boolean type, String message) {
		super(type, message);
	}

	public AEnumException(boolean type, Throwable ex) {
		super(type, ex);
	}

	/**
	 * 初始化异常信息
	 * @param type
	 * @param ienum
	 */
	private void inint(boolean type,IEnum ienum){
		super.setType(type);
		if(ienum != null){
			this.setCode(ienum.key());
			this.setMessage(ienum.value());
		}
	}
}
