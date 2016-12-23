package com.mouchina.base.utils;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonUtils {
	public static String javaToString(Object obj) {
		String json = "";

		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	public static <T> T jsonToObject(String json, Class<T> T) {
		ObjectMapper mapper = new ObjectMapper();
		T result = null;
		try {
			result = mapper.readValue(json, T);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static JavaType getCollectionType(Class<?> collectionClass,
			Class<?>... elementClasses) {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.getTypeFactory().constructParametricType(collectionClass,
				elementClasses);
	}

	public static void main(String[] args) {
//		Image image = new Image();
//		image.setUrl("htsdkfd://1231232");
//		String json = javaToString(image);
//		System.out.println(json);
//		Image image2 = jsonToObject(json, Image.class);
//		System.out.println(image2);

		
		Map map=JsonUtils.jsonToObject("sdfdf",Map.class);
		System.out.println("propertyMap="+map.size());
	
	}

	@SuppressWarnings("unchecked")
	public static <T> List<T> readValue(String jsonString, JavaType javaType)
			throws JsonParseException {
		ObjectMapper mapper = new ObjectMapper();
		List<T> list = null;
		try {
			list = (List<T>) mapper.readValue(jsonString, javaType);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

}
