package com.mouchina.base.resource.type;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

public class File
    implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -7804910257528700869L;

    public String getUrl(  )
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

    public String getType(  )
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public Date getAddDate(  )
    {
        return addDate;
    }

    public void setAddDate( Date addDate )
    {
        this.addDate = addDate;
    }

    public String getId(  )
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    private String url;
    private String type;
    private Date addDate;
    private String id;
    private String mimeType;

    public String getMimeType(  )
    {
        return mimeType;
    }

    public void setMimeType( String mimeType )
    {
        this.mimeType = mimeType;
    }

    private BigInteger size;

    public BigInteger getSize(  )
    {
        return size;
    }

    public void setSize( BigInteger size )
    {
        this.size = size;
    }
}
