package capitalhumano.gestores;

import capitalhumano.dao.FactorDAO;
import capitalhumano.dao.OpcionRespuestaDAO;
import capitalhumano.dao.PreguntaDAO;
import capitalhumano.dao.RegistroAuditoriaDAO;
import capitalhumano.dao.impl.FactorDAOImpl;
import capitalhumano.dao.impl.OpcionRespuestaDAOImpl;
import capitalhumano.dao.impl.PreguntaDAOImpl;
import capitalhumano.dao.impl.RegistroAuditoriaDAOImpl;
import capitalhumano.dto.PreguntaDTO;
import capitalhumano.entidades.Factor;
import capitalhumano.entidades.ItemOpcionDeRespuesta;
import capitalhumano.entidades.OpcionDeRespuesta;
import capitalhumano.entidades.PonderacionItem;
import capitalhumano.entidades.Pregunta;
import capitalhumano.entidades.RegistroAuditoria;
import capitalhumano.facade.ServiciosExternosFacade;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** CU020 - Gestionar Preguntas y CU021 - Dar de alta Pregunta. */
public final class GestorPreguntas {
    public static final int MAX_NOMBRE = 200;
    public static final int MAX_ENUNCIADO = 500;
    public static final int MAX_DESCRIPCION = 300;
    public static final double SUMA_PONDERACIONES = 10.0;

    private static GestorPreguntas instance;

    private final PreguntaDAO preguntaDAO = new PreguntaDAOImpl();
    private final FactorDAO factorDAO = new FactorDAOImpl();
    private final OpcionRespuestaDAO opcionDAO = new OpcionRespuestaDAOImpl();
    private final RegistroAuditoriaDAO auditoriaDAO = new RegistroAuditoriaDAOImpl();
    private final ServiciosExternosFacade serviciosFacade = new ServiciosExternosFacade();

    private GestorPreguntas() {}

    public static synchronized GestorPreguntas getInstance() {
        if (instance == null) instance = new GestorPreguntas();
        return instance;
    }

    // ======================= CU020 =======================

    /**
     * Filtros: "idCompetencia", "idFactor" (0 = sin filtro) y "nombre" (criterio "contiene").
     * Recorre los factores que cumplen los filtros y por cada uno trae sus preguntas (diag. de secuencia CU020).
     */
    public List<PreguntaDTO> buscarPreguntas(Map<String, String> filtros) {
        int idCompetencia = entero(filtros.get("idCompetencia"));
        int idFactor = entero(filtros.get("idFactor"));
        String nombre = mayus(filtros.get("nombre"));

        List<Factor> factores;
        if (idFactor > 0) {
            Factor f = factorDAO.buscarPorId(idFactor);
            factores = f == null ? List.of() : List.of(f);
        } else if (idCompetencia > 0) {
            factores = factorDAO.buscarPorCompetencia(idCompetencia);
        } else {
            factores = factorDAO.listarTodos();
        }

        List<PreguntaDTO> resultado = new ArrayList<>();
        for (Factor factor : factores) {
            for (Pregunta p : preguntaDAO.buscarPorFactor(factor.getId())) {
                if (nombre.isEmpty() || p.getNombre().toUpperCase().contains(nombre)) {
                    resultado.add(aDTO(p));
                }
            }
        }
        return resultado;
    }

    // ======================= CU021 =======================

    /**
     * Valida y registra la pregunta. Si alguna validación no se supera lanza ValidacionException
     * con TODOS los errores (2.A campos obligatorios, 2.B longitudes, ponderaciones, 2.C duplicada).
     */
    public PreguntaDTO crearPregunta(PreguntaDTO dto, String usuario) {
        normalizar(dto);
        List<String> errores = validar(dto);
        if (errores.isEmpty()) {
            List<Pregunta> existentes = preguntaDAO.buscarPorCriterios(dto.getIdCompetencia(), dto.getIdFactor(), dto.getNombre());
            errores.addAll(verificarDuplicado(existentes, dto.getNombre(), dto.getEnunciado()));
        }
        if (!errores.isEmpty()) throw new ValidacionException(errores);

        Factor factor = factorDAO.buscarPorId(dto.getIdFactor());
        OpcionDeRespuesta opcion = opcionDAO.buscarPorId(dto.getIdOpcionRespuesta());
        Pregunta preg = new Pregunta(dto.getNombre(), dto.getEnunciado(), dto.getDescripcion(), factor, opcion);
        for (ItemOpcionDeRespuesta item : opcion.getItems()) {
            if (!item.isActivo()) continue;
            PonderacionItem pi = new PonderacionItem(Double.parseDouble(dto.getPonderaciones().get(item.getId())), item);
            preg.agregarPonderacion(pi);
        }
        preguntaDAO.guardar(preg);
        auditoriaDAO.guardar(new RegistroAuditoria(usuario, "PREGUNTA", "ALTA " + preg.getId() + " - " + preg.getNombre()));
        return aDTO(preg);
    }

    /** Validaciones de campos (2.A, 2.B y observación de ponderaciones). */
    public List<String> validar(PreguntaDTO p) {
        List<String> errores = new ArrayList<>();
        List<String> faltantes = new ArrayList<>();
        if (p.getIdCompetencia() <= 0) faltantes.add("Competencia");
        if (p.getIdFactor() <= 0) faltantes.add("Factor");
        if (vacio(p.getNombre())) faltantes.add("Nombre de pregunta");
        if (vacio(p.getEnunciado())) faltantes.add("Pregunta");
        if (p.getIdOpcionRespuesta() <= 0) faltantes.add("Opción de respuesta");
        boolean faltaPonderacion = p.getIdOpcionRespuesta() > 0 && p.getPonderaciones().values().stream().anyMatch(this::vacio);
        if (faltaPonderacion || (p.getIdOpcionRespuesta() > 0 && p.getPonderaciones().isEmpty())) faltantes.add("Ponderación");
        if (!faltantes.isEmpty()) errores.add("Los siguientes campos son obligatorios: " + String.join(", ", faltantes));

        List<String> largos = new ArrayList<>();
        if (largo(p.getNombre()) > MAX_NOMBRE) largos.add("Nombre de pregunta (máx. " + MAX_NOMBRE + ")");
        if (largo(p.getEnunciado()) > MAX_ENUNCIADO) largos.add("Pregunta (máx. " + MAX_ENUNCIADO + ")");
        if (largo(p.getDescripcion()) > MAX_DESCRIPCION) largos.add("Descripción (máx. " + MAX_DESCRIPCION + ")");
        if (!largos.isEmpty()) errores.add("Los siguientes campos exceden la longitud máxima: " + String.join(", ", largos));

        if (p.getIdFactor() > 0 && p.getIdCompetencia() > 0) {
            Factor f = factorDAO.buscarPorId(p.getIdFactor());
            if (f == null || f.getCompetencia().getId() != p.getIdCompetencia()) {
                errores.add("El factor seleccionado no pertenece a la competencia seleccionada");
            }
        }
        if (p.getIdOpcionRespuesta() > 0 && !faltaPonderacion) {
            errores.addAll(validarPonderaciones(p));
        }
        return errores;
    }

    private List<String> validarPonderaciones(PreguntaDTO p) {
        OpcionDeRespuesta opcion = opcionDAO.buscarPorId(p.getIdOpcionRespuesta());
        if (opcion == null) return List.of("La opción de respuesta seleccionada no existe");
        double suma = 0;
        for (ItemOpcionDeRespuesta item : opcion.getItems()) {
            if (!item.isActivo()) continue;
            String valor = p.getPonderaciones().get(item.getId());
            if (vacio(valor)) return List.of("Los siguientes campos son obligatorios: Ponderación");
            double v;
            try {
                v = Double.parseDouble(valor.replace(',', '.'));
            } catch (NumberFormatException e) {
                return List.of("Las ponderaciones deben ser numéricas");
            }
            if (v < 0) return List.of("Las ponderaciones deben ser 0 o positivas");
            p.getPonderaciones().put(item.getId(), String.valueOf(v));
            suma += v;
        }
        if (Math.abs(suma - SUMA_PONDERACIONES) > 0.001) {
            return List.of("Las ponderaciones de las opciones de respuesta deben sumar 10 puntos (suman "
                    + String.format("%.2f", suma) + ")");
        }
        return List.of();
    }

    /** 2.C - Observación (1): se valida tanto el nombre como la pregunta en sí. */
    private List<String> verificarDuplicado(List<Pregunta> existentes, String nombre, String enunciado) {
        List<String> errores = new ArrayList<>();
        if (existentes.stream().anyMatch(e -> e.getNombre().equalsIgnoreCase(nombre))) {
            errores.add("Ya existe una pregunta con ese nombre para la competencia y factor seleccionados");
        }
        if (existentes.stream().anyMatch(e -> e.getEnunciado().equalsIgnoreCase(enunciado))) {
            errores.add("Ya existe una pregunta con ese enunciado para la competencia y factor seleccionados");
        }
        return errores;
    }

    /** CU021 1.A.1 -> CU033. Lanza ServicioExternoException si el servicio de IA no responde. */
    public PreguntaDTO generarConIA(int idCompetencia, int idFactor) {
        Factor factor = factorDAO.buscarPorId(idFactor);
        Pregunta p = serviciosFacade.generarPreguntaIA(factor == null ? null : factor.getCompetencia(), factor);
        return aDTO(p);
    }

    // ======================= auxiliares =======================

    private PreguntaDTO aDTO(Pregunta p) {
        PreguntaDTO dto = new PreguntaDTO();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        dto.setEnunciado(p.getEnunciado());
        dto.setDescripcion(p.getDescripcion());
        dto.setIdFactor(p.getFactor().getId());
        dto.setFactor(p.getFactor().getNombre());
        dto.setIdCompetencia(p.getFactor().getCompetencia().getId());
        dto.setCompetencia(p.getFactor().getCompetencia().getNombre());
        dto.setIdOpcionRespuesta(p.getOpcionDeRespuesta().getId());
        Map<Integer, String> pond = new LinkedHashMap<>();
        p.getPonderaciones().forEach(pi -> pond.put(pi.getItemOpcionDeRespuesta().getId(), String.valueOf(pi.getValor())));
        dto.setPonderaciones(pond);
        return dto;
    }

    /** Requerimiento especial: los datos literales se ingresan SIEMPRE en mayúsculas. */
    private void normalizar(PreguntaDTO dto) {
        dto.setNombre(mayus(dto.getNombre()));
        dto.setEnunciado(mayus(dto.getEnunciado()));
        String desc = mayus(dto.getDescripcion());
        dto.setDescripcion(desc.isEmpty() ? null : desc);
    }

    private static String mayus(String s) { return s == null ? "" : s.trim().toUpperCase(); }
    private boolean vacio(String s) { return s == null || s.isBlank(); }
    private static int largo(String s) { return s == null ? 0 : s.length(); }

    private static int entero(String s) {
        try {
            return s == null || s.isBlank() ? 0 : Integer.parseInt(s.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
