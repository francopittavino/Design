package capitalhumano.pantallas;

import capitalhumano.dto.CuestionarioDTO;
import capitalhumano.dto.PreguntaDTO;
import capitalhumano.entidades.estados.EstadoActivo;
import capitalhumano.entidades.estados.EstadoCompleto;
import capitalhumano.entidades.estados.EstadoEnProceso;
import capitalhumano.entidades.estados.EstadoIncompleto;
import capitalhumano.gestores.GestorCuestionario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** CU026 - Completar Cuestionario (instrucciones + wizard por bloques). */
@WebServlet("/cuestionario")
public class PantallaCompletarCuestionario extends PantallaBase {

    private static final String ACCESO_VERIFICADO = "cuestionario.accesoVerificado";
    private static final String REINGRESO = "cuestionario.reingreso";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession s = req.getSession();
        CuestionarioDTO dto = (CuestionarioDTO) s.getAttribute("cuestionarioDTO");
        GestorCuestionario gestor = GestorCuestionario.getInstance();

        // Paso 1: al ingresar se registra el acceso y se verifica el tiempo (una vez por ingreso)
        if (s.getAttribute(ACCESO_VERIFICADO) == null) {
            s.setAttribute(ACCESO_VERIFICADO, Boolean.TRUE);
            if (!gestor.verificarTiempo(dto)) {
                mostrarFin(req, resp, dto);
                return;
            }
            if (EstadoEnProceso.NOMBRE.equals(dto.getEstado())) s.setAttribute(REINGRESO, Boolean.TRUE);  // 1.C
        }

        String estado = gestor.obtenerEstado(dto);
        switch (estado) {
            case EstadoActivo.NOMBRE -> {
                // Paso 1: pantalla "Cuestionario - Instrucciones"
                req.setAttribute("instrucciones", gestor.obtenerInstrucciones());
                req.setAttribute("vista", "instrucciones");
                mostrarPantalla(req, resp);
            }
            case EstadoEnProceso.NOMBRE -> {
                if (dto.getBloqueActual() == 0) {   // ya respondió todo pero no se llegó a finalizar
                    gestor.finalizarCuestionario(dto);
                    mostrarFin(req, resp, dto);
                    return;
                }
                if (s.getAttribute(REINGRESO) != null) {
                    s.removeAttribute(REINGRESO);
                    req.setAttribute("reingreso", Boolean.TRUE);
                }
                mostrarBloque(req, resp, dto, Map.of(), List.of());
            }
            default -> mostrarFin(req, resp, dto);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CuestionarioDTO dto = (CuestionarioDTO) req.getSession().getAttribute("cuestionarioDTO");
        GestorCuestionario gestor = GestorCuestionario.getInstance();

        if ("iniciar".equals(param(req, "accion"))) {
            // Paso 2-3: acepta las instrucciones
            if (EstadoActivo.NOMBRE.equals(gestor.obtenerEstado(dto))) gestor.iniciarCuestionario(dto);
            redirigir(req, resp, "/cuestionario");
            return;
        }

        // Pasos 5-7: el actor responde el bloque y presiona Siguiente / Finalizar
        gestor.obtenerEstado(dto);
        if (entero(req, "bloque") != dto.getBloqueActual()) {   // bloque viejo (volvió atrás en el navegador)
            redirigir(req, resp, "/cuestionario");
            return;
        }
        List<PreguntaDTO> preguntas = gestor.obtenerBloque(dto, dto.getBloqueActual());
        Map<Integer, Integer> respuestas = new LinkedHashMap<>();
        List<String> sinResponder = new ArrayList<>();
        List<Integer> idsSinResponder = new ArrayList<>();
        for (PreguntaDTO p : preguntas) {
            int idItem = entero(req, "resp_" + p.getId());
            if (idItem > 0) {
                respuestas.put(p.getId(), idItem);
            } else {
                sinResponder.add("Pregunta " + p.getNombre());
                idsSinResponder.add(p.getId());
            }
        }
        if (!sinResponder.isEmpty()) {
            // 6.A: indica qué preguntas no fueron respondidas y mantiene las respuestas ya cargadas
            modal(req, new Modal(Modal.ADVERTENCIA, "Preguntas sin responder",
                    "Las siguientes preguntas no han sido respondidas:")
                    .lista(sinResponder).linea("Las respuestas ya ingresadas se conservan.").aceptar());
            mostrarBloque(req, resp, dto, respuestas, idsSinResponder);
            return;
        }
        if (!gestor.guardarRespuestas(dto, respuestas)) {
            mostrarFin(req, resp, dto);   // se venció el tiempo mientras respondía
            return;
        }
        if (dto.getBloqueActual() == 0) {
            // Paso 8: último bloque -> calcula y guarda los resultados, pasa a "Completo"
            gestor.finalizarCuestionario(dto);
            mostrarFin(req, resp, dto);
            return;
        }
        redirigir(req, resp, "/cuestionario");   // 8.A: siguiente bloque
    }

    private void mostrarBloque(HttpServletRequest req, HttpServletResponse resp, CuestionarioDTO dto,
                               Map<Integer, Integer> respuestas, List<Integer> sinResponder) throws ServletException, IOException {
        req.setAttribute("preguntas", GestorCuestionario.getInstance().obtenerBloque(dto, dto.getBloqueActual()));
        Map<String, String> marcadas = new LinkedHashMap<>();
        respuestas.forEach((k, v) -> marcadas.put(String.valueOf(k), String.valueOf(v)));
        req.setAttribute("marcadas", marcadas);
        Map<String, Boolean> faltan = new LinkedHashMap<>();
        sinResponder.forEach(id -> faltan.put(String.valueOf(id), Boolean.TRUE));
        req.setAttribute("faltan", faltan);
        req.setAttribute("vista", "bloque");
        mostrarPantalla(req, resp);
    }

    private void mostrarFin(HttpServletRequest req, HttpServletResponse resp, CuestionarioDTO dto) throws ServletException, IOException {
        String mensaje = switch (dto.getEstado()) {
            case EstadoCompleto.NOMBRE -> "El cuestionario ha sido completado con éxito.";
            case EstadoIncompleto.NOMBRE -> "Se superó el tiempo máximo para completar el cuestionario.";
            default -> "Se superó el tiempo máximo permitido para el cuestionario.";
        };
        req.setAttribute("mensajeFin", mensaje);
        req.setAttribute("finOk", EstadoCompleto.NOMBRE.equals(dto.getEstado()));
        req.setAttribute("vista", "fin");
        mostrarPantalla(req, resp);
        req.getSession().invalidate();   // el CU termina: el candidato ya no puede seguir con esta sesión
    }

    private void mostrarPantalla(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("tituloPagina", "Cuestionario");
        mostrar(req, resp, "cuestionario");
    }
}
