package com.mouchina.base.utils;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;

import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.bson.types.ObjectId;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * MongoDB调用工具
 * @author dbb
 *
 */
public class ImageDBAdapter
{
    private static final Log logger = LogFactory.getLog( ImageDBAdapter.class );
    public static final String FSN_MPLOGO = "MPLOGO"; // 公众号LOGO
    public static final String FSN_QRCODE = "QRCODE"; // 二维码
    public static final String FSN_SUBQRCODE = "SUBQRCODE"; // 二级二维码
    public static final String FSN_AVATAR = "AVATAR"; // 头像
    public static final String FSN_VCODE = "VCODE"; // 验证码
    public static final String FSN_SENDVOICE = "SENDVOICE"; // 发送语音
    public static final String FSN_RECEIVEVOICE = "RECEIVEVOICE"; // 收到语音
    public static final String FSN_RECEIVEVIDEO = "RECEIVEVIDEO"; // 收到视频
    public static final String FSN_ELEMENT = "ELEMENT"; // 图文素材
    public static final String FSN_PANORAMA = "PANORAMA"; // 3D全景
    private GridFS gridFS;

    public ImageDBAdapter( DB db, String fsname )
    {
        gridFS = new GridFS( db, fsname ); // fsname: root_collection
    }

    public boolean saveImageFile( InputStream is, String fileName )
    {
        gridFS.remove( fileName );

        GridFSInputFile gfsInput = gridFS.createFile( is );
        // gfsInput.setId(fileName);
        gfsInput.setFilename( fileName );
        gfsInput.setContentType( "image/jpeg" );
        // gfsInput.setMetaData(metaData);
        gfsInput.save(  );

        return true;
    }

    public boolean saveMusic( InputStream is, String fileName )
    {
        gridFS.remove( fileName );

        GridFSInputFile gfsInput = gridFS.createFile( is );
        // gfsInput.setId(fileName);
        gfsInput.setFilename( fileName );
        gfsInput.setContentType( "audio/mpeg" );
        // gfsInput.setMetaData(metaData);
        gfsInput.save(  );

        return true;
    }

    public boolean saveFile( InputStream is, String fileName )
    {
        gridFS.remove( fileName );

        GridFSInputFile gfsInput = gridFS.createFile( is );
        // gfsInput.setId(fileName);
        gfsInput.setFilename( fileName );
        gfsInput.setContentType( "text/xml" );
        // gfsInput.setMetaData(metaData);
        gfsInput.save(  );

        return true;
    }

    public boolean saveHtm( InputStream is, String fileName )
    {
        gridFS.remove( fileName );

        GridFSInputFile gfsInput = gridFS.createFile( is );
        // gfsInput.setId(fileName);
        gfsInput.setFilename( fileName );
        gfsInput.setContentType( "text/html" );
        // gfsInput.setMetaData(metaData);
        gfsInput.save(  );

        return true;
    }

    // 保存
    public boolean saveImageFile( File file, String fileName )
    {
        try
        {
            gridFS.remove( fileName );

            GridFSInputFile gfsInput = gridFS.createFile( file );
            // gfsInput.setId(fileName);
            gfsInput.setFilename( fileName );
            gfsInput.setContentType( "image/jpeg" );
            // gfsInput.setMetaData(metaData);
            gfsInput.save(  );

            return true;
        } catch ( IOException e )
        {
            logger.error( e, e );

            return false;
        }
    }

    public GridFSDBFile findFileByName( String fileName )
    {
        GridFSDBFile gfsFile;

        try
        {
            gfsFile = gridFS.findOne( new BasicDBObject( "filename", fileName ) );
        } catch ( Exception e )
        {
            logger.error( e, e );

            return null;
        }

        return gfsFile;
    }

    public GridFSDBFile findFileById( String id )
    {
        GridFSDBFile gfsFile;

        try
        {
            gfsFile = gridFS.find( new ObjectId( id ) );
        } catch ( Exception e )
        {
            logger.error( e, e );

            return null;
        }

        return gfsFile;
    }
}
