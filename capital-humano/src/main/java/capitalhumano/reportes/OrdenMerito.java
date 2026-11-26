package capitalhumano.reportes;

import capitalhumano.entidades.Puesto;
import capitalhumano.entidades.estados.EstadoActivo;
import capitalhumano.entidades.estados.EstadoEnProceso;
import capitalhumano.entidades.estados.EstadoIncompleto;
import capitalhumano.entidades.estados.EstadoSinContestar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * CU028: orden de mérito de un puesto.
 * Grupo A: cuestionarios completos que alcanzaron las ponderaciones mínimas, ordenados por puntaje.
 * Grupo B: completos que no las alcanzaron (también por puntaje).
 * Grupo C: el resto, agrupados por estado del cuestionario.
 */
public class OrdenMerito extends Reporte {
    private static final List<String> ORDEN_ESTADOS = List.of(
            EstadoEnProceso.NOMBRE, EstadoActivo.NOMBRE, EstadoIncompleto.NOMBRE, EstadoSinContestar.NOMBRE);

    private final Puesto puesto;
    private final List<ItemOrdenMerito> items = new ArrayList<>();
    private String descripcionEvaluaciones = "TODAS";

    private List<ItemOrdenMerito> alcanzaron = List.of();
    private List<ItemOrdenMerito> noAlcanzaron = List.of();
    private Map<String, List<ItemOrdenMerito>> porEstado = Map.of();

    public OrdenMerito(Puesto puesto) { this.puesto = puesto; }

    public void agregarItem(ItemOrdenMerito item) { items.add(item); }

    public boolean tieneCuestionariosCompletos() {
        return items.stream().anyMatch(ItemOrdenMerito::isCompleto);
    }

    @Override
    public void generar() {
        Comparator<ItemOrdenMerito> porPuntaje = Comparator
                .comparing(ItemOrdenMerito::getPuntajeObtenido, Comparator.reverseOrder())
                .thenComparing(i -> i.getCandidato().getApellido())
                .thenComparing(i -> i.getCandidato().getNombre());
        alcanzaron = items.stream().filter(i -> i.isCompleto() && i.isCumplePonderaciones()).sorted(porPuntaje).toList();
        for (int i = 0; i < alcanzaron.size(); i++) alcanzaron.get(i).setPosicion(i + 1);
        noAlcanzaron = items.stream().filter(i -> i.isCompleto() && !i.isCumplePonderaciones()).sorted(porPuntaje).toList();

        Map<String, List<ItemOrdenMerito>> grupos = new LinkedHashMap<>();
        for (String estado : ORDEN_ESTADOS) {
            List<ItemOrdenMerito> g = items.stream()
                    .filter(i -> !i.isCompleto() && i.getCuestionario().getNombreEstado().equals(estado))
                    .sorted(Comparator.comparing((ItemOrdenMerito i) -> i.getCandidato().getApellido())
                            .thenComparing(i -> i.getCandidato().getNombre()))
                    .toList();
            if (!g.isEmpty()) grupos.put(estado, g);
        }
        porEstado = grupos;
    }

    /** CU028 pasos 8-9: delega en el exportador que le pasó el gestor (Bridge). */
    public boolean imprimir() {
        Map<String, List<List<String>>> datos = new LinkedHashMap<>();
        List<String> encabezado = List.of("Pos.", "Tipo doc.", "Nro. doc.", "Apellido", "Nombre", "Puntaje", "Datos adicionales");
        datos.put("A - Candidatos que alcanzaron las ponderaciones mínimas (ordenado por puntaje)", filas(encabezado, alcanzaron, true));
        datos.put("B - Candidatos que no alcanzaron las ponderaciones mínimas", filas(encabezado, noAlcanzaron, true));
        porEstado.forEach((estado, lista) ->
                datos.put("C - Cuestionarios en estado " + estado, filas(encabezado, lista, false)));
        String titulo = "ORDEN DE MÉRITO - " + puesto.getCodigo() + " " + puesto.getNombre() + " / "
                + puesto.getEmpresa().getNombre() + " - Evaluaciones: " + descripcionEvaluaciones;
        return formatoExportacion.exportar(titulo, datos);
    }

    private static List<List<String>> filas(List<String> encabezado, List<ItemOrdenMerito> lista, boolean conPuntaje) {
        List<List<String>> filas = new ArrayList<>();
        filas.add(encabezado);
        for (ItemOrdenMerito i : lista) {
            filas.add(List.of(
                    i.getPosicion() > 0 ? i.getPosicion() + "°" : "-",
                    i.getCandidato().getTipoDoc().name(),
                    i.getCandidato().getNroDoc(),
                    i.getCandidato().getApellido(),
                    i.getCandidato().getNombre(),
                    conPuntaje && i.getPuntajeObtenido() != null ? String.format("%.2f", i.getPuntajeObtenido()) : "",
                    i.getDatosAdicionales()));
        }
        return filas;
    }

    public Puesto getPuesto() { return puesto; }
    public List<ItemOrdenMerito> getItems() { return items; }
    public List<ItemOrdenMerito> getAlcanzaron() { return alcanzaron; }
    public List<ItemOrdenMerito> getNoAlcanzaron() { return noAlcanzaron; }
    public Map<String, List<ItemOrdenMerito>> getPorEstado() { return porEstado; }
    public String getDescripcionEvaluaciones() { return descripcionEvaluaciones; }
    public void setDescripcionEvaluaciones(String d) { this.descripcionEvaluaciones = d; }
}
