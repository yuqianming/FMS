package com.mvc.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;

import com.mvc.pageModel.sys.ExcelInfo;

public class ExportUtils {
	public static String exportExcel(ExcelInfo info,List<Map<String,Object>> list)
	{
		String filePath="";
		try
		{
			filePath=info.getFileUrl()+info.getFileName();
			FileOutputStream out=new FileOutputStream(filePath);
			createWorkBook(info,list).write(out);
			out.flush();
			out.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return filePath;
	}
	
	public static Workbook createWorkBook_1(ExcelInfo info,List<Map<String,Object>> list)
	{
		Workbook wb=null;
		Sheet sheet=null;
		Row row=null;
		try
		{
			wb=new XSSFWorkbook();
			sheet=wb.createSheet(info.getSheetName());
			String[] keys=info.getColumnCodes();
			String[] cellTypes=info.getCellTypes();
			for(int i=0;i<keys.length;i++)
			{
				sheet.setColumnWidth(i,(short)(35.7*info.getColWidth()));
			}
			//创建第一行
			row=sheet.createRow(0);
			for(int i=0;i<info.getColumnNames().length;i++)
			{
				Cell cell=row.createCell(i);
				cell.setCellValue(info.getColumnNames()[i]);
			}
			
		   // CellStyle cellStyle = wb.createCellStyle();  
		    //cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
			
			for(int i=0;i<list.size();i++)
			{
				Row rows=sheet.createRow(i+1);
				for(int j=0;j<keys.length;j++)
				{
					Cell cell=rows.createCell(j);
					String value=list.get(i).get(keys[j])==null?"":list.get(i).get(keys[j]).toString();
					if("BigDecimal".equals(cellTypes[j]))
					{
						if(StringUtils.hasText(value))
						{
							cell.setCellValue(new DecimalFormat().parse(value).doubleValue());
						}
						else
						{
							cell.setCellValue(value);
						}
						
					}
					else
					{
						cell.setCellValue(value);
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return wb;
	}
	
	public static SXSSFWorkbook createWorkBook(ExcelInfo info,List<Map<String,Object>> list)
	{
		SXSSFWorkbook  wb=null;
		Sheet sheet=null;
		Row row=null;
		try
		{
			wb=new SXSSFWorkbook ();
			sheet=wb.createSheet(info.getSheetName());
			String[] keys=info.getColumnCodes();
			String[] cellTypes=info.getCellTypes();
			for(int i=0;i<keys.length;i++)
			{
				sheet.setColumnWidth(i,(short)(35.7*info.getColWidth()));
			}
			//创建第一行
			row=sheet.createRow(0);
			for(int i=0;i<info.getColumnNames().length;i++)
			{
				Cell cell=row.createCell(i);
				cell.setCellValue(info.getColumnNames()[i]);
				cell.setCellStyle(setCellStyle(wb));
			}
			
		   // CellStyle cellStyle = wb.createCellStyle();  
		    //cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
			String org_name="";
			int count=0;
			int index=1;
			for(int i=0;i<list.size();i++)
			{
				count++;
				Row rows=sheet.createRow(i+1);
				for(int j=0;j<keys.length;j++)
				{
					Cell cell=rows.createCell(j);
					String value=list.get(i).get(keys[j])==null?"":list.get(i).get(keys[j]).toString();
					if("成本单账龄分析.xlsx".equals(info.getFileName())&&j==0)
					{
						if(!StringUtils.hasText(org_name))
						{
							org_name=value;
						}
						else
						{
							if(!org_name.equals(value))
							{
								org_name=value;
								CellRangeAddress cra=new CellRangeAddress(index,count-1,0,0);
								sheet.addMergedRegion(cra);
								index=count;
							}
						}
					}
					if("BigDecimal".equals(cellTypes[j]))
					{
						if(StringUtils.hasText(value))
						{
							cell.setCellValue(new DecimalFormat().parse(value).doubleValue());
						}
						else
						{
							cell.setCellValue(value);
						}
						
					}
					else
					{
						cell.setCellValue(value);
					}
				}
			}
			if("成本单账龄分析.xlsx".equals(info.getFileName()))
			{
				CellRangeAddress cra=new CellRangeAddress(index,count,0,0);
				sheet.addMergedRegion(cra);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return wb;
	}
	
	public static String exportExcelHasRang(ExcelInfo info,List<Map<String,Object>> list)
	{
		String filePath="";
		try
		{
			filePath=info.getFileUrl()+info.getFileName();
			FileOutputStream out=new FileOutputStream(filePath);
			createWorkBookHasRange(info,list).write(out);
			out.flush();
			out.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return filePath;
	}
	
	public static SXSSFWorkbook createWorkBookHasRange(ExcelInfo info,List<Map<String,Object>> list)
	{
		SXSSFWorkbook  wb=null;
		Sheet sheet=null;
		Row row=null;
		try
		{
			wb=new SXSSFWorkbook ();
			sheet=wb.createSheet(info.getSheetName());
			String[] keys=info.getColumnCodes();
			String[] cellTypes=info.getCellTypes();
			for(int i=0;i<keys.length;i++)
			{
				sheet.setColumnWidth(i,(short)(35.7*info.getColWidth()));
			}
			String[] rangNames=info.getRangeNames();
			String[] rangCells=info.getRangeCells();
			row=sheet.createRow(0);
			for(int i=0;i<rangCells.length;i++)
			{
				String[] temp=rangCells[i].split(",");
				int firstRow=Integer.parseInt(temp[0]);
				int lastRow=Integer.parseInt(temp[1]);
				int firstCol=Integer.parseInt(temp[2]);
				int lastCol=Integer.parseInt(temp[3]);
				if(firstCol==lastCol)
				{
					Cell cell=row.createCell(firstCol);
					cell.setCellValue(rangNames[i]);
					cell.setCellStyle(setCellStyle(wb));
				}
				else
				{
					Cell cell=row.createCell(firstCol);
					cell.setCellValue(rangNames[i]);
					cell.setCellStyle(setCellStyle(wb));
					
					Cell cell1=row.createCell(lastCol);
					cell1.setCellValue(rangNames[i]);
					cell1.setCellStyle(setCellStyle(wb));
				}
				CellRangeAddress cra=new CellRangeAddress(firstRow,lastRow,firstCol,lastCol);
				sheet.addMergedRegion(cra);	
			}
			//创建第一行
			row=sheet.createRow(1);
			for(int i=0;i<info.getColumnNames().length;i++)
			{
				Cell cell=row.createCell(i);
				cell.setCellValue(info.getColumnNames()[i]);
				cell.setCellStyle(setCellStyle(wb));
			}
			
		   // CellStyle cellStyle = wb.createCellStyle();  
		    //cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00"));
			
			for(int i=0;i<list.size();i++)
			{
				Row rows=sheet.createRow(i+2);
				for(int j=0;j<keys.length;j++)
				{
					Cell cell=rows.createCell(j);
					String value=list.get(i).get(keys[j])==null?"":list.get(i).get(keys[j]).toString();
					if("BigDecimal".equals(cellTypes[j]))
					{
						if(StringUtils.hasText(value))
						{
							cell.setCellValue(new DecimalFormat().parse(value).doubleValue());
						}
						else
						{
							cell.setCellValue(value);
						}
						
					}
					else
					{
						cell.setCellValue(value);
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return wb;
	}
	
	private static CellStyle setCellStyle(SXSSFWorkbook wb)
	{
		CellStyle csTitle=wb.createCellStyle();
		Font fTitle=wb.createFont();
		fTitle.setFontHeightInPoints((short)13);
		fTitle.setColor(IndexedColors.BLACK.getIndex());
		fTitle.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fTitle.setFontName("华文细黑");
		
		csTitle.setFont(fTitle);
		csTitle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		csTitle.setLeftBorderColor(HSSFColor.BLACK.index);
		csTitle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		csTitle.setRightBorderColor(HSSFColor.BLACK.index);
		csTitle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		csTitle.setTopBorderColor(HSSFColor.BLACK.index);
		csTitle.setBorderBottom(HSSFCellStyle.BORDER_MEDIUM);
		csTitle.setBottomBorderColor(HSSFColor.BLACK.index);
		csTitle.setAlignment(CellStyle.ALIGN_CENTER);
		csTitle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		csTitle.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
		csTitle.setFillBackgroundColor(HSSFCellStyle.LEAST_DOTS);
		csTitle.setFillPattern(HSSFCellStyle.LEAST_DOTS);
		csTitle.setFillBackgroundColor(HSSFColor.BLUE_GREY.index);
		return csTitle;
	}
	
	public static void download(String path, HttpServletResponse response) {
		try {
			File file = new File(path);
			String filename = file.getName();
			InputStream fis = new BufferedInputStream(new FileInputStream(path));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			response.reset();

			//response.addHeader("Content-Disposition", "attachment;filename="
			//		+ new String(filename.getBytes(),"iso-8859-1"));
			//response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(
					response.getOutputStream());
			response.setContentType("application/msexcel");
            //设置响应的编码

            //response.setContentType("application/x-download");//下面三行是关键代码，处理乱码问题

            response.setCharacterEncoding("utf-8"); 

            //设置浏览器响应头对应的Content-disposition

            response.setHeader("Content-disposition", "attachment;filename="+new String(filename.getBytes("gbk"), "iso8859-1"));
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
			file.delete();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	//传入下载的文件名
	public static void download_fb(String path,String fileName,HttpServletResponse response) {
		try {
			File file = new File(path);
			//String filename = file.getName();
			InputStream fis = new BufferedInputStream(new FileInputStream(path));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			response.reset();

			//response.addHeader("Content-Disposition", "attachment;filename="
			//		+ new String(filename.getBytes(),"iso-8859-1"));
			//response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(
					response.getOutputStream());
			response.setContentType("application/msexcel");
            //设置响应的编码

            //response.setContentType("application/x-download");//下面三行是关键代码，处理乱码问题

            response.setCharacterEncoding("utf-8"); 

            //设置浏览器响应头对应的Content-disposition

            response.setHeader("Content-disposition", "attachment;filename="+new String(fileName.getBytes("gbk"), "iso8859-1"));
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
