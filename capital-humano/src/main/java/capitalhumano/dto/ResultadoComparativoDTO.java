package capitalhumano.dto;

/** CU027 - Emitir reporte comparativo (fuera del alcance de las Etapas 7 y 8). */
public class ResultadoComparativoDTO {
    private String apellido;
    private String nombre;
    private String tipoDocumento;
    private String nroDocumento;
    private String nombreCompetencia;
    private double puntajeRequerido;
    private double puntajeObtenido;

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }
    public String getNroDocumento() { return nroDocumento; }
    public void setNroDocumento(String nroDocumento) { this.nroDocumento = nroDocumento; }
    public String getNombreCompetencia() { return nombreCompetencia; }
    public void setNombreCompetencia(String nombreCompetencia) { this.nombreCompetencia = nombreCompetencia; }
    public double getPuntajeRequerido() { return puntajeRequerido; }
    public void setPuntajeRequerido(double puntajeRequerido) { this.puntajeRequerido = puntajeRequerido; }
    public double getPuntajeObtenido() { return puntajeObtenido; }
    public void setPuntajeObtenido(double puntajeObtenido) { this.puntajeObtenido = puntajeObtenido; }
}
