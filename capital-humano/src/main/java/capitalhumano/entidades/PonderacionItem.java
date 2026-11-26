package capitalhumano.entidades;

public class PonderacionItem {
    private int id;
    private double valor;
    private ItemOpcionDeRespuesta itemOpcionDeRespuesta;

    public PonderacionItem() {}
    public PonderacionItem(double valor, ItemOpcionDeRespuesta item) {
        this.valor = valor;
        this.itemOpcionDeRespuesta = item;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }
    public ItemOpcionDeRespuesta getItemOpcionDeRespuesta() { return itemOpcionDeRespuesta; }
    public void setItemOpcionDeRespuesta(ItemOpcionDeRespuesta item) { this.itemOpcionDeRespuesta = item; }
}
