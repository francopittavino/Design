package capitalhumano.dto;

import java.util.ArrayList;
import java.util.List;

public class PuestoDTO {
    private int id;
    private String codigo;
    private String nombre;
    private String empresa;
    private int cantCandidatos;
    private int cantEvaluacionesCompletas;
    /** Competencias con su ponderación mínima (CU025 paso 9). */
    private List<CaracteristicaPuestoDTO> caracteristicas = new ArrayList<>();

    public PuestoDTO() {}
    public PuestoDTO(int id) { this.id = id; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }
    public int getCantCandidatos() { return cantCandidatos; }
    public void setCantCandidatos(int cantCandidatos) { this.cantCandidatos = cantCandidatos; }
    public int getCantEvaluacionesCompletas() { return cantEvaluacionesCompletas; }
    public void setCantEvaluacionesCompletas(int n) { this.cantEvaluacionesCompletas = n; }
    public List<CaracteristicaPuestoDTO> getCaracteristicas() { return caracteristicas; }
    public void setCaracteristicas(List<CaracteristicaPuestoDTO> caracteristicas) { this.caracteristicas = caracteristicas; }
}
