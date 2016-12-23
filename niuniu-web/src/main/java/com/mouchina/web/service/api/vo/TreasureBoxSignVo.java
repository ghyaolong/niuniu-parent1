package com.mouchina.web.service.api.vo;

import com.mouchina.moumou_server.entity.award.SignHistory;
import com.mouchina.moumou_server.entity.award.TreasureAwardHistory;

public class TreasureBoxSignVo {

	private String result;
	private String errorMsg;
	private TreasureAwardHistory treasureAwardHistory;
	private SignHistory signHistory;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public TreasureAwardHistory getTreasureAwardHistory() {
		return treasureAwardHistory;
	}

	public void setTreasureAwardHistory(TreasureAwardHistory treasureAwardHistory) {
		this.treasureAwardHistory = treasureAwardHistory;
	}

	public SignHistory getSignHistory() {
		return signHistory;
	}

	public void setSignHistory(SignHistory signHistory) {
		this.signHistory = signHistory;
	}

}
