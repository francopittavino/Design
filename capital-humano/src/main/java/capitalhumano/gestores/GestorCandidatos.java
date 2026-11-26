package capitalhumano.gestores;

import capitalhumano.facade.ServiciosExternosFacade;
import capitalhumano.dto.ResultadoImportacionDTO;
import capitalhumano.dao.CandidatoDAO;
import capitalhumano.dao.impl.CandidatoDAOImpl;
import capitalhumano.dto.CandidatoGestionDTO;
import capitalhumano.entidades.Candidato;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class GestorCandidatos {
    private static GestorCandidatos instance;

    private final CandidatoDAO candidatoDAO = new CandidatoDAOImpl();
    private final ServiciosExternosFacade serviciosFacade = new ServiciosExternosFacade();

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
     * CU025 paso 6 / 6.A: devuelve los candidatos que NO pueden ser evaluados porque tienen un
     * cuestionario Activo o En Proceso (lista vacía = se puede seguir). Antes se llamaba
     * evaluarCandidatos, igual que el método de GestorEvaluacion que crea la evaluación
     * (corrección de la Entrega 5: un mismo nombre con dos significados).
     */
    public List<CandidatoGestionDTO> verificarCuestionariosActivos(List<String> nrosCandidatos) {
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

    /** CU006 - Importar candidatos (fuera del alcance de las Etapas 7 y 8). */
    public ResultadoImportacionDTO importarCandidatos(String archivoCsv) {
        throw new UnsupportedOperationException("CU006 - Importar candidatos: fuera del alcance de las Etapas 7 y 8");
    }
}
