package com.residential.backend.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.residential.backend.model.Students;

@Repository
public interface StudentRepo extends JpaRepository<Students, String> {
	public Optional<Students> findByEmail(String email);
	public Optional<Students> findByErp(String erp);
}
