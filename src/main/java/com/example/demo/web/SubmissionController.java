package com.example.demo.web;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

import javax.activation.DataSource;
import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.export.ExportPDF;
import com.example.demo.export.ExportPNG;
import com.example.demo.model.ActionType;
import com.example.demo.model.Submission;
import com.example.demo.model.SubmissionLog;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.SubmissionLogService;
import com.example.demo.service.SubmissionService;

@Controller
public class SubmissionController {
	@Autowired
	private SubmissionService submissionService;
	
	@Autowired
	private SubmissionLogService submissionLogService;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	JavaMailSender mailSenderObj;
	
	public SubmissionController(SubmissionService submissionService) {
		super();
		this.submissionService = submissionService;
	}
	
	@GetMapping("/")
	public String home(Model model, Authentication authentication) {
//		model.addAttribute("submission", submissionService.getAllSubmissionsByEmail(authentication.getName()));
		User user = userRepository.findByEmail(authentication.getName());
		
		model.addAttribute("submission", submissionService.getAllSubmissionsByUserIdOrderByDateDesc(user.getId()));
		return "submission";
	}
	
	@GetMapping("/submission/new")
	public String createSubmissionFrom(Model model) {
		System.out.println("TEST PRINT DATE: " + LocalDateTime.now());
        if (!model.containsAttribute("submission")) {
            model.addAttribute("submission", new Submission());
        }
		return "create_submission";
	}
	
	// Function for directly prompt to download generated PDF
	@GetMapping(value = "/pdf/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<InputStreamResource> printPDF(@PathVariable Long id, Authentication authentication) {
		Submission sub = submissionService.getSubmissionById(id);
		
		// Generate to PDF
		ByteArrayInputStream bis = ExportPDF.submissionsReport(sub);
		
		// Directly get the ByteArray data stored in db
		// ByteArrayInputStream bis = new ByteArrayInputStream(sub.getFilePdf());
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment;filename=output-" + sub.getDate() + ".pdf");
		
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF)
					.body(new InputStreamResource(bis));
	}
	
	// Function for directly prompt to download PNG
	@GetMapping(value = "/png/{id}", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<InputStreamResource> printPNG(@PathVariable Long id, Authentication authentication) throws IOException {
		Submission sub = submissionService.getSubmissionById(id);
		
		// Generate to PDF and Convert to PNG
		InputStream targetStream = ExportPDF.submissionsReport(sub);
		InputStream pngData = ExportPNG.exportOutputPNG(targetStream);
		
		// Directly get the ByteArray data stored in db
		// InputStream isFromFirstData = new ByteArrayInputStream(sub.getFilePng());
		
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Disposition", "attachment;filename=output-" + sub.getDate() + ".png");
		
		return ResponseEntity.ok().headers(headers).contentType(MediaType.IMAGE_PNG)
					.body(new InputStreamResource(pngData));
	}
	
	// Handle submit and send to email function call
	@PostMapping("/submission")
	public String saveSubmission(@ModelAttribute("submission") Submission submission, final BindingResult binding, RedirectAttributes attr, Authentication authentication) throws IOException {
		if(
			(submission.getModule().equals("1") &&
			submission.getMajorType().equals("2") &&
			submission.getMainType().equals("1")) ||
			(submission.getModule().equals("2") &&
			submission.getMajorType().equals("1") &&
			submission.getMainType().equals("1")) ||
			(submission.getModule().equals("2") &&
			submission.getMajorType().equals("2") &&
			submission.getMainType().equals("1")) ||
			(submission.getModule().equals("2") &&
			submission.getMajorType().equals("2") &&
			submission.getMainType().equals("2"))
		) {
		    attr.addFlashAttribute("org.springframework.validation.BindingResult.submission", binding);
		    attr.addFlashAttribute("submission", submission);
			return "redirect:/submission/new?error";
		}
		
		byte[] byteArray = null;
		User user = userRepository.findByEmail(authentication.getName());
		
		Submission sub = new Submission();
		sub.setModule(submission.getModule());
		sub.setMajorType(submission.getMajorType());
		sub.setMainType(submission.getMainType());
		sub.setUserId(user.getId());
		sub.setEmail(authentication.getName());
		
		// convert to PDF Byte Array to assign to Submission object
		ByteArrayInputStream bytedata = ExportPDF.submissionsReport(sub);
        byteArray = bytedata.readAllBytes();
        sub.setFilePdf(byteArray);
        bytedata.close();      
        
        // convert to PDF Byte Array and run function sendEmail
		ByteArrayInputStream bis = ExportPDF.submissionsReport(sub);
		sendEmail(sub, bis, sub.getFilePdf());
		
		// System.out.println("Bab1: " + sub.getFilePdf());
		return "redirect:/";
	}
	
	public void sendEmail(Submission submission, ByteArrayInputStream bis, byte[] data) {
		final String emailToRecipent = submission.getEmail();
		final String emailSubject = "Submission Report";
		final String emailMessage1 = "<html><body><p>Dear Sir/Madam,</p> <p>We have recieved your submission."
				+ "<br><br>"
				+ "<table border='1' width='300px' style='text-align:center;font-size:20px;'><tr> <td colspan='2'>Submitted"
				+ "</td></tr><tr><td>Module</td><td>Module " + submission.getModule() + "</td></tr><tr><td>Major Type</td><td>Major Type "
				+ submission.getMajorType() + "</td></tr><tr><td>Main Type</td><td>Main Type " + submission.getMainType()
				+ "</td></tr></table></body></html>";
		
		mailSenderObj.send(new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws MessagingException, IOException {
				MimeMessageHelper mimeMessageHelperObj = new MimeMessageHelper(mimeMessage, true, "UTF-8");
				mimeMessageHelperObj.setTo(emailToRecipent);
				mimeMessageHelperObj.setText(emailMessage1, true);
				mimeMessageHelperObj.setSubject(emailSubject);
				
				// PDF attachment
				DataSource attachment = new ByteArrayDataSource(bis, "application/pdf");
				mimeMessageHelperObj.addAttachment("output"+ ".pdf", attachment);
			
				// Converting input stream to output stream, PDF to PNG through byte array data
				InputStream pngAttachment = ExportPNG.exportOutputPNG(new ByteArrayInputStream(data));
				
				// PNG attachment
				DataSource attachment1 = new ByteArrayDataSource(pngAttachment, "image/png");
				mimeMessageHelperObj.addAttachment("output"+ ".png", attachment1);
				
				// set PNG file to object and save to table
				byte[] byteArray = null;
				InputStream pngData = ExportPNG.exportOutputPNG(new ByteArrayInputStream(data));
				byteArray = pngData.readAllBytes();
				
				submission.setFilePng(byteArray);
				submission.setDate(LocalDateTime.now());
				submissionService.saveSubmission(submission);
				
				// Call function to save to Submission Log
				saveSubmissionLog(submission);
				
			}
		});
	}
	
	public void saveSubmissionLog(Submission submission) {
		SubmissionLog subLog = new SubmissionLog();
		subLog.setModule(submission.getModule());
		subLog.setMajorType(submission.getMajorType());
		subLog.setMainType(submission.getMainType());
		subLog.setFilePdf(submission.getFilePdf());
		subLog.setFilePng(submission.getFilePng());
		subLog.setSubmissionId(submission.getId());
		subLog.setUserId(submission.getUserId());
		subLog.setEmail(submission.getEmail());
		subLog.setAction(ActionType.SUBMITTED);
		subLog.setDate(submission.getDate());
		
		submissionLogService.saveSubmissionLog(subLog);
	}
}
