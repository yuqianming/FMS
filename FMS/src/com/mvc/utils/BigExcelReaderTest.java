package com.mvc.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BigExcelReaderTest {
    public static void main(String[] args) throws Exception{
    	List<String[]> result=new ArrayList<String[]>();
        String filepath = "D:\\wwc\\成本单20180607154050.xlsx";
        System.out.println("###### start time : "+new Date());
        BigExcelReader reader = new BigExcelReader(filepath) {  
            @Override  
            protected void outputRow(String[] datas, int[] rowTypes, int rowIndex) {  
                // 此处输出每一行的数据  
                //System.out.println(Arrays.toString(datas));
            	result.add(datas);
            }  
        };  
        // 执行解析  
        reader.parse();
        System.out.println("###### end time : "+new Date());
        System.out.println("###### result size : "+result.size());
    }  
}
