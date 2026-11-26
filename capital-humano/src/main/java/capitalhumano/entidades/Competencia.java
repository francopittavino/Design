package capitalhumano.entidades;

import java.util.ArrayList;
import java.util.List;

public class Competencia {
    private int id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private TipoCompetencia tipo;
    private boolean activo = true;
    private List<Factor> factores = new ArrayList<>();

    public boolean puedeEliminarse() { return true; }

    /** Se puede evaluar si tiene al menos un factor con 2 o más preguntas (CU025 9.A / CU026 obs. 1). */
    public boolean puedeEvaluarse() {
        return factores.stream().anyMatch(Factor::tieneSuficientesPreguntas);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public TipoCompetencia getTipo() { return tipo; }
    public void setTipo(TipoCompetencia tipo) { this.tipo = tipo; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public List<Factor> getFactores() { return factores; }
    public void setFactores(List<Factor> factores) { this.factores = factores; }
}
