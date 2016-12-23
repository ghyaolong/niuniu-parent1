package com.mouchina.base.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class EmailUtils
{
    private static Logger logger = LogManager.getLogger( "EmailUtils" );

    public static Integer postEmail( String emailUrl, String title, String content, Integer messageType, String tos,
                                     String uid )
                             throws IOException
    {
        Integer result = 0;
        String response = null;

        try
        {
            response = HttpUtil.executeGet( emailUrl + "?messageType=" + messageType + "&tos=" + tos + "&content=" +
                                            content + "&uid=" + uid + "&title=" + title, null );
        } catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace(  );
        }

        logger.info( response );

        Map json = JsonUtils.jsonToObject( response, HashMap.class );

        if ( ( json != null ) && json.containsKey( "result" ) )
        {
            result = (Integer) json.get( "result" );
        }

        // ImageUtils.GenerateImage(base64Str, "/Users/larry/test.png");
        return result;
    }
}
