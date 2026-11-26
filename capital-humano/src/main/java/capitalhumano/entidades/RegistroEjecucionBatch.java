package capitalhumano.entidades;

import java.time.LocalDateTime;

/** Registro de cada ejecución del proceso batch de finalización de cuestionarios (CU032). */
public class RegistroEjecucionBatch {
    private int id;
    private LocalDateTime fechaHora;
    private String accion;

    public RegistroEjecucionBatch(String accion) {
        this.accion = accion;
        this.fechaHora = LocalDateTime.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public String getAccion() { return accion; }
}
