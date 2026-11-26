package capitalhumano.entidades;

/** Resultado de importar el CSV del Sistema de Gestión de Candidatos y CV (CU006, fuera del alcance). */
public class ResultadoImport {
    private int cantCreados;
    private int cantActualizados;
    private int cantEliminados;

    public ResultadoImport(int cantCreados, int cantActualizados, int cantEliminados) {
        this.cantCreados = cantCreados;
        this.cantActualizados = cantActualizados;
        this.cantEliminados = cantEliminados;
    }

    public String getResumen() {
        return "Creados: " + cantCreados + " - Actualizados: " + cantActualizados + " - Eliminados: " + cantEliminados;
    }

    public int getCantCreados() { return cantCreados; }
    public int getCantActualizados() { return cantActualizados; }
    public int getCantEliminados() { return cantEliminados; }
}
