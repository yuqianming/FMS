package com.mvc.utils;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Test2 {

    public static void main(String[] args) throws Exception{
       /* //File test = new File(".");
        //String file = test.getAbsolutePath()+"/src/main/resources/empty_cell 中文名.xlsx";
    	String file="D://wwc//安装文件//20180731//11111.xlsx";
    	//String file="D://wwc//安装文件//20180731//核算入库表201806.xlsx";
        OPCPackage pkg = OPCPackage.open(file);
        XSSFReader r = new XSSFReader( pkg );
        InputStream in =  r.getSheet("rId1");
        //查看转换的xml原始文件，方便理解后面解析时的处理,
        // 注意：如果打开注释，下面parse()就读不到流的内容了
        Test2.streamOut(in);

        //下面是SST 的索引会用到的
        SharedStringsTable sst = r.getSharedStringsTable();
        //sst.writeTo(System.out);

        XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        List<List<String>> container = new ArrayList<>();
        parser.setContentHandler(new Myhandler(sst,container));

        InputSource inputSource = new InputSource(in);
        parser.parse(inputSource);

        in.close();

        Test2.printContainer(container);*/
    	SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    	System.out.println("--- : "+sdf.format(new Date()));
    	for(int i=0;i<200000;i++)
    	{
    		 String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
    	}
    	System.out.println("--- : "+sdf.format(new Date()));
    }

    public static void printContainer(List<List<String>> container) {
        for(List<String> stringList:container)
        {
            for(String str:stringList)
            {
                //System.out.printf("%15s",str+" | ");
            	System.out.println(str);
            }
            //System.out.println("");
        }
    }

    //读取流，查看文件内容
    public static void streamOut(InputStream in) throws Exception{
        byte[] buf = new byte[1024];
        int len;
        while ((len=in.read(buf))!=-1){
            System.out.write(buf,0,len);
        }
    }


}

class Myhandler extends DefaultHandler{


    //取SST 的索引对应的值
    private SharedStringsTable sst;

    public void setSst(SharedStringsTable sst) {
        this.sst = sst;
    }

    //解析结果保存
    private List<List<String>> container;

    public Myhandler(SharedStringsTable sst, List<List<String>> container) {
        this.sst = sst;
        this.container = container;
    }

    private String lastContents;

    //有效数据矩形区域,A1:Y2
    private String dimension;

    //根据dimension得出每行的数据长度
    private int longest;

    //上个有内容的单元格id，判断空单元格
    private String lastRowid;

    //行数据保存
    private List<String> currentRow;

    //单元格内容是SST 的索引
    private boolean isSSTIndex=false;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
//        System.out.println("startElement:"+qName);

        if (qName.equals("dimension")){
            dimension = attributes.getValue("ref");
            longest = covertRowIdtoInt(dimension.substring(dimension.indexOf(":")+1) );
        }
        //行开始
        if (qName.equals("row")) {
            currentRow = new ArrayList<>();
        }
        if (qName.equals("c")) {
            String rowId = attributes.getValue("r");

            //空单元判断，添加空字符到list
            if (lastRowid!=null)
            {
                int gap = covertRowIdtoInt(rowId)-covertRowIdtoInt(lastRowid);
                for(int i=0;i<gap-1;i++)
                {
                    currentRow.add("");
                }
            }else{
                //第一个单元格可能不是在第一列
                if (!"A1".equals(rowId))
                {
                    for(int i=0;i<covertRowIdtoInt(rowId)-1;i++)
                    {
                        currentRow.add("");
                    }
                }
            }
            lastRowid = rowId;


            //判断单元格的值是SST 的索引，不能直接characters方法取值
            if (attributes.getValue("t")!=null && attributes.getValue("t").equals("s"))
            {
                isSSTIndex = true;
            }else{
                isSSTIndex = false;
            }
        }



    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
//        System.out.println("endElement:"+qName);

        //行结束,存储一行数据
        if (qName.equals("row")) {

            //判断最后一个单元格是否在最后，补齐列数
            if(covertRowIdtoInt(lastRowid)<longest){
                for(int i=0;i<longest- covertRowIdtoInt(lastRowid);i++)
                {
                    currentRow.add("");
                }
            }

            container.add(currentRow);
            lastRowid=null;
        }
        //单元格内容标签结束，characters方法会被调用处理内容
        if (qName.equals("v")) {
            //单元格的值是SST 的索引
            if (isSSTIndex){
                String sstIndex = lastContents.toString();
                try {
                    int idx = Integer.parseInt(sstIndex);
                    XSSFRichTextString rtss = new XSSFRichTextString(
                            sst.getEntryAt(idx));
                    lastContents = rtss.toString();
                    currentRow.add(lastContents);
                } catch (NumberFormatException ex) {
                    System.out.println(lastContents);
                }
            }else {
                currentRow.add(lastContents);
            }

        }

    }


    /**
     * 获取element的文本数据
     */
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        lastContents = new String(ch, start, length);
    }

    /**
     * 列号转数字   AB7-->28 第28列
     * @param rowId
     * @return
     */
    public static int covertRowIdtoInt(String rowId){
        int firstDigit = -1;
        for (int c = 0; c < rowId.length(); ++c) {
            if (Character.isDigit(rowId.charAt(c))) {
                firstDigit = c;
                break;
            }
        }
        //AB7-->AB
        //AB是列号, 7是行号
        String newRowId = rowId.substring(0,firstDigit);
        int num = 0;
        int result = 0;
        int length = newRowId.length();
        for(int i = 0; i < length; i++) {
            //先取最低位，B
            char ch = newRowId.charAt(length - i - 1);
            //B表示的十进制2，ascii码相减，以A的ascii码为基准，A表示1，B表示2
            num = (int)(ch - 'A' + 1) ;
            //列号转换相当于26进制数转10进制
            num *= Math.pow(26, i);
            result += num;
        }
        return result;

    }

    public static void main(String[] args) {
        System.out.println(Myhandler.covertRowIdtoInt("AB7"));

    }
}