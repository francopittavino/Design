package capitalhumano.entidades;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Pregunta {
    private int id;
    private String nombre;
    private String enunciado;
    private String descripcion;
    private boolean activo = true;
    private LocalDate fechaBaja;
    private Factor factor;
    private OpcionDeRespuesta opcionDeRespuesta;
    private List<PonderacionItem> ponderaciones = new ArrayList<>();

    public Pregunta() {}

    public Pregunta(String nombre, String enunciado, String descripcion, Factor factor, OpcionDeRespuesta opcion) {
        this.nombre = nombre;
        this.enunciado = enunciado;
        this.descripcion = descripcion;
        this.factor = factor;
        this.opcionDeRespuesta = opcion;
    }

    public boolean puedeEliminarse() { return true; }

    public void agregarPonderacion(PonderacionItem pi) { ponderaciones.add(pi); }

    /** Valor de la ponderación que esta pregunta asigna al ítem elegido (0 si no tiene). */
    public double getValorPonderacion(int idItem) {
        return ponderaciones.stream()
                .filter(p -> p.getItemOpcionDeRespuesta().getId() == idItem)
                .mapToDouble(PonderacionItem::getValor).findFirst().orElse(0);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEnunciado() { return enunciado; }
    public void setEnunciado(String enunciado) { this.enunciado = enunciado; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public LocalDate getFechaBaja() { return fechaBaja; }
    public void setFechaBaja(LocalDate fechaBaja) { this.fechaBaja = fechaBaja; }
    public Factor getFactor() { return factor; }
    public void setFactor(Factor factor) { this.factor = factor; }
    public OpcionDeRespuesta getOpcionDeRespuesta() { return opcionDeRespuesta; }
    public void setOpcionDeRespuesta(OpcionDeRespuesta opcionDeRespuesta) { this.opcionDeRespuesta = opcionDeRespuesta; }
    public List<PonderacionItem> getPonderaciones() { return ponderaciones; }
    public void setPonderaciones(List<PonderacionItem> ponderaciones) { this.ponderaciones = ponderaciones; }
}
