package com.mouchina.web.controller.base;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mouchina.web.base.framework.BaseController;

@Controller
@RequestMapping("/test")
public class ApiTestController extends BaseController{
	@RequestMapping("/show")
	public String show(){
		return "test";
	}
	
	@RequestMapping(value="/index",method=RequestMethod.GET)
	public String index() {
		return "index";
	}
}
