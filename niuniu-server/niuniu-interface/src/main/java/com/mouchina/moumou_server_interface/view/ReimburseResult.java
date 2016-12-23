package com.mouchina.moumou_server_interface.view;

/**
 * 类说明
 *
 * @author larry 2015年6月19日下午5:55:53
 */

public class ReimburseResult<T> extends Result<T> {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6252073184901168767L;
	private UserReimburseState userReimburseState;
	private String userReimburseStateContent;
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
