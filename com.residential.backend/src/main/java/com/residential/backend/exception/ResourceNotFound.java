package com.residential.backend.exception;


import lombok.Data;


@Data
public class ResourceNotFound extends RuntimeException {
	
	String resourceName;
	String fieldName;
	String fieldValue;
	public ResourceNotFound(String resourceName, String fieldName, String fieldValue) {
		super(String.format("%s not found with %s: %s", resourceName,fieldName,fieldValue ));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}
}
