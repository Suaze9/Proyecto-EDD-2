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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jespxml.JespXML;
import org.jespxml.modelo.Encoding;
import org.jespxml.modelo.Tag;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author josue
 */
public class XMLExport {

    Tag raiz;

    public XMLExport() {
        raiz = new Tag("Archivo");
    }

    public void createXML(JTable tabla) {
        try {
            TableModel model = tabla.getModel();
            TableColumnModel tcm = tabla.getColumnModel();

            for (int i = 0; i < model.getRowCount(); i++) {
                Tag registro = new Tag("Registro");
                for (int j = 0; j < model.getColumnCount(); j++) {
                    String col = tcm.getColumn(j).getHeaderValue().toString();
                    col = col.replace(' ', '_');
                    Tag columna = new Tag(col);
                    String info = "";
                    if (model.getValueAt(i, j) != null) {
                        info = model.getValueAt(i, j).toString();
                    } else {
                        info = "NoDisponible";
                    }
                    info = info.replace(' ', '_');
                    columna.addContenido(info);
                    registro.addTagHijo(columna);
                }
                raiz.addTagHijo(registro);
            }

        } catch (Exception ex) {
            //Logger.getLogger(XMLExport.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveXML(String path) throws FileNotFoundException, ParserConfigurationException, TransformerException {
        JespXML saver = new JespXML(new File(path), Encoding.UTF_8);
        saver.escribirXML(raiz);
        //System.out.println("Se supone que ya :v");

    }

}
