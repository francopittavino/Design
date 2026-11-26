package capitalhumano.dto;

/** Candidato con la clave (token) de su cuestionario: CU025 paso 11 y sesión del CU001/CU026. */
public class CandidatoCuestionarioDTO {
    private String nroCandidato;
    private String apellido;
    private String nombre;
    private String tipoDoc;
    private String nroDoc;
    private String token;

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
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
}
