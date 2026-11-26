package capitalhumano.dao;

import capitalhumano.entidades.Competencia;

import java.util.List;

public interface CompetenciaDAO {
    Competencia buscarPorId(int id);
    List<Competencia> listarTodas();
    List<Competencia> buscarPorNombre(String nombre);

    /** CU013 - Dar de alta competencia (fuera del alcance de las Etapas 7 y 8). */
    void guardar(Competencia comp);
    /** CU014 - Modificar competencia (fuera del alcance de las Etapas 7 y 8). */
    void actualizar(Competencia comp);
    /** CU015 - Dar de baja competencia (fuera del alcance de las Etapas 7 y 8). */
    void eliminarLogico(int id);
}
