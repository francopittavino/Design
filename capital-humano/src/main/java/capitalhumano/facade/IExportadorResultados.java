package capitalhumano.facade;

import capitalhumano.dto.CandidatoCuestionarioDTO;

import java.io.OutputStream;
import java.util.List;

public interface IExportadorResultados {
    /** CU025: lista de candidatos con sus claves en Excel. En la web la "ruta" es la descarga del navegador. */
    boolean exportarExcel(String titulo, List<CandidatoCuestionarioDTO> candidatos, OutputStream salida);
}
