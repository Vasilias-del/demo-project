package com.example.demo.export;

import java.awt.Canvas;
import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.example.demo.model.Submission;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.TextField;

public class ExportPDF {
	public static ByteArrayInputStream submissionsReport(Submission submission) {
		Document document = new Document(PageSize.A4);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		try {
			Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
			int count = 0;
			if(submission.getModule().equals("1") && 
					submission.getMajorType().equals("1") && 
					submission.getMainType().equals("1")) {
				// 2 text boxes
				System.out.println("=================================================");
				System.out.println("Generate 2 Text Boxes PDF: ");
				System.out.println("=================================================");
				count = 2;
			}
			if(submission.getModule().equals("1") && 
					submission.getMajorType().equals("1") && 
					submission.getMainType().equals("2")) {
				// 1 text boxes
				System.out.println("=================================================");
				System.out.println("Generate 1 Text Boxes PDF: ");
				System.out.println("=================================================");
				count = 1;
				
			}
			if(submission.getModule().equals("1") && 
					submission.getMajorType().equals("2") && 
					submission.getMainType().equals("2")) {
				// 4 text boxes
				System.out.println("=================================================");
				System.out.println("Generate 4 Text Boxes PDF: ");
				System.out.println("=================================================");
				count = 4;

			}
			if(submission.getModule().equals("2") && 
					submission.getMajorType().equals("1") && 
					submission.getMainType().equals("2")) {
				// 3 text boxes
				System.out.println("=================================================");
				System.out.println("Generate 3 Text Boxes PDF: ");
				System.out.println("=================================================");
				count = 3;
			} 
//			else {
//				System.out.println("=================================================");
//				System.out.println("Generate Default PDF: ");
//				System.out.println("=================================================");
//				PdfPTable table = new PdfPTable(4);
//				table.setWidths(new int[] {2, 2, 2, 10});
//				table.setWidthPercentage(80);
//				
//				PdfPCell hcell;
//				hcell = new PdfPCell(new Phrase("Module", headFont));
//				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//				table.addCell(hcell);
//				
//				
//				hcell = new PdfPCell(new Phrase("Major Type", headFont));
//				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//				table.addCell(hcell);
//				
//				
//				hcell = new PdfPCell(new Phrase("Main Type", headFont));
//				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//				table.addCell(hcell);
//				
//				
//				hcell = new PdfPCell(new Phrase("Email", headFont));
//				hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
//				table.addCell(hcell);
//				
//				PdfPCell cell;
//				cell = new PdfPCell(new Phrase(submission.getModule()));
//				cell.setVerticalAlignment(Element.ALIGN_RIGHT);
//				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//				table.addCell(cell);
//				
//				cell = new PdfPCell(new Phrase(submission.getMajorType()));
//				cell.setVerticalAlignment(Element.ALIGN_RIGHT);
//				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//				table.addCell(cell);
//				
//				cell = new PdfPCell(new Phrase(submission.getMainType()));
//				cell.setVerticalAlignment(Element.ALIGN_RIGHT);
//				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//				table.addCell(cell);
//				
//				cell = new PdfPCell(new Phrase(submission.getEmail()));
//				cell.setVerticalAlignment(Element.ALIGN_RIGHT);
//				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//				table.addCell(cell);
//				
//				
//				PdfWriter.getInstance(document, out);
//				
//				document.open();
//				document.add(table);
//			}
			
			PdfPTable table = new PdfPTable(2);
			table.setWidths(new int[] {2, 2});
			table.setWidthPercentage(50);
			table.setHorizontalAlignment(Element.ALIGN_LEFT);
			
			for(int i=0; i<count; i++) {
				PdfPCell cell1;
				cell1 = new PdfPCell(new Phrase("Tex box "+ (i+1) +": "));
				cell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell1.setBorder(0);
				table.addCell(cell1);
				
				cell1 = new PdfPCell(new Phrase("Info " + (i+1)));
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				table.addCell(cell1);
				
				PdfPCell cell2;
				cell2 = new PdfPCell(new Phrase("  "));
				cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
				cell2.setBorder(0);
				table.addCell(cell2);
				
				cell2 = new PdfPCell(new Phrase("  "));
				cell2.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell2.setBorder(0);
				table.addCell(cell2);
			}
			
			PdfWriter writer = PdfWriter.getInstance(document, out);
			document.open();
			document.add(table);
			document.close();
			writer.setFullCompression();
			
		} catch (DocumentException ex) {
			System.out.println("=================================================");
			System.out.println("Error Generating PDF: "+ ex);
			System.out.println("=================================================");
		}
		
		return new ByteArrayInputStream(out.toByteArray());
	}
}
