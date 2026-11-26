package capitalhumano.reportes;

import capitalhumano.entidades.Candidato;
import capitalhumano.entidades.Cuestionario;
import capitalhumano.entidades.Puesto;

import java.util.Map;

/** CU027 - Emitir reporte comparativo (fuera del alcance de las Etapas 7 y 8). Lado abstracción del Bridge. */
public class ReporteComparativo extends Reporte {
    private final Candidato candidato;
    private final Puesto puesto;
    private final Cuestionario cuestionario;

    public ReporteComparativo(Candidato candidato, Puesto puesto, Cuestionario cuestionario) {
        this.candidato = candidato;
        this.puesto = puesto;
        this.cuestionario = cuestionario;
    }

    @Override
    public void generar() {
        throw new UnsupportedOperationException("CU027 - Emitir reporte comparativo: fuera del alcance de las Etapas 7 y 8");
    }

    /** Puntaje requerido vs obtenido por competencia, para el gráfico del reporte. */
    public Map<String, double[]> getDatosGrafico() {
        throw new UnsupportedOperationException("CU027 - Emitir reporte comparativo: fuera del alcance de las Etapas 7 y 8");
    }

    public boolean imprimir() {
        throw new UnsupportedOperationException("CU027 - Emitir reporte comparativo: fuera del alcance de las Etapas 7 y 8");
    }

    public Candidato getCandidato() { return candidato; }
    public Puesto getPuesto() { return puesto; }
    public Cuestionario getCuestionario() { return cuestionario; }
}
