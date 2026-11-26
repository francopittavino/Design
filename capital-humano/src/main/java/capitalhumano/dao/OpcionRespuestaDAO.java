package capitalhumano.dao;

import capitalhumano.entidades.OpcionDeRespuesta;

import java.util.List;

public interface OpcionRespuestaDAO {
    /** Opción de respuesta con sus ítems ordenados por orden. */
    OpcionDeRespuesta buscarPorId(int id);
    List<OpcionDeRespuesta> listarTodas();
    List<OpcionDeRespuesta> buscarPorNombre(String nombre);
}
