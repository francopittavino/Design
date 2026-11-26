package capitalhumano.dto;

import java.time.LocalDateTime;

public class EvaluacionDTO {
    private int id;
    private String candidato;
    private String puesto;
    private String estado;
    private LocalDateTime fechaCreacion;
    private int cantCandidatos;
    private int cantCompletos;

    public EvaluacionDTO() {}
    public EvaluacionDTO(int id) { this.id = id; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCandidato() { return candidato; }
    public void setCandidato(String candidato) { this.candidato = candidato; }
    public String getPuesto() { return puesto; }
    public void setPuesto(String puesto) { this.puesto = puesto; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public int getCantCandidatos() { return cantCandidatos; }
    public void setCantCandidatos(int cantCandidatos) { this.cantCandidatos = cantCandidatos; }
    public int getCantCompletos() { return cantCompletos; }
    public void setCantCompletos(int cantCompletos) { this.cantCompletos = cantCompletos; }
}
