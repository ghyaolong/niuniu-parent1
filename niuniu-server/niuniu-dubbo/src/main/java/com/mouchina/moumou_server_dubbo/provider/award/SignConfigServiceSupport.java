package com.mouchina.moumou_server_dubbo.provider.award;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mouchina.moumou_server.dao.award.SignConfigMapper;
import com.mouchina.moumou_server.entity.award.SignConfig;
import com.mouchina.moumou_server_interface.award.SignConfigService;

/**
 * 签到
 * @author Administrator
 *
 */
@Service("signConfigServiceSupport")
public class SignConfigServiceSupport implements SignConfigService {
	
	@Resource
	private SignConfigMapper signConfigMapper;

	@Override
	public void addSignConfig(SignConfig signConfig) {
		signConfigMapper.insert(signConfig);
	}

	@Override
	public void deleteSignConfig(Long signConfigId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateSignConfig(SignConfig signConfig) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<SignConfig> findAll() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
