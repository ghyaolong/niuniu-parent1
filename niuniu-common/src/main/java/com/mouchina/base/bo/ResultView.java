package com.mouchina.base.bo;

import java.io.Serializable;

public class ResultView
    implements Serializable
{
    /**
     *
     */
    private static final long serialVersionUID = -7184873996487489407L;
    private int state;
    private String message;
    private int code;
    private String url;
    private String title = "提示信息";

    public String getTitle(  )
    {
        return title;
    }

    public void setTitle( String title )
    {
        this.title = title;
    }

    public String getUrl(  )
    {
        return url;
    }

    public void setUrl( String url )
    {
        this.url = url;
    }

    public int getState(  )
    {
        return state;
    }

    public void setState( int state )
    {
        this.state = state;
    }

    public String getMessage(  )
    {
        return message;
    }

    public void setMessage( String message )
    {
        this.message = message;
    }

    public int getCode(  )
    {
        return code;
    }

    public void setCode( int code )
    {
        this.code = code;
    }
}
