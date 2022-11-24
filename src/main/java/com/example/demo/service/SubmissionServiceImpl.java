package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.demo.model.Submission;
import com.example.demo.repository.SubmissionRepository;

@Service
public class SubmissionServiceImpl implements SubmissionService {
	
	@Autowired
	private SubmissionRepository submissionRepository;
	
	public SubmissionServiceImpl(SubmissionRepository submissionRepository) {
		super();
		this.submissionRepository = submissionRepository;
	}
	
	@Override
	public List<Submission> getAllSubmissions() {
		// TODO Auto-generated method stub
		return submissionRepository.findAll(Sort.by(Sort.Direction.DESC, "date"));
	}
	
	@Override
	public List<Submission> getAllSubmissionsByEmail(String email) {
		// TODO Auto-generated method stub
		return submissionRepository.getAllSubmissionsByEmail(email);
	}
	
	@Override
	public List<Submission> getAllSubmissionsByUserId(Long id) {
		// TODO Auto-generated method stub
		return submissionRepository.getAllSubmissionsByUserId(id);
	}
	
	@Override
	public List<Submission> getAllSubmissionsByUserIdOrderByDateDesc(Long id) {
		// TODO Auto-generated method stub
		return submissionRepository.getAllSubmissionsByUserIdOrderByDateDesc(id);
	}
	
	@Override
	public Submission getSubmissionById(Long id) {
		// TODO Auto-generated method stub
		return submissionRepository.findById(id).get();
	}
	
	@Override
	public Submission saveSubmission(Submission submission) {
		return submissionRepository.save(submission);
	}
	
}
