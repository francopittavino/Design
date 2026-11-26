package capitalhumano.dao;

import capitalhumano.entidades.Cuestionario;

public interface CuestionarioDAO {
    /** Cuestionario "Activo" o "En Proceso" del candidato (null si no tiene). */
    Cuestionario buscarPorCandidato(int idCandidato);
    /** Cuestionario completo: evaluación, puesto, preguntas, respuestas y resultados. */
    Cuestionario buscarPorId(int id);
    /** Actualiza el cuestionario e inserta las preguntas, respuestas y resultados nuevos. */
    void guardar(Cuestionario cuestionario);
    void actualizarEstado(int id, String nombreEstado);
}
