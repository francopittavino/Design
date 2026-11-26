package capitalhumano.gestores;

import capitalhumano.dao.impl.RegistroAuditoriaDAOImpl;
import capitalhumano.dao.impl.PreguntaDAOImpl;
import capitalhumano.dao.RegistroAuditoriaDAO;
import capitalhumano.dao.PreguntaDAO;
import capitalhumano.dao.FactorDAO;
import capitalhumano.dao.impl.FactorDAOImpl;
import capitalhumano.dto.FactorDTO;
import capitalhumano.entidades.Factor;

import java.util.List;
import java.util.Map;

public final class GestorFactores {
    private static GestorFactores instance;

    private final FactorDAO factorDAO = new FactorDAOImpl();
    private final PreguntaDAO preguntaDAO = new PreguntaDAOImpl();
    private final RegistroAuditoriaDAO auditoriaDAO = new RegistroAuditoriaDAOImpl();

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

    /** CU017 - Dar de alta factor (fuera del alcance de las Etapas 7 y 8). */
    public FactorDTO crearFactor(FactorDTO datos) {
        throw new UnsupportedOperationException("CU017 - Dar de alta factor: fuera del alcance de las Etapas 7 y 8");
    }

    /** CU018 - Modificar factor (fuera del alcance de las Etapas 7 y 8). */
    public FactorDTO modificarFactor(int id, FactorDTO datos) {
        throw new UnsupportedOperationException("CU018 - Modificar factor: fuera del alcance de las Etapas 7 y 8");
    }

    /** CU019 - Dar de baja factor (fuera del alcance de las Etapas 7 y 8). */
    public void darDeBajaFactor(int id) {
        throw new UnsupportedOperationException("CU019 - Dar de baja factor: fuera del alcance de las Etapas 7 y 8");
    }

    /** CU017 - Validar factor (fuera del alcance de las Etapas 7 y 8). */
    public List<String> validar(FactorDTO f) {
        throw new UnsupportedOperationException("CU017 - Validar factor: fuera del alcance de las Etapas 7 y 8");
    }
}
