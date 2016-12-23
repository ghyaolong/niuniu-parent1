package com.mouchina.web.base.listener;

import com.mouchina.base.progress.Progress;
import org.apache.commons.fileupload.ProgressListener;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;

@Component
public class FileUploadProgressListener
    implements ProgressListener
{
    private HttpSession session;

    public void setSession( HttpSession session )
    {
        this.session = session;

        Progress status = new Progress(  );
        session.setAttribute( "status", status );
    }

    @Override
    public void update( long pBytesRead, long pContentLength, int pItems )
    {
        // TODO Auto-generated method stub
        Progress status = (Progress) session.getAttribute( "status" );
        status.setpBytesRead( pBytesRead );
        status.setpContentLength( pContentLength );
        status.setpItems( pItems );
    }
}
