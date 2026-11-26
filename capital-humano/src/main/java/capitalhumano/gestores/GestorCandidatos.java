package capitalhumano.gestores;

import capitalhumano.dao.CandidatoDAO;
import capitalhumano.dao.impl.CandidatoDAOImpl;
import capitalhumano.dto.CandidatoGestionDTO;
import capitalhumano.dto.PuestoDTO;
import capitalhumano.entidades.Candidato;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class GestorCandidatos {
    private static GestorCandidatos instance;

    private final CandidatoDAO candidatoDAO = new CandidatoDAOImpl();

    private GestorCandidatos() {}

    public static synchronized GestorCandidatos getInstance() {
        if (instance == null) instance = new GestorCandidatos();
        return instance;
    }

    /** CU025 paso 3: filtros "apellido", "nombre", "nroCandidato" (criterio "contiene"). */
    public List<CandidatoGestionDTO> buscarCandidatos(Map<String, String> filtros) {
        return candidatoDAO.buscarPorCriterios(filtros.get("apellido"), filtros.get("nombre"), filtros.get("nroCandidato"))
                .stream().map(GestorCandidatos::aDTO).toList();
    }

    /**
     * CU025 paso 6 / 6.A: verifica los candidatos a evaluar. Devuelve los que NO pueden ser evaluados
     * porque tienen un cuestionario Activo o En Proceso (lista vacía = se puede seguir).
     * El puesto todavía no se eligió, por eso llega null.
     */
    public List<CandidatoGestionDTO> evaluarCandidatos(List<String> nrosCandidatos, PuestoDTO puesto) {
        List<CandidatoGestionDTO> conCuestionarioActivo = new ArrayList<>();
        for (String nro : nrosCandidatos) {
            Candidato candidato = candidatoDAO.buscarPorNroCandidato(nro);
            if (candidato != null && candidato.tieneCuestionarioActivo()) conCuestionarioActivo.add(aDTO(candidato));
        }
        return conCuestionarioActivo;
    }

    static CandidatoGestionDTO aDTO(Candidato c) {
        CandidatoGestionDTO dto = new CandidatoGestionDTO();
        dto.setNroCandidato(c.getNroCandidato());
        dto.setApellido(c.getApellido());
        dto.setNombre(c.getNombre());
        dto.setTipoDoc(c.getTipoDoc().name());
        dto.setNroDoc(c.getNroDoc());
        dto.setEmail(c.getEmail());
        dto.setEscolaridad(c.getEscolaridad());
        return dto;
    }
}
