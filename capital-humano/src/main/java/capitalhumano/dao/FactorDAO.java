package capitalhumano.dao;

import capitalhumano.entidades.Factor;

import java.util.List;

public interface FactorDAO {
    Factor buscarPorId(int id);
    List<Factor> buscarPorCompetencia(int idComp);
    /** Todos los factores activos (CU020 cuando no se filtra por competencia). */
    List<Factor> listarTodos();

    /** CU017 - Dar de alta factor (fuera del alcance de las Etapas 7 y 8). */
    void guardar(Factor factor);
    /** CU018 - Modificar factor (fuera del alcance de las Etapas 7 y 8). */
    void actualizar(Factor factor);
    /** CU019 - Dar de baja factor (fuera del alcance de las Etapas 7 y 8). */
    void eliminarLogico(int id);
}
