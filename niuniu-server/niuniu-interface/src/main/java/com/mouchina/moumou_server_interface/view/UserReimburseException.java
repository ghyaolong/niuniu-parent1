package com.mouchina.moumou_server_interface.view;


/**
 *类说明
 *@author larry
 * 2015年7月3日上午10:10:32
 */
public class UserReimburseException  extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3713931282835294866L;
	
	private UserReimburseState  userReimburseState;
	private String  userReimburseStateContent;
	
	public UserReimburseException()
	{
		super();
	}

	public UserReimburseException(UserReimburseState userReimburseState)
	{
		this.userReimburseState = userReimburseState;
	}
	public UserReimburseException(UserReimburseState userReimburseState, String userReimburseStateContent)
	{
		super();
		this.userReimburseState=userReimburseState;
		this.userReimburseStateContent=userReimburseStateContent;
	}
	public UserReimburseException(UserReimburseState userReimburseState, String message, Throwable cause)
	{
		super(message, cause);
		this.userReimburseState = userReimburseState;
	}
	public String toExecptionString(){
		
		return this.userReimburseState+","+this.userReimburseState+"";
	}

	public UserReimburseState getUserReimburseState() {
		return userReimburseState;
	}

	public void setUserReimburseState(UserReimburseState userReimburseState) {
		this.userReimburseState = userReimburseState;
	}

	public String getUserReimburseStateContent() {
		return userReimburseStateContent;
	}

	public void setUserReimburseStateContent(String userReimburseStateContent) {
		this.userReimburseStateContent = userReimburseStateContent;
	}

	

}
