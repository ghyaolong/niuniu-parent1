package com.mouchina.base.utils;

import com.mouchina.base.session.SessionData;

import javax.servlet.http.HttpServletRequest;

public class SessionUtil
{
    private SessionUtil(  )
    {
    }
    ;
    public static String getBusinessId( HttpServletRequest request )
    {
        String businessId = (String) request.getSession(  ).getAttribute( SessionData.BUSINESSID );

        //businessId="B1207323560";
        return businessId;
    }

    public static String getIndustryid( HttpServletRequest request )
    {
        String businessId = (String) request.getSession(  ).getAttribute( SessionData.INDUSTRYID );

        return businessId;
    }
}
