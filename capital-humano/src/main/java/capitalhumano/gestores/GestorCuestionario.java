package capitalhumano.gestores;

import capitalhumano.config.Configuracion;
import capitalhumano.dao.CuestionarioDAO;
import capitalhumano.dao.impl.CuestionarioDAOImpl;
import capitalhumano.dto.CuestionarioDTO;
import capitalhumano.dto.OpcionRespuestaDTO;
import capitalhumano.dto.PreguntaDTO;
import capitalhumano.entidades.Competencia;
import capitalhumano.entidades.Cuestionario;
import capitalhumano.entidades.Evaluacion;
import capitalhumano.entidades.Factor;
import capitalhumano.entidades.ItemOpcionDeRespuesta;
import capitalhumano.entidades.Pregunta;
import capitalhumano.entidades.PreguntaCuestionario;
import capitalhumano.entidades.Puesto;
import capitalhumano.entidades.RespuestaCandidata;
import capitalhumano.entidades.ResultadoCompetencia;
import capitalhumano.entidades.ResultadoFactor;
import capitalhumano.entidades.estados.EstadoEnProceso;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/** CU026 - Completar Cuestionario. */
public final class GestorCuestionario {
    private static GestorCuestionario instance;

    private final CuestionarioDAO cuestionarioDAO = new CuestionarioDAOImpl();
    private final Random random = new SecureRandom();

    private GestorCuestionario() {}

    public static synchronized GestorCuestionario getInstance() {
        if (instance == null) instance = new GestorCuestionario();
        return instance;
    }

    /**
     * Paso 1 / 1.A / 1.B: registra el acceso y verifica el tiempo. El estado actual hace el cambio
     * (Incompleto o Sin Contestar) si corresponde. Devuelve true si el candidato puede seguir.
     */
    public boolean verificarTiempo(CuestionarioDTO dto) {
        Cuestionario c = cargar(dto);
        c.registrarAcceso();
        boolean superado = c.superaTiempoMaximo();
        cuestionarioDAO.guardar(c);
        actualizar(dto, c);
        return !superado;
    }

    /** Devuelve el nombre del estado actual (para distinguir el primer ingreso de 1.C "En Proceso"). */
    public String obtenerEstado(CuestionarioDTO dto) {
        Cuestionario c = cargar(dto);
        actualizar(dto, c);
        return c.getNombreEstado();
    }

    /** Las instrucciones y los parámetros salen del repositorio de configuraciones. */
    public String obtenerInstrucciones() {
        Configuracion cfg = Configuracion.getInstance();
        return cfg.getInstrucciones()
                .replace("{tiempo}", String.valueOf(cfg.getTiempoMaxMinutos()))
                .replace("{accesos}", String.valueOf(cfg.getAccesosMaximos()));
    }

    /**
     * Paso 3: elige 2 preguntas al azar por cada factor (con al menos 2 preguntas) de cada competencia
     * del puesto, las mezcla, las agrupa en bloques, registra la fecha de inicio y pasa a "En Proceso".
     */
    public void iniciarCuestionario(CuestionarioDTO dto) {
        Cuestionario c = cargar(dto);
        if (!c.estaActivo()) throw new IllegalStateException("El cuestionario ya fue iniciado");

        Evaluacion evaluacion = c.getEvaluacion();
        Puesto puesto = evaluacion.getPuesto();
        List<Pregunta> elegidas = new ArrayList<>();
        for (Competencia competencia : puesto.getCompetencias()) {
            for (Factor factor : competencia.getFactores()) {
                if (factor.tieneSuficientesPreguntas()) {
                    elegidas.addAll(elegirAlAzar(factor.getPreguntasActivas(), Factor.PREGUNTAS_POR_FACTOR));
                }
            }
        }
        List<Pregunta> mezcladas = mezclar(elegidas);
        int porBloque = evaluacion.getBloqueTamanio();
        for (int i = 0; i < mezcladas.size(); i++) {
            c.agregarPregunta(new PreguntaCuestionario(mezcladas.get(i), i + 1, i / porBloque + 1));
        }

        LocalDateTime ahora = LocalDateTime.now();
        c.setFechaInicio(ahora);
        c.setEstadoActual(new EstadoEnProceso());
        c.setFechaCambioEstado(ahora);
        cuestionarioDAO.guardar(c);
        actualizar(dto, c);
    }

    /** Paso 4: preguntas del bloque con las opciones (ítems) de su opción de respuesta. */
    public List<PreguntaDTO> obtenerBloque(CuestionarioDTO dto, int nro) {
        Cuestionario c = cargar(dto);
        actualizar(dto, c);
        List<PreguntaDTO> lista = new ArrayList<>();
        for (PreguntaCuestionario pc : c.getPreguntasBloque(nro)) {
            PreguntaDTO p = new PreguntaDTO();
            p.setId(pc.getId());
            p.setNombre(String.valueOf(pc.getNroOrden()));
            p.setEnunciado(pc.getPregunta().getEnunciado());
            p.setOpciones(pc.getPregunta().getOpcionDeRespuesta().getItems().stream()
                    .filter(ItemOpcionDeRespuesta::isActivo)
                    .sorted(Comparator.comparingInt(ItemOpcionDeRespuesta::getOrden))
                    .map(i -> new OpcionRespuestaDTO(i.getId(), i.getDescripcion())).toList());
            lista.add(p);
        }
        return lista;
    }

    /**
     * Paso 7: guarda el bloque contestado (idPreguntaCuestionario -> idItem). Antes vuelve a
     * verificar el tiempo: si se venció mientras respondía, el cuestionario pasa a "Incompleto".
     * Devuelve false en ese caso.
     */
    public boolean guardarRespuestas(CuestionarioDTO dto, Map<Integer, Integer> resp) {
        Cuestionario c = cargar(dto);
        if (c.superaTiempoMaximo()) {
            cuestionarioDAO.guardar(c);
            actualizar(dto, c);
            return false;
        }
        c.responder(resp);
        cuestionarioDAO.guardar(c);
        actualizar(dto, c);
        return true;
    }

    /** Paso 8: calcula los puntajes, pasa a "Completo" y guarda. */
    public void finalizarCuestionario(CuestionarioDTO dto) {
        Cuestionario c = cargar(dto);
        if (c.getPrimerBloqueSinResponder() != 0) throw new IllegalStateException("Quedan preguntas sin responder");
        calcularPuntajes(c);
        c.finalizar();
        cuestionarioDAO.guardar(c);
        actualizar(dto, c);
    }

    /**
     * Algoritmo (3) del CU026: total factor = suma de las ponderaciones elegidas / 2;
     * total competencia = suma de los totales de sus factores / cantidad de factores evaluados.
     */
    void calcularPuntajes(Cuestionario c) {
        for (Competencia competencia : c.getEvaluacion().getPuesto().getCompetencias()) {
            List<Factor> factores = c.getFactoresEvaluados(competencia);
            if (factores.isEmpty()) continue;
            ResultadoCompetencia rc = new ResultadoCompetencia(competencia);
            double sumaFactores = 0;
            for (Factor factor : factores) {
                double suma = c.getRespuestasFactor(factor).stream().mapToDouble(RespuestaCandidata::getPonderacion).sum();
                double totalFactor = redondear(suma / Factor.PREGUNTAS_POR_FACTOR);
                rc.agregarResultadoFactor(new ResultadoFactor(factor, totalFactor));
                sumaFactores += totalFactor;
            }
            rc.setPuntajeObtenido(redondear(sumaFactores / factores.size()));
            c.agregarResultado(rc);
        }
    }

    private List<Pregunta> elegirAlAzar(List<Pregunta> preguntas, int cant) {
        List<Pregunta> copia = new ArrayList<>(preguntas);
        Collections.shuffle(copia, random);
        return copia.subList(0, Math.min(cant, copia.size()));
    }

    private List<Pregunta> mezclar(List<Pregunta> preguntas) {
        List<Pregunta> copia = new ArrayList<>(preguntas);
        Collections.shuffle(copia, random);
        return copia;
    }

    private Cuestionario cargar(CuestionarioDTO dto) {
        Cuestionario c = cuestionarioDAO.buscarPorId(dto.getIdCuestionario());
        if (c == null) throw new IllegalStateException("No existe el cuestionario " + dto.getIdCuestionario());
        return c;
    }

    private static void actualizar(CuestionarioDTO dto, Cuestionario c) {
        dto.setEstado(c.getNombreEstado());
        dto.setCantBloques(c.getCantBloques());
        dto.setBloqueActual(c.getPrimerBloqueSinResponder());
    }

    private static double redondear(double v) { return Math.round(v * 100) / 100.0; }
}
