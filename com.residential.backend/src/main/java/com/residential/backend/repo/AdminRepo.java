package com.residential.backend.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.residential.backend.model.Admins;
@Repository
public interface AdminRepo extends JpaRepository<Admins, String> {
	
	public Optional<Admins> findByEmail(String email);
}
