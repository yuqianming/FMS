package com.mvc.utils;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public class ReadFileUtil {

	public Map<String, List<String[]>> readFile(MultipartFile file) throws Exception {
		if (file == null) {
			return null;
		} else {
			String fileName = file.getOriginalFilename().toLowerCase();
			System.out.println("解析文件格式");
			if (fileName.endsWith("xls")) {
				return readXls(file);
			} else if (fileName.endsWith("xlsx")) {
				return readXlsx(file);
			} else {
				throw new Exception("未知文件格式！");
			}
		}
	}

	public Map<String, List<String[]>> readXlsx(MultipartFile file) throws Exception {
		System.out.println("解析文件开始");
		InputStream is = file.getInputStream();
		XSSFWorkbook workbook = new XSSFWorkbook(is);
		Map<String, List<String[]>> map = new HashMap<String, List<String[]>>();
		List<String[]> list = new ArrayList<String[]>();
		// Read the Sheet
		if (workbook != null) {
			for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
				// 获得当前sheet工作表
				Sheet sheet = workbook.getSheetAt(sheetNum);
				if (sheet == null) {
					continue;
				}

				list = readSheet(sheet);

				if (list.size() > 0) {
					map.put("sheet" + sheetNum, list);
				}
			}
		}
		is.close();
		return map;
	}

	public Map<String, List<String[]>> readXls(MultipartFile file) throws Exception {
		System.out.println("解析文件开始");
		InputStream is = file.getInputStream();
		HSSFWorkbook workbook = new HSSFWorkbook(is);
		Map<String, List<String[]>> map = new HashMap<String, List<String[]>>();
		List<String[]> list = new ArrayList<String[]>();
		// Read the Sheet
		if (workbook != null) {
			for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
				// 获得当前sheet工作表
				Sheet sheet = workbook.getSheetAt(sheetNum);
				if (sheet == null) {
					continue;
				}

				list = readSheet(sheet);

				map.put("sheet" + sheetNum, list);

			}
		}
		is.close();
		System.out.println("解析数据完成");
		return map;
	}

	private List<String[]> readSheet(Sheet sheet) throws Exception{

		List<String[]> list = new ArrayList<String[]>();
		// 获得当前sheet的开始行
		int firstRowNum = sheet.getFirstRowNum();
		// 获得当前sheet的结束行
		int lastRowNum = sheet.getLastRowNum();

		// 获得当前行的开始列
		int firstCellNum = 0;
		// 获得当前行的列数
		int lastCellNum = 0;
		for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {

			// 获得当前行
			Row row = sheet.getRow(rowNum);
			if (row == null) {
				continue;
			}

			// 获得当前行的开始列
			firstCellNum = row.getFirstCellNum();
			// 获得当前行的列数
			// lastCellNum = row.getPhysicalNumberOfCells();
			lastCellNum = row.getLastCellNum();

			String[] cells = new String[lastCellNum];
			// 循环当前行
			for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
				Cell cell = row.getCell(cellNum);
				cells[cellNum] = getCellValue(cell);
			}
			list.add(cells);
		}

		return list;
	}

	private String getCellValue(Cell cell) {
		String cellValue = "";
		if (cell == null) {
			return cellValue;
		}
		// 判断数据的类型
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC: // 数字
			cellValue = getNumericValue(cell);
			break;
		case Cell.CELL_TYPE_STRING: // 字符串
			//cellValue = checkErrValue(String.valueOf(cell.getStringCellValue()));
			cellValue = String.valueOf(cell.getStringCellValue());
			break;
		case Cell.CELL_TYPE_BOOLEAN: // Boolean
			cellValue = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_FORMULA: // 公式
			// cellValue = String.valueOf(cell.getCellFormula());
			try {
				cellValue = String.valueOf(cell.getStringCellValue());
			} catch (IllegalStateException e) {
				cellValue = Integer.valueOf((int) cell.getNumericCellValue()).toString();
			}
			break;
		case Cell.CELL_TYPE_BLANK: // 空值
			cellValue = "";
			break;
		case Cell.CELL_TYPE_ERROR: // 故障
			cellValue = "非法字符";
			break;
		default:
			cellValue = "未知类型";
			break;
		}
		return cellValue;
	}

	private String getNumericValue(Cell cell) {
		String result;
		short format = cell.getCellStyle().getDataFormat();
		SimpleDateFormat sdf = null;
		if (format == 14 || format == 31 || format == 57 || format == 58) {
			// 日期
			sdf = new SimpleDateFormat("yyyyMM");
		} else if (format == 20 || format == 32) {
			// 时间
			sdf = new SimpleDateFormat("hh:mm:ss");
		}
		if (sdf != null) {
			double value = cell.getNumericCellValue();
			Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
			result = sdf.format(date);
		} else {
			// 把数字当成String来读，避免出现1读成1.0的情况
			cell.setCellType(Cell.CELL_TYPE_STRING);
			//result = checkErrValue(String.valueOf(cell.getStringCellValue()));
			result = String.valueOf(cell.getStringCellValue());
		}
		return result;
	}

	/*private String checkErrValue(String value) {
		if (value != null && !("".equals(value))) {
			if ("-".equals(value.substring(value.length() - 1))) {
				value = "-" + value.substring(0, value.indexOf("."));
			} else if (value.endsWith("%")) {
				float result = new Float(value.substring(0, value.indexOf("%"))) / 100;
				value = result + "";
			}
		}
		return value;
	}*/
}
