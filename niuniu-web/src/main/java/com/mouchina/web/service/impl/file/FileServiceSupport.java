package com.mouchina.web.service.impl.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.mouchina.base.utils.DateUtils;
import com.mouchina.base.utils.MIMEConvertFileType;
import com.mouchina.base.utils.UUIDGenerator;
import com.mouchina.moumou_server.entity.file.FileHelper;
import com.mouchina.web.service.api.file.FileService;

public class FileServiceSupport implements FileService {
	
	//上传图片
	@Override
	public List<String> uploadPictures(MultipartFile[] photos) throws Exception {
		List<String> list = new ArrayList<String>();
		for (MultipartFile photo : photos) {
			if (!photo.isEmpty()) {
				// 获得文件类型（可以判断如果不是图片，禁止上传）
				String contentType = photo.getContentType();
				String fileName = photo.getOriginalFilename();
				String datePath = DateUtils.getNowDateString("yyyy/MM/dd");
	            String suffix = MIMEConvertFileType.getContentTypeSuffix( contentType );
				String path = FileHelper.PIC_PHOTO.getMarker() + FileHelper.FILE_SEPARATE.getMarker() + datePath + FileHelper.FILE_SEPARATE.getMarker() + UUIDGenerator.getUUID() + FileHelper.FILE_DOT + suffix;
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
		return list.size() == 0 ? null : list;
	}

}
