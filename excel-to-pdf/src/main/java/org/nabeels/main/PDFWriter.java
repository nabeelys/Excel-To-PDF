package org.nabeels.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

public class PDFWriter {
	
	private String templateId;
	private Properties props = null;
	private String htmlTemplateData = null;
	
	public PDFWriter(String templateNameId) {
		this.templateId = templateNameId;
	}
	
	
	public boolean prepareTemplate() {
		boolean result = false;
		
		try {
			this.props = new Properties();
			InputStream propInStream = new FileInputStream(templateId + Constants.EXT_PROPERTIES);
			props.load(new InputStreamReader(propInStream, Charset.forName("UTF-8")));
			
			StringBuilder strBuilder = new StringBuilder();
			Path path = Paths.get(templateId + Constants.EXT_HTML);
			Stream<String> lines = Files.lines(path);
			lines.forEach(str -> strBuilder.append(str).append(Constants.NEW_LINE));
			lines.close();
			this.htmlTemplateData = strBuilder.toString();
			
			result = true;
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	/**
	 * @return status, true means all files read/write completed successfully
	 */
	public boolean writeToFile(Map<String, String> substitutions,String filename) {
		boolean result = false;
		
		props.forEach( (key,value) -> {
			if(substitutions.containsKey(value)) {
				this.htmlTemplateData = this.htmlTemplateData.replace((String)key, substitutions.get(value));
			}
		});
		
		try {
			File out = new File(filename + Constants.EXT_HTML);
			String path = out.getAbsolutePath();
			Writer writer = new OutputStreamWriter(new FileOutputStream(out), StandardCharsets.UTF_8);
			writer.write(htmlTemplateData);
			writer.close();
	        
			OutputStream finalPdf2 = new FileOutputStream(filename + ".2" + Constants.EXT_PDF);
	        
			Document document = new Document();
			PdfWriter writer22 = PdfWriter.getInstance(document, finalPdf2);
	        document.open();
	        XMLWorkerHelper.getInstance().parseXHtml(writer22, document, new FileInputStream(path), StandardCharsets.UTF_8);
	        document.close();
	        
			result = true;
		}
		catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}		
		
		return result;
	}

}
