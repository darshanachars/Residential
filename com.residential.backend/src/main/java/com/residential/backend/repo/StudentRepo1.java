package com.residential.backend.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.residential.backend.model.Students;

@Repository
public interface StudentRepo1 extends JpaRepository<Students, Integer>{
	public Optional<Students> findByErp(long erp);
}
