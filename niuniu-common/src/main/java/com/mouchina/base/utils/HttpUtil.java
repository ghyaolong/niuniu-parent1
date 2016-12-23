package com.mouchina.base.utils;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpUtil
{
    private static Logger logger = LogManager.getLogger( "HttpUtil" );

    public static String executeGet( String url, Cookie[] cookies )
                             throws Exception
    {
        BasicCookieStore cookieStore = new BasicCookieStore(  );
        cookieStore.addCookies( cookies );

        CloseableHttpClient httpclient = HttpClients.custom(  ).setDefaultCookieStore( cookieStore ).build(  );

        HttpGet httpget = new HttpGet( url );

        HttpResponse response = httpclient.execute( httpget );
        HttpEntity entity = response.getEntity(  );
        String html = EntityUtils.toString( entity );
        httpclient.close(  );

        return html;
    }

    private static String paseResponse( HttpResponse response )
    {
        logger.debug( "get response from http server.." );

        HttpEntity entity = response.getEntity(  );

        logger.debug( "response status: " + response.getStatusLine(  ) );

        String charset = EntityUtils.getContentCharSet( entity );
        logger.debug( charset );

        String body = null;

        try
        {
            body = EntityUtils.toString( entity );
            logger.debug( body );
        } catch ( ParseException e )
        {
            e.printStackTrace(  );
        } catch ( IOException e )
        {
            e.printStackTrace(  );
        }

        return body;
    }

    private static HttpResponse sendRequest( DefaultHttpClient httpclient, HttpUriRequest httpost )
    {
        logger.debug( "execute post..." );

        HttpResponse response = null;

        try
        {
            response = httpclient.execute( httpost );
        } catch ( ClientProtocolException e )
        {
            e.printStackTrace(  );
        } catch ( IOException e )
        {
            e.printStackTrace(  );
        }

        return response;
    }

    private static HttpPost postForm( String url, Map<String, String> params )
    {
        HttpPost httpost = new HttpPost( url );
        List<NameValuePair> nvps = new ArrayList<NameValuePair>(  );
        logger.debug( "httpost url:" + url );

        Set<String> keySet = params.keySet(  );

        for ( String key : keySet )
        {
            nvps.add( new BasicNameValuePair( key,
                                              params.get( key ) ) );
        }

        try
        {
            logger.debug( "set utf-8 form entity to httppost" );
            httpost.setEntity( new UrlEncodedFormEntity( nvps, HTTP.UTF_8 ) );
        } catch ( UnsupportedEncodingException e )
        {
            e.printStackTrace(  );
        }

        return httpost;
    }

    private static String invoke( DefaultHttpClient httpclient, HttpUriRequest httpost )
    {
        HttpResponse response = sendRequest( httpclient, httpost );
        String body = paseResponse( response );

        return body;
    }

    private static String invokeNoResponse( DefaultHttpClient httpclient, HttpUriRequest httpost )
    {
        HttpResponse response = sendRequest( httpclient, httpost );

        return response.getStatusLine(  ) + "";
    }

    public static String executePost( String url, Map<String, String> params )
    {
        DefaultHttpClient httpclient = new DefaultHttpClient(  );
        String body = null;

        logger.debug( "create httppost:" + url );

        HttpPost post = postForm( url, params );

        body = invoke( httpclient, post );

        httpclient.getConnectionManager(  ).shutdown(  );

        return body;
    }

    public static String executePostNoRequest( String url, Map<String, String> params )
    {
        DefaultHttpClient httpclient = new DefaultHttpClient(  );
        String body = null;

        logger.debug( "create httppost:" + url );

        HttpPost post = postForm( url, params );

        body = invokeNoResponse( httpclient, post );

        httpclient.getConnectionManager(  ).shutdown(  );

        return body;
    }

    public static String executePostBinaryFile( String url, byte[] postBody, String postName, Map<String, String> params )
    {
        String restr = "";
        CloseableHttpClient httpclient = HttpClients.createDefault(  );

        try
        {
            HttpPost httppost = new HttpPost( url );

            MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create(  );
            mEntityBuilder.addBinaryBody( postName, postBody );

            Iterator iter = params.entrySet(  ).iterator(  );

            while ( iter.hasNext(  ) )
            {
                Map.Entry entry = (Map.Entry) iter.next(  );
                Object key = entry.getKey(  );

                Object val = entry.getValue(  );
                mEntityBuilder.addTextBody( key + "", val + "" );
            }

            httppost.setEntity( mEntityBuilder.build(  ) );

//            System.out.println( "executing request " + httppost.getRequestLine(  ) );

            CloseableHttpResponse response = null;

            try
            {
                response = httpclient.execute( httppost );
            } catch ( IOException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace(  );
            }

            try
            {
//                System.out.println( "----------------------------------------" );
//                System.out.println( response.getStatusLine(  ) );

                HttpEntity resEntity = response.getEntity(  );

                try
                {
                    restr = EntityUtils.toString( resEntity );
                } catch ( org.apache.http.ParseException e1 )
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace(  );
                } catch ( IOException e1 )
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace(  );
                }

                if ( resEntity != null )
                {
//                    System.out.println( "Response content length: " + resEntity.getContentLength(  ) );
                }

                try
                {
                    EntityUtils.consume( resEntity );
                } catch ( IOException e )
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace(  );
                }
            } finally
            {
                try
                {
                    response.close(  );
                } catch ( IOException e )
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace(  );
                }
            }
        } finally
        {
            try
            {
                httpclient.close(  );
            } catch ( IOException e )
            {
                // TODO Auto-generated catch block
                e.printStackTrace(  );
            }
        }

        return restr;
    }

    public static void main( String[] args )
                     throws Exception
    {
        Map<String, String> params = new HashMap<String, String>(  );

        /*
         * params.put("query", "银行"); params.put("region", "济南");
         * params.put("output", "json"); params.put("ak",
         * "E4805d16520de693a3fe707cdc962045");
         */

//		String xml = executeGet("http://api.map.baidu.com/place/v2/search?scope=1&query=德外大街新风街2号天成科技大厦首层&page_size=1&page_num=0&region=北京&output=json&ak=246b954b9286db88493a65fac87c2ae5");
//		System.out.println(xml);
    }
}
