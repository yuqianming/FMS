package com.mvc.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

/**
 * CSV文件导出工具类
 * 
 * @author
 * @reviewer
 */
public class CSVUtils {

	/**
	 * CSV文件生成方法
	 * 
	 * @param head
	 * @param dataList
	 * @param outPutPath
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public void createCSVFile(String[] head, List<String[]> dataList, String outPutPath, String filename) throws IOException {
		File csvFile = null;
		BufferedWriter csvWtriter = null;
		System.out.println("======================" + outPutPath + File.separator + filename);
		csvFile = new File(outPutPath + File.separator + filename);
		File parent = csvFile.getParentFile();
		if (parent != null && !parent.exists()) {
			parent.mkdirs();
		}
		csvFile.createNewFile();

		// UTF-8使正确读取分隔符","
		csvWtriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "UTF-8"), 1024);
		// 写入文件头部
		csvWtriter.write(getWriteRowStr(head));
		csvWtriter.newLine();

		// 写入文件内容
		for (String[] row : dataList) {
			csvWtriter.write(getWriteRowStr(row));
			csvWtriter.newLine();
		}
		csvWtriter.flush();
		csvWtriter.close();

	}

	/**
	 * 写一行数据方法
	 * 
	 * @param row
	 * @param csvWriter
	 * @throws IOException
	 */
	private String getWriteRowStr(String[] row) {
		// 写入文件头部
		StringBuffer sb = new StringBuffer();
		for (String data : row) {
			sb.append(",").append(data);
		}
		return sb.substring(1);
	}
}