package com.mouchina.admin.controller;

import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping( "/index" )
public class AdminController
{
    @RequestMapping( method = RequestMethod.GET )
    private String index( ModelMap modelMap )
    {
        return "admin/index";
    }
}
