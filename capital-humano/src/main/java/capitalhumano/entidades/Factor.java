package capitalhumano.entidades;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Factor {
    /** CU026 obs. (1): se toman 2 preguntas al azar por factor. */
    public static final int PREGUNTAS_POR_FACTOR = 2;

    private int id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private int nroOrden;
    private boolean activo = true;
    private LocalDate fechaBaja;
    private Competencia competencia;
    private List<Pregunta> preguntas = new ArrayList<>();

    public boolean puedeEliminarse() { return true; }

    public boolean tieneSuficientesPreguntas() {
        return getPreguntasActivas().size() >= PREGUNTAS_POR_FACTOR;
    }

    public List<Pregunta> getPreguntasActivas() {
        return preguntas.stream().filter(Pregunta::isActivo).toList();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public int getNroOrden() { return nroOrden; }
    public void setNroOrden(int nroOrden) { this.nroOrden = nroOrden; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public LocalDate getFechaBaja() { return fechaBaja; }
    public void setFechaBaja(LocalDate fechaBaja) { this.fechaBaja = fechaBaja; }
    public Competencia getCompetencia() { return competencia; }
    public void setCompetencia(Competencia competencia) { this.competencia = competencia; }
    public List<Pregunta> getPreguntas() { return preguntas; }
    public void setPreguntas(List<Pregunta> preguntas) { this.preguntas = preguntas; }
}
