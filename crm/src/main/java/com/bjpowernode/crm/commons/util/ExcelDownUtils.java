package com.bjpowernode.crm.commons.util;

import com.bjpowernode.crm.workbench.pojo.Activity;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ExcelDownUtils {
    public static void down(HttpServletResponse response, List<Activity> activities) throws IOException {
        String[] attributes={"名称","开始日期","结束日期","成本","描述"};
        HSSFWorkbook wb=new HSSFWorkbook();
        HSSFSheet sheet=wb.createSheet("活动表");
        HSSFRow row = sheet.createRow(0);
        HSSFCell cell=null;
        for(int i=0;i<attributes.length;i++){
            cell = row.createCell(i);
            cell.setCellValue(attributes[i]);
        }
        Activity activity=null;
        for(int i=0;i<activities.size();i++){
            row=sheet.createRow(i+1);
            activity=activities.get(i);
            cell=row.createCell(0);
            cell.setCellValue(activity.getName());
            cell=row.createCell(1);
            cell.setCellValue(activity.getStartDate());
            cell=row.createCell(2);
            cell.setCellValue(activity.getEndDate());
            cell=row.createCell(3);
            cell.setCellValue(activity.getCost());
            cell=row.createCell(4);
            cell.setCellValue(activity.getDescription());
        }
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition","attachment;filename=activityList.xls");
        OutputStream out = response.getOutputStream();
        wb.write(out);
        wb.close();
        out.flush();
    }
    public static String getCellValueForStr(HSSFCell cell) {
        String ret="";
        if(cell.getCellType()==HSSFCell.CELL_TYPE_STRING){
            ret=cell.getStringCellValue();
        }else if(cell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
            ret=cell.getNumericCellValue()+"";
        }else if(cell.getCellType()==HSSFCell.CELL_TYPE_BOOLEAN){
            ret=cell.getBooleanCellValue()+"";
        }else if(cell.getCellType()==HSSFCell.CELL_TYPE_FORMULA){
            ret=cell.getCellFormula();
        }else{
            ret="";
        }
        return ret;
    }
}
