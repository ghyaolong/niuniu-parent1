package com.mouchina.web.controller.common;

import com.mouchina.base.resource.type.Image;
import com.mouchina.base.utils.DateUtils;
import com.mouchina.base.utils.FileUtil;
import com.mouchina.base.utils.MIMEConvertFileType;
import com.mouchina.base.utils.UUIDGenerator;
import com.mouchina.moumou_server.entity.file.FileHelper;
import com.mouchina.web.base.config.Env;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    /**
     * 多图上传接口
     * @param request
     * @param response
     * @param modelMap
     * @return
     * @throws IOException
     */
    @RequestMapping( value = "/images/upload", method = RequestMethod.POST )
    public String imagesUpload( HttpServletRequest request, HttpServletResponse response, ModelMap modelMap )
                       throws IOException
    {
        MultipartHttpServletRequest mulRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = mulRequest.getFileMap(  );

        logger.debug( "start fileMap=" + fileMap );
        List<Image> images = new ArrayList<Image>();
        for ( Entry<String, MultipartFile> entry : fileMap.entrySet(  ) )
        {
            MultipartFile item = entry.getValue(  );
            String contentType = item.getContentType(  );
            String suffix = MIMEConvertFileType.getContentTypeSuffix( contentType );

            logger.info("item : " + item + " ,contentType : " + contentType + " ,suffix : " + suffix);
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
                images.add(image);
                logger.info( "finshied upload res image=" + image.getUrl(  ) );
            }

            logger.debug( "after upload res=" + image );

        }
        modelMap.put("images", images);
        logger.debug( "end upload fileMap=" + fileMap );

        return "";
    }
    
    @RequestMapping(value = "/video/upload", method = RequestMethod.POST)
	public String videoUpload(HttpServletRequest request,
			HttpServletResponse response, ModelMap modelMap) throws IOException {
		MultipartHttpServletRequest mulRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = mulRequest.getFileMap();

		logger.debug("start fileMap=" + fileMap);
		Map audioMap = new HashMap();
		for (Entry<String, MultipartFile> entry : fileMap.entrySet()) {
			MultipartFile item = entry.getValue();
			String contentType = item.getContentType();
			String suffix=item.getOriginalFilename().substring(item.getOriginalFilename().lastIndexOf("."));
			String basePath = env.getProp("base.resource.path") + "/audio/";
			String dateTimeString = DateUtils.getNowDateStringALL() +suffix;
			String mp3Path=env.getProp("base.resource.url")+"/audio/"+dateTimeString;
			String audioPath = basePath + dateTimeString;
			audioMap.put("url", mp3Path);
			if (item.getSize() > 10000000) {
				audioMap.put("result", 0);

			} else {

				SaveFileFromInputStream(item.getInputStream(), audioPath);
				logger.debug("after upload res=" + mp3Path);
				audioMap.put("result", 1);
			}
			
			break;
		}

		modelMap.put("audio", audioMap);

		logger.debug("end upload fileMap=" + fileMap);

		return "";
	}

	public void SaveFileFromInputStream(InputStream stream, String path)
			throws IOException {
		FileOutputStream fs = new FileOutputStream(path);
		byte[] buffer = new byte[1024 * 1024];
		int bytesum = 0;
		int byteread = 0;
		while ((byteread = stream.read(buffer)) != -1) {
			bytesum += byteread;
			fs.write(buffer, 0, byteread);
			fs.flush();
		}
		fs.close();
		stream.close();
	}
	
	// 上传图片
	@RequestMapping("/uploadPictures")
	private String uploadPictures(@RequestParam MultipartFile[] files, HttpServletResponse response, ModelMap modelMap)
			throws Exception {
		List<String> list = new ArrayList<String>();
		for (MultipartFile photo : files) {
			if (!photo.isEmpty()) {
				// 获得文件类型（可以判断如果不是图片，禁止上传）
				String contentType = photo.getContentType();
				String fileName = photo.getOriginalFilename();
				String datePath = DateUtils.getNowDateString("yyyy/MM/dd");
	            String suffix = MIMEConvertFileType.getContentTypeSuffix( contentType );
				String path = FileHelper.PIC_PHOTO.getMarker() + FileHelper.FILE_SEPARATE.getMarker() + datePath + FileHelper.FILE_SEPARATE.getMarker() + UUIDGenerator.getUUID() + FileHelper.FILE_DOT.getMarker() + suffix;
				String url = FileHelper.FILE_ROOT.getMarker() + path;
				File file = new File(url);
				if (!file.exists()) {
					file.mkdirs();
				} else {
					file.delete();
				}
				photo.transferTo(file);
				list.add(FileHelper.PIC_BASE_URL.getMarker() + path);
			}
		}
		list = list.size() == 0 ? null : list;
		modelMap.put("photos", list);
		return "";
	}
	
}
