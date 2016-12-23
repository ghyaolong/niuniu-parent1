package com.mouchina.base.resource.type;

import java.io.Serializable;

public class Image
    extends File
    implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = 486144576798284012L;

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

    private int width;
    private int height;
}
