package com.example.demo.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "submission")
public class Submission {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String module;
	
	@Column(name = "major_type")
	private String majorType;
	
	@Column(name = "main_type")
	private String mainType;
	
	@Lob
	@Column(name = "file_pdf", nullable=true)
	private byte[] filePdf;
	
	@Lob
	@Column(name = "file_png", nullable=true)
	private byte[] filePng;
	
	@Column(name = "date", nullable=true)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime date;
	
	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "email")
	private String email;
	
	public Submission() {}

	public Submission(String module, String majorType, String mainType, byte[] filePdf, byte[] filePng,
			LocalDateTime date, Long userId, String email) {
		super();
		this.module = module;
		this.majorType = majorType;
		this.mainType = mainType;
		this.filePdf = filePdf;
		this.filePng = filePng;
		this.date = date;
		this.userId = userId;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getMajorType() {
		return majorType;
	}

	public void setMajorType(String majorType) {
		this.majorType = majorType;
	}

	public String getMainType() {
		return mainType;
	}

	public void setMainType(String mainType) {
		this.mainType = mainType;
	}

	public byte[] getFilePdf() {
		return filePdf;
	}

	public void setFilePdf(byte[] filePdf) {
		this.filePdf = filePdf;
	}

	public byte[] getFilePng() {
		return filePng;
	}

	public void setFilePng(byte[] filePng) {
		this.filePng = filePng;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
