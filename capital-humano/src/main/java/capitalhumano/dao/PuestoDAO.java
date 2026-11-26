package capitalhumano.dao;

import capitalhumano.entidades.CaracteristicaPuesto;
import capitalhumano.entidades.Puesto;

import java.util.List;
import java.util.Map;

public interface PuestoDAO {
    /** Puesto con su empresa y sus características (competencias con factores y preguntas). */
    Puesto buscarPorId(int id);
    /** Filtros "codigo", "nombre", "empresa" con criterio "contiene". */
    List<Puesto> buscarPorFiltros(Map<String, String> filtros);
    List<CaracteristicaPuesto> buscarCaracteristicas(int idPuesto);
    boolean existePuestoConCompetencia(int idCompetencia);
    /** CU028 paso 4: cantidad de candidatos evaluados en el puesto. */
    int contarCandidatos(int idPuesto);
    /** CU028 paso 4: cantidad de cuestionarios en estado "Completo" del puesto. */
    int contarEvaluacionesCompletas(int idPuesto);
}
