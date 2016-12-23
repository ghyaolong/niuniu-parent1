package com.mouchina.base.utils.Image;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

public class TextQRCodeHelp
{
    private static final String FORMAT_NAME = "png";

    public static void createTEXTImage( List<String> texts, String qRCodeUrl )
    {
        try
        {
            StringBuffer sb = new StringBuffer(  );

            for ( String text : texts )
            {
                sb.append( text + "\n" );
            }

            FileImageCreator creator = new FileImageCreator( new SimpleDrawer(  ),
                                                             "/Users/larry/Desktop/img.png" );
            creator.setWidth( 600 ); // 图片宽度
            creator.setHeight( 300 ); // 图片高度
            creator.setLineNum( 0 ); // 干扰线条数
            creator.setFontSize( 20 ); // 字体大小
            creator.setFontName( "楷体" );
            creator.setInterval( 10 );

            Color fontColor = new Color( 34, 34, 34 );
            creator.setFontColor( fontColor );
            creator.setqRCodeUrl( qRCodeUrl );

            int textSize = texts.size(  );
            int textHeight = textSize * 30;
            int textTop = ( ( 300 - textHeight ) / 2 ) - 15;
            creator.setTextTop( textTop );

            // 文字旋转
            // creator.setRadian(30.0); //旋转弧度
            // creator.setRotateX(creator.getWidth()/5);
            // creator.setRotateY(creator.getHeight()*5/10);
            creator.generateImage( sb.toString(  ) );

            System.out.println( "ok" );
        } catch ( IOException ex )
        {
            ex.printStackTrace(  );
        }
    }

    public static byte[] createTEXTImageUrl( List<String> texts, String qRCodeUrl )
    {
        byte[] imageByte = null;
        int width = 300;

        try
        {
            StringBuffer sb = new StringBuffer(  );
            int offset = 0;

            for ( String text : texts )
            {
                if ( ( 20 * text.length(  ) ) > width )
                {
                    int off = ( 20 * text.length(  ) ) / width;

                    if ( ( ( 20 * text.length(  ) ) % width ) == 0 )
                    {
                        off = off - 1;
                    }

                    offset += off;
                }

                sb.append( text + "\n" );
            }

            System.out.print( "sbu_____________" + sb );

            FileImageCreator creator = new FileImageCreator( new SimpleDrawer(  ),
                                                             "/Users/larry/Desktop/img.png" );
            creator.setWidth( 600 ); // 图片宽度
            creator.setHeight( width ); // 图片高度
            creator.setLineNum( 0 ); // 干扰线条数
            creator.setFontSize( 20 ); // 字体大小
                                       // creator.setFontName("楷体");

            creator.setInterval( 10 );

            Color fontColor = new Color( 34, 34, 34 );
            creator.setFontColor( fontColor );
            creator.setqRCodeUrl( qRCodeUrl );

            int textSize = texts.size(  ) + offset;
            int textHeight = textSize * 30;
            int textTop = ( width - textHeight ) / 2;
            creator.setTextTop( textTop );

            // 文字旋转
            // creator.setRadian(30.0); //旋转弧度
            // creator.setRotateX(creator.getWidth()/5);
            // creator.setRotateY(creator.getHeight()*5/10);
            imageByte = creator.generateImageNet( sb.toString(  ) );

            System.out.println( "ok" );
        } catch ( IOException ex )
        {
            ex.printStackTrace(  );
        }

        return imageByte;
    }
}
