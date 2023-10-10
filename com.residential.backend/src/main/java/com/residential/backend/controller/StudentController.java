package com.residential.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.residential.backend.model.Students;
import com.residential.backend.service.StudentService;


@RestController
@RequestMapping("/home")
public class StudentController {
	@Autowired
	private StudentService service;
	
	@GetMapping("/student/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Students> getByName(@PathVariable int id){
		Students student=service.findByName(id);
		
			return ResponseEntity.ok(student);
		
	}
	
	@GetMapping("/message")
	
	public ResponseEntity<String> getMsg() {
		return ResponseEntity.ok("this URI can be accessed by any one with valid credentials");
	}
	
	
}
