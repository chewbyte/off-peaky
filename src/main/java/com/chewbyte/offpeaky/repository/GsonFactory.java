package com.chewbyte.offpeaky.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonFactory {

	private static Gson gson;
	
	public static String json(Object element) {
		
		if(gson == null) {
			gson = new GsonBuilder().setPrettyPrinting().create();
		}
		return gson.toJson(element);
	}
	
	public static Object object(String json, Class<?> type) {
		
		if(gson == null) {
			gson = new Gson();
		}
		return gson.fromJson(json, type);
	}
}
