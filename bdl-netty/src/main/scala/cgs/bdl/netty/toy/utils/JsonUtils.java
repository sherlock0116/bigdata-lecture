package cgs.bdl.netty.toy.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
public final class JsonUtils {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private JsonUtils() {
		//no instance
	}

	public static <T> T fromJson(String jsonStr, Class<T> clazz) throws JsonProcessingException {
		return MAPPER.readValue(jsonStr, clazz);
	}

	public static String toJson(Object object) throws JsonProcessingException {
		return MAPPER.writeValueAsString(object);
	}

}
