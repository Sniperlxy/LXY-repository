package com.lxy.crm.util;

import jxl.format.Alignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WriteException;

/**这个ExcelUtil只是来创建Excel表格的第1行(也就是索引为0的那行)
 * @author LiuXiaoYu
 * @date 2020/8/17- 13:04
 */
public class ExcelUtil {

    public static void setLabel(WritableSheet sheet){
        try {
            //设置格式为居中显示
            WritableCellFormat format=new WritableCellFormat();
            format.setAlignment(Alignment.CENTRE);
            // 创建单元格Lable（行，列，文本内容,居中显示）
            Label cell = new Label(0, 0, "员工姓名",format);
            //加入到这个sheet中
            sheet.addCell(cell);
            Label cell1 = new Label(1, 0, "员工电话",format);
            sheet.addCell(cell1);
            Label cell2 = new Label(2, 0, "员工邮箱",format);
            sheet.addCell(cell2);
            Label cell3 = new Label(3, 0, "员工部门",format);
            sheet.addCell(cell3);
            Label cell4 = new Label(4, 0, "入职时间",format);
            sheet.addCell(cell4);
        } catch (WriteException e) {
            System.out.println("标签创建失败");
        }

    }


}
