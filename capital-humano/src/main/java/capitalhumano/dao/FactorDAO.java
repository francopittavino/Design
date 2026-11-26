package capitalhumano.dao;

import capitalhumano.entidades.Factor;

import java.util.List;

public interface FactorDAO {
    Factor buscarPorId(int id);
    List<Factor> buscarPorCompetencia(int idComp);
    /** Todos los factores activos (CU020 cuando no se filtra por competencia). */
    List<Factor> listarTodos();
}
