package com.mouchina.moumou_server_interface.order;

import com.mouchina.base.common.page.ListRange;
import com.mouchina.moumou_server.entity.order.WithdrawalHistoryOrder;
import com.mouchina.moumou_server_interface.view.WelfareResult;
import com.mouchina.moumou_server_interface.view.WithdrawalView;

import java.util.Map;

/**
 * Created by douzy on 16/2/18.
 */
public interface WithdrawalsService {
    /**
     * 添加提现（修改用户余额）
     * @param
     * @return 0 老师不存在  －1 余额不够  1请求成功
     */
    WelfareResult<WithdrawalHistoryOrder> addWithdrawalHistoryOrder(WithdrawalHistoryOrder withdrawalHistoryOrder);
    /**
     * 获取提现对象
     * @param withdrawalHistoryOrderId
     * @return
     */
    WelfareResult<WithdrawalHistoryOrder> selectWithdrawalHistoryOrder(Long withdrawalHistoryOrderId);
    /**
     * 更新提现对象
     * @param
     * @return
     */
    WelfareResult<Integer> updateWithdrawalHistoryOrder(WithdrawalHistoryOrder withdrawalHistoryOrder);
    /**
     * 删除提现对象
     * @param withdrawalHistoryOrderId
     * @return
     */
    WelfareResult<Integer> deleteWithdrawalHistoryOrder(Long withdrawalHistoryOrderId);
    /**
     * 获取提现历史
     * @param map
     * @return
     */
    WelfareResult<ListRange<WithdrawalView>> selectListRangeWithdrawalHistory(Map map);

    /**
     * 提现历史分页
     * @param map
     * @return
     */
    WelfareResult<ListRange<WithdrawalView>> selectListRangeWithdrawalHistoryByPager(Map map);
    /**
     * 获取提现对象
     * @param map
     * @return
     */
    WelfareResult<WithdrawalHistoryOrder> selectWithdrawalOrderModel(Map map);
    /**
     * 当前月老师申请提现次数
     * @param map
     * @return
     */
    Integer selectApplyWithdrawalOrderCount(Map map);


}
