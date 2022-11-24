package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.SubmissionLog;

public interface SubmissionLogRepository extends JpaRepository<SubmissionLog, Long> {

}
