package com.example.demo.export;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

public class ExportPNG {
	public static InputStream exportOutputPNG(InputStream pdfData) throws IOException { 
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PDDocument document = null;
		document = PDDocument.load(pdfData);
		PDFRenderer renderer = new PDFRenderer(document);
		int pageNumber = 0;
		BufferedImage bi = renderer.renderImageWithDPI(pageNumber, 300);
		ImageIO.write(bi, "png", baos);
		baos.flush();
		
		InputStream pngData = new ByteArrayInputStream(baos.toByteArray());
		
		return pngData;
	}
}
