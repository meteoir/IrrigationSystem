package com.andela.irrigation_system.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

	private static final ObjectMapper mapper = new ObjectMapper();
	
	public static <T> T fromString(Class<T> clazz, String json) {
		try {
			return mapper.readValue(json, clazz);
		} catch (Exception e) {
			throw new IllegalStateException("Could not deserialize object: " + json);
		}
	}

	public static String asString(Object object) {
		try {
			return mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new IllegalStateException("Could not serialize object: " + object);
		}
	}
}
