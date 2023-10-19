package com.residential.backend.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class JwtRequest {
	private String role;
	private String email;
	private String password;
	
	
	public JwtRequest(String role,String name, String password) {
		super();
		this.role=role;
		this.email = name;
		this.password = password;
	}
	public JwtRequest() {
		super();
	}
	
}
