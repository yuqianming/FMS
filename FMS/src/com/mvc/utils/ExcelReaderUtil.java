package com.mvc.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author y
 * @create 2018-01-19 0:13
 * @desc
 **/
public class ExcelReaderUtil {
	//excel2003扩展名
	public static final String EXCEL03_EXTENSION = ".xls";
	//excel2007扩展名
	public static final String EXCEL07_EXTENSION = ".xlsx";

	//private static List<String[]> result=new ArrayList<String[]>();
	/**
	 * 每获取一条记录，即打印
	 * 在flume里每获取一条记录即发送，而不必缓存起来，可以大大减少内存的消耗，这里主要是针对flume读取大数据量excel来说的
	 * @param sheetName
	 * @param sheetIndex
	 * @param curRow
	 * @param cellList
	 */
	public static void sendRows(String filePath, String sheetName, int sheetIndex, int curRow, List<String> cellList) {
			StringBuffer oneLineSb = new StringBuffer();
			oneLineSb.append(filePath);
			oneLineSb.append("--");
			oneLineSb.append("sheet" + sheetIndex);
			oneLineSb.append("::" + sheetName);//加上sheet名
			oneLineSb.append("--");
			oneLineSb.append("row" + curRow);
			oneLineSb.append("::");
			for (String cell : cellList) {
				oneLineSb.append(cell.trim());
				oneLineSb.append("|");
			}
			String oneLine = oneLineSb.toString();
			if (oneLine.endsWith("|")) {
				oneLine = oneLine.substring(0, oneLine.lastIndexOf("|"));
			}// 去除最后一个分隔符

			System.out.println(oneLine);
	}

	public static void readExcel(String fileName) throws Exception {
		int totalRows =0;
		if (fileName.endsWith(EXCEL03_EXTENSION)) { //处理excel2003文件
			ExcelXlsReader excelXls=new ExcelXlsReader();
			totalRows =excelXls.process(fileName);
		} else if (fileName.endsWith(EXCEL07_EXTENSION)) {//处理excel2007文件
			ExcelXlsxReaderWithDefaultHandler excelXlsxReader = new ExcelXlsxReaderWithDefaultHandler();
			totalRows = excelXlsxReader.process(fileName);
		} else {
			throw new Exception("文件格式错误，fileName的扩展名只能是xls或xlsx。");
		}
		System.out.println("发送的总行数：" + totalRows);	
	}
	
	public static List<String[]> readExcel(MultipartFile file) throws Exception {
		List<String[]> result=new ArrayList<String[]>();
		String fileName=file.getOriginalFilename().toLowerCase();
		if (fileName.endsWith(EXCEL03_EXTENSION)) { //处理excel2003文件
			ExcelXlsReader excelXls=new ExcelXlsReader();
			result =excelXls.process(file.getInputStream());
		} 
		else if (fileName.endsWith(EXCEL07_EXTENSION)) {//处理excel2007文件
			ExcelXlsxReaderWithDefaultHandler excelXlsxReader = new ExcelXlsxReaderWithDefaultHandler();
			result = excelXlsxReader.process(file.getInputStream());
		} else {
			throw new Exception("文件格式错误，fileName的扩展名只能是xlsx或者xls。");
		}
		return result;	
	}


	public static void copyToTemp(File file,String tmpDir) throws Exception{
		FileInputStream fis=new FileInputStream(file);
		File file1=new File(tmpDir);
		if (file1.exists()){
			file1.delete();
		}
		FileOutputStream fos=new FileOutputStream(tmpDir);
		byte[] b=new byte[1024];
		int n=0;
		while ((n=fis.read(b))!=-1){
			fos.write(b,0,n);
		}
		fis.close();
		fos.close();
	}

	public static void main(String[] args) throws Exception {
		//String path="C:\\Users\\y15079\\Desktop\\shenjiangnan\\TestSample\\Test_0_10_H_20180125_Cto_Process_1834.xlsx";
		/*String path="/test/m05177new";*/
		/*String path="C:\\Users\\y15079\\Desktop\\shenjiangnan\\TestSample\\1010filesCollection5000100";*/
		//H_20180111_Base_Date(4)_0420.xlsx
		/*String path="C:\\Users\\y15079\\Desktop\\shenjiangnan\\TestSample\\REWORK\\H_20180105_Cto_REWORK_1600.xlsx";*/
		/*String path="C:\\Users\\y15079\\Desktop\\shenjiangnan\\TestSample\\ASS_ITEM\\H_20180116_ASS_ITEM_1915.xlsx";*/
		/*String path="C:\\Users\\y15079\\Desktop\\shenjiangnan\\TestSample\\Cto_Process\\H_20180116_Cto_Process_2000.xlsx";*/
		/*String path="C:\\Users\\y15079\\Desktop\\shenjiangnan\\TestSample\\Cto_Ship\\H_20180105_Cto_Ship_0005.xlsx";*/
		/*String path="C:\\Users\\y15079\\Desktop\\shenjiangnan\\TestSample\\Po_sn\\H_20180105_Po_sn_1020.xlsx";*/
		/*String path="C:\\Users\\y15079\\Desktop\\shenjiangnan\\TestSample\\Cto_CODE\\H_20180109_Cto_CODE_1640.xlsx";*/
		/*String path="C:\\Users\\y15079\\Desktop\\shenjiangnan\\TestSample\\ASS_PRODUCT\\H_20171226_ASS_PRODUCT_0430 - 副本.xlsx";*/
		/*String path="C:\\Users\\y15079\\Desktop\\shenjiangnan\\TestSample\\MAC\\H_20180108_MAC_2130.xlsx";*/
		/*String path = "C:\\Users\\y15079\\Desktop\\shenjiangnan\\TestSample\\Base_Data3\\H_20180106_Base_Data3_1520 - 副本.xlsx";*/
		/*String path="C:\\Users\\y15079\\Desktop\\shenjiangnan\\TestSample\\H_20171226_ASS_PRODUCT_0430.xls";*/

		/*copyToTemp(file2,"/home/test/tmp.xlsx");*/

		/*ExcelReaderUtil.readExcel(file2.getAbsolutePath(),"/home/test/tmp.xlsx");*/
		String path="D://wwc//安装文件//20180731//11111.xlsx";
		//String path="D://wwc//安装文件//20180731//数据导入-成本单.xlsx";
		ExcelReaderUtil.readExcel(path);
		/*readXlsx(file2.getAbsolutePath());*/

	}
}
