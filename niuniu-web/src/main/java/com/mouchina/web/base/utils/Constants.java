package com.mouchina.web.base.utils;

import java.text.SimpleDateFormat;

/**
 * Description: Constants
 * Author: ourufeng
 * Update: ourufeng(2015-05-07 14:40)
 */
public final class Constants {
	/**
	 * 保存活动邀请注册人数给予奖励个数
	 */
	public static final int invite_num = 3;
	
	public static SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
	public static SimpleDateFormat dateformat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat dateformat3 = new SimpleDateFormat("yyyy-MM-dd");
	
	public static final int ERROR_OK = 1;
    public static final int ERROR_ERROR = 0;
    public static final int ERROR_NO_DATA = 2;
	public static final int ERROR_UNKNOW = 5;
	
	/**
	 * 信息错误
	 */
	public static final int ERROR_CODE_100000 = 100000;
	
	/**
	 * 信息不完整
	 */
	public static final int ERROR_CODE_100001 = 100001;
	/**
	 *  签名验证失败
	 */
	public static final int ERROR_CODE_100002 = 100002;
	/**
	 * 请求异常
	 */
	public static final int ERROR_CODE_100003 = 100003;
	/**
	 * 校验码错误
	 */
	public static final int ERROR_CODE_100004 = 100004;
	
	/**
	 * 校验码错误
	 */
	public static final int ERROR_CODE_100005 = 100005;
	
	/**
	 * 邀请码错误
	 */
	public static final int ERROR_CODE_100015 = 100015;
    public static final int ERROR_CODE_100013 = 100013;
	public static final int ERROR_CODE_100016 = 100016;
	public static final int ERROR_CODE_100019 = 100019;
	public static final int ERROR_CODE_100020 = 100020;
	public static final int ERROR_CODE_100021 = 100021;
    public static final int ERROR_CODE_100022 = 100022;
    
    
    /**
	 * 用户不存在
	 */
	public static final int ERROR_CODE_100101 = 100101;
	
	/**
	 * 用户被禁用
	 */
	public static final int ERROR_CODE_100103 = 100103;
	
	/**
	 * 用户验证错误
	 */
	public static final int ERROR_CODE_100102 = 100102;
    /**
     * 没有获取到广告
     */
    public static final int ERROR_CODE_200001 = 200001;
    
    
    /**
     * 没有获取到红包(实物奖品)
     */
    public static final int ERROR_CODE_200002 = 200002;
    
    
   /**
    * 广告发布金额错误
    */
    public static final int ERROR_CODE_200100 = 200100;
    
    /**
     * 广告发布余额不足
     */
    public static final int ERROR_CODE_200101 = 200101;
    
    public static final int ERROR_CODE_200004 = 200004;
    public static final int ERROR_CODE_200006 = 200006;
	public static final int ERROR_CODE_200008 = 200008;
	public static final int ERROR_CODE_200203 = 200203;
	public static final int ERROR_CODE_200205 = 200205;
	public static final int ERROR_CODE_200216 = 200216;
	
	
	/**
	 * 提现余额不足
	 */
	 public static final int ERROR_CODE_200501 = 200501;
	 
	 /***
	  * 不能满足红包余额条件
	  */
	 public static final int ERROR_CODE_200502 = 200502;
	 
	 /**
	  * 用户提现中
	  */
	 public static final int ERROR_CODE_200503 = 200503;
	 
	 /**
	 * 提现账户类型有误
	 */
	 public static final int ERROR_CODE_200504 = 200504;
	 
	 /**
	 * 提现限制不通过
	 */
	 public static final int ERROR_CODE_200505 = 200505;
	
	 /**
	 * 提现账户记录入库失败
	 */
	 public static final int ERROR_CODE_200506 = 200506;
	 /**
	  * 更新余额失败
	  */
	 public static final int ERROR_CODE_200507 = 200507;

	public static final int ERROR_CODE_300001 = 300001;
	
	/**
	 * 未查询到认证信息
	 */
	public static final int ERROR_CODE_300100 = 300100;
	
	/**
	 * 不是认证商户，不可以发布优惠券(首页广告)
	 */
	public static final int ERROR_CODE_300101 = 300101;
	
	/**
	 * 当天发布优惠券(说说)次数已达上限
	 */
	public static final int ERROR_CODE_300102 = 300102;
	
	/**
	 * 未查询到商户店铺信息
	 */
	public static final int ERROR_CODE_400001 = 400001;
	/**
	 * 维护商户店铺信息错误
	 */
	public static final int ERROR_CODE_400002 = 400002;
	/**
	 * 首页广告竞价(重新竞价)金额错误
	 */
	public static final int ERROR_CODE_400003 = 400003;
	/**
	 * 点赞数为零
	 */
	public static final int ERROR_CODE_500003 = 500003;
	/**
	 * 未查到广告信息
	 */
	public static final int ERROR_CODE_500004 = 500004;
	
	/**
	 * 接力福袋(首页广告福袋)不存在
	 */
	public static final int ERROR_CODE_500001 = 500001;
	/**
	 * 当前用户已开启过此接力福袋(当前用户当天已领取此首页广告福袋)
	 */
	public static final int ERROR_CODE_500002 = 500002;
	/**
	 * 当前用户当天领取首页广告福袋已达次数上限
	 */
	public static final int ERROR_CODE_500005 = 500005;
	/**
	 * 当前用户余额不足
	 */
	public static final int ERROR_CODE_500006 = 500006;
	/**
	 * 维护星级代理商信息错误
	 */
	public static final int ERROR_CODE_500007 = 500007;
	
	/**
	 * 请求参数值错误
	 */
	public static final int ERROR_CODE_600000 = 600000;
	
	/**
	 * 非法数据
	 */
	public static final int ERROR_CODE_900100 = 900100;
	/**
	 * 一天内多次修改
	 */
	public static final int ERROR_CODE_800100 = 800100;
	
	public static final String REG_SMS_CODE="reg_sms_code_";
	public static final String FROGET_SMS_CODE="forget_sms_code_";
	public static final String BINDING_SMS_CODE="binding_sms_code_";
	public static final String CHARSET_UTF8 = "utf-8";
	/**
	 * 昵称重复
	 */
	public static final int ERROR_CODE_500008 = 500008;
	/**
	 * 捐款金额不正确
	 */
	public static final int ERROR_CODE_500009 = 500009;
	/**
	 * 活动次数不足
	 */
	public static final int ERROR_CODE_600009 = 600009;
	/**
	 * 抽奖失败
	 */
	public static final int ERROR_CODE_600008 = 600008;
	
	public static final String MALLADDRESS ="";
}
