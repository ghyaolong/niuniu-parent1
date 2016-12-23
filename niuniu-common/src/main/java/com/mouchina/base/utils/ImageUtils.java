package com.mouchina.base.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImageUtils
{
    private static Logger logger = LogManager.getLogger( "ImageUtils" );

    public static boolean GenerateImage( String imgStr, String imgFilePath )
    { // 对字符串进行Base64解码并生成图片

        if ( imgStr == null ) // 图像数据为空
        {
            return false;
        }

        try
        {
            // Base64解码
            byte[] bytes = decodeBase64ToByteArr( imgStr );

            // 生成jpeg图片
            OutputStream out = new FileOutputStream( imgFilePath );
            out.write( bytes );
            out.flush(  );
            out.close(  );

            return true;
        } catch ( Exception e )
        {
            return false;
        }
    }

    public static byte[] decodeBase64ToByteArr( String imgStr )
    { // 对字节数组字符串进行Base64解码

        if ( imgStr == null ) // 图像数据为空
        {
            return null;
        }

        BASE64Decoder decoder = new BASE64Decoder(  );

        // Base64解码
        byte[] bytes = null;

        try
        {
            bytes = decoder.decodeBuffer( imgStr );

            for ( int i = 0; i < bytes.length; ++i )
            {
                if ( bytes[i] < 0 )
                { // 调整异常数据
                    bytes[i] += 256;
                }
            }
        } catch ( IOException e )
        {
            e.printStackTrace(  );
        }

        return bytes;
    }

    public static String GetImageStr( ByteArrayOutputStream byteArrayOutputStream )
    { // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
      // 对字节数组Base64编码

        BASE64Encoder encoder = new BASE64Encoder(  );

        return encoder.encode( byteArrayOutputStream.toByteArray(  ) ); // 返回Base64编码过的字节数组字符串
    }

    public static String GetBetysImageStr( byte[] bytes )
    { //将比特数组进行Base64编码处理
      // 对字节数组Base64编码

        BASE64Encoder encoder = new BASE64Encoder(  );

        return encoder.encode( bytes ); // 返回Base64编码过的字节数组字符串
    }
}
