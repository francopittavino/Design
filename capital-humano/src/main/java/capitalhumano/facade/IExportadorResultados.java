package capitalhumano.facade;

import capitalhumano.dto.CandidatoCuestionarioDTO;
import capitalhumano.entidades.Puesto;

import java.io.OutputStream;
import java.util.List;

public interface IExportadorResultados {
    /** CU007 - Exportar resultados de un puesto en CSV (fuera del alcance de las Etapas 7 y 8). */
    boolean exportarCSV(Puesto puesto, OutputStream salida);
    /** CU025: lista de candidatos con sus claves en Excel. En la web la "ruta" es la descarga del navegador. */
    boolean exportarExcel(String titulo, List<CandidatoCuestionarioDTO> candidatos, OutputStream salida);
}
