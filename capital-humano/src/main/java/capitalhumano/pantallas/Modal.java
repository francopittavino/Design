package capitalhumano.pantallas;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Ventana de mensaje (tabla de mensajes de cada CU) que muestra _pie.jspf. */
public class Modal {
    public static final String ADVERTENCIA = "⚠";
    public static final String PROHIBIDO = "⛔";
    public static final String INFO = "ℹ";
    public static final String OK = "✔";

    private final String titulo;
    private final String icono;
    private final List<String> lineas = new ArrayList<>();
    private final List<String> lista = new ArrayList<>();
    private final List<Boton> botones = new ArrayList<>();

    public Modal(String icono, String titulo, String... lineas) {
        this.icono = icono;
        this.titulo = titulo;
        this.lineas.addAll(List.of(lineas));
    }

    /** Botón que solo cierra la ventana. */
    public Modal aceptar() { return boton("ACEPTAR", null); }

    /** Botón que hace POST a {@code accion} con los parámetros dados (clave, valor, clave, valor...). */
    public Modal boton(String texto, String accion, String... parametros) {
        Map<String, String> p = new LinkedHashMap<>();
        for (int i = 0; i + 1 < parametros.length; i += 2) p.put(parametros[i], parametros[i + 1]);
        botones.add(new Boton(texto, accion, p));
        return this;
    }

    public Modal lista(List<String> items) {
        lista.addAll(items);
        return this;
    }

    public Modal linea(String l) {
        lineas.add(l);
        return this;
    }

    public String getTitulo() { return titulo; }
    public String getIcono() { return icono; }
    public List<String> getLineas() { return lineas; }
    public List<String> getLista() { return lista; }
    public List<Boton> getBotones() { return botones; }

    public static class Boton {
        private final String texto;
        private final String accion;
        private final Map<String, String> parametros;

        Boton(String texto, String accion, Map<String, String> parametros) {
            this.texto = texto;
            this.accion = accion;
            this.parametros = parametros;
        }

        public String getTexto() { return texto; }
        public String getAccion() { return accion; }
        public Map<String, String> getParametros() { return parametros; }
    }
}
