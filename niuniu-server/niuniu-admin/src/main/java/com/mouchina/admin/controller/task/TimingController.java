package com.mouchina.admin.controller.task;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mouchina.admin.service.api.advert.AdvertAdminService;
import com.mouchina.admin.service.api.income.AgentIncomeAdminService;
import com.mouchina.admin.service.api.income.UserIncomeAdminService;
import com.mouchina.base.utils.DateUtils;
import com.mouchina.moumou_server_interface.advert.AdvertService;
import com.mouchina.moumou_server_interface.view.AdvertResult;

/**
 * Created by douzy on 15/11/21.
 */
@Controller
@RequestMapping("/task/timing")
public class TimingController {

	
	static Logger logger = LogManager.getLogger(  );
	@Resource
	AdvertAdminService advertAdminService;
	
	@Resource
	UserIncomeAdminService  userIncomeAdminService;
	@Resource
	AgentIncomeAdminService agentIncomeAdminService;
	@RequestMapping(value = "/advert/expired", method = RequestMethod.GET)
	public String advertExpire(HttpServletRequest _req, ModelMap modelMap) {

		Date start =new Date();
		logger.info("============TimingController advertExpire date="+start+"==========start");
		
		
		AdvertResult<Integer> advertResult= advertAdminService.updateTimingAdvertExpire();
		
		Date end =new Date();
		modelMap.put("count", advertResult.getContent());
		logger.info("============TimingController advertExpire date="+end+"==count close advert="+advertResult.getContent()+"==cost second="+(end.getTime()-start.getTime())/1000+"======start");
		return "";
	}

	
	
	@RequestMapping(value = "/user/income/sum/cal", method = RequestMethod.GET)
	public String userIncomeSumCal(HttpServletRequest _req, ModelMap modelMap,String day) {

		Date start =new Date();
		logger.info("============TimingController userIncomeSumCal date="+start+"==========start");
		
		Date targetDay=new Date(start.getTime()-3600*1000*24);
		if(_req.getParameter("day")!=null){
			
			try {
				targetDay=DateUtils.parseStartDate(day);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		int result= userIncomeAdminService.insertCalUserIncomeByDay(targetDay);
		
		Date end =new Date();
		modelMap.put("===========TimingController userIncomeSumCal  count", result);
		logger.info("============TimingController userIncomeSumCal date="+end+"==count close count="+result+"==cost second="+(end.getTime()-start.getTime())/1000+"======start");
		return "";
	}
	@RequestMapping(value = "/agent/income/detail/sum/cal", method = RequestMethod.GET)
	public String agentIncomeDetailSumCal(HttpServletRequest _req, ModelMap modelMap,String day) {

		Date start =new Date();
		logger.info("============TimingController agentIncomeDetailSumCal date="+start+"==========start");
		
		Date targetDay=new Date(start.getTime()-3600*1000*24);
		if(_req.getParameter("day")!=null){
			
			try {
				targetDay=DateUtils.parseStartDate(day);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		int result= agentIncomeAdminService.insertCalAgentIncomeDetailByDay(targetDay);
		
		Date end =new Date();
		modelMap.put("===========TimingController agentIncomeDetailSumCal  count", result);
		logger.info("============TimingController agentIncomeDetailSumCal date="+end+"==count close count="+result+"==cost second="+(end.getTime()-start.getTime())/1000+"======start");
		return "";
	}
	
	@RequestMapping(value = "/agent/income/sum/cal", method = RequestMethod.GET)
	public String agentIncomeSumCal(HttpServletRequest _req, ModelMap modelMap,String day) {

		Date start =new Date();
		logger.info("============TimingController agentIncomeSumCal date="+start+"==========start");
		
		Date targetDay=new Date(start.getTime()-3600*1000*24);
		if(_req.getParameter("day")!=null){
			
			try {
				targetDay=DateUtils.parseStartDate(day);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		int result= agentIncomeAdminService.insertCalAgentIncomeByMonth(targetDay);
		
		Date end =new Date();
		modelMap.put("===========TimingController agentIncomeSumCal  count", result);
		logger.info("============TimingController agentIncomeSumCal date="+end+"==count close count="+result+"==cost second="+(end.getTime()-start.getTime())/1000+"======start");
		return "";
	}
	public static void main(String[] args){
		
		Date targetDay=new Date(new Date().getTime()-3600*1000*24);
	
		
		System.out.println(""+targetDay);
	}
	
	

}
