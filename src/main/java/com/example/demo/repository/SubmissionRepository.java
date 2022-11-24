package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Submission;

public interface SubmissionRepository extends JpaRepository<Submission, Long> {
	List<Submission> getAllSubmissionsByEmail(String email);
	
	List<Submission> getAllSubmissionsByUserId(Long id);
	
	List<Submission> getAllSubmissionsByUserIdOrderByDateDesc(Long id);
}
