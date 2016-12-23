package com.mouchina.niuniu.job;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.mouchina.moumou_server.entity.member.User;
import com.mouchina.moumou_server_interface.member.UserVerifyService;
import com.mouchina.niuniu.util.AliDaYuSMSUtils;

/**
 * 短信召回沉默用户job
 * @author Administrator
 *
 */
public class SendMessageJob extends ABaseJob {
	@Resource
	UserVerifyService userVerifyService;
	
	public void execute() {
		/*8张用户表循环8次*/
		for(int i=0;i<8;i++){
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("tableNum", i);
			map.put("createTime", new Date());
			/*查找沉默用户*/
			List<User> list = userVerifyService.selectComeBackUser(map);
			if(list.size() != 0){
				for(User user:list){
					String phone = user.getPhone();
					String nickName = user.getNickName();
					String bindingMobile = user.getBindingMobile();
					int num = user.getNum();
					if(phone == null){
						phone = bindingMobile;
					}
					if(num == 8){
						/*7天未登录用户*/
						AliDaYuSMSUtils.sendSmsNew("https://eco.taobao.com/router/rest", "23485707",
								"a1e8f4efd4062af22d404362f1e4ece1", "{\"name\" : " + "\"" + nickName + "\"}", phone, "SMS_33545380");
					}else if(num == 15){
						/*14天未登录用户*/
						AliDaYuSMSUtils.sendSmsNew("https://eco.taobao.com/router/rest", "23485707",
								"a1e8f4efd4062af22d404362f1e4ece1", "{\"name\" : " + "\"" + nickName + "\"}", phone, "SMS_33525333");
					}else if(num == 31){
						/*30天未登录用户*/
						AliDaYuSMSUtils.sendSmsNew("https://eco.taobao.com/router/rest", "23485707",
								"a1e8f4efd4062af22d404362f1e4ece1", "{\"name\" : " + "\"" + nickName + "\"}", phone, "SMS_33670217");
					}
				}
			}
		}
	}
	
	public static void main(String[] args) {
		
	}
	
}
