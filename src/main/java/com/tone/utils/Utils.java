package com.tone.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.tone.model.LuthierEntity;
import com.tone.model.LuthierFeatureEntity;
import com.tone.model.payload.Luthier;
import com.tone.model.payload.LuthierFeature;

public class Utils {
	
	private static final String PACKAGE_MODEL = "com.tone.model"; 

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List convertFromTo(List list, Class clazz) {
		
		List newList = new ArrayList<>();
		
		list.stream().forEach(obj -> {			
				Object newObj = convertFromTo(obj, clazz);
				newList.add(newObj);
			}
		);
		
		return newList;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Set convertFromTo(Set list, Class clazz) {
		
		Set newList = new HashSet<>();
		
		list.stream().forEach(obj -> {			
				Object newObj = convertFromTo(obj, clazz);
				newList.add(newObj);
			}
		);
		
		return newList;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Object convertFromTo(Object obj, Class clazz) {
		
		try {					
			
			//create object
			Object newObject = clazz.getDeclaredConstructor().newInstance();
			
			Class<?> current = clazz;
		    do {
		    	// course all fields from new object
	           for (Field field: current.getDeclaredFields()) {
		    		field.setAccessible(true);           
	            	
		    		// verify if it is a field to be converted
		    		if (!field.isAnnotationPresent(IgnoreField.class) || !field.getAnnotation(IgnoreField.class).value()) {	    			
			    		
		    			Class typeClass = (field.getGenericType() instanceof ParameterizedType) ? 
		    					(Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0] : field.getType();
		    			
		    					/*
		    			if(field.getGenericType() instanceof ParameterizedType) {
		    				typeClass = (Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
		    			}	*/	    			
		    			
		    			// get the value
		    			Object value = getFielValue(obj, field.getName(), typeClass);		    			
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
	
	private static Object getFielValue(Object obj, String nameField, Class clazz) throws IllegalArgumentException, IllegalAccessException {
		
		Object returnObject = null;
		
		// get class from object
		Class<?> current = obj.getClass();
	    do {
	    	// course the field of object
	    	for (Field field: current.getDeclaredFields()) {
	    		field.setAccessible(true);
           	
	    		// verify if the object name's field is equal to new object
	    		if (field.getName().equals(nameField)) {
	    			
	    			returnObject = field.get(obj);
	    			
	    			// verify if it is other entity's class
	    			if(returnObject != null && field.getType().getName().startsWith(PACKAGE_MODEL)) {
	    				// convert the field to respective
	    				returnObject = convertFromTo(returnObject, clazz);	    				
	    			}else if(returnObject != null && field.getGenericType() instanceof ParameterizedType) {

	    				//ParameterizedType pType = (ParameterizedType) field.getGenericType();
    	                if(Set.class.isAssignableFrom(field.getType())) {	    	                	
    	                	returnObject = convertFromTo((Set)returnObject, clazz /*(Class) pType.getActualTypeArguments()[0]*/);
    	                }else if(List.class.isAssignableFrom(field.getType())) {
    	                	returnObject = convertFromTo((List)returnObject, clazz /*(Class) pType.getActualTypeArguments()[0]*/);
    	                }
	    			}
	    			
	    		}
	    		
	    		if(returnObject != null) {
	    			return returnObject;
	    		}
	    	}           
	    } while((current = current.getSuperclass()) != null);
		
		return null;
	} 
	
	/*
	public static void test() {
		
		Class<?> current = LuthierEntity.class;
		
		for (Field field: current.getDeclaredFields()) {
    			field.setAccessible(true);

    			if(Collection.class.isAssignableFrom(field.getType())) {
    				
    				if (field.getGenericType() instanceof ParameterizedType) {

    	                ParameterizedType pType = (ParameterizedType) field.getGenericType();
    	                Class clzz = (Class) pType.getActualTypeArguments()[0];

   	                    System.out.println(clzz.getName());
    	            }
    			}else {
    				System.out.println("LuthierEntity: "+field.getType().getName());
    			}
   		}        	

		current = LuthierFeatureEntity.class;
		
		for (Field field: current.getDeclaredFields()) {
				field.setAccessible(true);
				System.out.println("LuthierFeatureEntity: "+field.getType().getName());
			}
		
		current = Luthier.class;
		
		for (Field field: current.getDeclaredFields()) {
    			field.setAccessible(true);
    			
    			if(Collection.class.isAssignableFrom(field.getType())) {
    				
    				if (field.getGenericType() instanceof ParameterizedType) {

    	                ParameterizedType pType = (ParameterizedType) field.getGenericType();
    	                Class clzz = (Class) pType.getActualTypeArguments()[0];

   	                    System.out.println(clzz.getName());
    	            }
    			}else {
    				System.out.println("Luthier: "+field.getType().getName());
    			}
   		}        	

		current = LuthierFeature.class;
		
		for (Field field: current.getDeclaredFields()) {
				field.setAccessible(true);
				System.out.println("LuthierFeature: "+field.getType().getName());
			}
		
	}
	*/
	
	/*
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
	} */
	
}
