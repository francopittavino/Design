package capitalhumano.gestores;

import capitalhumano.dao.FactorDAO;
import capitalhumano.dao.impl.FactorDAOImpl;
import capitalhumano.dto.FactorDTO;
import capitalhumano.entidades.Factor;

import java.util.List;
import java.util.Map;

public final class GestorFactores {
    private static GestorFactores instance;

    private final FactorDAO factorDAO = new FactorDAOImpl();

    private GestorFactores() {}

    public static synchronized GestorFactores getInstance() {
        if (instance == null) instance = new GestorFactores();
        return instance;
    }

    /** Filtro "idCompetencia": todos los factores de esa competencia (obs. del CU021). */
    public List<FactorDTO> buscarFactores(Map<String, Integer> filtros) {
        Integer idCompetencia = filtros.get("idCompetencia");
        List<Factor> lista = idCompetencia == null || idCompetencia == 0
                ? factorDAO.listarTodos() : factorDAO.buscarPorCompetencia(idCompetencia);
        return lista.stream().map(f -> new FactorDTO(f.getId(), f.getCodigo(), f.getNombre())).toList();
    }
}
