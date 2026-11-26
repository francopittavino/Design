package capitalhumano.entidades;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Puesto {
    private int id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private boolean activo = true;
    private LocalDate fechaBaja;
    private Empresa empresa;
    private List<CaracteristicaPuesto> caracteristicas = new ArrayList<>();

    public boolean puedeEliminarse() { return true; }

    /** CU025 9.A: todas las competencias del puesto tienen al menos un factor evaluable. */
    public boolean todasCompetenciasEvaluables() {
        return getCompetencias().stream().allMatch(Competencia::puedeEvaluarse);
    }

    public List<Competencia> getCompetencias() {
        return caracteristicas.stream().map(CaracteristicaPuesto::getCompetencia).toList();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public LocalDate getFechaBaja() { return fechaBaja; }
    public void setFechaBaja(LocalDate fechaBaja) { this.fechaBaja = fechaBaja; }
    public Empresa getEmpresa() { return empresa; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }
    public List<CaracteristicaPuesto> getCaracteristicas() { return caracteristicas; }
    public void setCaracteristicas(List<CaracteristicaPuesto> caracteristicas) { this.caracteristicas = caracteristicas; }
}
