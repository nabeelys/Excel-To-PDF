/**
 * 
 * @author Nabeel Saabna
 * @data 2019-Sep-07
 * @see https://www.callicoder.com/java-read-excel-file-apache-poi/
 * 
 */
package org.nabeels.main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;


public class ExcelReader {

	private String filename = null;
	private Workbook workbook = null;
	private Map<Integer, List<String>> sheetsHeaders = null;

	
	public ExcelReader(String filename) {
		this.filename = filename;
		this.sheetsHeaders = new HashMap<Integer, List<String>>();
	}

	
	/**
	 * read and parse the file
	 * @return true if file was read successfully
	 */
	public boolean parse() {
		boolean status = false;
		
		try {
			this.workbook = WorkbookFactory.create(new File(this.filename));
			status = true;
		}
		catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return status;
	}

	
	/**
	 * @return number of sheets
	 */
	public int getNumberOfSheets() {
		return this.workbook.getNumberOfSheets();
	}
	
	

	/**
	 * @param sheetId 	a number starting from zero
	 * @param rowId 	a number starting from 1 (row 0 expected to contain the headers)
	 * @return a Mapping between headers and values from the specified sheet and row
	 */	
	public Map<String, String> getSheetRowValues(int sheetId, int rowId) {
		Map<String, String> result = null;
		
		List<String> headers = this.sheetsHeaders.get(sheetId);
		if (headers == null) {
			headers = getSheetRow(sheetId, 0);
			this.sheetsHeaders.put(sheetId, headers);
		}
		
		List<String> values = getSheetRow(sheetId, rowId);
		
		if (values != null) {
			result = new HashMap<String, String>();
			for (int i=0; i<headers.size(); i++) {
				String value = Constants.EMPTY;
				if (i<values.size()) {
					value = values.get(i);
				}
				result.put(headers.get(i), value);
			}
		}
		
		return result;
	}

	
	/**
	 * @param sheetId 	a number starting from zero
	 * @param rowId 	a number starting from 1 (row 0 expected to contain the headers)
	 * @return list of values from the specified sheet and row
	 */
	private List<String> getSheetRow(int sheetId, int rowId) {
		List<String> rowValues = null;
		String value = null;
		
		try {
			Sheet sheet = workbook.getSheetAt(sheetId);
			Row row = sheet.getRow(rowId);
			
			Iterator<Cell> itr = row.cellIterator();
			rowValues = new LinkedList<String>();
			
			while (itr.hasNext()) {
				Cell cell = itr.next();
				value = extractCellValueAsString(cell);				
				rowValues.add(value);
			}
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace();  // TODO: logger
		}
		catch (IllegalStateException e) {
			e.printStackTrace(); // TODO: logger
		}
		catch (NullPointerException e) {
			e.printStackTrace(); // TODO: logger
		}
		
		
		return rowValues;
	}
	

	/**
	 * @param cell
	 * @return extracts the value from the cell and converts it to String
	 */
	private String extractCellValueAsString(Cell cell) {
		CellType type = cell.getCellType();
		String value = null;
		
		switch (type) {
		case NUMERIC:
			double tmp = cell.getNumericCellValue();
			if (tmp == (int)tmp) {
				value = String.valueOf((int)tmp);	
			} else {
				value = String.valueOf(tmp);
			}					
			break;
			
		case BOOLEAN:
			value = String.valueOf(cell.getBooleanCellValue());
			break;
			
		case STRING:
			value = cell.getStringCellValue();
			break;
			
		case BLANK:
			value = Constants.EMPTY;
			break;
			
		default:
			value = Constants.EMPTY;
			break;
		}
		
		return value;
	}
	
	
	

}
