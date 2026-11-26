package capitalhumano.dao;

import capitalhumano.entidades.Evaluacion;

import java.util.List;

public interface EvaluacionDAO {
    /** Guarda la evaluación con todos sus cuestionarios en la misma transacción. */
    void guardar(Evaluacion eval);
    /** Evaluaciones del puesto con sus cuestionarios (candidato, estado, fechas y resultados). */
    List<Evaluacion> buscarPorPuesto(int idPuesto);
}
