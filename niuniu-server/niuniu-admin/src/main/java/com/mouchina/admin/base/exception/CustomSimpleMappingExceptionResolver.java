package com.mouchina.admin.base.exception;

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
        // Expose ModelAndView for chosen error view.
        String viewName = determineViewName( ex, request );
//		SysState sysState = getSysState(ex);
        logger.error( ex );

        if ( viewName != null )
        { // JSON格式返回

            if ( request.getRequestURI(  ).endsWith( ".json" ) )
            {
                try
                {
                    PrintWriter writer = response.getWriter(  );
                    response.setCharacterEncoding( "UTF-8" );
                    response.setContentType( "text/plain; charset=utf-8" );

                    Map map = new HashMap(  );
//					map.put("sysState", sysState);
                    writer.write( JsonUtils.javaToString( map ) );
                    writer.flush(  );
                } catch ( IOException e )
                {
                    e.printStackTrace(  );
                }

                return null;
            } else
            { // JSP格式返回
              // 如果不是异步请求
              // Apply HTTP status code for error views, if specified.
              // Only apply it if we're processing a top-level request.

                Integer statusCode = determineStatusCode( request, viewName );

                if ( statusCode != null )
                {
                    applyStatusCodeIfPossible( request, response, statusCode );
                }

                ModelAndView modelAndView = new ModelAndView( viewName );

//				modelAndView.getModelMap().put("sysState", sysState);
                return modelAndView;
            }
        } else
        {
            return null;
        }
    }

//	private SysState getSysState(Exception ex) {
//		int stateCode = 10000000;
//		if (ex instanceof AppException)
//			stateCode = ((AppException) ex).getStateCode();
//		SysState sysState = sysStateService.selectByPrimaryKey(stateCode);
//		return sysState;
//	}
}
