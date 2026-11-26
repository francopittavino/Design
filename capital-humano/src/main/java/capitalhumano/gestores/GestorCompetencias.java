package capitalhumano.gestores;

import capitalhumano.dao.CompetenciaDAO;
import capitalhumano.dao.impl.CompetenciaDAOImpl;
import capitalhumano.dto.CompetenciaDTO;
import capitalhumano.entidades.Competencia;

import java.util.List;

public final class GestorCompetencias {
    private static GestorCompetencias instance;

    private final CompetenciaDAO competenciaDAO = new CompetenciaDAOImpl();

    private GestorCompetencias() {}

    public static synchronized GestorCompetencias getInstance() {
        if (instance == null) instance = new GestorCompetencias();
        return instance;
    }

    /** nombre null o vacío: todas las competencias activas. */
    public List<CompetenciaDTO> buscarCompetencias(String nombre) {
        List<Competencia> lista = (nombre == null || nombre.isBlank())
                ? competenciaDAO.listarTodas() : competenciaDAO.buscarPorNombre(nombre);
        return lista.stream().map(c -> new CompetenciaDTO(c.getId(), c.getCodigo(), c.getNombre())).toList();
    }
}
