package com.mouchina.base.utils;

import org.springframework.http.MediaType;


public class MIMEConvertFileType {
	public static String getContentTypeSuffix(String contentType) {
		String suffix = "";
		switch (contentType) {
		case "image/pjpeg"://应对ie6、7、8
			suffix = "jpg";
			break;
		case MediaType.IMAGE_JPEG_VALUE:
			suffix = "jpg";
			break;
		case "image/x-png"://应对ie6、7、8
			suffix = "png";
			break;
		case MediaType.IMAGE_PNG_VALUE:
			suffix = "png";
			break;
		case MediaType.IMAGE_GIF_VALUE:
			suffix = "gif";
			break;
		default:
			suffix = "jpg";
			break;
		}
		return suffix;
	}
}