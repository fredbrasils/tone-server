package com.tone.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utils {
	
	/** 
	 * Path of package's models
	 * */
	private static final String PACKAGE_MODEL = "com.tone.model"; 
	
	/** 
	 * The level of deep from a hierarchy
	 * */
	private static final int DEEP = 4; 

	/**
	 * Convert the object's list to the match's class
	 * 
	 * @param list list of model
	 * @param clazz Class will be converted
	 * @return A list of converted's classes
	 */
	@SuppressWarnings({ "rawtypes" })
	public static List convertFromTo(List list, Class clazz) {
		return convertFromTo(list, clazz, DEEP);
	}
	
	/**
	 * Convert the object's list to the match's class
	 * 
	 * @param list list of model
	 * @param clazz Class will be converted
	 * @param deep How deep is the hierarchy
	 * @return A list of converted's classes
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static List convertFromTo(List list, Class clazz, int deep) {
		
		// create a new list
		List newList = new ArrayList<>();
		
		// course the list and convert each element
		list.stream().forEach(obj -> {			
				Object newObj = convertFromTo(obj, clazz, deep);
				newList.add(newObj);
			}
		);
		
		return newList;
	}
	
	/**
	 * Convert the object's list to the match's class
	 * 
	 * @param list list of model
	 * @param clazz Class will be converted
	 * @return A list of converted's classes
	 */
	@SuppressWarnings({ "rawtypes"})
	public static Set convertFromTo(Set list, Class clazz) {
		return convertFromTo(list, clazz, DEEP);		
	}

	/**
	 * Convert the object's list to the match's class
	 * 
	 * @param list list of model
	 * @param clazz Class will be converted
	 * @param deep How deep is the hierarchy
	 * @return A list of converted's classes
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Set convertFromTo(Set list, Class clazz, int deep) {
		
		// create a new set list
		Set newList = new HashSet<>();
		
		// course the set list and convert each element
		list.stream().forEach(obj -> {			
				Object newObj = convertFromTo(obj, clazz, deep);
				newList.add(newObj);
			}
		);
		
		return newList;
	}

	/**
	 * Convert the object to the match's class
	 * 
	 * @param obj Object that will be converted
	 * @param clazz Class will be converted
	 * @return Object converted
	 */
	@SuppressWarnings({ "rawtypes"})
	public static Object convertFromTo(Object obj, Class clazz) {
		return convertFromTo(obj, clazz, DEEP);
	}
	
	/**
	 * Convert the object to the match's class
	 * 
	 * @param obj Object that will be converted
	 * @param clazz Class will be converted
	 * @param deep How deep is the hierarchy
	 * @return Object converted
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Object convertFromTo(Object obj, Class clazz, int deep) {
		
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
		    			
		    			// get the value
		    			Object value = getFielValue(obj, field.getName(), typeClass, deep);		    			

		    			// verify if it is null
		    			if(value != null) {
		            		// set the value
		            		field.set(newObject, value);	            		
		            	}
		    		}
		        }
		          
	           // get the super class
		    } while((current = current.getSuperclass()) != null);
	        
	        return newObject;
	        
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 *  Get the value from the object that will be converted
	 * 
	 * @param obj Object that will be converted
	 * @param nameField field's name
	 * @param clazz Class will be converted
	 * @param deep How deep is the hierarchy
	 * @return Object converted
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings({ "rawtypes" })
	private static Object getFielValue(Object obj, String nameField, Class clazz, int deep) throws IllegalArgumentException, IllegalAccessException {
		
		Object returnObject = null;
		
		// verify if reached the deep of hierarchy. This is used to avoid an infinity loop
		if(deep == 0) {
			return returnObject;
		}
		
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
	    				returnObject = convertFromTo(returnObject, clazz, --deep);	    				

	    			// verify if it is A Collection
	    			}else if(returnObject != null && field.getGenericType() instanceof ParameterizedType) {

	    				// it is a Set
    	                if(Set.class.isAssignableFrom(field.getType())) {	    	                	
    	                	returnObject = convertFromTo((Set)returnObject, clazz, --deep);
   	    				// it is a List
    	                }else if(List.class.isAssignableFrom(field.getType())) {
    	                	returnObject = convertFromTo((List)returnObject, clazz, --deep);
    	                }
	    			}
	    		}
	    		
	    		// verify is the object is not null
	    		if(returnObject != null) {
	    			return returnObject;
	    		}
	    	}           
	    	
	    	// get the super class
	    } while((current = current.getSuperclass()) != null);
		
		return null;
	} 
}
