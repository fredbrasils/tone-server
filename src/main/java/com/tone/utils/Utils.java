package com.tone.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Utils {
	

	public static List convertFromTo(List list, Class clazz) {
		
		List newList = new ArrayList();
		
		for(Object obj : list) {
			
			Object newObj = convertFromTo(obj, clazz);
			newList.add(newObj);
		}
		
		return newList;
	}

	public static Object convertFromTo(Object obj, Class clazz) {
		
		try {					
			
			Object newObject = clazz.getDeclaredConstructor().newInstance();
			
			Class<?> current = clazz;
		    do {
	           for (Field field: current.getDeclaredFields()) {
		    		field.setAccessible(true);           
	            	
		    		if (!field.isAnnotationPresent(IgnoreField.class) || !field.getAnnotation(IgnoreField.class).value()) {	    			
			    		Object value = getFielValue(obj, field.getName());
		            	if(value != null) {
		            		field.set(newObject, value);	            		
		            	}
		    		}
		        }
		           
		    } while((current = current.getSuperclass()) != null);
	        
	        return newObject;
	        
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Object getFielValue(Object obj, String nameField) throws IllegalArgumentException, IllegalAccessException {

		Class<?> current = obj.getClass();
	    do {
	    	for (Field field: current.getDeclaredFields()) {
	    		field.setAccessible(true);           
           	
	    		if (field.getName().equals(nameField)) {    			
	    			return field.get(obj);
	    		}        	
	    	}           
	    } while((current = current.getSuperclass()) != null);
		
		return null;
	} 
	
}
