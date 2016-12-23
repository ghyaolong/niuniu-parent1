package com.mouchina.moumou_server_dubbo.provider.message;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.mouchina.moumou_server.dao.message.SmsTemplateMapper;
import com.mouchina.moumou_server.entity.message.SmsTemplate;
import com.mouchina.moumou_server_interface.message.SmsTemplateService;
import com.mouchina.moumou_server_interface.view.MessageResult;

public class SmsTemplateServiceSupport implements SmsTemplateService {
	@Resource
	private SmsTemplateMapper smsTemplateMapper;

	@Override
	public MessageResult<SmsTemplate> selectSmsTemplate(String templateKey) {
		MessageResult<SmsTemplate> messageResult = new MessageResult<SmsTemplate>();
		SmsTemplate  smsTemplate=smsTemplateMapper.selectByPrimaryKey(templateKey);
		if(smsTemplate!=null){
			messageResult.setContent(smsTemplate);
			messageResult.setState(1);
			
		}
		return messageResult;
	}

	@Override
	public MessageResult<Integer> updateSmsTemplate(SmsTemplate smsTemplate) {
		MessageResult<Integer> messageResult = new MessageResult<Integer>();
		messageResult.setState(smsTemplateMapper.updateByPrimaryKeySelective(smsTemplate));
		return messageResult;
	}

	@Override
	public MessageResult<SmsTemplate> addSmsTemplate(SmsTemplate smsTemplate) {
		MessageResult<SmsTemplate> messageResult = new MessageResult<SmsTemplate>();
		messageResult.setState(smsTemplateMapper.insertSelective(smsTemplate));
		messageResult.setContent(smsTemplate);
		return messageResult;
	}

	@Override
	public MessageResult<Integer> deleteSmsTemplate(String templateKey) {
		MessageResult<Integer> messageResult = new MessageResult<Integer>();
		messageResult.setState(smsTemplateMapper.deleteByPrimaryKey(templateKey));
		return messageResult;
	}

	@Override
	public MessageResult<List<SmsTemplate>> selectListSmsTemplate(Map map) {
		MessageResult<List<SmsTemplate>> messageResult = new MessageResult<List<SmsTemplate>>();
		messageResult.setState(1);
		messageResult.setContent(smsTemplateMapper.selectList(map));
		return messageResult;
	}

}
