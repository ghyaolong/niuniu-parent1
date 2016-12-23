package com.mouchina.base.progress;

import java.io.Serializable;

public class Progress
    implements Serializable
{
    private long pBytesRead;
    private long pContentLength;
    private long pItems;

    public long getpBytesRead(  )
    {
        return pBytesRead;
    }

    public void setpBytesRead( long pBytesRead )
    {
        this.pBytesRead = pBytesRead;
    }

    public long getpContentLength(  )
    {
        return pContentLength;
    }

    public void setpContentLength( long pContentLength )
    {
        this.pContentLength = pContentLength;
    }

    public long getpItems(  )
    {
        return pItems;
    }

    public void setpItems( long pItems )
    {
        this.pItems = pItems;
    }
}
