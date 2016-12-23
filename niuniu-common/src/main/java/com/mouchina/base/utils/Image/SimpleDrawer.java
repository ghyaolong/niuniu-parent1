package com.mouchina.base.utils.Image;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class SimpleDrawer
    implements Drawer
{
    public void draw( AbstractImageCreator creator, Graphics2D g, String text )
    {
        // 文字旋转
        g.rotate( Math.toRadians( creator.getRadian(  ) ),
                  creator.getRotateX(  ),
                  creator.getRotateY(  ) );
        g.scale( creator.getScale(  ),
                 creator.getScale(  ) );

        g.setColor( creator.getFontColor(  ) );

        Font font = new Font( creator.getFontName(  ),
                              Font.PLAIN,
                              creator.getFontSize(  ) );
        g.setFont( font );

        FontMetrics fm = g.getFontMetrics( font );
        int fontHeight = fm.getHeight(  ); // 字符的高度

        int offsetLeft = 0;
        int rowIndex = 1;

        for ( int i = 0; i < text.length(  ); i++ )
        {
            char c = text.charAt( i );
            int charWidth = fm.charWidth( c ); // 字符的宽度

            // 另起一行
            if ( Character.isISOControl( c ) || ( offsetLeft >= ( creator.getWidth(  ) - charWidth ) ) )
            {
                rowIndex++;
                offsetLeft = 0;
            }

            g.drawString( String.valueOf( c ),
                          offsetLeft,
                          rowIndex * fontHeight );
            offsetLeft += charWidth;
        }
    }

    public void draw( AbstractImageCreator creator, Graphics2D g, String text, int textTop, int interval, int left,
                      int top )
    {
        // 文字旋转
        g.rotate( Math.toRadians( creator.getRadian(  ) ),
                  creator.getRotateX(  ),
                  creator.getRotateY(  ) );
        g.scale( creator.getScale(  ),
                 creator.getScale(  ) );
        // 让字体变平滑
        g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        g.setColor( creator.getFontColor(  ) );

        Font font = new Font( creator.getFontName(  ),
                              Font.PLAIN,
                              creator.getFontSize(  ) );
        g.setFont( font );

        FontMetrics fm = g.getFontMetrics( font );
        int fontHeight = fm.getHeight(  ); // 字符的高度

        int offsetLeft = 0;
        int rowIndex = 1;

        for ( int i = 0; i < text.length(  ); i++ )
        {
            char c = text.charAt( i );
            int charWidth = fm.charWidth( c ); // 字符的宽度

            // 另起一行
            if ( Character.isISOControl( c ) || ( offsetLeft >= ( ( creator.getWidth(  ) / 2 ) - charWidth ) ) )
            {
                rowIndex++;
                offsetLeft = 0;
            }

            if ( ! ( String.valueOf( c ).equals( "\n" ) ) )
            {
                g.drawString( String.valueOf( c ),
                              offsetLeft + left,
                              ( ( rowIndex - 1 ) * interval ) + ( rowIndex * ( fontHeight ) ) + top + textTop );
                offsetLeft += charWidth;
            }
        }
    }

    public void drawNet( AbstractImageCreator creator, Graphics2D g, String text, int textTop, int interval, int left,
                         int top )
    {
        // 文字旋转
        g.rotate( Math.toRadians( creator.getRadian(  ) ),
                  creator.getRotateX(  ),
                  creator.getRotateY(  ) );
        g.scale( creator.getScale(  ),
                 creator.getScale(  ) );
        // 让字体变平滑
        g.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );
        g.setColor( creator.getFontColor(  ) );

        Font font = new Font( creator.getFontName(  ),
                              Font.PLAIN,
                              creator.getFontSize(  ) );
        g.setFont( font );

        FontMetrics fm = g.getFontMetrics( font );
        int fontHeight = fm.getHeight(  ); // 字符的高度

        int offsetLeft = 0;
        int rowIndex = 1;

        for ( int i = 0; i < text.length(  ); i++ )
        {
            char c = text.charAt( i );
            int charWidth = fm.charWidth( c ); // 字符的宽度

            // 另起一行
            if ( Character.isISOControl( c ) || ( offsetLeft >= ( ( creator.getWidth(  ) / 2 ) - charWidth ) ) )
            {
                rowIndex++;
                offsetLeft = 0;
            }

            if ( ! ( String.valueOf( c ).equals( "\n" ) ) )
            {
                g.drawString( String.valueOf( c ),
                              offsetLeft + left,
                              ( ( rowIndex - 1 ) * interval ) + ( rowIndex * ( fontHeight ) ) + top + textTop );
                offsetLeft += charWidth;
            }
        }
    }
}
