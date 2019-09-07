package org.nabeels.main;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class Arguments {

	@Parameter(names = { "-f", "-file" }, description = "Excel file to be parsed")
	private String inputFile = null;

	@Parameter(names = { "-tn",
			"-template-name" }, description = "template file name, a prefix to the .html and .properties files (both files should be in same location)")
	private String templateName = null;

	@Parameter(names = { "-o",
			"-output" }, description = "output location, the target directory to save the generated files")
	private String output = ".";

	
	public String getInputFile() {
		return inputFile;
	}

	public void setInputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	
	public void parse(String[] args) {
		JCommander.newBuilder().addObject(this).build().parse(args);
	}

}
