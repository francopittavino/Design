package capitalhumano.facade;

import capitalhumano.dto.CandidatoCuestionarioDTO;
import capitalhumano.entidades.Competencia;
import capitalhumano.entidades.Consultor;
import capitalhumano.entidades.Factor;
import capitalhumano.entidades.Pregunta;
import capitalhumano.entidades.ResultadoImport;

import java.io.OutputStream;
import java.util.List;

/** Patrón Facade: punto único de acceso a los subsistemas externos (LDAP, IA, archivos). */
public class ServiciosExternosFacade {
    private final IAutenticacionLDAP authLDAP;
    private final IServicioIA servicioIA;
    private final IImportadorCandidatos importador;
    private final IExportadorResultados exportador;

    public ServiciosExternosFacade() {
        this(new AutenticacionLDAPSimulada(), new ServicioIANoDisponible(),
                new ImportadorCandidatosNoImplementado(), new ExportadorResultadosArchivo());
    }

    public ServiciosExternosFacade(IAutenticacionLDAP authLDAP, IServicioIA servicioIA,
                                   IImportadorCandidatos importador, IExportadorResultados exportador) {
        this.authLDAP = authLDAP;
        this.servicioIA = servicioIA;
        this.importador = importador;
        this.exportador = exportador;
    }

    public Consultor autenticarConsultor(String usr, String pass) {
        return authLDAP.autenticar(usr, pass);
    }

    public Pregunta generarPreguntaIA(Competencia comp, Factor factor) {
        return servicioIA.generarPregunta(comp, factor);
    }

    /** CU006 - Importar candidatos (fuera del alcance de las Etapas 7 y 8). */
    public ResultadoImport importarCandidatos(String ruta) {
        return importador.importar(ruta);
    }

    /** CU025: exporta la lista de candidatos con sus claves (formato "EXCEL"). */
    public boolean exportarResultados(String titulo, String formato, List<CandidatoCuestionarioDTO> candidatos, OutputStream salida) {
        if (!"EXCEL".equals(formato)) throw new IllegalArgumentException("Formato no soportado: " + formato);
        return exportador.exportarExcel(titulo, candidatos, salida);
    }
}
