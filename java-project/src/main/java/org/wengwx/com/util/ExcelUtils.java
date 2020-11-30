package org.wengwx.com.util;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName ExeclUtils
 * @Author wengweixin
 * @Date 2020/11/30 10:32
 **/
public class ExcelUtils {
    private static final String XLS="xls";
    private static final String XLSX="xlsx";
    private static final DateFormat FORMAT=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    /**
     * 输出数据到自定义模版的Excel输出流
     *
     * @param excelTemplate 自定义模版文件
     * @param data 数据
     * @param outputStream Excel输出流
     * @throws IOException 错误时抛出异常，由调用者处理
     */
    public static void writeDataToTemplateOutputStream(File excelTemplate, List<List<Object>> data, int pageIndex,String sheetName,List<String> tilteList)throws IOException{
        Workbook book=getWorkbookFromExcel(excelTemplate);
        writeDataToWorkbook(tilteList,data,book,pageIndex,sheetName);
        FileOutputStream fileOutputStream = new FileOutputStream(excelTemplate);
        writeWorkbookToOutputStream(book,fileOutputStream);
        fileOutputStream.close();
    }

    /**
     * 从Excel文件获取Workbook对象
     *
     * @param excelFile Excel文件
     * @return Workbook对象
     * @throws IOException 错误时抛出异常，由调用者处理
     */
    public static Workbook getWorkbookFromExcel(File excelFile)throws IOException{
       {
            if(!excelFile.exists()){
                String absolutePath=excelFile.getAbsolutePath();
                int point=absolutePath.indexOf(File.separator);
                String folder=absolutePath.substring(0,point);
                File folderFile=new File(folder);
                if(!folderFile.exists()){
                    folderFile.createNewFile();
                }
                excelFile.createNewFile();
            }
            if(excelFile.getName().endsWith(XLS)){
                return new HSSFWorkbook();
            }else if(excelFile.getName().endsWith(XLSX)){
                return new XSSFWorkbook ();
            }else{
                throw new IOException("文件类型错误");
            }
        }
    }

    /**
     * 输出数据到Workbook对象中指定页码
     *
     * @param titleList 标题，写在第一行，可传null
     * @param data 数据
     * @param book Workbook对象
     * @param pageIndex 输出数据到Workbook指定页码的页面数
     */
    public static void writeDataToWorkbook(List<String> titleList,List<List<Object>> data,Workbook book,int pageIndex,String sheetName){
        Sheet sheet=null;
        Row row=null;
        Cell cell=null;
        try {
            sheet = book.getSheetAt(pageIndex);
            if (sheetName != null && !sheet.equals("")) {
                book.setSheetName(pageIndex, sheetName);
            }
        }catch (IllegalArgumentException e){
            if(sheetName!=null && !sheetName.equals("")){
                sheet=book.createSheet(sheetName);
            }else{
                sheet=book.createSheet("sheet"+pageIndex);
            }

        }


        // 设置表头
        if(null!=titleList && !titleList.isEmpty()){
            row=sheet.getRow(0);
            if(null==row){
                row=sheet.createRow(0);
            }
            for(int i=0;i<titleList.size();i++){
                cell=row.getCell(i);
                if(null==cell){
                    cell=row.createCell(i);
                }
                cell.setCellValue(titleList.get(i));
            }
        }
        List<Object> rowData=null;
        for(int i=0;i<data.size();i++){
            row=sheet.getRow(i+1);
            if(null==row){
                row=sheet.createRow(i+1);
            }
            rowData=data.get(i);
            if(null==rowData){
                continue;
            }
            for(int j=0;j<rowData.size();j++){
                cell=row.getCell(j);
                if(null==cell){
                    cell=row.createCell(j);
                }
                setValue(cell,rowData.get(j));
            }
        }
    }

    /**
     * 把Workbook对象输出到Excel输出流
     *
     * @param book Workbook对象
     * @param outputStream Excel输出流
     * @throws IOException 错误时抛出异常，由调用者处理
     */
    public static void writeWorkbookToOutputStream(Workbook book,OutputStream outputStream)throws IOException{
        book.write(outputStream);

    }


    /**
     * 设置单元格值
     *
     * @param cell 单元格
     * @param value 值
     */
    private static void setValue(Cell cell,Object value){
        if(null==cell){
            return;
        }
        if(null==value){
            cell.setCellValue((String)null);
        }
        else if(value instanceof Boolean){
            cell.setCellValue((Boolean)value);
        }else if(value instanceof Date){
            cell.setCellValue(FORMAT.format((Date)value));
        }else  if(value instanceof Double){
            cell.setCellValue((Double)value);
        }else {
            cell.setCellValue(value.toString());
        }
    }


    /***
     * @Author wengweixin
     * @Date  2020/11/30 14:41
     * @Description     获取输入流的workbook
     * @param file
     * @return org.apache.poi.ss.usermodel.Workbook
     **/
    public static Workbook getWorkbookToRead(File file)throws FileNotFoundException,IOException{
        if(!file.exists()){
            throw new FileNotFoundException();
        }
        FileInputStream fileInputStream = new FileInputStream(file);
        Workbook workbook=null;
        if(file.getName().endsWith(XLSX)){
            workbook=new XSSFWorkbook(fileInputStream);
        }else if(file.getName().endsWith(XLS)){
            workbook=new HSSFWorkbook(fileInputStream);
        }else{
            throw new IOException("文件类型错误");
        }
        return workbook;
    }

    /***
     * @Author wengweixin
     * @Date  2020/11/30 14:42
     * @Description     读取单个sheet内的内容
     * @param file
    	 * @param sheetName
    	 * @param pageIndex
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    public static Map<String,Object> readExcelSheet(File file,String sheetName,int pageIndex)throws FileNotFoundException,IOException{
            Workbook workbook=getWorkbookToRead(file);
            Map<String,Object> map=readSheetOfExcel(workbook,sheetName,pageIndex);
            return map;
        }


    public static Map<String,Object> readSheetOfExcel(Workbook workbook,String sheetName,int pageIndex)throws IllegalArgumentException{
        Sheet sheet=null;
        if(sheetName!=null && !sheetName.equals("")){
            sheet=workbook.getSheet(sheetName);//名字不存在为null
        }
        if(sheet==null){//超过下标会抛出异常
            try{
                sheet=workbook.getSheetAt(pageIndex);
            }catch (IllegalArgumentException e){
                throw e;
            }
        }
        Map<String,Object> map=new HashMap<>();
        List<List<Object>> list=new ArrayList<>();
        for(int i=0;i<=sheet.getLastRowNum();i++){//行的下标是从0开始，getLastRowNum()是最后下标
            Row row=sheet.getRow(i);
            if(null==row){
                map.put("titleList",null);
            }
            List<Object> cloumns=new ArrayList<>();
            for(int j=0;j<row.getLastCellNum();j++){//列的下标是从0开始，getLastCellNum()是所有列的数量
                Cell cell=row.getCell(j);
                cloumns.add(getValue(cell));
            }
            if(i==0){
                map.put("titleList",cloumns);
            }else{
                list.add(cloumns);
            }
        }
        map.put("content",list);
        return map;
    }

    /**
     * 解析单元格中的值
     *
     * @param cell 单元格
     * @return 单元格内的值
     */

    private static Object getValue(Cell cell){
        if(null==cell){
            return null;
        }
        Object value=null;
        switch (cell.getCellTypeEnum()){
            case BOOLEAN:
                value=cell.getBooleanCellValue();
                break;
            case NUMERIC:
                // 日期类型，转换为日期
                if(DateUtil.isCellDateFormatted(cell)){
                    value=cell.getDateCellValue();
                }
                // 日期类型，转换为日期
                else{
                    // 默认返回double，创建BigDecimal返回准确值
                    value=new Double(cell.getNumericCellValue());
                }
                break;
            default:
                value=cell.toString();
                break;
        }
        return value;
    }

    /***
     * @Author wengweixin
     * @Date  2020/11/30 14:40
     * @Description     获取excel所有表信息
     * @param file
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    public static List<Map<String,Object>> readExcel(File file)throws FileNotFoundException,IOException{
        List<Map<String,Object>> dataList=new ArrayList<>();
        Workbook workbook=getWorkbookToRead(file);
        int sheetNum=workbook.getNumberOfSheets();
        for(int i=0;i<sheetNum;i++){
            String sheetName=workbook.getSheetAt(i).getSheetName();
            Map<String,Object> map=readSheetOfExcel(workbook,sheetName,i);
            dataList.add(map);
        }
        return dataList;
    }
}
