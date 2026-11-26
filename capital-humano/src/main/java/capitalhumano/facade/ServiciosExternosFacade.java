package capitalhumano.facade;

import capitalhumano.dto.CandidatoCuestionarioDTO;
import capitalhumano.entidades.Competencia;
import capitalhumano.entidades.Consultor;
import capitalhumano.entidades.Factor;
import capitalhumano.entidades.Pregunta;

import java.io.OutputStream;
import java.util.List;

/** Patrón Facade: punto único de acceso a los subsistemas externos (LDAP, IA, archivos). */
public class ServiciosExternosFacade {
    private final IAutenticacionLDAP authLDAP;
    private final IServicioIA servicioIA;
    private final IExportadorResultados exportador;

    public ServiciosExternosFacade() {
        this(new AutenticacionLDAPSimulada(), new ServicioIANoDisponible(), new ExportadorResultadosArchivo());
    }

    public ServiciosExternosFacade(IAutenticacionLDAP authLDAP, IServicioIA servicioIA, IExportadorResultados exportador) {
        this.authLDAP = authLDAP;
        this.servicioIA = servicioIA;
        this.exportador = exportador;
    }

    public Consultor autenticarConsultor(String usr, String pass) {
        return authLDAP.autenticar(usr, pass);
    }

    public Pregunta generarPreguntaIA(Competencia comp, Factor factor) {
        return servicioIA.generarPregunta(comp, factor);
    }

    /** CU025: exporta la lista de candidatos con sus claves (formato "EXCEL"). */
    public boolean exportarResultados(String titulo, String formato, List<CandidatoCuestionarioDTO> candidatos, OutputStream salida) {
        if (!"EXCEL".equals(formato)) throw new IllegalArgumentException("Formato no soportado: " + formato);
        return exportador.exportarExcel(titulo, candidatos, salida);
    }
}
