package capitalhumano.entidades;

public class Empresa {
    private int id;
    private String nombre;
    private boolean activo = true;

    public Empresa() {}
    public Empresa(int id, String nombre) { this.id = id; this.nombre = nombre; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
