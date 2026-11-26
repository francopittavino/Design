package capitalhumano.exportacion;

import java.util.List;
import java.util.Map;

/**
 * Patrón Bridge (lado implementación): formato de salida de los reportes.
 * {@code datos}: sección -> filas; la primera fila de cada sección es el encabezado.
 * Cada exportador escribe en el OutputStream que recibe al crearse (en la web, la respuesta HTTP).
 */
public interface ExportadorFormato {
    boolean exportar(String titulo, Map<String, List<List<String>>> datos);
}
