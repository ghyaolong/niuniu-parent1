package com.mouchina.base.utils;

import com.mouchina.base.resource.type.Image;

import org.apache.http.cookie.Cookie;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.web.multipart.MultipartFile;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class FileUtil
{
    private static Logger logger = LogManager.getLogger( "FileUtil" );

    public static Image uploadImage( MultipartFile item, String suffix, String baseUrl )
                             throws IOException
    {
        String originalFilename = item.getOriginalFilename(  );
        Pattern reg = Pattern.compile( "[\\.](jpg|png|jpeg|gif)$" );
        Matcher matcher = reg.matcher( originalFilename );
        Image image = null;

        if ( ! matcher.find(  ) )
        {
            return null;
        } else
        {
            image =
                uploadBytesImage( item.getBytes(  ),
                                  suffix,
                                  baseUrl );
        }

        return image;
    }

    private static BufferedImage ByteToBufferedImage( byte[] bytes )
    {
        ByteArrayInputStream in = new ByteArrayInputStream( bytes ); // 将b作为输入流；
        BufferedImage image = null;

        try
        {
            image = ImageIO.read( in );
        } catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace(  );
        } // 将in作为输入流，读取图片存入image中，而这里in可以为ByteArrayInputStream();

        return image;
    }

    protected static BufferedImage get24BitImage( BufferedImage image, Color bgColor )
    {
        int w = image.getWidth(  );
        int h = image.getHeight(  );
        BufferedImage __image = new BufferedImage( w, h, BufferedImage.TYPE_INT_RGB );
        Graphics2D __graphic = __image.createGraphics(  );
        __graphic.setColor( bgColor );
        __graphic.fillRect( 0, 0, w, h );
        __graphic.drawRenderedImage( image, null );
        __graphic.dispose(  );

        return __image;
    }

    private static byte[] comproessByteImage( byte[] bytes, float quality, String suffix )
    {
        // return bytes;
        try
        {
            return bufferedImageTobytes( ByteToBufferedImage( bytes ),
                                         quality,
                                         suffix );
        } catch ( Exception ex )
        {
            return bytes;
        }
    }

    /**
     *
     * 自己设置压缩质量来把图片压缩成byte[]
     *
     * @param image
     *            压缩源图片
     * @param quality
     *            压缩质量，在0-1之间，
     * @return 返回的字节数组
     */
    private static byte[] bufferedImageTobytes( BufferedImage image, float quality, String suffix )
    {
        // 如果图片空，返回空
        if ( image == null )
        {
            return null;
        }

        // 得到指定Format图片的writer
        Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName( suffix ); // 得到迭代器
                                                                                    // 找到一个jpeg writer

        ImageWriter writer = null;

        if ( iter.hasNext(  ) )
        {
            writer = (ImageWriter) iter.next(  );
        }

        // 得到指定writer的输出参数设置(ImageWriteParam )
        ImageWriteParam iwp = writer.getDefaultWriteParam(  );

        iwp.setCompressionMode( ImageWriteParam.MODE_EXPLICIT ); // 设置可否压缩

        iwp.setCompressionQuality( quality ); // 设置压缩质量参数

        // iwp.setProgressiveMode(ImageWriteParam.MODE_DISABLED);

        // 开始打包图片，写入byte[]
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(  ); // 取得内存输出流
        IIOImage iIamge = new IIOImage( image, null, null );

        try
        {
            // 此处因为ImageWriter中用来接收write信息的output要求必须是ImageOutput
            // 通过ImageIo中的静态方法，得到byteArrayOutputStream的ImageOutput
            writer.setOutput( ImageIO.createImageOutputStream( byteArrayOutputStream ) );
            writer.write( null, iIamge, iwp );
        } catch ( IOException e )
        {
            System.out.println( "write errro" );
            e.printStackTrace(  );
        }

        return byteArrayOutputStream.toByteArray(  );
    }

    public static Image uploadBytesImage( byte[] bytes, String suffix, String baseUrl )
                                  throws IOException
    {
        // String base64Str = ImageUtils.GetBetysImageStr(bytes);
        // Map<String, String> params = new HashMap<String, String>();
        // params.put("imageStr", base64Str);
        // params.put("suffix", suffix);
        // String json = HttpUtil.executePost(baseUrl + "/image/upload",
        // params);
        // logger.info(json);
        Image image = uploadBytesAndPostBinImage( bytes, suffix, baseUrl );

        // ImageUtils.GenerateImage(base64Str, "/Users/larry/test.png");
        return image;
    }

    public static Image uploadBytesAndPostBinImage( byte[] bytes, String suffix, String baseUrl )
                                            throws IOException
    {
        byte[] comperssbytes = bytes; // comproessByteImage(bytes, 0.6f, suffix);
        Map<String, String> params = new HashMap<String, String>(  );
        params.put( "suffix", suffix );

        
        File file=new File(baseUrl + "/image/upload");
        
        if(!file.exists()){
        	file.mkdirs();
        }
        
        String json = HttpUtil.executePostBinaryFile( baseUrl + "/image/upload", comperssbytes, "myfile", params );
//        logger.info( json );

        Image image = JsonUtils.jsonToObject( json, Image.class );

        // ImageUtils.GenerateImage(base64Str, "/Users/larry/test.png");
        return image;
    }

    public static Map fetchUrl( String url, Cookie[] cookies )
                        throws IOException
    {
        String json = null;

        try
        {
            json = HttpUtil.executeGet( url, cookies );
        } catch ( Exception e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace(  );
        }

        Map result = null;

        if ( json != null )
        {
            result = JsonUtils.jsonToObject( json, Map.class );
        }

        return result;
    }

    public static String uploadImage( MultipartFile item, float width, float height )
    {
        Map<String, String> params = new HashMap<String, String>(  );
        params.put( "imageStr", "base64Str" );

        String xml = HttpUtil.executePost( "http://baozoumanhua.com/session.js?1379939844006", params );
        logger.info( xml );

        return "";
    }

    public String uploadScaleImage( MultipartFile item, float width, float height )
    {
        Map<String, String> params = new HashMap<String, String>(  );
        params.put( "imageStr", "base64Str" );
        params.put( "scaleWidth",
                    String.valueOf( 0 ) );

        String xml = HttpUtil.executePost( "http://baozoumanhua.com/session.js?1379939844006", params );
        logger.info( xml );

        return "";
    }

    public static boolean deletImage( MultipartFile item )
    {
        return false;
    }

    private static InputStream getImageInputStream( BufferedImage image )
    {
        InputStream is = null;

        image.flush(  );

        ByteArrayOutputStream bs = new ByteArrayOutputStream(  );

        ImageOutputStream imOut;

        try
        {
            imOut = ImageIO.createImageOutputStream( bs );

            ImageIO.write( image, "png", imOut );

            is = new ByteArrayInputStream( bs.toByteArray(  ) );
        } catch ( Exception e )
        {
            e.printStackTrace(  );
        }

        return is;
    }

    /**
     * 合并图片 用正方形图片垒起来成为一张柱形图片
     *
     * @param childs
     *            字图片路径列表
     * @param bigWidth
     *            生成大图宽度，（自动等比缩放小图至bigWidth，然后整合）
     * @param fileName
     *            保存文件路径
     * @param flag
     *            小图文件类型（0：本地图片路径，1：网络路径）
     * @return 0：失败，1：成功
     */
    private static int drawImage( ArrayList<String> childs, int bigWidth, String fileName, int flag )
    {
        try
        {
            int num = childs.size(  );

            if ( num == 0 )
            {
                return 0;
            }

            int bigHeight = bigWidth * num;
            BufferedImage bufferedImage = new BufferedImage( bigWidth, bigHeight, BufferedImage.TYPE_INT_RGB );
            Graphics g = bufferedImage.getGraphics(  );
            BufferedImage image = null;

            for ( int i = 0; i < num; i++ )
            {
                String path = childs.get( i );

                switch ( flag )
                {
                    case 0: // 0：本地图片路径

                        java.awt.Image src1 = ImageIO.read( new File( path ) );
                        image = reduceImg( src1, bigWidth, bigWidth, 1 );

                        break;

                    case 1: // 1：网络路径

                        java.awt.Image src2 = ImageIO.read( new URL( path ) );
                        image = reduceImg( src2, bigWidth, bigWidth, 1 );

                        break;
                }

                g.drawImage( image, 0, i * bigWidth, null ); // 由于是正方形，所以*bigWidth和height一样
            }

            InputStream in = getImageInputStream( bufferedImage );
            ImageDBAdapter imageDBAdapter = new ImageDBAdapter( MongoManager.getDB(  ),
                                                                ImageDBAdapter.FSN_PANORAMA );
            imageDBAdapter.saveImageFile( in, fileName );

            return 1;
        } catch ( Exception e )
        {
            e.printStackTrace(  );
        }

        return 0;
    }

    /**
     *
     * @param imgsrc
     *            :原图片文件
     * @param widthdist
     *            :生成图片的宽度
     * @param heightdist
     *            :生成图片的高度
     * @param flag
     *            :0:清晰，1：粗糙
     */
    private static BufferedImage reduceImg( java.awt.Image src, int widthdist, int heightdist, int flag )
    {
        try
        {
            int imageWidth = src.getWidth( null );
            int imageHeight = src.getHeight( null );
            BufferedImage tag = new BufferedImage( (int) widthdist, (int) heightdist, BufferedImage.TYPE_INT_RGB );

            /*
             * Image.SCALE_FAST 的缩略算法 生成缩略图片的平滑度的 优先级比速度低 生成的图片质量比较差 但速度快
             */
            tag.getGraphics(  ).drawImage( src.getScaledInstance( widthdist, heightdist,
                                                                  ( flag == 0 ) ? java.awt.Image.SCALE_SMOOTH
                                                                                : java.awt.Image.SCALE_FAST ),
                                           0,
                                           0,
                                           null );

            return tag;
        } catch ( Exception e )
        {
            e.printStackTrace(  );
        }

        return null;
    }
}
