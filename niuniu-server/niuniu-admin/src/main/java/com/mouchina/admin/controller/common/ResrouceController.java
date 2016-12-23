package com.mouchina.admin.controller.common;

import com.mouchina.admin.base.config.Env;
import com.mouchina.base.resource.type.Image;
import com.mouchina.base.utils.FileUtil;
import com.mouchina.base.utils.MIMEConvertFileType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

@Controller
@RequestMapping( "/common/resource" )
public class ResrouceController
{
    static Logger logger = LogManager.getLogger(  );
    @Resource
    private Env env;

    @RequestMapping( value = "/image/upload", method = RequestMethod.POST )
    public String imageUpload( HttpServletRequest request, HttpServletResponse response, ModelMap modelMap )
                       throws IOException
    {
        MultipartHttpServletRequest mulRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = mulRequest.getFileMap(  );

        logger.debug( "start fileMap=" + fileMap );

        for ( Entry<String, MultipartFile> entry : fileMap.entrySet(  ) )
        {
            MultipartFile item = entry.getValue(  );
            String contentType = item.getContentType(  );
            String suffix = MIMEConvertFileType.getContentTypeSuffix( contentType );

            logger.debug( "before upload res=" + suffix );

            Image image = new Image(  );
            modelMap.put( "success", false );
            // image.setUrl("https://ss0.baidu.com/-Po3dSag_xI4khGko9WTAnF6hhy/super/whfpf%3D425%2C260%2C50/sign=69ffdd25dff9d72a1731435db2171c06/8b82b9014a90f603003e255d3f12b31bb051ed1a.jpg");
            image =
                FileUtil.uploadImage( item,
                                      suffix,
                                      env.getProp( "base.resource.url" ) );

            if ( image != null )
            {
                /*
                 * File f = new File(""); InputStream i = new
                 * FileInputStream(f);
                 */
                modelMap.put( "success", true );
                modelMap.put( "image", image );
                logger.debug( "finshied upload res image=" + image.getUrl(  ) );
            }

            logger.debug( "after upload res=" + image );

            break;
        }

        logger.debug( "end upload fileMap=" + fileMap );

        return "";
    }
    
}
