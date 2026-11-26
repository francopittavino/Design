package capitalhumano.entidades;

import java.util.ArrayList;
import java.util.List;

public class OpcionDeRespuesta {
    private int id;
    private String nombre;
    private String descripcion;
    private boolean activo = true;
    private List<ItemOpcionDeRespuesta> items = new ArrayList<>();

    public boolean puedeEliminarse() { return true; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
    public List<ItemOpcionDeRespuesta> getItems() { return items; }
    public void setItems(List<ItemOpcionDeRespuesta> items) { this.items = items; }
}
