package com.mouchina.base.utils;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;

public class HttpConnectionManager
{
    private static HttpParams httpParams;
    private static PoolingClientConnectionManager cm;

    /**
     * 最大连接数
     */
    public final static int MAX_TOTAL_CONNECTIONS = 200;

    /**
     * 获取连接的最大等待时间
     */
    public final static int WAIT_TIMEOUT = 60000;

    /**
     * 每个路由最大连接数
     */
    public final static int MAX_ROUTE_CONNECTIONS = 300;

    /**
     * 连接超时时间
     */
    public final static int CONNECT_TIMEOUT = 10000;

    /**
     * 读取超时时间
     */
    public final static int READ_TIMEOUT = 10000;

    static
    {
        SchemeRegistry schemeRegistry = new SchemeRegistry(  );
        schemeRegistry.register( new Scheme( "http",
                                             80,
                                             PlainSocketFactory.getSocketFactory(  ) ) );
        schemeRegistry.register( new Scheme( "https",
                                             443,
                                             SSLSocketFactory.getSocketFactory(  ) ) );

        cm = new PoolingClientConnectionManager( schemeRegistry );
        cm.setMaxTotal( 10000 ); // 创建socket的上线是200
        cm.setDefaultMaxPerRoute( 2000 ); // 对每个指定连接的服务器（指定的ip）可以创建并发20 socket进行访问

        HttpParams params = new BasicHttpParams(  );
        params.setParameter( CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECT_TIMEOUT );
        params.setParameter( CoreConnectionPNames.SO_TIMEOUT, READ_TIMEOUT );
    }

    public static HttpClient getHttpClient(  )
    {
        return new DefaultHttpClient( cm, httpParams );
    }
}
