package com.mvc.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

public class ReadFileUtil_2 {
	public static final String FILE_SEPARATOR = System.getProperties()
			.getProperty("file.separator");
	public static List<String[]> readFile(HttpSession session,String userId,MultipartFile file) throws Exception {
		//String docsPath = session.getServletContext()
		//		.getRealPath("excel")+FILE_SEPARATOR;
		//String fileName = file.getOriginalFilename().toLowerCase();
		//String filePath=docsPath+userId+"_"+fileName;
		List<String[]> result=new ArrayList<String[]>();
		InputStream is = file.getInputStream();
	    /*int index;  
	    byte[] bytes = new byte[1024];  
	    FileOutputStream downloadFile = new FileOutputStream(filePath);  
	    while ((index = is.read(bytes)) != -1) {  
	        downloadFile.write(bytes, 0, index);  
	        downloadFile.flush();  
	    }  
	    downloadFile.close();  
	    is.close(); */
        BigExcelReader reader = new BigExcelReader(is) {  
            @Override  
            protected void outputRow(String[] datas, int[] rowTypes, int rowIndex) {  
                // 此处输出每一行的数据  
            	result.add(datas);
            }  
        };  
        // 执行解析  
        reader.parse();
        is.close();
        if(result.size()>0)
        {
        	result.remove(0);
        }
		return result;
	}
}
