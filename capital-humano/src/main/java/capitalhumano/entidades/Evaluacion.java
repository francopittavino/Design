package capitalhumano.entidades;

import capitalhumano.entidades.estados.EstadoActivo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Evaluacion {
    private int id;
    private LocalDateTime fechaCreacion;
    private int bloqueTamanio;
    private Puesto puesto;
    private Consultor consultor;
    private List<Cuestionario> cuestionarios = new ArrayList<>();

    public Evaluacion() {}

    public Evaluacion(Puesto puesto, Consultor consultor, int bloqueTamanio) {
        this.puesto = puesto;
        this.consultor = consultor;
        this.bloqueTamanio = bloqueTamanio;
        this.fechaCreacion = LocalDateTime.now();
    }

    /**
     * CU025 paso 13: crea un cuestionario en estado "Activo" por cada candidato,
     * con la clave de ingreso que ya se le mostró al consultor en el paso 11.
     * Todos salen de la misma evaluación (mismo puesto: competencias, factores, preguntas).
     */
    public void crearCuestionarios(Map<Candidato, String> clavesPorCandidato, int tiempoMax, int accesosMaximos) {
        clavesPorCandidato.forEach((candidato, clave) -> {
            Cuestionario c = new Cuestionario();
            c.setEvaluacion(this);
            c.setCandidato(candidato);
            c.setClave(clave);
            c.setTiempoMax(tiempoMax);
            c.setAccesosMaximos(accesosMaximos);
            c.setEstadoActual(new EstadoActivo());
            c.setFechaCambioEstado(fechaCreacion);
            cuestionarios.add(c);
        });
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public int getBloqueTamanio() { return bloqueTamanio; }
    public void setBloqueTamanio(int bloqueTamanio) { this.bloqueTamanio = bloqueTamanio; }
    public Puesto getPuesto() { return puesto; }
    public void setPuesto(Puesto puesto) { this.puesto = puesto; }
    public Consultor getConsultor() { return consultor; }
    public void setConsultor(Consultor consultor) { this.consultor = consultor; }
    public List<Cuestionario> getCuestionarios() { return cuestionarios; }
    public void setCuestionarios(List<Cuestionario> cuestionarios) { this.cuestionarios = cuestionarios; }
}
