package org.nabeels.main;

import java.io.IOException;
import java.util.Map;

public class Main {

	public static void main(String[] args) throws IOException {
		Map<String, String> substitutions = null;
		PDFWriter writer = null;
		
		Arguments userArgs = new Arguments();
		userArgs.parse(args);
		
		ExcelReader excelReader = new ExcelReader(userArgs.getInputFile());
		if (!excelReader.parse()) {
			// error
		}
		
		for (int i=0; i<excelReader.getNumberOfSheets(); i++) {
			int rowIndex = 1;
			
			while(true) {
				substitutions = excelReader.getSheetRowValues(i, rowIndex);
				if (substitutions == null) {
					break;
				}
				
				writer = new PDFWriter(userArgs.getTemplateName());
				if (!writer.prepareTemplate()) {
					// error
				}
				
				String filename = userArgs.getOutput() + "sheet_" + i + "_row_" + rowIndex;
				writer.writeToFile(substitutions, filename);
				
				rowIndex++;
			}
		}
	}

}
