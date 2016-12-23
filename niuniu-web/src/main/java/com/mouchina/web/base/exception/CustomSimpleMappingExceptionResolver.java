package com.mouchina.web.base.exception;

import com.mouchina.base.utils.JsonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class CustomSimpleMappingExceptionResolver
    extends SimpleMappingExceptionResolver
{
    private static Logger logger = LogManager.getLogger( CustomSimpleMappingExceptionResolver.class.getName(  ) );

    @Override
    protected ModelAndView doResolveException( HttpServletRequest request, HttpServletResponse response,
                                               Object handler, Exception ex )
    {
//        // Expose ModelAndView for chosen error view.
//        String viewName = determineViewName( ex, request );
////		SysState sysState = getSysState(ex);
        logger.error( ex );

      ModelAndView modelAndView=new ModelAndView();
      modelAndView.addObject("result", "0");
      modelAndView.addObject("errorCode", "100003");
      return modelAndView;
    }

//	private SysState getSysState(Exception ex) {
//		int stateCode = 10000000;
//		if (ex instanceof AppException)
//			stateCode = ((AppException) ex).getStateCode();
//		SysState sysState = sysStateService.selectByPrimaryKey(stateCode);
//		return sysState;
//	}
}
