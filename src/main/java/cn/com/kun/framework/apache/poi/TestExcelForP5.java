package cn.com.kun.framework.apache.poi;

import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;


public class TestExcelForP5 {

    /**
     * POI解析Excel文件内容
     * @author David
     */
    public static void main(String[] args) {

        //需要解析的Excel文件
        File file = new File("D:\\home\\p5\\p5.xlsx");
        try {
            //创建Excel工作簿
            XSSFWorkbook workbook = new XSSFWorkbook(FileUtils.openInputStream(file));

            //输出Excel版本
            String spreadsheetVersion = workbook.getSpreadsheetVersion().name();
            System.out.println(spreadsheetVersion);

            //遍历所有sheet页
            for(int i = 0; i < workbook.getNumberOfSheets(); i++){

                XSSFSheet sheet = workbook.getSheetAt(i);
                if (i > 0){
                    //考试只在第一页
                    break;
                }
                System.out.println("开始处理第" + i + "页");
                dealSheet(sheet);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public static void dealSheet(XSSFSheet sheet){

        int indexOfZqda = 0;
        int indexOfTx = 0;
        int indexOfTg = 0;
        int indexOfXxa = 0;
        int indexOfXxb = 0;
        int indexOfXxc = 0;
        int indexOfXxd = 0;
        int indexOfXxe = 0;
        int indexOfXxf = 0;

        int firstRowNum = 0;
        //获取sheet中最后一行行号
        int lastRowNum = sheet.getLastRowNum();

        int count = 0;
        int errorCount = 0;

        for (int i = firstRowNum; i <= lastRowNum; i++) {

            if (i < 2){
                //前面两行不是题目

                //正确答案	题型	题干	选项A	选项B	选项C	选项D	选项E	选项F
                if (i == 1) {
                    XSSFRow row = sheet.getRow(i);
                    //获取当前行最后单元格列号
                    int lastCellNum = row.getLastCellNum();
                    for (int j = 0; j < lastCellNum; j++) {
                        XSSFCell cell = row.getCell(j);
                        if (cell != null) {
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            String value = cell.getStringCellValue();

                            if (i == 1) {
                                //第二行，解析下标
                                if (value.equals("正确答案")) {
                                    indexOfZqda = j;
                                }
                                if (value.equals("题型")) {
                                    indexOfTx = j;
                                }
                                if (value.equals("题干")) {
                                    indexOfTg = j;
                                }
                                if (value.equals("选项A")) {
                                    indexOfXxa = j;
                                }
                                if (value.equals("选项B")) {
                                    indexOfXxb = j;
                                }
                                if (value.equals("选项C")) {
                                    indexOfXxc = j;
                                }
                                if (value.equals("选项D")) {
                                    indexOfXxd = j;
                                }
                                if (value.equals("选项E")) {
                                    indexOfXxe = j;
                                }
                                if (value.equals("选项F")) {
                                    indexOfXxf = j;
                                }
                            }

                        }
                    }
                }

                continue;
            }

            count++;

            //开始改卷
            //获取正确答案
            String indexOfZqdaSrting = null;
            String indexOfTxSrting = null;
            String indexOfTgSrting = null;
            String indexOfXxaSrting = null;
            String indexOfXxbSrting = null;
            String indexOfXxcSrting = null;
            String indexOfXxdSrting = null;
            String indexOfXxeSrting = null;
            String indexOfXxfSrting = null;
            XSSFRow row = sheet.getRow(i);
            StringBuilder userChoose = new StringBuilder();
            //获取当前行最后单元格列号
            int lastCellNum = row.getLastCellNum();
            for (int j = 0; j < lastCellNum; j++) {
                XSSFCell cell = row.getCell(j);
                if (cell != null){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String value = cell.getStringCellValue();
                    if (j == indexOfZqda){
                        indexOfZqdaSrting = value;
                    }
                    if (j == indexOfTx){
                        indexOfTxSrting = value;
                    }
                    if (j == indexOfTg){
                        indexOfTgSrting = value;
                    }
                    if (j == indexOfXxa){
                        indexOfXxaSrting = value;
                    }
                    if (j == indexOfXxb){
                        indexOfXxbSrting = value;
                    }
                    if (j == indexOfXxc){
                        indexOfXxcSrting = value;
                    }
                    if (j == indexOfXxd){
                        indexOfXxdSrting = value;
                    }
                    if (j == indexOfXxe){
                        indexOfXxeSrting = value;
                    }
                    if (j == indexOfXxf){
                        indexOfXxfSrting = value;
                    }
                    XSSFCellStyle xssfCellStyle = cell.getCellStyle();
                    //获取用户所选择的答案
                    //判断当前格的颜色
                    if (isChoose(getBackgroudColor(xssfCellStyle))){
                        //说明选择了
                        if (j == indexOfXxa){
                            userChoose.append("A");
                        }
                        if (j == indexOfXxb){
                            userChoose.append("B");
                        }
                        if (j == indexOfXxc){
                            userChoose.append("C");
                        }
                        if (j == indexOfXxd){
                            userChoose.append("D");
                        }
                        if (j == indexOfXxe){
                            userChoose.append("E");
                        }
                        if (j == indexOfXxf){
                            userChoose.append("F");
                        }
                    }


//                    cell.getCellTypeEnum();
//                    System.out.print(value + "  ");
                }else {
//                    System.out.print("nan" + "  ");
                }
            }

            //遍历完所有列，开始比对 正确答案和 用户所选答案
            String userChooseString = userChoose.toString();
            if (!indexOfZqdaSrting.trim().equals(userChooseString.trim())){
                errorCount++;
                System.out.println(String.format("出现错题，正确答案:%s 用户选择：%s 题干:%s 选项A:%s 选项B:%s 选项C:%s 选项D:%s 选项E:%s 选项F:%s ",
                        indexOfZqdaSrting, userChooseString, indexOfTgSrting,
                        indexOfXxaSrting, indexOfXxbSrting, indexOfXxcSrting, indexOfXxdSrting, indexOfXxeSrting, indexOfXxfSrting));
            }

//            if (i > 10){
//                break;//调试
//            }
        }

        java.text.DecimalFormat df = new java.text.DecimalFormat("##.##%");//传入格式模板
        String result2 = df.format((float)count-errorCount/(float)count);
        System.out.println(String.format("总题数：%s 错题数：%s 命中率：%s", count, errorCount, getPercent(count-errorCount, count)));
    }


    /**
     * 等于黄色 表示选中
     * @param backgroudColor
     * @return
     */
    private static boolean isChoose(String backgroudColor) {
        return "#FFFF00".equals(backgroudColor);
    }

    private static String getBackgroudColor(XSSFCellStyle xssfCellStyle) {

        String res = "";
        XSSFColor xssfColor = xssfCellStyle.getFillForegroundColorColor();
        byte[] bytes;
        if (xssfColor != null) {
            bytes = xssfColor.getRGB();
            res = String.format("#%02X%02X%02X", bytes[0], bytes[1], bytes[2]);
        }

        return res;
    }

    /**
     * 方式一：使用java.text.NumberFormat实现
     * @param x
     * @param y
     * @return
     */
    public static String getPercent(int x, int y) {
        double d1 = x * 1.0;
        double d2 = y * 1.0;
        NumberFormat percentInstance = NumberFormat.getPercentInstance();
        // 设置保留几位小数，这里设置的是保留两位小数
        percentInstance.setMinimumFractionDigits(2);
        return percentInstance.format(d1 / d2);
    }
}