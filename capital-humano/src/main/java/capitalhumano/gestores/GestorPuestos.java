package capitalhumano.gestores;

import capitalhumano.dao.PuestoDAO;
import capitalhumano.dao.impl.PuestoDAOImpl;
import capitalhumano.dto.CaracteristicaPuestoDTO;
import capitalhumano.dto.PuestoDTO;
import capitalhumano.entidades.CaracteristicaPuesto;
import capitalhumano.entidades.Puesto;

import java.util.List;
import java.util.Map;

public final class GestorPuestos {
    private static GestorPuestos instance;

    private final PuestoDAO puestoDAO = new PuestoDAOImpl();

    private GestorPuestos() {}

    public static synchronized GestorPuestos getInstance() {
        if (instance == null) instance = new GestorPuestos();
        return instance;
    }

    /** Filtros "codigo", "nombre", "empresa" (criterio "contiene"). Incluye candidatos y evaluaciones completas (CU028). */
    public List<PuestoDTO> buscarPuestos(Map<String, String> filtros) {
        return puestoDAO.buscarPorFiltros(filtros).stream().map(p -> {
            PuestoDTO dto = aDTO(p);
            dto.setCantCandidatos(puestoDAO.contarCandidatos(p.getId()));
            dto.setCantEvaluacionesCompletas(puestoDAO.contarEvaluacionesCompletas(p.getId()));
            return dto;
        }).toList();
    }

    /** CU025 paso 9: el puesto con sus competencias y la ponderación requerida en cada una. */
    public PuestoDTO obtenerDetalle(int idPuesto) {
        Puesto p = puestoDAO.buscarPorId(idPuesto);
        if (p == null) return null;
        PuestoDTO dto = aDTO(p);
        for (CaracteristicaPuesto cp : p.getCaracteristicas()) {
            dto.getCaracteristicas().add(new CaracteristicaPuestoDTO(
                    cp.getCompetencia().getNombre(), cp.getPonderacionMinima(), cp.getCompetencia().puedeEvaluarse()));
        }
        return dto;
    }

    private static PuestoDTO aDTO(Puesto p) {
        PuestoDTO dto = new PuestoDTO(p.getId());
        dto.setCodigo(p.getCodigo());
        dto.setNombre(p.getNombre());
        dto.setEmpresa(p.getEmpresa().getNombre());
        return dto;
    }
}
