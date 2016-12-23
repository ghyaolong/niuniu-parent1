package com.mouchina.base.utils;

import java.io.UnsupportedEncodingException;
import java.util.Set;

/**
 * 字符串处理工具
 *
 *
 */
public class StringUtil
{
    /**
     * 判断字符串空或者是空字符穿
     *
     * @param str
     * @return
     */
    public static boolean isEmpty( String str )
    {
        return ( str == null ) || str.trim(  ).equals( "" ) || str.trim(  ).equals( "null" );
    }

    /**
     * Object 转换成字符串，null时返回“”
     * @param obj
     * @return
     */
    public static String toString( Object obj )
    {
        return ( obj == null ) ? "" : String.valueOf( obj );
    }

    public static String toString( Object obj, String def )
    {
        return ( obj == null ) ? def : String.valueOf( obj );
    }

    /**
     * Object 转换成字符串，null时返回“”
     * @param obj
     * @return
     */
    public static Integer toInteger( Object obj )
    {
        return Integer.valueOf( toString( obj, "0" ) );
    }

    /**
     * Object 转换成字符串，null时返回“”
     * @param obj
     * @return
     */
    public static Double toDouble( Object obj )
    {
        return Double.valueOf( toString( obj, "0" ) );
    }

    public static String toDecodeString( Object obj )
                                 throws UnsupportedEncodingException
    {
        String str = toString( obj );

        return new String( str.getBytes( "iso8859-1" ),
                           "UTF-8" );
    }

    /**
     * 生成实体编号 规则 ,默认每组编码为 6 ,各组编码长度必须一致
     * @example A00001 -->A00002
     * @example A000001A00009 -->A000001A00010
     * @example A000001A00099 -->A000001A00100
     * @example A00001A00001A00013 -->A00001A00001A00014
     * @param str
     * @return
     */
    public static String parseObjectNum( String str )
    {
        return parseObjectNum( str, 6 );
    }

    /**
     * 生成实体编号 规则 ,各组编码长度必须一致
     * @example A00001 -->A00002
     * @example A000001A00009 -->A000001A00010
     * @example A000001A00099 -->A000001A00100
     * @example A00001A00001A00013 -->A00001A00001A00014
     * @param str 当前编码
     * @param num 每组编码长度
     * @return retStr 下一编码
     */
    public static String parseObjectNum( String str, int num )
    {
        String retStr = "";
        int starIndex = str.length(  ) - num;
        String headStr = str.substring( starIndex, starIndex + 1 );
        String numStr = str.substring( starIndex + 1 );
        char[] nsArr = numStr.toCharArray(  );
        int indexFlag = 0;

        for ( int i = 0; i < nsArr.length; i++ )
        {
            if ( toInteger( nsArr[i] ) > 0 )
            {
                indexFlag = i;

                break;
            }
        }

        String curNum = numStr.substring( indexFlag );
        String nextNum = toString( toInteger( curNum ) + 1 );

        for ( int i = nsArr.length - nextNum.length(  ); i > 0; i-- )
        {
            nextNum = "0" + nextNum;
        }

        retStr = str.substring( 0, starIndex ) + headStr + nextNum;

        return retStr;
    }

    public static void main( String[] args )
    {
        System.out.println( parseObjectNum( "A00013", 6 ) );
        System.out.println( parseObjectNum( "A00001A00019", 6 ) );
        System.out.println( parseObjectNum( "A00001A00099", 6 ) );
        System.out.println( parseObjectNum( "A00001A00001A00013", 6 ) );
    }

    /**清除所有的html标签**/
    public static String delHtmlTag( String content )
    {
        if ( ( content != null ) && ! content.trim(  ).equals( "" ) )
        {
            return content.replaceAll( "\\&[a-zA-Z]{1,10};", "" ).replaceAll( "<[a-zA-Z]+[1-9]?[^><]*>", "" )
                          .replaceAll( "</[a-zA-Z]+[1-9]?>", "" );
        } else
        {
            return content;
        }
    }

    /**
     * Object 转换成字符串，null时返回“”
     * @param obj
     * @return
     */
    public static Integer toInteger( Object obj, String def )
    {
        return Integer.valueOf( toString( obj, def ) );
    }

    public static boolean isNotEmpty( String str )
    {
        return ( str != null ) && ( str.trim(  ).length(  ) > 0 );
    }
}
