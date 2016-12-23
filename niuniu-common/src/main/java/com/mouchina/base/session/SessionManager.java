package com.mouchina.base.session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpSession;

/**
 * Session管理器
 *
 * @author cuixl
 *
 */
public class SessionManager
{
    private static Logger logger = LogManager.getLogger( "SessionManager" );
    private static Set<String> sessionSet = new HashSet<String>(  );

    public static Object getSessionAttribute( HttpSession session, String key )
    {
        logger.info( "get session attribute " + key );

        return session.getAttribute( key );
    }

    public static void setSessionAttribute( HttpSession session, String key, Object object )
    {
        logger.info( "set session attribute " + key + "=" + object );
        session.setAttribute( key, object );
        sessionSet.add( key );
    }

    public static void removeSessionAttrbute( HttpSession session, String key )
    {
        logger.info( "remove session attribute " + key );
        session.removeAttribute( key );
        sessionSet.remove( key );
    }

    public static void clearSession( HttpSession session )
    {
        logger.info( "clear session attribute " );

        for ( String key : sessionSet )
        {
            session.removeAttribute( key );
        }

        sessionSet.clear(  );
    }

    public static void initSession( HttpSession session )
    {
    }
}
