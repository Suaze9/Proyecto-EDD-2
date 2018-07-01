/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication116;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author josue
 */
public class ExcelExport {
    
    HSSFWorkbook fWorkbook;
    HSSFSheet fSheet;
    HSSFCellStyle cellStyle;
    boolean hasHeader;
    int filas;
   
    public ExcelExport(){
        fWorkbook = new HSSFWorkbook();
        fSheet = fWorkbook.createSheet("Base de Datos");
        cellStyle = fWorkbook.createCellStyle();
        hasHeader = false;
        filas = 0;       
    }
    
    public void createExcel(JTable tabla) throws FileNotFoundException, IOException {
       
        TableModel model = tabla.getModel();
        
        if(hasHeader == false){
            filas++;
            hasHeader = true;
            //Get Header
            TableColumnModel tcm = tabla.getColumnModel();
            HSSFRow hRow = fSheet.createRow((short) 0);
            for (int j = 0; j < tcm.getColumnCount(); j++) {
                HSSFCell cell = hRow.createCell((short) j);
                cell.setCellValue(tcm.getColumn(j).getHeaderValue().toString());
                cell.setCellStyle(cellStyle);
            }            
        }

        //Get Other details
        for (int i = 0; i < model.getRowCount(); i++) {
            HSSFRow fRow = fSheet.createRow((short) filas);
            for (int j = 0; j < model.getColumnCount(); j++) {
                HSSFCell cell = fRow.createCell((short) j);
                //System.out.println(model.getValueAt(i, j));
                if (model.getValueAt(i, j) != null) {
                    cell.setCellValue(model.getValueAt(i, j).toString());
                    cell.setCellStyle(cellStyle);
                } else {
                    cell.setCellValue(" ");
                    cell.setCellStyle(cellStyle);
                }
            }
            filas++;
        }
    }
    
    public void saveExcel(String path) throws FileNotFoundException, IOException{
        FileOutputStream fileOutputStream;
        fileOutputStream = new FileOutputStream(path);
        try (BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream)) {
            fWorkbook.write(bos);
        }
        fileOutputStream.close();
        //System.out.println("se supone que ya");
    }
}
