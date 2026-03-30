
package com.unhacrm;
import org.apache.poi.xssf.usermodel.*;
import java.io.FileOutputStream;
import java.util.List;

public class ExcelExporter{
 public static void exportar(List<Cliente> clientes){
  try{
   XSSFWorkbook wb=new XSSFWorkbook();
   XSSFSheet sheet=wb.createSheet("Clientes");
   int rowNum=0;
   for(Cliente c:clientes){
    XSSFRow row=sheet.createRow(rowNum++);
    row.createCell(0).setCellValue(c.nome);
    row.createCell(1).setCellValue(c.telefone);
    row.createCell(2).setCellValue(c.tipoBase);
    row.createCell(3).setCellValue(c.ultimaCompra);
   }
   FileOutputStream out=new FileOutputStream("clientes.xlsx");
   wb.write(out);
   out.close();
  }catch(Exception e){e.printStackTrace();}
 }
}
