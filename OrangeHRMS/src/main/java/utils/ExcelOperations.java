package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelOperations {

	public static XSSFWorkbook wb;
	public static XSSFSheet  sheet;
	public static XSSFRow  rw;
	public static XSSFCell cell;
	public static FileInputStream fis;
	public static String value;
	HashMap<String, Integer> colNumber=new HashMap<String, Integer>();
	
	public ExcelOperations() {
		
		try {
			FileInputStream	fis=new FileInputStream(new File(System.getProperty("user.dir")+File.separator+"src"+File.separator+"main"+File.separator+"java"+File.separator+"testData"+File.separator+"TestData.xlsx"));
			wb=new XSSFWorkbook(fis);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
		
		public int populateRowNums(String sheetName,String TCId) {
			int rowNumber=0;
			sheet=wb.getSheet(sheetName);
			int rowCount=sheet.getLastRowNum();
			for(int i=1;i<=rowCount;i++) {
				String cellData=sheet.getRow(i).getCell(rowNumber).getStringCellValue();
				if(cellData.trim().equals(TCId)) {
					rowNumber=i;
					break;
				}
			}
			return rowNumber;

	}
	
	
		
		public void populateColumnNums() {
			int colIndex=0;
			Row row=sheet.getRow(0);
			Iterator<Cell> cells=row.cellIterator();
			while(cells.hasNext()) {
				Cell cell=cells.next();
				String colName=cell.getStringCellValue();
				colNumber.put(colName, colIndex);
				colIndex++;
			}
		}
	
	public int getColNumber(String colName) {
		populateColumnNums();
		return colNumber.get(colName);
	}
	
	public String getCellData(String SheetName,int rowNum,String colName) {
		String ret="";
		int colNum=getColNumber(colName);
		ret=getCellData( rowNum, colNum);
		return ret;
	}
	
	public String getCellData(String SheetName,String rowName,String colName) {
		String ret="";
		int rowNum=populateRowNums(SheetName, rowName);
		int colNum=getColNumber(colName);
		ret=getCellData(rowNum, colNum);
		return ret;
	}
	
	public String getCellData(int rowNum,int colNum) {
		String ret="";
		try {
			Row row=sheet.getRow(rowNum);
			Cell cell=row.getCell(colNum);
			if(cell.getCellType()==CellType.STRING) {
				ret=cell.getStringCellValue();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	
	public void readSheetData() {
		Iterator<Row> rows=sheet.iterator();
		while(rows.hasNext()) {
			Row currentRow=rows.next();
			Iterator<Cell> cells=currentRow.iterator();
			while(cells.hasNext()) {
				Cell currentCell=cells.next();
				CellType cType=currentCell.getCellType();
				if(cType==CellType.STRING) {
					value=currentCell.getStringCellValue();
				}else if(cType==CellType.NUMERIC){
					value=""+currentCell.getNumericCellValue();
				}
				System.out.println(value);
			}
		}
	}
	
	
	public void writeData(String SheetName,String scenarioId,String colName,String enterValue) {
		try {
			sheet=wb.getSheet(SheetName);
			int rowNum=populateRowNums(SheetName, scenarioId);
			int colNum=getColNumber(colName);
			rw=sheet.getRow(rowNum);
			cell=rw.createCell(colNum);
			cell.setCellValue(enterValue);
			
			FileOutputStream fout=new FileOutputStream(new File(System.getProperty("user.dir")+File.separator+"src"+File.separator+"main"+File.separator+"java"+File.separator+"testData"+File.separator+"TestData.xlsx"));
			wb.write(fout);
			wb.close();
		}catch(Exception e) {
			
		}
	}
	
	public String getRowData(int rowNum) {
		String ret="";
		Row row=sheet.getRow(rowNum);
		if(row == null) {
			System.out.println("Row "+ rowNum +" is empty.");
		}else {
			for(Cell cell : row) {
				switch(cell.getCellType()) {
				case STRING:
					ret=cell.getStringCellValue() +"\t";
					break;
				case NUMERIC:
					ret=cell.getNumericCellValue() +"\t";
					break;
				case BOOLEAN:
					ret=cell.getBooleanCellValue() +"\t";
					break;
				case FORMULA:
					ret=cell.getCellFormula() +"\t";
					break;
					default:
						System.out.println("Unknown\t");
				}
			}
		}
		
		return ret;
	}
	
}
