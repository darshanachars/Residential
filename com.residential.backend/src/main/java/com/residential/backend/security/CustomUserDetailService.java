package com.residential.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.residential.backend.model.Students;
import com.residential.backend.repo.StudentRepo;


@Component
public class CustomUserDetailService implements UserDetailsService{
	@Autowired
	private StudentRepo studentRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Students students = studentRepo.findByName(username).orElseThrow(null);
		return students;
	}
	
}
