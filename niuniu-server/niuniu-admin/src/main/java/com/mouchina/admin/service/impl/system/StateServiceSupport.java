package com.mouchina.admin.service.impl.system;

import com.mouchina.admin.service.api.system.StateService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by douzy on 15/8/27.
 */
@Service
public class StateServiceSupport implements StateService {

    /**
     * 获取订单状态
     *
     * @return
     */
    @Override
    public Map<Integer,String> getOrderState() {
        Map<Integer,String> map = new HashMap();
        map.put(100, "下单成功");
        map.put(108, "超时取消");
        map.put(109, "用户取消");
        map.put(200, "支付成功");
        map.put(201, "支付成功");
        map.put(300, "服务成功");
        map.put(310, "老师服务签到");
        map.put(320, "服务完成");
        map.put(390, "服务投诉");
        map.put(400, "交易完成");
        map.put(401, "交易完成并评价");
        return map;
    }

    /**
     *订单明细状态
     * @return
     */
    @Override
    public Map<Integer,String> getOrderDetailState()
    {
        Map<Integer,String> map = new HashMap();
        map.put(1, "初始");
        map.put(2, "签到");
        map.put(3, "签退");
        map.put(4, "完成");
        map.put(5, "投诉");
        map.put(6, "退款申请");
        map.put(7, "已退款");
        map.put(8, "投诉完成");
        map.put(9, "已评价");
        return map;
    }

    /**
     * 用户退款状态
     * @return
     */
    @Override
    public Map<Integer,String> getOrderRemiburseState() {
        Map<Integer, String> map = new HashMap<Integer,String>();
        map.put(1, "初始");
        map.put(2, "退款成功");
        map.put(3, "退款失败");
        map.put(4, "退款中");
        map.put(5, "退款异常");
        return map;
    }
    @Override
    public Map<Integer,String> getCashCouponState()
    {
        Map<Integer,String> map=new HashMap<Integer,String>();
        map.put(1, "未使用");
        map.put(2, "已使用");
        map.put(5, "过期");
        map.put(6, "锁定中");
        return map;
    }

    /**
     *支付方式
     * @return
     */
    @Override
    public Map<Integer,String> getPayChannelState()
    {
        Map<Integer,String> map=new HashMap();
        map.put(1, "支付宝");
        map.put(2, "微信");
        map.put(3, "余额");
        map.put(4, "管理员");
        return map;
    }

    /**
     * 支付状态
     * @return
     */
    @Override
    public Map<Integer,String> getPayOrderState()
    {
        Map<Integer,String> map=new HashMap();
        map.put(1, "未支付");
        map.put(2, "支付成功");
        map.put(3, "支付失败");
        map.put(4, "用户取消");
        map.put(5, "支付完成");
        map.put(6, "退款申请");
        map.put(7, "退款完成");
        return map;
    }
    @Override
    public Map<Integer,String> getWithbrawalState() {
        Map<Integer, String> map = new HashMap();
        map.put(1, "申请");
        map.put(2, "完成");
        map.put(3, "失败"); // 按现有程序逻辑，3应该表示成功
        map.put(4, "提现中");
        return map;
    }
}
