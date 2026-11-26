package capitalhumano.dto;

public class CandidatoGestionDTO {
    private String nroCandidato;
    private String apellido;
    private String nombre;
    private String tipoDoc;
    private String nroDoc;
    private String email;
    private String escolaridad;

    public String getNroCandidato() { return nroCandidato; }
    public void setNroCandidato(String nroCandidato) { this.nroCandidato = nroCandidato; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTipoDoc() { return tipoDoc; }
    public void setTipoDoc(String tipoDoc) { this.tipoDoc = tipoDoc; }
    public String getNroDoc() { return nroDoc; }
    public void setNroDoc(String nroDoc) { this.nroDoc = nroDoc; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getEscolaridad() { return escolaridad; }
    public void setEscolaridad(String escolaridad) { this.escolaridad = escolaridad; }
}
