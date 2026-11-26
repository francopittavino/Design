package capitalhumano.entidades;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Candidato {
    private int id;
    private String nroCandidato;
    private String apellido;
    private String nombre;
    private TipoDocumento tipoDoc;
    private String nroDoc;
    private LocalDate fechaNacimiento;
    private Genero genero;
    private String nacionalidad;
    private String email;
    private String escolaridad;
    private boolean activo = true;
    /** Clave del cuestionario activo / en proceso (en la BD vive en cuestionario.clave). */
    private String claveAcceso;
    /** Cuestionarios del candidato (navegación inversa de Cuestionario -> Candidato). */
    private List<Cuestionario> cuestionarios = new ArrayList<>();

    /** CU025 6.A: no se puede evaluar a quien tiene un cuestionario Activo o En Proceso. */
    public boolean tieneCuestionarioActivo() {
        return cuestionarios.stream().anyMatch(c -> c.estaActivo() || c.estaEnProceso());
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNroCandidato() { return nroCandidato; }
    public void setNroCandidato(String nroCandidato) { this.nroCandidato = nroCandidato; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public TipoDocumento getTipoDoc() { return tipoDoc; }
    public void setTipoDoc(TipoDocumento tipoDoc) { this.tipoDoc = tipoDoc; }
    public String getNroDoc() { return nroDoc; }
    public void setNroDoc(String nroDoc) { this.nroDoc = nroDoc; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public Genero getGenero() { return genero; }
    public void setGenero(Genero genero) { this.genero = genero; }
    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getEscolaridad() { return escolaridad; }
    public void setEscolaridad(String escolaridad) { this.escolaridad = escolaridad; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public String getClaveAcceso() { return claveAcceso; }
    public void setClaveAcceso(String claveAcceso) { this.claveAcceso = claveAcceso; }
    public List<Cuestionario> getCuestionarios() { return cuestionarios; }
    public void setCuestionarios(List<Cuestionario> cuestionarios) { this.cuestionarios = cuestionarios; }
}
