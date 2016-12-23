package com.mouchina.web.service.api.file;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

	//上传图片
	public List<String> uploadPictures(MultipartFile[] photos) throws Exception;

}
