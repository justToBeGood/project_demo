package execl;

import org.apache.poi.hssf.usermodel.*;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.wengwx.com.util.ExcelUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ExeclTest
 * @Author wengweixin
 * @Date 2020/11/30 9:56
 **/
public class ExeclTest {
    @Test
    public void createTest(){
        HSSFWorkbook wb = new HSSFWorkbook();
        //建立新HSSFWorkbook对象
        HSSFSheet sheet = wb.createSheet("new sheet");
        //建立新的sheet对象
        // Create a row and put some cells in it. Rows are 0 based.
        HSSFRow row = sheet.createRow((short)0);
        //建立新行
        // Create a cell and put a value in it.
        HSSFCell cell = row.createCell((short)0);
        //建立新cell
        cell.setCellValue(1);
        //设置cell的整数类型的值
        // Or do it on one line.
        row.createCell((short)1).setCellValue(1.2);
        //设置cell浮点类型的值
        row.createCell((short)2).setCellValue("test");
        //设置cell字符类型的值
        row.createCell((short)3).setCellValue(true);
        //设置cell布尔类型的值
        HSSFCellStyle cellStyle = wb.createCellStyle();
        //建立新的cell样式
        //cellStyle.setDataFormat(new HSSFDataFormat().getFormat("m/d/yy h:mm"));
        cellStyle.setDataFormat(wb.createDataFormat().getFormat("m/d/yy h:mm"));
        //设置cell样式为定制的日期格式
        HSSFCell dCell =row.createCell((short)4);
        dCell.setCellValue(new Date());
        //设置cell为日期类型的值
        dCell.setCellStyle(cellStyle);
        //设置该cell日期的显示格式
        HSSFCell csCell =row.createCell((short)5);
        //csCell.setEncoding(HSSFCell.ENCODING_UTF_16);
        //设置cell编码解决中文高位字节截断
        csCell.setCellValue("中文测试_Chinese Words Test");
        //设置中西文结合字符串
        row.createCell((short)6).setCellType(HSSFCell.CELL_TYPE_ERROR);
        //建立错误cell
        // Write the output to a file
        try{
            FileOutputStream fileOut = new FileOutputStream("C:\\Users\\Dreamer\\Desktop\\execl\\workbook.xls");
            wb.write(fileOut);
            fileOut.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void test1(){
        File file=new File("C:\\Users\\Dreamer\\Desktop\\execl\\test.xlsx");
        List<String> titleList= Lists.newArrayList("String","Date","Boolean","Double");
        List<Object> list= Lists.newArrayList("content",new Date(),true,2.1231231);
        List<List<Object>> data=new ArrayList<>();
        data.add(list);
        int pageIndex=2;
        String sheetName="testSheet";
        try{
            ExcelUtils.writeDataToTemplateOutputStream(file,data,pageIndex,sheetName,titleList);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @Test
    public void test2(){
        File file=new File("C:\\Users\\Dreamer\\Desktop\\execl\\test.xlsx");
        try {
            List data=ExcelUtils.readExcel(file);
            System.out.println(data.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void test3(){
        File file=new File("C:\\Users\\Dreamer\\Desktop\\execl\\test.xlsx");
        try {
            Map<String,Object> data=ExcelUtils.readExcelSheet(file,null,0);
            System.out.println(data.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
