package capitalhumano.dto;

/**
 * Cuestionario del candidato que el CU001 deja en la sesión y usa el CU026
 * ("cuestionarioDTO" del diagrama de secuencia).
 */
public class CuestionarioDTO {
    private int idCuestionario;
    private String estado;
    private String puesto;
    private int cantBloques;
    private int bloqueActual;
    private CandidatoCuestionarioDTO candidato;

    public int getIdCuestionario() { return idCuestionario; }
    public void setIdCuestionario(int idCuestionario) { this.idCuestionario = idCuestionario; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getPuesto() { return puesto; }
    public void setPuesto(String puesto) { this.puesto = puesto; }
    public int getCantBloques() { return cantBloques; }
    public void setCantBloques(int cantBloques) { this.cantBloques = cantBloques; }
    public int getBloqueActual() { return bloqueActual; }
    public void setBloqueActual(int bloqueActual) { this.bloqueActual = bloqueActual; }
    public CandidatoCuestionarioDTO getCandidato() { return candidato; }
    public void setCandidato(CandidatoCuestionarioDTO candidato) { this.candidato = candidato; }
}
