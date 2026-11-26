package capitalhumano.dao;

import capitalhumano.entidades.PonderacionItem;
import capitalhumano.entidades.Pregunta;

import java.util.List;

public interface PreguntaDAO {
    Pregunta buscarPorId(int id);
    /** Preguntas activas del factor, con su factor y competencia (CU020). */
    List<Pregunta> buscarPorFactor(int idFactor);
    /** Persiste la pregunta junto con sus ponderaciones en la misma transacción. */
    void guardar(Pregunta pregunta);
    /** CU021 2.C: preguntas activas de esa competencia y factor (para validar duplicados). */
    /** Preguntas activas que cumplen los criterios (0 / vacío = sin filtro; nombre con criterio "contiene"). */
    List<Pregunta> buscarPorCriterios(int idCompetencia, int idFactor, String nombre);
    List<PonderacionItem> buscarPonderaciones(int idPregunta);
    boolean existePreguntaConOpcion(int idOpcion);

    /** CU022 - Modificar pregunta (fuera del alcance de las Etapas 7 y 8). */
    void actualizar(Pregunta pregunta);
    /** CU023 - Dar de baja pregunta (fuera del alcance de las Etapas 7 y 8). */
    void eliminarLogico(int id);
}
