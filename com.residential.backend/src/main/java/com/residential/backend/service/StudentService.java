package com.residential.backend.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.residential.backend.exception.ResourceNotFound;
import com.residential.backend.model.Roles;
import com.residential.backend.model.Students;

import com.residential.backend.repo.StudentRepo1;
import com.residential.backend.twilio.SmsService;


@Service
public class StudentService {
	@Autowired
	private StudentRepo1 studentRepo1;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private SmsService smsService;
	
	
	public Students findByName(String id) {
		
		
		return studentRepo1.findById(id).orElseThrow(null);
	}
	
	public Students registerStudent(Students student) {
		Roles roles=new Roles();
		roles.setId(1);
		roles.setRole("ROLE_ADMIN");
		Set<Roles> li=new HashSet<>();
		li.add(roles);
		student.setAssignedRoles(li);
		student.setPassword(this.passwordEncoder.encode(student.getPassword()));
		Students save = studentRepo1.save(student);
		smsService.registerMessage(student);
		return save ;
	}
	
	public Students forgetPassword(String email, String password) {
		Students student=studentRepo1.findByEmail(email).orElseThrow(()->new ResourceNotFound("resource", "email", email));
		student.setPassword(this.passwordEncoder.encode(password));
		student=studentRepo1.save(student);
		return student;
	}
}
