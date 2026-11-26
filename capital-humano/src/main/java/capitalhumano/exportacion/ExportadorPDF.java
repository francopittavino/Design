package capitalhumano.exportacion;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.Color;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/** Exporta a PDF; el PDF se abre con el diálogo de impresión (CU028 paso 9: "envía el listado a la impresora"). */
public class ExportadorPDF implements ExportadorFormato {
    private final OutputStream salida;

    public ExportadorPDF(OutputStream salida) { this.salida = salida; }

    @Override
    public boolean exportar(String titulo, Map<String, List<List<String>>> datos) {
        Document doc = new Document(PageSize.A4.rotate(), 30, 30, 30, 30);
        try {
            PdfWriter writer = PdfWriter.getInstance(doc, salida);
            doc.open();
            writer.addJavaScript(PdfAction.javaScript("this.print({bUI: true, bSilent: false});", writer));

            Font fTitulo = FontFactory.getFont(FontFactory.COURIER_BOLD, 14);
            Font fSeccion = FontFactory.getFont(FontFactory.COURIER_BOLD, 10);
            Font fEnc = FontFactory.getFont(FontFactory.COURIER_BOLD, 8);
            Font fCelda = FontFactory.getFont(FontFactory.COURIER, 8);

            Paragraph p = new Paragraph(titulo, fTitulo);
            p.setAlignment(Element.ALIGN_CENTER);
            p.setSpacingAfter(12);
            doc.add(p);

            for (Map.Entry<String, List<List<String>>> seccion : datos.entrySet()) {
                Paragraph ps = new Paragraph(seccion.getKey(), fSeccion);
                ps.setSpacingBefore(8);
                ps.setSpacingAfter(4);
                doc.add(ps);
                List<List<String>> filas = seccion.getValue();
                if (filas.isEmpty()) continue;
                int columnas = filas.get(0).size();
                PdfPTable tabla = new PdfPTable(columnas);
                tabla.setWidthPercentage(100);
                // La última columna (datos adicionales / descripción) suele ser la más larga
                float[] anchos = new float[columnas];
                java.util.Arrays.fill(anchos, 1f);
                if (columnas > 2) anchos[columnas - 1] = 3f;
                tabla.setWidths(anchos);
                for (int i = 0; i < filas.size(); i++) {
                    for (String valor : filas.get(i)) {
                        PdfPCell celda = new PdfPCell(new Phrase(valor == null ? "" : valor, i == 0 ? fEnc : fCelda));
                        if (i == 0) celda.setBackgroundColor(new Color(225, 225, 225));
                        celda.setPadding(3);
                        tabla.addCell(celda);
                    }
                }
                doc.add(tabla);
            }
            doc.close();
            return true;
        } catch (DocumentException e) {
            return false;
        }
    }
}
