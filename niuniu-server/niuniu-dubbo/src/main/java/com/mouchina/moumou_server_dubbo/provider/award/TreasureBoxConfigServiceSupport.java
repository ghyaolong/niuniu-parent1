package com.mouchina.moumou_server_dubbo.provider.award;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.mouchina.moumou_server.dao.award.TreasureBoxConfigMapper;
import com.mouchina.moumou_server.entity.award.TreasureBoxConfig;
import com.mouchina.moumou_server_interface.award.TreasureBoxConfigService;

@Service(value="treasureBoxConfigServiceSupport")
public class TreasureBoxConfigServiceSupport implements TreasureBoxConfigService {
	
	@Resource
	private TreasureBoxConfigMapper treasureBoxConfigMapper;

	@Override
	public void addTreasureBoxConfig(TreasureBoxConfig treasureBoxConfig) {
		treasureBoxConfigMapper.insert(treasureBoxConfig);
	}

	@Override
	public void deleteTreasureBoxConfig(Long id) {
		treasureBoxConfigMapper.deleteByPrimaryKey(id);
	}

	@Override
	public void updateTreasureBoxConfig(TreasureBoxConfig treasureBoxConfig) {
		treasureBoxConfigMapper.updateByPrimaryKeySelective(treasureBoxConfig);
	}

	@Override
	public List<TreasureBoxConfig> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
