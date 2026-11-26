package capitalhumano.facade;

import capitalhumano.dto.CandidatoCuestionarioDTO;
import capitalhumano.exportacion.ExportadorExcel;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Genera los archivos que el consultor descarga (reutiliza los exportadores del Bridge). */
public class ExportadorResultadosArchivo implements IExportadorResultados {

    @Override
    public boolean exportarExcel(String titulo, List<CandidatoCuestionarioDTO> candidatos, OutputStream salida) {
        List<List<String>> filas = new ArrayList<>();
        filas.add(List.of("Apellido", "Nombre", "Tipo documento", "Nro documento", "Clave de ingreso"));
        for (CandidatoCuestionarioDTO c : candidatos) {
            filas.add(List.of(c.getApellido(), c.getNombre(), c.getTipoDoc(), c.getNroDoc(), c.getToken()));
        }
        Map<String, List<List<String>>> datos = new LinkedHashMap<>();
        datos.put("Candidatos", filas);
        return new ExportadorExcel(salida).exportar(titulo, datos);
    }
}
