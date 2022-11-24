package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.SubmissionLog;
import com.example.demo.repository.SubmissionLogRepository;

@Service
public class SubmissionLogServiceImpl implements SubmissionLogService {
	
	@Autowired
	private SubmissionLogRepository submissionLogRepository;
	
	public SubmissionLogServiceImpl(SubmissionLogRepository submissionLogRepository) {
		super();
		this.submissionLogRepository = submissionLogRepository;
	}
	
	@Override
	public SubmissionLog saveSubmissionLog(SubmissionLog submissionLog) {
		return submissionLogRepository.save(submissionLog);
	}
}
