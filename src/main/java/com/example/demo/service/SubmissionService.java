package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Submission;

public interface SubmissionService {
	List<Submission> getAllSubmissions();
	
	List<Submission> getAllSubmissionsByEmail(String email);
	
	List<Submission> getAllSubmissionsByUserId(Long id);
	
	List<Submission> getAllSubmissionsByUserIdOrderByDateDesc(Long id);
	
	Submission getSubmissionById(Long id);
	
	Submission saveSubmission(Submission Submission);
}
