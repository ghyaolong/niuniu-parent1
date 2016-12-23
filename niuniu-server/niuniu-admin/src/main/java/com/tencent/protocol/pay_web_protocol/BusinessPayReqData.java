package com.tencent.protocol.pay_web_protocol;

import com.tencent.common.RandomStringGenerator;
import com.tencent.common.Signature;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by douzy on 16/3/10.
 */
public class BusinessPayReqData {
    //公众账号appid
     private String mch_appid="";
    //商户号
     private String mchid="";
    //设备号   --否
     private String device_info="";
    //随机字符串
     private String nonce_str="";
    //签名
     private String sign="";
    //商户订单号
     private String partner_trade_no="";
    // 用户openid
     private String openid="";
    /***
     *校验用户姓名选项
     * NO_CHECK：不校验真实姓名
     *FORCE_CHECK：强校验真实姓名（未实名认证的用户会校验失败，无法转账）
     *OPTION_CHECK：针对已实名认证的用户才校验真实姓名（未实名认证用户不校验，可以转账成功）
     */
     private String check_name="";
     //收款用户姓名
     private String re_user_name="";
     //金额
     private int amount=0;
     //企业付款描述信息
     private String desc="";
     //Ip地址
     private String spbill_create_ip="";

    public BusinessPayReqData(String mchAppId,String mchId,String devIceInfo,String partnerTradeNo,String openId,String checkName,String reUserName,int amount,String desc,String  spbillCreateIp) {
        setMch_appid(mchAppId);
        setMchid(mchId);
        setDevice_info(devIceInfo);

        setPartner_trade_no(partnerTradeNo);
        setOpenid(openId);
        setCheck_name(checkName);
        setRe_user_name(reUserName);
        setAmount(amount);
        setDesc(desc);
        setSpbill_create_ip(spbillCreateIp);
        //随机字符串，不长于32 位
        setNonce_str(RandomStringGenerator.getRandomStringByLength(32));

        //根据API给的签名规则进行签名
        String sg = Signature.getSign(toMap());
        setSign(sg);//把签名数据设置到Sign这个属性中
    }

    public String getMch_appid() {
        return mch_appid;
    }

    public void setMch_appid(String mch_appid) {
        this.mch_appid = mch_appid;
    }

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getPartner_trade_no() {
        return partner_trade_no;
    }

    public void setPartner_trade_no(String partner_trade_no) {
        this.partner_trade_no = partner_trade_no;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getCheck_name() {
        return check_name;
    }

    public void setCheck_name(String check_name) {
        this.check_name = check_name;
    }

    public String getRe_user_name() {
        return re_user_name;
    }

    public void setRe_user_name(String re_user_name) {
        this.re_user_name = re_user_name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSpbill_create_ip() {
        return spbill_create_ip;
    }

    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }
    public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<String, Object>();
        Field[] fields = this.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object obj;
            try {
                obj = field.get(this);
                if(obj!=null){
                    map.put(field.getName(), obj);
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

}
