package com.mouchina.moumou_server_interface.view;

import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server.entity.order.WithdrawalHistoryOrder;

import java.io.Serializable;

/**
 * Created by douzy on 15/10/15.
 */
public class WithdrawalView implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6777617670943071565L;

	public WithdrawalHistoryOrder getWithdrawalHistoryOrder() {
        return withdrawalHistoryOrder;
    }

    public void setWithdrawalHistoryOrder(WithdrawalHistoryOrder withdrawalHistoryOrder) {
        this.withdrawalHistoryOrder = withdrawalHistoryOrder;
    }

    private WithdrawalHistoryOrder withdrawalHistoryOrder;
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
