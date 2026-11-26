package capitalhumano.facade;

import capitalhumano.entidades.ResultadoImport;

/** Sistema de Gestión de Candidatos y CV: importación del CSV (CU006, fuera del alcance). */
public interface IImportadorCandidatos {
    ResultadoImport importar(String rutaArchivo);
    boolean validarFormato(String rutaArchivo);
}
