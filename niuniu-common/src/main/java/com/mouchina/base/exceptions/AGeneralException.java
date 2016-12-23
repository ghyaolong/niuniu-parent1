package com.mouchina.base.exceptions;

public abstract class AGeneralException extends RuntimeException implements IException{
	private static final long serialVersionUID = -7162258241193319567L;
	protected static final String TEMPLATE = "%s -> %s";
	
	protected String code;
	@Override
	public final String getMessage() {
		if(this.getCause() != null){
			this.setMessage(this.extendMessage(this.message, this.getCause()));
		}
		return message == null?super.getMessage():message;
	}

	@Override
	public final StackTraceElement[] getStackTrace() {
		return super.getStackTrace();
	}
	
	public AGeneralException(){
		super();
	}
	
	public AGeneralException(boolean type,Throwable ex){
		super(ex);
		this.inint(type, message);
	}
	
	public AGeneralException(boolean type,String message,Throwable ex){
		super(message,ex);
		this.inint(type, message);
	}
	
	public AGeneralException(boolean type,String message){
		super(message);
		this.inint(type, message);
	}
	
	public AGeneralException(boolean type,String message,String code){
		super(message);
		this.inint(type, message,code);
	}
	
	/**
	 * 初始化异常信息
	 * @param type
	 * @param message
	 */
	protected void inint(boolean type,String message){
		this.setType(type);
		this.setMessage(message);
	}
	/**
	 * 初始化异常信息
	 * @param type
	 * @param message
	 */
	protected void inint(boolean type,String message,String code){
		this.setType(type);
		this.setCode(code);
		this.setMessage(message);
	}
	
	/**
	 * 异常类型
	 */
	private boolean type;

	
	/**
	 * 消息
	 */
	private String message;
	
	@Override
	public final boolean type() {
		return type;
	}
	
	protected final void setType(boolean type){
		this.type = type;
	}

	protected final void setMessage(String message){
		this.message = message;
	}
	
	protected final void setCode(String code){
		this.code = code;
	}
	
	

	
	/**
	 * message 消息追加
	 * @param message 当前 消息
	 * @param ex 异常信息
	 * @return
	 */
	protected String extendMessage(String message,Throwable ex){
		if(null == ex){
			return message;
		}
		return String.format(TEMPLATE, message,ex.getMessage());
	}
	
	@Override
	public String code() {
		return code;
	}
	@Override
	public String code(String defaultCode){
		return (null == code || "".equals(code.trim()))?defaultCode:code;
	}
	
}
