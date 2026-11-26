package capitalhumano.entidades;

import java.time.LocalDateTime;

public class RegistroAuditoria {
    private int id;
    private String usuario;
    private String entidad;
    private String accion;
    private LocalDateTime fechaHora;

    public RegistroAuditoria(String usuario, String entidad, String accion) {
        this.usuario = usuario;
        this.entidad = entidad;
        this.accion = accion;
        this.fechaHora = LocalDateTime.now();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsuario() { return usuario; }
    public String getEntidad() { return entidad; }
    public String getAccion() { return accion; }
    public LocalDateTime getFechaHora() { return fechaHora; }
}
