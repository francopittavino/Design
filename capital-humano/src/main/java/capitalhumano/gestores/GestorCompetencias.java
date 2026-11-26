package capitalhumano.gestores;

import capitalhumano.dao.impl.RegistroAuditoriaDAOImpl;
import capitalhumano.dao.impl.PuestoDAOImpl;
import capitalhumano.dao.RegistroAuditoriaDAO;
import capitalhumano.dao.PuestoDAO;
import capitalhumano.dao.CompetenciaDAO;
import capitalhumano.dao.impl.CompetenciaDAOImpl;
import capitalhumano.dto.CompetenciaDTO;
import capitalhumano.entidades.Competencia;

import java.util.List;

public final class GestorCompetencias {
    private static GestorCompetencias instance;

    private final CompetenciaDAO competenciaDAO = new CompetenciaDAOImpl();
    private final PuestoDAO puestoDAO = new PuestoDAOImpl();
    private final RegistroAuditoriaDAO auditoriaDAO = new RegistroAuditoriaDAOImpl();

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

    /** CU013 - Dar de alta competencia (fuera del alcance de las Etapas 7 y 8). */
    public CompetenciaDTO crearCompetencia(CompetenciaDTO datos) {
        throw new UnsupportedOperationException("CU013 - Dar de alta competencia: fuera del alcance de las Etapas 7 y 8");
    }

    /** CU014 - Modificar competencia (fuera del alcance de las Etapas 7 y 8). */
    public CompetenciaDTO modificarCompetencia(int id, CompetenciaDTO datos) {
        throw new UnsupportedOperationException("CU014 - Modificar competencia: fuera del alcance de las Etapas 7 y 8");
    }

    /** CU015 - Dar de baja competencia (fuera del alcance de las Etapas 7 y 8). */
    public void darDeBajaCompetencia(int id) {
        throw new UnsupportedOperationException("CU015 - Dar de baja competencia: fuera del alcance de las Etapas 7 y 8");
    }

    /** CU013 - Validar competencia (fuera del alcance de las Etapas 7 y 8). */
    public List<String> validar(CompetenciaDTO c) {
        throw new UnsupportedOperationException("CU013 - Validar competencia: fuera del alcance de las Etapas 7 y 8");
    }
}
