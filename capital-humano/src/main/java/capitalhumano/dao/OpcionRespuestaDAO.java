package capitalhumano.dao;

import capitalhumano.entidades.OpcionDeRespuesta;

import java.util.List;

public interface OpcionRespuestaDAO {
    /** Opción de respuesta con sus ítems ordenados por orden. */
    OpcionDeRespuesta buscarPorId(int id);
    List<OpcionDeRespuesta> listarTodas();
    List<OpcionDeRespuesta> buscarPorNombre(String nombre);

    /** CU029 - Dar de alta opción de respuesta (fuera del alcance de las Etapas 7 y 8). */
    void guardar(OpcionDeRespuesta opcion);
    /** CU030 - Modificar opción de respuesta (fuera del alcance de las Etapas 7 y 8). */
    void actualizar(OpcionDeRespuesta opcion);
    /** CU031 - Dar de baja opción de respuesta (fuera del alcance de las Etapas 7 y 8). */
    void eliminarLogico(int id);
}
