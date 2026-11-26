package capitalhumano.exportacion;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/** Exporta a Excel 97 (.xls), como pide el CU025 (aaaa-mm-dd-hh-mm-NombreFunción.xls). Una hoja por sección. */
public class ExportadorExcel implements ExportadorFormato {
    private final OutputStream salida;

    public ExportadorExcel(OutputStream salida) { this.salida = salida; }

    @Override
    public boolean exportar(String titulo, Map<String, List<List<String>>> datos) {
        try (Workbook wb = new HSSFWorkbook()) {
            CellStyle negrita = wb.createCellStyle();
            var font = wb.createFont();
            font.setBold(true);
            negrita.setFont(font);

            for (Map.Entry<String, List<List<String>>> seccion : datos.entrySet()) {
                Sheet hoja = wb.createSheet(seccion.getKey().length() > 31 ? seccion.getKey().substring(0, 31) : seccion.getKey());
                Row r0 = hoja.createRow(0);
                r0.createCell(0).setCellValue(titulo);
                r0.getCell(0).setCellStyle(negrita);
                List<List<String>> filas = seccion.getValue();
                for (int i = 0; i < filas.size(); i++) {
                    Row r = hoja.createRow(i + 2);
                    List<String> fila = filas.get(i);
                    for (int j = 0; j < fila.size(); j++) {
                        r.createCell(j).setCellValue(fila.get(j) == null ? "" : fila.get(j));
                        if (i == 0) r.getCell(j).setCellStyle(negrita);
                    }
                }
                if (!filas.isEmpty()) {
                    for (int j = 0; j < filas.get(0).size(); j++) hoja.autoSizeColumn(j);
                }
            }
            wb.write(salida);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
