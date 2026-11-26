package capitalhumano.dao;

import capitalhumano.entidades.Competencia;

import java.util.List;

public interface CompetenciaDAO {
    Competencia buscarPorId(int id);
    List<Competencia> listarTodas();
    List<Competencia> buscarPorNombre(String nombre);
}
