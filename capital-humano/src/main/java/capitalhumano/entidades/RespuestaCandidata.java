package capitalhumano.entidades;

public class RespuestaCandidata {
    private int id;
    private double ponderacion;
    private ItemOpcionDeRespuesta itemOpcionDeRespuesta;

    public RespuestaCandidata() {}

    public RespuestaCandidata(ItemOpcionDeRespuesta item, double ponderacion) {
        this.itemOpcionDeRespuesta = item;
        this.ponderacion = ponderacion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public double getPonderacion() { return ponderacion; }
    public void setPonderacion(double ponderacion) { this.ponderacion = ponderacion; }
    public ItemOpcionDeRespuesta getItemOpcionDeRespuesta() { return itemOpcionDeRespuesta; }
    public void setItemOpcionDeRespuesta(ItemOpcionDeRespuesta item) { this.itemOpcionDeRespuesta = item; }
}
