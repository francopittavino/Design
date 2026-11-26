package capitalhumano.entidades;

import capitalhumano.entidades.estados.EstadoCompleto;
import capitalhumano.entidades.estados.EstadoCuestionarioState;
import capitalhumano.entidades.estados.EstadoEnProceso;
import capitalhumano.entidades.estados.EstadoActivo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

public class Cuestionario {
    private int id;
    private String clave;
    private EstadoCuestionarioState estadoActual;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private LocalDateTime fechaCambioEstado;
    private LocalDateTime fechaUltimoAcceso;
    private int cantAccesos;
    /** Minutos que tiene el candidato para completarlo desde el primer ingreso (CU026 obs. 2). */
    private int tiempoMax;
    /** Cantidad máxima de accesos (atributo accesos_maximos del DER). */
    private int accesosMaximos;

    private Evaluacion evaluacion;
    private Candidato candidato;
    private List<PreguntaCuestionario> preguntas = new ArrayList<>();
    private List<ResultadoCompetencia> resultados = new ArrayList<>();

    // ---------- Patrón State: el comportamiento depende del estado actual ----------

    /** Guarda las respuestas de un bloque: idPreguntaCuestionario -> idItemOpcionDeRespuesta. */
    public void responder(Map<Integer, Integer> respuestas) {
        estadoActual.responder(this, respuestas);   // valida que se pueda responder
        respuestas.forEach((idPreguntaCuest, idItem) -> {
            PreguntaCuestionario pc = getPreguntaCuestionario(idPreguntaCuest);
            if (pc == null || pc.getRespuesta() != null) {
                throw new IllegalArgumentException("La pregunta " + idPreguntaCuest + " no se puede responder");
            }
            ItemOpcionDeRespuesta item = pc.getPregunta().getOpcionDeRespuesta().getItems().stream()
                    .filter(i -> i.getId() == idItem).findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Opción inválida para la pregunta " + idPreguntaCuest));
            RespuestaCandidata r = new RespuestaCandidata(item, pc.getPregunta().getValorPonderacion(idItem));
            pc.setRespuesta(r);
        });
    }

    public void finalizar() { estadoActual.finalizar(this); }

    public boolean superaTiempoMaximo() { return estadoActual.verificarTiempo(this); }

    public boolean estaActivo() { return estadoActual instanceof EstadoActivo; }
    public boolean estaEnProceso() { return estadoActual instanceof EstadoEnProceso; }
    public boolean estaCompleto() { return estadoActual instanceof EstadoCompleto; }

    /** Incrementa la cantidad de accesos y actualiza la fecha del último acceso. */
    public void registrarAcceso() {
        cantAccesos++;
        fechaUltimoAcceso = LocalDateTime.now();
    }

    // ---------- Preguntas, bloques y resultados ----------

    public void agregarPregunta(PreguntaCuestionario pc) { preguntas.add(pc); }

    public List<PreguntaCuestionario> getPreguntasBloque(int nro) {
        return preguntas.stream().filter(p -> p.getNroBloque() == nro)
                .sorted(Comparator.comparingInt(PreguntaCuestionario::getNroOrden)).toList();
    }

    public int getCantBloques() {
        return preguntas.stream().mapToInt(PreguntaCuestionario::getNroBloque).max().orElse(0);
    }

    /** CU026 1.C: primer bloque que todavía tiene preguntas sin responder (0 si no queda ninguno). */
    public int getPrimerBloqueSinResponder() {
        return preguntas.stream().filter(p -> p.getRespuesta() == null)
                .mapToInt(PreguntaCuestionario::getNroBloque).min().orElse(0);
    }

    public List<RespuestaCandidata> getRespuestasFactor(Factor f) {
        return preguntas.stream()
                .filter(p -> p.getPregunta().getFactor().getId() == f.getId() && p.getRespuesta() != null)
                .map(PreguntaCuestionario::getRespuesta).toList();
    }

    /** Factores que tienen preguntas en este cuestionario (los "factores evaluados"). */
    public List<Factor> getFactoresEvaluados(Competencia c) {
        return preguntas.stream().map(p -> p.getPregunta().getFactor())
                .filter(f -> f.getCompetencia().getId() == c.getId())
                .filter(distinctById()).toList();
    }

    private static java.util.function.Predicate<Factor> distinctById() {
        java.util.Set<Integer> vistos = new java.util.HashSet<>();
        return f -> vistos.add(f.getId());
    }

    public void agregarResultado(ResultadoCompetencia rc) { resultados.add(rc); }

    public List<ResultadoCompetencia> getResultados() { return resultados; }

    public ResultadoCompetencia getResultado(Competencia c) {
        return resultados.stream().filter(r -> r.getCompetencia().getId() == c.getId()).findFirst().orElse(null);
    }

    /** Puntaje total (atributo derivado del DER): promedio de los puntajes de las competencias. */
    public Double getPuntajeTotal() {
        OptionalDouble prom = resultados.stream().mapToDouble(ResultadoCompetencia::getPuntajeObtenido).average();
        return prom.isPresent() ? Math.round(prom.getAsDouble() * 100) / 100.0 : null;
    }

    public PreguntaCuestionario getPreguntaCuestionario(int idPreguntaCuest) {
        return preguntas.stream().filter(p -> p.getId() == idPreguntaCuest).findFirst().orElse(null);
    }

    public String getNombreEstado() { return estadoActual.getNombreEstado(); }

    // ---------- getters / setters ----------

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }
    public EstadoCuestionarioState getEstadoActual() { return estadoActual; }
    public void setEstadoActual(EstadoCuestionarioState estado) { this.estadoActual = estado; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }
    public LocalDateTime getFechaCambioEstado() { return fechaCambioEstado; }
    public void setFechaCambioEstado(LocalDateTime fecha) { this.fechaCambioEstado = fecha; }
    public LocalDateTime getFechaUltimoAcceso() { return fechaUltimoAcceso; }
    public void setFechaUltimoAcceso(LocalDateTime fecha) { this.fechaUltimoAcceso = fecha; }
    public int getCantAccesos() { return cantAccesos; }
    public void setCantAccesos(int cantAccesos) { this.cantAccesos = cantAccesos; }
    public int getTiempoMax() { return tiempoMax; }
    public void setTiempoMax(int tiempoMax) { this.tiempoMax = tiempoMax; }
    public int getAccesosMaximos() { return accesosMaximos; }
    public void setAccesosMaximos(int accesosMaximos) { this.accesosMaximos = accesosMaximos; }
    public Evaluacion getEvaluacion() { return evaluacion; }
    public void setEvaluacion(Evaluacion evaluacion) { this.evaluacion = evaluacion; }
    public Candidato getCandidato() { return candidato; }
    public void setCandidato(Candidato candidato) { this.candidato = candidato; }
    public List<PreguntaCuestionario> getPreguntas() { return preguntas; }
    public void setPreguntas(List<PreguntaCuestionario> preguntas) { this.preguntas = preguntas; }
    public void setResultados(List<ResultadoCompetencia> resultados) { this.resultados = resultados; }
}
