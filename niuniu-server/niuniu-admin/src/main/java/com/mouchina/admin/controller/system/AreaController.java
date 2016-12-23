package com.mouchina.admin.controller.system;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mouchina.moumou_server.entity.system.AreaData;
import com.mouchina.moumou_server_interface.system.AreaService;

/**
 * 区域controller
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/area")
public class AreaController {

	@Resource
	private AreaService areaService;
	
	private List<AreaData> areaDatas;
	
	@SuppressWarnings("rawtypes")
	private Map map = new HashMap();
	

	@RequestMapping( value = "/province", method = RequestMethod.POST )
    private String queryProvince( HttpServletRequest request, ModelMap modelMap )
    {
        areaDatas = areaService.selectProvinceListByCode();
        modelMap.put( "areaDatas", areaDatas );
        return "";
    }
	
	@SuppressWarnings("unchecked")
	@RequestMapping( value = "/city", method = RequestMethod.POST )
    private String queryCity( HttpServletRequest request, ModelMap modelMap )
    {
		String areaCode = request.getParameter("city");
		map.clear();
		map.put("areaCode", areaCode != null && !areaCode.isEmpty() ? areaCode : null);
        areaDatas = areaService.selectCityListByCode(map);
        modelMap.put( "areaDatas", areaDatas );
        return "";
    }
    
    @SuppressWarnings("unchecked")
	@RequestMapping( value = "/county", method = RequestMethod.POST )
    private String queryArea( HttpServletRequest request, ModelMap modelMap )
    {
    	String areaCode = request.getParameter("county");
    	String userId = request.getParameter("userId");
    	map.clear();
		map.put("areaCode", areaCode != null && !areaCode.isEmpty() ? areaCode : null);
		map.put("userId", userId != null && !userId.isEmpty() ? userId : null);
		areaDatas = areaService.selectAreaListByCode(map);
        modelMap.put( "areaDatas", areaDatas );
        return ""; 
    }

}
