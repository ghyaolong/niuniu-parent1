package com.mouchina.moumou_server_interface.pay;

import java.util.Map;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.base.redis.RedisHelper;
import com.mouchina.moumou_server.entity.pay.PayOrder;
import com.mouchina.moumou_server.entity.pay.UserReimburse;
import com.mouchina.moumou_server_interface.view.OrderDetailClocks;
import com.mouchina.moumou_server_interface.view.PayResult;
import com.mouchina.moumou_server_interface.view.ReimburseResult;
import com.mouchina.moumou_server_interface.view.UserReimburseException;

/**
 * 用户退款服务
 * @author larry
 *
 */
public interface ReimburseService {

	/**
	 * 添加用户退款信息
	 * @param userReimburse
	 * @return
	 */
	PayResult<UserReimburse>  addUserReimburse(UserReimburse userReimburse);
	
	/**
	 * 更新用户退款信息
	 * @param userReimburse
	 * @return
	 */
	PayResult<Integer> updateUserReimburse(UserReimburse userReimburse);
	/**
	 * 查询用户退款信息
	 * @param reimburseNo
	 * @return
	 */
	 PayResult<UserReimburse> selectUserReimburseByReimburseNo(
			 String reimburseNo);
	
	/***
	 * 删除用户退款信息
	 * @param reimburseNo
	 * @return
	 */
	PayResult<Integer>  deleteUserReimburseByReimburseNo(String reimburseNo);
	
	/**
	 * 获取用户退款分页
	 * @return
	 */
	PayResult<ListRange<UserReimburse>>  selectListRangeUserReimbursePage(Map map);
	
	
	/**
	 * 拒绝用户退款
	 * @param UserReimburse
	 * @return
	 */
	PayResult<Integer>  updateRefuseUserUserReimburse(UserReimburse userReimburse);
	
	/**
	 * 同意用户退款
	 * @param UserReimburse
	 * @return
	 */
	 ReimburseResult<Integer> updateAgreeUserUserReimburse(
			 UserReimburse userReimburse);

//	 void reimburseReduceAssets(UserReimburse userReimburse,
//								OrderDetailClocks orderDetailClocks) throws UserReimburseException;

	public String userReimburseNoGenerator();
	PayResult<Integer> updateReimburse(UserReimburse userReimburse);

	PayResult<UserReimburse> getReimburseModel(Map map);

}
