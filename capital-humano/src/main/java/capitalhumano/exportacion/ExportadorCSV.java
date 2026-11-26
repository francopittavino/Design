package capitalhumano.exportacion;

import java.io.OutputStreamWriter;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Exporta a CSV separado por ';' (formato del Sistema de Gestión de Candidatos, CU007). */
public class ExportadorCSV implements ExportadorFormato {
    private final OutputStream salida;

    public ExportadorCSV(OutputStream salida) { this.salida = salida; }

    @Override
    public boolean exportar(String titulo, Map<String, List<List<String>>> datos) {
        PrintWriter out = new PrintWriter(new OutputStreamWriter(salida, StandardCharsets.UTF_8));
        for (List<List<String>> filas : datos.values()) {
            for (List<String> fila : filas) {
                out.println(fila.stream().map(ExportadorCSV::escapar).collect(Collectors.joining(";")));
            }
        }
        out.flush();
        return !out.checkError();
    }

    private static String escapar(String v) {
        if (v == null) return "";
        return v.contains(";") || v.contains("\"") ? "\"" + v.replace("\"", "\"\"") + "\"" : v;
    }
}
