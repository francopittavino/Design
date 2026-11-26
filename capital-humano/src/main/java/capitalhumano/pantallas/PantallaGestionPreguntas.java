package capitalhumano.pantallas;

import capitalhumano.dto.PreguntaDTO;
import capitalhumano.gestores.GestorCompetencias;
import capitalhumano.gestores.GestorFactores;
import capitalhumano.gestores.GestorPreguntas;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** CU020 - Gestionar Preguntas. */
@WebServlet("/preguntas")
public class PantallaGestionPreguntas extends PantallaBase {

    private static final String ULTIMA_BUSQUEDA = "preguntas.ultimaBusqueda";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> filtros = new LinkedHashMap<>();
        filtros.put("idCompetencia", param(req, "idCompetencia"));
        filtros.put("idFactor", param(req, "idFactor"));
        filtros.put("nombre", param(req, "nombre").toUpperCase());
        req.setAttribute("filtros", filtros);

        int idCompetencia = entero(req, "idCompetencia");
        req.setAttribute("competencias", GestorCompetencias.getInstance().buscarCompetencias(null));
        req.setAttribute("factores", idCompetencia > 0
                ? GestorFactores.getInstance().buscarFactores(Map.of("idCompetencia", idCompetencia)) : List.of());

        String accion = param(req, "accion");
        if ("buscar".equals(accion)) {
            boolean hayCriterio = entero(req, "idCompetencia") > 0 || entero(req, "idFactor") > 0 || !filtros.get("nombre").isEmpty();
            if (!hayCriterio) {
                modal(req, new Modal(Modal.ADVERTENCIA, "Criterios de búsqueda",
                        "Debe ingresar al menos un criterio de búsqueda").aceptar());
            } else {
                List<PreguntaDTO> preguntas = GestorPreguntas.getInstance().buscarPreguntas(filtros);
                req.setAttribute("preguntas", preguntas);
                req.getSession().setAttribute(ULTIMA_BUSQUEDA, req.getQueryString());
                if (preguntas.isEmpty()) {
                    modal(req, new Modal(Modal.INFO, "Sin resultados",
                            "No se encontraron preguntas con los criterios ingresados").aceptar());
                }
            }
        } else if ("noImplementado".equals(accion)) {
            String cu = "modificar".equals(param(req, "cu")) ? "CU022 - Modificar Pregunta" : "CU023 - Dar de baja Pregunta";
            modal(req, new Modal(Modal.INFO, "No disponible",
                    "El caso de uso \"" + cu + "\" no forma parte de las etapas 7 y 8.").aceptar());
            if (req.getSession().getAttribute(ULTIMA_BUSQUEDA) != null) {
                req.setAttribute("preguntas", GestorPreguntas.getInstance().buscarPreguntas(filtros));
            }
        }
        req.setAttribute("tituloPagina", "Gestionar Preguntas");
        mostrar(req, resp, "preguntas");
    }

    /** Vuelta desde el CU021 ("NO" / "Cancelar"): 3.A.2 el sistema actualiza la grilla. */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String ultima = (String) req.getSession().getAttribute(ULTIMA_BUSQUEDA);
        String creada = param(req, "creada");
        if (ultima != null) {
            redirigir(req, resp, "/preguntas?" + ultima);
        } else if (!creada.isEmpty()) {
            redirigir(req, resp, "/preguntas?accion=buscar&nombre=" + URLEncoder.encode(creada, StandardCharsets.UTF_8));
        } else {
            redirigir(req, resp, "/preguntas");
        }
    }
}
