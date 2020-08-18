import com.lxy.crm.domain.Employee;
import com.lxy.crm.service.IEmployeeService;
import com.lxy.crm.util.ExcelUtil;
import com.sun.corba.se.spi.ior.Writeable;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.CellFormat;
import jxl.write.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * @author LiuXiaoYu
 * @date 2020/8/17- 11:57
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring/applicationContext.xml")
public class ExcelTest {

    @Autowired
    private IEmployeeService employeeService;

    @Test
    public void outTest(){
            try {
                //创建excel文件
                WritableWorkbook wb = Workbook.createWorkbook(new File("employee.xls"));
                // 创建sheet
                WritableSheet sheet = wb.createSheet("employee", 0);
                //设置行高
                sheet.setRowView(0, 300);
                for (int i=0;i<6;i++){
                    // 设置列宽
                    sheet.setColumnView(i, 25);
                }
                ExcelUtil.setLabel(sheet);

                // 将内容写入Excel文件
                wb.write();
                // 关闭
                wb.close();
            } catch (Exception e) {
                System.out.println("文件创建失败");
            }
    }

    @Test
    public void testRead() throws Exception {
        // 读取文件
        Workbook workbook = Workbook.getWorkbook(new File("read.xls"));
        // 拿到sheet
        Sheet sheet = workbook.getSheet(0);
        // 计算sheet中的行数、列数
        int rows = sheet.getRows();
        int columns = sheet.getColumns();
        for (int i = 0; i < rows; i++) {
            for (int k = 0; k < columns; k++) {
                Cell cell = sheet.getCell(k, i);
                System.out.println(cell.getContents() + "\t");
            }
        }

        workbook.close();
    }

    @Test
    public void Test(){
        //查询所有的在职员工
        List<Employee> employees=employeeService.selectAllForExcel();
        try {
            //创建excel文件
            WritableWorkbook wb = Workbook.createWorkbook(new File("employee.xls"));
            // 创建sheet
            WritableSheet sheet = wb.createSheet("employee", 0);
            //设置行高
            for (int x=0;x<employees.size()+1;x++){
                sheet.setRowView(0, 300);
            }
            for (int i=0;i<5;i++){
                // 设置列宽
                sheet.setColumnView(i, 25);
            }
            //设置表头标签
            ExcelUtil.setLabel(sheet);
            //
            Label cell;
            //设置格式为居中显示
            WritableCellFormat format=new WritableCellFormat();
            format.setAlignment(Alignment.CENTRE);
            for (int i=0;i<employees.size();i++){
                    Employee employee = employees.get(i);
                    cell=new Label(0,i+1,employee.getRealname(),format);
                    sheet.addCell(cell);
                    cell=new Label(1,i+1,employee.getTel(),format);
                    sheet.addCell(cell);
                    cell=new Label(2,i+1,employee.getEmail(),format);
                    sheet.addCell(cell);
                    cell=new Label(3,i+1,employee.getDept().getName(),format);
                    sheet.addCell(cell);

                    //将数据库的时间转换为字符串
                Date date = employee.getInputtime();
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
                String inputtime = simpleDateFormat.format(date);
                cell=new Label(4,i+1,inputtime,format);
                    sheet.addCell(cell);
            }

            // 将内容写入Excel文件
            wb.write();
            // 关闭
            wb.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 把当前时间格式为指定格式
     */
    @Test
    public void test5(){
        //获得当前时间
        LocalDateTime ldt = LocalDateTime.now();
        System.out.println(ldt);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss");
        String format = ldt.format(dtf);
        System.out.println(format);
    }

    /**
     * 把指定字符串格式化为日期
     */
    @Test
    public void test6(){
        String str1="2018-07-05 12:24:12";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parse = LocalDateTime.parse(str1, dtf);
        System.out.println(parse);
    }

}
