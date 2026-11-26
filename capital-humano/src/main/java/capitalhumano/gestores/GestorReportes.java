package capitalhumano.gestores;

import capitalhumano.dao.EvaluacionDAO;
import capitalhumano.dao.PuestoDAO;
import capitalhumano.dao.impl.EvaluacionDAOImpl;
import capitalhumano.dao.impl.PuestoDAOImpl;
import capitalhumano.dto.EvaluacionDTO;
import capitalhumano.dto.PuestoDTO;
import capitalhumano.entidades.CaracteristicaPuesto;
import capitalhumano.entidades.Cuestionario;
import capitalhumano.entidades.Evaluacion;
import capitalhumano.entidades.Puesto;
import capitalhumano.entidades.ResultadoCompetencia;
import capitalhumano.exportacion.ExportadorPDF;
import capitalhumano.reportes.ItemOrdenMerito;
import capitalhumano.reportes.OrdenMerito;

import java.io.OutputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/** CU028 - Emitir Orden de Mérito. */
public final class GestorReportes {
    private static GestorReportes instance;

    private final EvaluacionDAO evaluacionDAO = new EvaluacionDAOImpl();
    private final PuestoDAO puestoDAO = new PuestoDAOImpl();

    private GestorReportes() {}

    public static synchronized GestorReportes getInstance() {
        if (instance == null) instance = new GestorReportes();
        return instance;
    }

    /** CU028 pasos 5-6: evaluaciones del puesto (vacía = 6.A "El puesto no posee evaluaciones"). */
    public List<EvaluacionDTO> buscarEvaluaciones(PuestoDTO puesto) {
        return evaluacionDAO.buscarPorPuesto(puesto.getId()).stream().map(e -> {
            EvaluacionDTO dto = new EvaluacionDTO(e.getId());
            dto.setPuesto(e.getPuesto().getNombre());
            dto.setFechaCreacion(e.getFechaCreacion());
            dto.setCantCandidatos(e.getCuestionarios().size());
            dto.setCantCompletos((int) e.getCuestionarios().stream().filter(Cuestionario::estaCompleto).count());
            return dto;
        }).toList();
    }

    /**
     * CU028 paso 7: arma el orden de mérito con los cuestionarios de las evaluaciones seleccionadas
     * (lista vacía = TODAS).
     */
    public OrdenMerito emitirOrdenMerito(PuestoDTO puestoDTO, List<EvaluacionDTO> evaluaciones) {
        Set<Integer> ids = evaluaciones.stream().map(EvaluacionDTO::getId).collect(Collectors.toSet());
        List<Evaluacion> todas = evaluacionDAO.buscarPorPuesto(puestoDTO.getId());
        List<Evaluacion> seleccionadas = ids.isEmpty() ? todas
                : todas.stream().filter(e -> ids.contains(e.getId())).toList();
        if (seleccionadas.isEmpty()) throw new ValidacionException("Se debe seleccionar al menos una evaluación");

        Puesto puesto = seleccionadas.get(0).getPuesto();
        OrdenMerito om = new OrdenMerito(puesto);
        om.setDescripcionEvaluaciones(ids.isEmpty() || seleccionadas.size() == todas.size() ? "TODAS"
                : seleccionadas.stream().map(e -> "N° " + e.getId()).collect(Collectors.joining(", ")));
        List<CaracteristicaPuesto> caracteristicas = puestoDAO.buscarCaracteristicas(puesto.getId());

        for (Evaluacion evaluacion : seleccionadas) {
            for (Cuestionario cuestionario : evaluacion.getCuestionarios()) {
                ItemOrdenMerito item = new ItemOrdenMerito(cuestionario);
                if (cuestionario.estaCompleto()) {
                    boolean cumple = true;
                    for (CaracteristicaPuesto cp : caracteristicas) {
                        ResultadoCompetencia rc = cuestionario.getResultado(cp.getCompetencia());
                        if (rc == null || !rc.superaPonderacionMinima(cp.getPonderacionMinima())) cumple = false;
                    }
                    item.setPuntajeObtenido(cuestionario.getPuntajeTotal());
                    item.setCumplePonderaciones(cumple);
                    item.setFechaInicio(cuestionario.getFechaInicio());
                    item.setFechaFin(cuestionario.getFechaFin());
                    item.setCantAccesos(cuestionario.getCantAccesos());
                } else {
                    item.setFechaInicio(cuestionario.getFechaInicio());
                    item.setUltimoAcceso(cuestionario.getFechaUltimoAcceso());
                    item.setCantAccesos(cuestionario.getCantAccesos());
                }
                om.agregarItem(item);
            }
        }
        om.generar();
        return om;
    }

    /** CU028 pasos 8-9: el gestor elige el formato (PDF) y el reporte le delega exportar() (Bridge). */
    public boolean imprimirOrdenMerito(OrdenMerito om, OutputStream salida) {
        om.setFormato(new ExportadorPDF(salida));
        return om.imprimir();
    }
}
