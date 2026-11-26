package capitalhumano.reportes;

import capitalhumano.entidades.Candidato;
import capitalhumano.entidades.Cuestionario;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Una fila del orden de mérito (un candidato / cuestionario). */
public class ItemOrdenMerito {
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final Cuestionario cuestionario;
    private int posicion;
    private Double puntajeObtenido;
    private boolean cumplePonderaciones;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private LocalDateTime ultimoAcceso;
    private int cantAccesos;

    public ItemOrdenMerito(Cuestionario cuestionario) { this.cuestionario = cuestionario; }

    /**
     * CU028 obs. (2): completos -> Fecha Inicio, Fecha Fin, Cantidad de Accesos;
     * el resto -> Fecha Inicio, Último ingreso, Cantidad de Accesos.
     */
    public String getDatosAdicionales() {
        if (cuestionario.estaCompleto()) {
            return "Inicio: " + f(fechaInicio) + " | Fin: " + f(fechaFin) + " | Accesos: " + cantAccesos;
        }
        return "Inicio: " + f(fechaInicio) + " | Último ingreso: " + f(ultimoAcceso) + " | Accesos: " + cantAccesos;
    }

    static String f(LocalDateTime t) { return t == null ? "-" : t.format(FMT); }

    public boolean isCompleto() { return cuestionario.estaCompleto(); }

    /** Estado que se muestra en el listado: los completos que no alcanzan los mínimos se marcan aparte. */
    public String getEstado() {
        if (cuestionario.estaCompleto() && !cumplePonderaciones) return "NO ALCANZA MÍNIMO";
        return cuestionario.getNombreEstado();
    }

    public Candidato getCandidato() { return cuestionario.getCandidato(); }
    public Cuestionario getCuestionario() { return cuestionario; }
    public int getPosicion() { return posicion; }
    public void setPosicion(int posicion) { this.posicion = posicion; }
    public Double getPuntajeObtenido() { return puntajeObtenido; }
    public void setPuntajeObtenido(Double puntajeObtenido) { this.puntajeObtenido = puntajeObtenido; }
    public boolean isCumplePonderaciones() { return cumplePonderaciones; }
    public void setCumplePonderaciones(boolean cumple) { this.cumplePonderaciones = cumple; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDateTime fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDateTime getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDateTime fechaFin) { this.fechaFin = fechaFin; }
    public LocalDateTime getUltimoAcceso() { return ultimoAcceso; }
    public void setUltimoAcceso(LocalDateTime ultimoAcceso) { this.ultimoAcceso = ultimoAcceso; }
    public int getCantAccesos() { return cantAccesos; }
    public void setCantAccesos(int cantAccesos) { this.cantAccesos = cantAccesos; }
}
