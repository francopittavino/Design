package capitalhumano.reportes;

import capitalhumano.exportacion.ExportadorFormato;

/** Patrón Bridge (lado abstracción): el reporte no sabe en qué formato se exporta. */
public abstract class Reporte {
    protected ExportadorFormato formatoExportacion;

    public abstract void generar();

    public void setFormato(ExportadorFormato f) { this.formatoExportacion = f; }
}
