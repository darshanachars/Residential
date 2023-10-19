package com.residential.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import com.residential.backend.exception.ResourceNotFound;
import com.residential.backend.repo.AdminRepo;
import com.residential.backend.repo.StudentRepo;


@Component
public class CustomUserDetailService implements UserDetailsService{
	@Autowired
	private StudentRepo studentRepo;
	
	@Autowired
	private AdminRepo adminRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails user;
		if(username.startsWith("Admin")) {
			String email=username.substring(6);
			 user=adminRepo.findByEmail(email).orElseThrow(()->new ResourceNotFound("User", "email", username));
		}
		
		else{
			 user = studentRepo.findByEmail(username).orElseThrow(()->new ResourceNotFound("user","email",username));
		}
		
		return user;
	}
	
}
