package capitalhumano.dto;

/** Ítem de una opción de respuesta (grilla Orden / Descripción / Ponderación del CU021). */
public class ItemOpcionRespuestaDTO {
    private int id;
    private int orden;
    private String descripcion;

    public ItemOpcionRespuestaDTO(int id, int orden, String descripcion) {
        this.id = id;
        this.orden = orden;
        this.descripcion = descripcion;
    }

    public int getId() { return id; }
    public int getOrden() { return orden; }
    public String getDescripcion() { return descripcion; }
}
