package com.residential.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.residential.backend.model.Students;

import com.residential.backend.repo.StudentRepo1;


@Service
public class StudentService {
	@Autowired
	private StudentRepo1 studentRepo1;
	
	public Students findByName(int id) {
		
		
		return studentRepo1.findById(id).orElseThrow(null);
	}
	
//	public Students findByErp(long erp) {
//		return 
//	}
}
