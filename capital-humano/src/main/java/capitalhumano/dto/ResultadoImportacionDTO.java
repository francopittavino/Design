package capitalhumano.dto;

/** CU006 - Importar candidatos (fuera del alcance de las Etapas 7 y 8). */
public class ResultadoImportacionDTO {
    private int cantCreados;
    private int cantActualizados;
    private int cantEliminados;

    public ResultadoImportacionDTO() {}
    public ResultadoImportacionDTO(int cantCreados, int cantActualizados, int cantEliminados) {
        this.cantCreados = cantCreados;
        this.cantActualizados = cantActualizados;
        this.cantEliminados = cantEliminados;
    }

    public int getCantCreados() { return cantCreados; }
    public void setCantCreados(int cantCreados) { this.cantCreados = cantCreados; }
    public int getCantActualizados() { return cantActualizados; }
    public void setCantActualizados(int cantActualizados) { this.cantActualizados = cantActualizados; }
    public int getCantEliminados() { return cantEliminados; }
    public void setCantEliminados(int cantEliminados) { this.cantEliminados = cantEliminados; }
}
