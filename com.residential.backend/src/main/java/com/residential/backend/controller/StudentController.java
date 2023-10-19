package com.residential.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.residential.backend.config.Response;
import com.residential.backend.model.Students;
import com.residential.backend.service.StudentService;


@RestController
@RequestMapping("/home")
public class StudentController {
	@Autowired
	private StudentService service;
	
	@GetMapping("/student/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Students> getByName(@PathVariable String id){
		Students student=service.findByName(id);
		
			return ResponseEntity.ok(student);
		
	}
	
	@GetMapping("/message")
	
	public ResponseEntity<Response> getMsg() {
		Response response=new Response();
		response.setMessage("hello");
		response.setStatus (true);
		return ResponseEntity.ok(response);
	}
	
	
	@PostMapping("/register")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Students> register(@RequestBody Students student){
		Students stu=service.registerStudent(student);
		return ResponseEntity.ok(stu);
	}
	
}
