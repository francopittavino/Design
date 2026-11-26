package capitalhumano.facade;

import capitalhumano.entidades.ResultadoImport;

/** El CU006 (Importar candidatos) no forma parte de las Etapas 7 y 8. */
public class ImportadorCandidatosNoImplementado implements IImportadorCandidatos {
    @Override
    public ResultadoImport importar(String rutaArchivo) {
        throw new UnsupportedOperationException("CU006 - Importar candidatos: fuera del alcance de las Etapas 7 y 8");
    }

    @Override
    public boolean validarFormato(String rutaArchivo) {
        throw new UnsupportedOperationException("CU006 - Importar candidatos: fuera del alcance de las Etapas 7 y 8");
    }
}
