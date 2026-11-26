package capitalhumano.pantallas;

import capitalhumano.dto.EvaluacionDTO;
import capitalhumano.dto.PuestoDTO;
import capitalhumano.gestores.GestorPuestos;
import capitalhumano.gestores.GestorReportes;
import capitalhumano.gestores.ValidacionException;
import capitalhumano.reportes.OrdenMerito;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** CU028 - Emitir Orden de Mérito. */
@WebServlet({"/orden-merito", "/orden-merito/imprimir"})
public class PantallaOrdenMerito extends PantallaBase {

    private static final String ORDEN = "ordenMerito";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getServletPath().endsWith("/imprimir")) {
            imprimir(req, resp);
            return;
        }
        Map<String, String> filtros = filtros(req);
        if ("buscar".equals(param(req, "accion"))) {
            if (filtros.values().stream().allMatch(String::isEmpty)) {
                modal(req, new Modal(Modal.ADVERTENCIA, "Criterios de búsqueda",
                        "Debe ingresar al menos un criterio de búsqueda").aceptar());
            } else {
                List<PuestoDTO> puestos = GestorPuestos.getInstance().buscarPuestos(filtros);
                req.setAttribute("puestos", puestos);
                if (puestos.isEmpty()) {
                    modal(req, new Modal(Modal.INFO, "Sin resultados", "No se encontraron puestos con los criterios ingresados").aceptar());
                }
            }
        }
        mostrarBusqueda(req, resp, filtros);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> filtros = filtros(req);
        int idPuesto = entero(req, "idPuesto");
        GestorReportes gestor = GestorReportes.getInstance();

        if (idPuesto == 0) {
            req.setAttribute("puestos", GestorPuestos.getInstance().buscarPuestos(filtros));
            modal(req, new Modal(Modal.ADVERTENCIA, "Seleccionar puesto", "Debe seleccionar un puesto de la grilla").aceptar());
            mostrarBusqueda(req, resp, filtros);
            return;
        }
        List<PuestoDTO> puestos = GestorPuestos.getInstance().buscarPuestos(filtros);
        req.setAttribute("puestos", puestos);
        req.setAttribute("idPuesto", idPuesto);
        PuestoDTO puesto = puestos.stream().filter(p -> p.getId() == idPuesto).findFirst().orElse(new PuestoDTO(idPuesto));

        if ("emitir".equals(param(req, "accion"))) {
            String[] ids = req.getParameterValues("idEvaluacion");
            boolean todas = !param(req, "todas").isEmpty();
            List<EvaluacionDTO> seleccion = new ArrayList<>();
            if (!todas && ids != null) for (String id : ids) seleccion.add(new EvaluacionDTO(Integer.parseInt(id)));
            if (!todas && seleccion.isEmpty()) {
                req.setAttribute("evaluaciones", gestor.buscarEvaluaciones(puesto));
                req.setAttribute("errorSeleccion", "Debe seleccionar al menos una evaluación o la opción TODAS");
                mostrarBusqueda(req, resp, filtros);
                return;
            }
            try {
                OrdenMerito om = gestor.emitirOrdenMerito(puesto, seleccion);
                req.getSession().setAttribute(ORDEN, om);
                if (!om.tieneCuestionariosCompletos()) {
                    modal(req, new Modal(Modal.ADVERTENCIA, "Sin cuestionarios completos",
                            "Ninguna de las evaluaciones seleccionadas tiene cuestionarios completos.",
                            "El orden de mérito no tendrá puntajes.").aceptar());
                }
                req.setAttribute("om", om);
                req.setAttribute("filtros", filtros);
                req.setAttribute("tituloPagina", "Orden de Mérito");
                mostrar(req, resp, "orden-merito-reporte");
            } catch (ValidacionException e) {
                modal(req, new Modal(Modal.ADVERTENCIA, "Orden de mérito").lista(e.getErrores()).aceptar());
                mostrarBusqueda(req, resp, filtros);
            }
            return;
        }

        // Paso 5-6: el actor presiona "Emitir el orden de mérito" sobre un puesto
        List<EvaluacionDTO> evaluaciones = gestor.buscarEvaluaciones(puesto);
        if (evaluaciones.isEmpty()) {
            // 6.A: mensaje y continúa en el paso 4
            modal(req, new Modal(Modal.ADVERTENCIA, "Sin evaluaciones",
                    "El puesto " + puesto.getNombre() + " no posee evaluaciones.").aceptar());
        } else {
            req.setAttribute("evaluaciones", evaluaciones);
        }
        mostrarBusqueda(req, resp, filtros);
    }

    /** Pasos 8-9: "Imprimir" genera el PDF y el navegador abre el diálogo de impresión. */
    private void imprimir(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        OrdenMerito om = (OrdenMerito) req.getSession().getAttribute(ORDEN);
        if (om == null) {
            redirigir(req, resp, "/orden-merito");
            return;
        }
        resp.setContentType("application/pdf");
        resp.setHeader("Content-Disposition", "inline; filename=\"orden-merito-" + om.getPuesto().getCodigo() + ".pdf\"");
        GestorReportes.getInstance().imprimirOrdenMerito(om, resp.getOutputStream());
    }

    private void mostrarBusqueda(HttpServletRequest req, HttpServletResponse resp, Map<String, String> filtros)
            throws ServletException, IOException {
        req.setAttribute("filtros", filtros);
        req.setAttribute("tituloPagina", "Orden de Mérito");
        mostrar(req, resp, "orden-merito");
    }

    private static Map<String, String> filtros(HttpServletRequest req) {
        Map<String, String> f = new LinkedHashMap<>();
        f.put("codigo", param(req, "codigo").toUpperCase());
        f.put("nombre", param(req, "nombre").toUpperCase());
        f.put("empresa", param(req, "empresa").toUpperCase());
        return f;
    }
}
