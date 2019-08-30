package com.tone.utils;

import java.lang.reflect.Field;

import com.tone.model.LuthierEntity;

public class fasdfasdf {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
			System.out.println("dafasd");
			Class<?> current = LuthierEntity.class;
			
			for (Field field: current.getDeclaredFields()) {
	    			field.setAccessible(true);

	    			System.out.println("LuthierEntity: "+field.getType().getName());
	   		}  

	}

}
