package com.mouchina.base.utils.Image;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Random;

/**
 * 模板方法模式应用：图片生成器
 */
public abstract class AbstractImageCreator
{
    private static final String FORMAT_NAME = "png";
    private static Random rnd = new Random( new Date(  ).getTime(  ) );
    private Drawer drawer;

    // 图片宽度
    private int width = 600;

    // 图片高度
    private int height = 300;

    // 外框颜色
    private Color rectColor;

    // 背景色
    private Color bgColor;

    // 干扰线数目
    private int lineNum = 0;

    // 图片格式
    private String formatName = "PNG";

    // 字体颜色
    private Color fontColor = new Color( 0, 0, 0 );

    // 字体名称
    private String fontName = "宋体";

    // 字体大小
    private int fontSize = 15;

    // 文字旋转的弧度数
    private double radian = 0;
    private double rotateX = 0;
    private double rotateY = 0;

    // 缩放
    private double scale = 1;
    private int top = 0;
    private int left = 300;
    private int interval = 10;
    private int textTop = 0;
    private String qRCodeUrl;

    // ##### 此处省略getter、setter方法 #####
    public String getqRCodeUrl(  )
    {
        return qRCodeUrl;
    }

    public void setqRCodeUrl( String qRCodeUrl )
    {
        this.qRCodeUrl = qRCodeUrl;
    }

    public int getTextTop(  )
    {
        return textTop;
    }

    public void setTextTop( int textTop )
    {
        this.textTop = textTop;
    }

    public int getInterval(  )
    {
        return interval;
    }

    public void setInterval( int interval )
    {
        this.interval = interval;
    }

    public AbstractImageCreator( Drawer drawer )
    {
        this.drawer = drawer;
    }

    /**
     * 画干扰线
     */
    private void drawRandomLine( Graphics graph )
    {
        for ( int i = 0; i < lineNum; i++ )
        {
            // 线条的颜色
            graph.setColor( getRandomColor( 100, 155 ) );

            // 线条两端坐标值
            int x1 = rnd.nextInt( width );
            int y1 = rnd.nextInt( height );

            int x2 = rnd.nextInt( width );
            int y2 = rnd.nextInt( height );

            // 画线条
            graph.drawLine( x1, y1, x2, y2 );
        }
    }

    /**
     * 随机获取颜色对象
     */
    private Color getRandomColor( int base, int range )
    {
        if ( ( base + range ) > 255 )
        {
            range = 255 - base;
        }

        int red = base + rnd.nextInt( range );
        int green = base + rnd.nextInt( range );
        int blue = base + rnd.nextInt( range );

        return new Color( red, green, blue );
    }

    public void generateImage( String text )
                       throws IOException
    {
        BufferedImage image = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );

        if ( rectColor == null )
        {
            rectColor = new Color( 0, 0, 0 );
        }

        if ( bgColor == null )
        {
            bgColor = new Color( 255, 255, 255 );
        }

        // 获取画布
        Graphics2D g = (Graphics2D) image.getGraphics(  );

        // 画长方形
        g.setColor( bgColor );
        g.fillRect( 0, 0, width, height );

        // 外框
        // g.setColor(rectColor);
        // g.drawRect(0, 0, width-1, height-1);

        // 画干扰线
        drawRandomLine( g );

        // 画字符串
        drawer.draw( this, g, text, textTop, interval, left, top );

        if ( qRCodeUrl != null )
        {
            insertQRCode( getqRCodeUrl(  ),
                          image,
                          300,
                          300 );
        }

        // 执行
        g.dispose(  );

        // 输出图片结果
        saveImage( image );
    }

    public byte[] generateImageNet( String text )
                            throws IOException
    {
        BufferedImage image = new BufferedImage( width, height, BufferedImage.TYPE_INT_RGB );

        if ( rectColor == null )
        {
            rectColor = new Color( 0, 0, 0 );
        }

        if ( bgColor == null )
        {
            bgColor = new Color( 255, 255, 255 );
        }

        // 获取画布
        Graphics2D g = (Graphics2D) image.getGraphics(  );

        // 画长方形
        g.setColor( bgColor );
        g.fillRect( 0, 0, width, height );

        // 外框
        // g.setColor(rectColor);
        // g.drawRect(0, 0, width-1, height-1);

        // 画干扰线
        drawRandomLine( g );

        // 画字符串
        drawer.draw( this, g, text, textTop, interval, left, top );

        if ( qRCodeUrl != null )
        {
            insertQRCode( getqRCodeUrl(  ),
                          image,
                          300,
                          300 );
        }

        // 执行
        g.dispose(  );

        ByteArrayOutputStream baos = new ByteArrayOutputStream(  );
        ImageOutputStream ios = ImageIO.createImageOutputStream( baos );
        ImageIO.write( image, FORMAT_NAME, ios );

        return baos.toByteArray(  );
    }

    private void insertQRCode( String imageUrl, BufferedImage source, int width, int height )
    {
        Image src = null;

        try
        {
            src = ImageIO.read( new URL( imageUrl ) );
        } catch ( MalformedURLException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace(  );
        } catch ( IOException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace(  );
        }

        // 插入LOGO
        Graphics2D graph = source.createGraphics(  );
        int x = 0;
        int y = 0;
        graph.drawImage( src, x, y, width, height, null );

        Shape shape = new RoundRectangle2D.Float( x, y, width, height, 0, 0 );
        graph.setStroke( new BasicStroke( 3f ) );
        graph.draw( shape );
        graph.dispose(  );
    }

    protected abstract void saveImage( BufferedImage image )
                               throws IOException;

    public Drawer getDrawer(  )
    {
        return drawer;
    }

    public void setDrawer( Drawer drawer )
    {
        this.drawer = drawer;
    }

    public int getWidth(  )
    {
        return width;
    }

    public void setWidth( int width )
    {
        this.width = width;
    }

    public int getHeight(  )
    {
        return height;
    }

    public void setHeight( int height )
    {
        this.height = height;
    }

    public Color getRectColor(  )
    {
        return rectColor;
    }

    public void setRectColor( Color rectColor )
    {
        this.rectColor = rectColor;
    }

    public Color getBgColor(  )
    {
        return bgColor;
    }

    public void setBgColor( Color bgColor )
    {
        this.bgColor = bgColor;
    }

    public int getLineNum(  )
    {
        return lineNum;
    }

    public void setLineNum( int lineNum )
    {
        this.lineNum = lineNum;
    }

    public String getFormatName(  )
    {
        return formatName;
    }

    public void setFormatName( String formatName )
    {
        this.formatName = formatName;
    }

    public Color getFontColor(  )
    {
        return fontColor;
    }

    public void setFontColor( Color fontColor )
    {
        this.fontColor = fontColor;
    }

    public String getFontName(  )
    {
        return fontName;
    }

    public void setFontName( String fontName )
    {
        this.fontName = fontName;
    }

    public int getFontSize(  )
    {
        return fontSize;
    }

    public void setFontSize( int fontSize )
    {
        this.fontSize = fontSize;
    }

    public double getRadian(  )
    {
        return radian;
    }

    public void setRadian( double radian )
    {
        this.radian = radian;
    }

    public double getRotateX(  )
    {
        return rotateX;
    }

    public void setRotateX( double rotateX )
    {
        this.rotateX = rotateX;
    }

    public double getRotateY(  )
    {
        return rotateY;
    }

    public void setRotateY( double rotateY )
    {
        this.rotateY = rotateY;
    }

    public double getScale(  )
    {
        return scale;
    }

    public void setScale( double scale )
    {
        this.scale = scale;
    }
}
