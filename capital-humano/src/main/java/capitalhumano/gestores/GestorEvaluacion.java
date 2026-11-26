package capitalhumano.gestores;

import capitalhumano.config.Configuracion;
import capitalhumano.dao.CandidatoDAO;
import capitalhumano.dao.EvaluacionDAO;
import capitalhumano.dao.PuestoDAO;
import capitalhumano.dao.impl.CandidatoDAOImpl;
import capitalhumano.dao.impl.EvaluacionDAOImpl;
import capitalhumano.dao.impl.PuestoDAOImpl;
import capitalhumano.dto.CandidatoCuestionarioDTO;
import capitalhumano.dto.CandidatoGestionDTO;
import capitalhumano.dto.EvaluacionDTO;
import capitalhumano.dto.PuestoDTO;
import capitalhumano.entidades.Candidato;
import capitalhumano.entidades.Competencia;
import capitalhumano.entidades.Consultor;
import capitalhumano.entidades.Evaluacion;
import capitalhumano.entidades.Puesto;
import capitalhumano.facade.ServiciosExternosFacade;

import java.io.OutputStream;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** CU025 - Evaluar a Candidatos. */
public final class GestorEvaluacion {
    private static final String LETRAS = "ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final String DIGITOS = "23456789";
    private static final int LARGO_CLAVE = 8;

    private static GestorEvaluacion instance;

    private final EvaluacionDAO evaluacionDAO = new EvaluacionDAOImpl();
    private final PuestoDAO puestoDAO = new PuestoDAOImpl();
    private final CandidatoDAO candidatoDAO = new CandidatoDAOImpl();
    private final ServiciosExternosFacade serviciosFacade = new ServiciosExternosFacade();
    private final SecureRandom random = new SecureRandom();

    private GestorEvaluacion() {}

    public static synchronized GestorEvaluacion getInstance() {
        if (instance == null) instance = new GestorEvaluacion();
        return instance;
    }

    /** CU025 9.A: todas las competencias del puesto tienen al menos un factor con 2 o más preguntas. */
    public boolean validarCompetenciasEvaluables(PuestoDTO puestoDTO) {
        Puesto puesto = puestoDAO.buscarPorId(puestoDTO.getId());
        return puesto != null && puesto.todasCompetenciasEvaluables();
    }

    /** CU025 9.A.1: nombres de las competencias que no pueden ser evaluadas. */
    public List<String> competenciasNoEvaluables(PuestoDTO puestoDTO) {
        Puesto puesto = puestoDAO.buscarPorId(puestoDTO.getId());
        return puesto.getCompetencias().stream().filter(c -> !c.puedeEvaluarse()).map(Competencia::getNombre).toList();
    }

    /**
     * CU025 paso 11: arma la lista de candidatos a evaluar con su clave de ingreso
     * (8 caracteres al azar, con letras y dígitos). Solo genera las claves: los cuestionarios se
     * crean una sola vez, en Evaluacion.crearCuestionarios, cuando el consultor presiona "Finalizar"
     * (corrección de la Entrega 5: antes se llamaba generarCuestionarios).
     */
    public List<CandidatoCuestionarioDTO> generarClaves(List<CandidatoGestionDTO> candidatos) {
        List<CandidatoCuestionarioDTO> lista = new ArrayList<>();
        Set<String> usadas = new HashSet<>();
        for (CandidatoGestionDTO c : candidatos) {
            String clave;
            do { clave = generarClave(); } while (!usadas.add(clave));
            CandidatoCuestionarioDTO dto = new CandidatoCuestionarioDTO();
            dto.setNroCandidato(c.getNroCandidato());
            dto.setApellido(c.getApellido());
            dto.setNombre(c.getNombre());
            dto.setTipoDoc(c.getTipoDoc());
            dto.setNroDoc(c.getNroDoc());
            dto.setToken(clave);
            lista.add(dto);
        }
        return lista;
    }

    /** Garantiza al menos una letra y un dígito. */
    String generarClave() {
        String todos = LETRAS + DIGITOS;
        char[] clave = new char[LARGO_CLAVE];
        clave[0] = LETRAS.charAt(random.nextInt(LETRAS.length()));
        clave[1] = DIGITOS.charAt(random.nextInt(DIGITOS.length()));
        for (int i = 2; i < LARGO_CLAVE; i++) clave[i] = todos.charAt(random.nextInt(todos.length()));
        for (int i = LARGO_CLAVE - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char t = clave[i];
            clave[i] = clave[j];
            clave[j] = t;
        }
        return new String(clave);
    }

    /**
     * CU025 pasos 12-13: crea la evaluación y un cuestionario "Activo" por candidato (con la clave
     * que ya se mostró) y la guarda. Vuelve a verificar que nadie tenga un cuestionario activo.
     */
    public EvaluacionDTO evaluarCandidatos(List<CandidatoCuestionarioDTO> candidatos, PuestoDTO puestoDTO, Consultor consultor) {
        Puesto puesto = puestoDAO.buscarPorId(puestoDTO.getId());
        if (puesto == null) throw new ValidacionException("El puesto seleccionado no existe");
        if (!puesto.todasCompetenciasEvaluables()) throw new ValidacionException("Alguna competencia del puesto no puede ser evaluada");

        Map<Candidato, String> claves = new LinkedHashMap<>();
        List<String> bloqueados = new ArrayList<>();
        for (CandidatoCuestionarioDTO dto : candidatos) {
            Candidato c = candidatoDAO.buscarPorNroCandidato(dto.getNroCandidato());
            if (c.tieneCuestionarioActivo()) bloqueados.add(c.getApellido() + ", " + c.getNombre());
            claves.put(c, dto.getToken());
        }
        if (!bloqueados.isEmpty()) {
            throw new ValidacionException("Los siguientes candidatos poseen cuestionarios activos o en proceso y no pueden ser evaluados: "
                    + String.join("; ", bloqueados));
        }

        Configuracion cfg = Configuracion.getInstance();
        Evaluacion evaluacion = new Evaluacion(puesto, consultor, cfg.getPreguntasPorBloque());
        evaluacion.crearCuestionarios(claves, cfg.getTiempoMaxMinutos(), cfg.getAccesosMaximos());
        evaluacionDAO.guardar(evaluacion);

        EvaluacionDTO dto = new EvaluacionDTO(evaluacion.getId());
        dto.setPuesto(puesto.getNombre());
        dto.setFechaCreacion(evaluacion.getFechaCreacion());
        dto.setCantCandidatos(evaluacion.getCuestionarios().size());
        return dto;
    }

    /** CU025 pasos 15-17: nombre del archivo aaaa-mm-dd-hh-mm-NombreFunción.xls. */
    public String nombreArchivoClaves(EvaluacionDTO eval) {
        String funcion = eval.getPuesto().replaceAll("[\\\\/:*?\"<>|]", "").trim();
        return eval.getFechaCreacion().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")) + "-" + funcion + ".xls";
    }

    public boolean exportarClaves(EvaluacionDTO eval, List<CandidatoCuestionarioDTO> candidatos, OutputStream salida) {
        String titulo = "Evaluación " + eval.getId() + " - " + eval.getPuesto() + " - "
                + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        return serviciosFacade.exportarResultados(titulo, "EXCEL", candidatos, salida);
    }

    /** CU007 - Exportar resultados (fuera del alcance de las Etapas 7 y 8). */
    public boolean exportarResultados(PuestoDTO puesto, OutputStream salida) {
        throw new UnsupportedOperationException("CU007 - Exportar resultados: fuera del alcance de las Etapas 7 y 8");
    }
}
