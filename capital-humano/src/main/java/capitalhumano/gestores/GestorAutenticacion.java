package capitalhumano.gestores;

import capitalhumano.dao.CandidatoDAO;
import capitalhumano.dao.CuestionarioDAO;
import capitalhumano.dao.impl.CandidatoDAOImpl;
import capitalhumano.dao.impl.CuestionarioDAOImpl;
import capitalhumano.dto.CandidatoCuestionarioDTO;
import capitalhumano.dto.CuestionarioDTO;
import capitalhumano.entidades.Candidato;
import capitalhumano.entidades.Consultor;
import capitalhumano.entidades.Cuestionario;
import capitalhumano.facade.ServiciosExternosFacade;

/** CU001 - Autenticar usuario (lo mínimo para entrar a los CU de las etapas 7 y 8). */
public final class GestorAutenticacion {
    private static GestorAutenticacion instance;

    private final ServiciosExternosFacade serviciosFacade = new ServiciosExternosFacade();
    private final CandidatoDAO candidatoDAO = new CandidatoDAOImpl();
    private final CuestionarioDAO cuestionarioDAO = new CuestionarioDAOImpl();

    private GestorAutenticacion() {}

    public static synchronized GestorAutenticacion getInstance() {
        if (instance == null) instance = new GestorAutenticacion();
        return instance;
    }

    /** CU001 pasos 4-5: valida usuario y contraseña contra el LDAP. Devuelve null si no son válidos. */
    public Consultor autenticarConsultor(String usuario, String password) {
        return serviciosFacade.autenticarConsultor(usuario, password);
    }

    /**
     * CU001 3.A: existe el candidato con ese tipo y nro de documento, tiene un cuestionario
     * Activo o En Proceso y la clave corresponde a ese cuestionario. Devuelve null si no.
     */
    public CuestionarioDTO autenticarCandidato(String tipoDoc, String nroDoc, String clave) {
        if (tipoDoc == null || nroDoc == null || clave == null) return null;
        Candidato candidato = candidatoDAO.buscarPorDocumento(tipoDoc.trim().toUpperCase(), nroDoc.trim());
        if (candidato == null) return null;
        Cuestionario cuestionario = cuestionarioDAO.buscarPorCandidato(candidato.getId());
        if (cuestionario == null || !cuestionario.getClave().equals(clave.trim())) return null;

        CandidatoCuestionarioDTO c = new CandidatoCuestionarioDTO();
        c.setNroCandidato(candidato.getNroCandidato());
        c.setApellido(candidato.getApellido());
        c.setNombre(candidato.getNombre());
        c.setTipoDoc(candidato.getTipoDoc().name());
        c.setNroDoc(candidato.getNroDoc());
        c.setToken(cuestionario.getClave());

        CuestionarioDTO dto = new CuestionarioDTO();
        dto.setIdCuestionario(cuestionario.getId());
        dto.setEstado(cuestionario.getNombreEstado());
        dto.setPuesto(cuestionario.getEvaluacion().getPuesto().getNombre());
        dto.setCandidato(c);
        return dto;
    }
}
