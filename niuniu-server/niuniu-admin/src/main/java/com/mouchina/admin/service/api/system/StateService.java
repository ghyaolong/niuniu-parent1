package com.mouchina.admin.service.api.system;

import java.util.Map;

/**
 * Created by douzy on 15/8/27.
 */
public interface StateService {
    /**
     * 获取订单状态
     * @return
     */
    Map<Integer,String> getOrderState();

    /**
     * 订单详细   处理状态
     * @return
     */
    Map<Integer,String> getOrderDetailState();

    /**
     * 退款状态
     * @return
     */
    Map<Integer,String> getOrderRemiburseState();

    /**
     *优惠券状态
     * @return
     */
    Map<Integer,String> getCashCouponState();

    /**
     * 支付方式
     * @return
     */
    Map<Integer,String> getPayChannelState();

    /**
     * 支付状态
     * @return
     */
    Map<Integer,String> getPayOrderState();

    /**
     * 提现状态
     * @return
     */
    Map<Integer,String> getWithbrawalState();
}
