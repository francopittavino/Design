package capitalhumano.pantallas;

import capitalhumano.dto.FactorDTO;
import capitalhumano.dto.ItemOpcionRespuestaDTO;
import capitalhumano.dto.PreguntaDTO;
import capitalhumano.facade.ServicioExternoException;
import capitalhumano.gestores.GestorCompetencias;
import capitalhumano.gestores.GestorFactores;
import capitalhumano.gestores.GestorOpcionesRespuesta;
import capitalhumano.gestores.GestorPreguntas;
import capitalhumano.gestores.ValidacionException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/** CU021 - Dar de alta Pregunta. */
@WebServlet("/preguntas/nueva")
public class FormularioPregunta extends PantallaBase {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (param(req, "accion")) {
            // Carga asistida de los combos (pedidos del navegador)
            case "factores" -> json(resp, GestorFactores.getInstance()
                    .buscarFactores(Map.of("idCompetencia", entero(req, "idCompetencia"))).stream()
                    .map(f -> "{\"id\":" + f.getId() + ",\"nombre\":" + texto(f.getNombre()) + "}").toList());
            case "items" -> json(resp, GestorOpcionesRespuesta.getInstance().obtenerItems(entero(req, "idOpcion")).stream()
                    .map(i -> "{\"id\":" + i.getId() + ",\"orden\":" + i.getOrden() + ",\"descripcion\":" + texto(i.getDescripcion()) + "}")
                    .toList());
            default -> mostrarFormulario(req, resp, new PreguntaDTO(), List.of());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PreguntaDTO dto = leer(req);
        switch (param(req, "accion")) {
            case "ia" -> {
                // 1.A.1 -> CU033 - Generar pregunta con IA
                try {
                    PreguntaDTO propuesta = GestorPreguntas.getInstance().generarConIA(dto.getIdCompetencia(), dto.getIdFactor());
                    dto.setNombre(propuesta.getNombre());
                    dto.setEnunciado(propuesta.getEnunciado());
                    dto.setDescripcion(propuesta.getDescripcion());
                } catch (ServicioExternoException e) {
                    modal(req, new Modal(Modal.ADVERTENCIA, "Servicio de IA",
                            "Ocurrió un error al comunicarse con el servicio de IA, intente nuevamente más tarde").aceptar());
                }
                mostrarFormulario(req, resp, dto, List.of());
            }
            case "nueva" -> mostrarFormulario(req, resp, new PreguntaDTO(), List.of());   // 5.A "SI": vuelve al paso 1
            default -> {
                try {
                    PreguntaDTO creada = GestorPreguntas.getInstance().crearPregunta(dto, consultor(req).getUsuario());
                    // 3 - 4: mensaje de éxito con los botones SI / NO
                    modal(req, new Modal(Modal.OK, "Alta exitosa",
                            "La pregunta " + creada.getNombre() + " se ha creado correctamente.", "¿Desea cargar otra?")
                            .boton("SI", req.getContextPath() + "/preguntas/nueva", "accion", "nueva")
                            .boton("NO", req.getContextPath() + "/preguntas", "creada", creada.getNombre()));
                    mostrarFormulario(req, resp, dto, List.of());
                } catch (ValidacionException e) {
                    // 2.A / 2.B / 2.C: se muestran TODOS los errores sin tapar campos ni botones
                    mostrarFormulario(req, resp, dto, e.getErrores());
                }
            }
        }
    }

    private void mostrarFormulario(HttpServletRequest req, HttpServletResponse resp, PreguntaDTO dto, List<String> errores)
            throws ServletException, IOException {
        req.setAttribute("p", dto);
        Map<String, String> pond = new java.util.HashMap<>();
        dto.getPonderaciones().forEach((k, v) -> pond.put(String.valueOf(k), v));
        req.setAttribute("pond", pond);   // EL convierte los int a Long: se indexa por String
        req.setAttribute("errores", errores);
        req.setAttribute("competencias", GestorCompetencias.getInstance().buscarCompetencias(null));
        req.setAttribute("opciones", GestorOpcionesRespuesta.getInstance().buscarOpciones(null));
        List<FactorDTO> factores = dto.getIdCompetencia() > 0
                ? GestorFactores.getInstance().buscarFactores(Map.of("idCompetencia", dto.getIdCompetencia())) : List.of();
        req.setAttribute("factores", factores);
        List<ItemOpcionRespuestaDTO> items = dto.getIdOpcionRespuesta() > 0
                ? GestorOpcionesRespuesta.getInstance().obtenerItems(dto.getIdOpcionRespuesta()) : List.of();
        req.setAttribute("items", items);
        req.setAttribute("tituloPagina", "Dar de alta Pregunta");
        mostrar(req, resp, "pregunta-alta");
    }

    private PreguntaDTO leer(HttpServletRequest req) {
        PreguntaDTO dto = new PreguntaDTO();
        dto.setIdCompetencia(entero(req, "idCompetencia"));
        dto.setIdFactor(entero(req, "idFactor"));
        dto.setNombre(param(req, "nombre").toUpperCase());
        dto.setEnunciado(param(req, "enunciado").toUpperCase());
        dto.setDescripcion(param(req, "descripcion").toUpperCase());
        dto.setIdOpcionRespuesta(entero(req, "idOpcion"));
        if (dto.getIdOpcionRespuesta() > 0) {
            for (ItemOpcionRespuestaDTO item : GestorOpcionesRespuesta.getInstance().obtenerItems(dto.getIdOpcionRespuesta())) {
                dto.getPonderaciones().put(item.getId(), param(req, "pond_" + item.getId()));
            }
        }
        return dto;
    }

    private static void json(HttpServletResponse resp, List<String> objetos) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            out.print("[" + String.join(",", objetos) + "]");
        }
    }

    private static String texto(String s) {
        StringBuilder sb = new StringBuilder("\"");
        for (char ch : s.toCharArray()) {
            switch (ch) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '<' -> sb.append("\\u003c");
                default -> { if (ch < 0x20) sb.append(String.format("\\u%04x", (int) ch)); else sb.append(ch); }
            }
        }
        return sb.append('"').toString();
    }
}
