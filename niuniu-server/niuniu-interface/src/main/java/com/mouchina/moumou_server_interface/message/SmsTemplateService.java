package com.mouchina.moumou_server_interface.message;

import com.mouchina.moumou_server.entity.message.SmsTemplate;
import com.mouchina.moumou_server_interface.view.MessageResult;

import java.util.List;
import java.util.Map;

public interface SmsTemplateService {

	/**
	 * 获取短信模版
	 * @param templateKey
	 * @return
	 */
	MessageResult<SmsTemplate>  selectSmsTemplate(String templateKey);
	/**
	 * 更新短信模版
	 * @param smsTemplate
	 * @return
	 */
	MessageResult<Integer>  updateSmsTemplate(SmsTemplate smsTemplate);
	
	/**
	 * 添加短信模版
	 * @param smsTemplate
	 * @return
	 */
	MessageResult<SmsTemplate> addSmsTemplate(SmsTemplate smsTemplate);
	/***
	 * 删除短信模版
	 * @param templateKey
	 * @return
	 */
	MessageResult<Integer>  deleteSmsTemplate(String templateKey);
	/**
	 * 获取短信模版列表
	 * @param map
	 * @return
	 */
	MessageResult<List<SmsTemplate>> selectListSmsTemplate(Map map);
}
