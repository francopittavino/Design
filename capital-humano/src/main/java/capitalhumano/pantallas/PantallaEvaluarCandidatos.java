package capitalhumano.pantallas;

import capitalhumano.dto.CandidatoCuestionarioDTO;
import capitalhumano.dto.CandidatoGestionDTO;
import capitalhumano.dto.EvaluacionDTO;
import capitalhumano.dto.PuestoDTO;
import capitalhumano.gestores.GestorCandidatos;
import capitalhumano.gestores.GestorEvaluacion;
import capitalhumano.gestores.GestorPuestos;
import capitalhumano.gestores.ValidacionException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** CU025 - Evaluar a Candidatos (wizard de 3 pasos + exportación de claves). */
@WebServlet({"/evaluar", "/evaluar/claves"})
public class PantallaEvaluarCandidatos extends PantallaBase {

    /** Estado del wizard en la sesión del consultor (los gestores son singleton y no guardan estado). */
    public static class EvaluacionEnCurso implements Serializable {
        final Map<String, CandidatoGestionDTO> aEvaluar = new LinkedHashMap<>();
        PuestoDTO puesto;
        List<CandidatoCuestionarioDTO> claves = List.of();
        EvaluacionDTO evaluacion;

        public List<CandidatoGestionDTO> getCandidatosAEvaluar() { return new ArrayList<>(aEvaluar.values()); }
        public PuestoDTO getPuesto() { return puesto; }
        public List<CandidatoCuestionarioDTO> getClaves() { return claves; }
        public EvaluacionDTO getEvaluacion() { return evaluacion; }
    }

    private static final String ESTADO = "evaluacionEnCurso";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getServletPath().endsWith("/claves")) {
            descargarClaves(req, resp);
            return;
        }
        EvaluacionEnCurso ev = estado(req);
        if ("nueva".equals(param(req, "accion")) || ev.evaluacion != null) {
            req.getSession().removeAttribute(ESTADO);
            ev = estado(req);
        }
        int paso = Math.max(1, entero(req, "paso"));
        if (paso == 1 && "buscar".equals(param(req, "accion"))) buscarCandidatos(req);
        if (paso == 2 && ev.aEvaluar.isEmpty()) paso = 1;
        if (paso == 3 && ev.claves.isEmpty()) paso = 1;
        mostrarPaso(req, resp, paso);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EvaluacionEnCurso ev = estado(req);
        switch (param(req, "accion")) {
            case "agregar" -> {   // pasos 4-5 / 4.A
                String[] nros = req.getParameterValues("nro");
                if (nros == null || nros.length == 0) {
                    modal(req, new Modal(Modal.ADVERTENCIA, "Sin candidatos", "Debe seleccionar al menos un candidato para evaluar").aceptar());
                } else {
                    for (CandidatoGestionDTO c : GestorCandidatos.getInstance().buscarCandidatos(filtros(req))) {
                        for (String nro : nros) if (c.getNroCandidato().equals(nro)) ev.aEvaluar.put(nro, c);
                    }
                }
                buscarCandidatos(req);
                mostrarPaso(req, resp, 1);
            }
            case "quitar" -> {   // 5.A
                String[] nros = req.getParameterValues("quitar");
                if (nros != null) for (String nro : nros) ev.aEvaluar.remove(nro);
                buscarCandidatos(req);
                mostrarPaso(req, resp, 1);
            }
            case "siguiente1" -> siguientePaso1(req, resp, ev);
            case "elegirPuesto" -> {   // paso 8-9
                ev.puesto = GestorPuestos.getInstance().obtenerDetalle(entero(req, "idPuesto"));
                mostrarPaso(req, resp, 2);
            }
            case "siguiente2" -> siguientePaso2(req, resp, ev);
            case "finalizar" -> finalizar(req, resp, ev);
            case "exportar" -> mostrarPaso(req, resp, 4);   // 14-15: pantalla para guardar el archivo
            case "preguntarExportar" -> {   // 16.A: cancela y vuelve a preguntar
                preguntarExportar(req);
                mostrarPaso(req, resp, 3);
            }
            default -> mostrarPaso(req, resp, 1);
        }
    }

    /** Paso 6 / 6.A: nadie puede tener un cuestionario activo o en proceso. */
    private void siguientePaso1(HttpServletRequest req, HttpServletResponse resp, EvaluacionEnCurso ev) throws ServletException, IOException {
        if (ev.aEvaluar.isEmpty()) {
            modal(req, new Modal(Modal.ADVERTENCIA, "Sin candidatos", "Debe seleccionar al menos un candidato para evaluar").aceptar());
            buscarCandidatos(req);
            mostrarPaso(req, resp, 1);
            return;
        }
        List<CandidatoGestionDTO> bloqueados = GestorCandidatos.getInstance()
                .verificarCuestionariosActivos(new ArrayList<>(ev.aEvaluar.keySet()));
        if (!bloqueados.isEmpty()) {
            modal(req, new Modal(Modal.PROHIBIDO, "Cuestionario activo",
                    "Los siguientes candidatos poseen cuestionarios activos o en proceso y no pueden ser evaluados nuevamente:")
                    .lista(bloqueados.stream().map(c -> c.getApellido() + ", " + c.getNombre()).toList()).aceptar());
            buscarCandidatos(req);
            mostrarPaso(req, resp, 1);
            return;
        }
        mostrarPaso(req, resp, 2);
    }

    /** Pasos 10-11 / 10.A / 9.A. */
    private void siguientePaso2(HttpServletRequest req, HttpServletResponse resp, EvaluacionEnCurso ev) throws ServletException, IOException {
        if (ev.puesto == null) {
            modal(req, new Modal(Modal.ADVERTENCIA, "Sin función", "Debe seleccionar una función o puesto para continuar").aceptar());
            mostrarPaso(req, resp, 2);
            return;
        }
        GestorEvaluacion gestor = GestorEvaluacion.getInstance();
        if (!gestor.validarCompetenciasEvaluables(ev.puesto)) {
            // 9.A: muestra las competencias, el actor presiona Aceptar y el CU vuelve a la pantalla principal
            modal(req, new Modal(Modal.ADVERTENCIA, "Competencias sin factores",
                    "Las siguientes competencias no pueden ser evaluadas:")
                    .lista(gestor.competenciasNoEvaluables(ev.puesto))
                    .boton("ACEPTAR", req.getContextPath() + "/menu", "cancelar", "evaluacion"));
            mostrarPaso(req, resp, 2);
            return;
        }
        ev.claves = gestor.generarClaves(new ArrayList<>(ev.aEvaluar.values()));
        mostrarPaso(req, resp, 3);
    }

    /** Pasos 12-13: guarda la evaluación y pregunta si se exportan las claves. */
    private void finalizar(HttpServletRequest req, HttpServletResponse resp, EvaluacionEnCurso ev) throws ServletException, IOException {
        if (ev.evaluacion == null) {
            if (ev.claves.isEmpty() || ev.puesto == null) {
                mostrarPaso(req, resp, 1);
                return;
            }
            try {
                ev.evaluacion = GestorEvaluacion.getInstance().evaluarCandidatos(ev.claves, ev.puesto, consultor(req));
            } catch (ValidacionException e) {
                modal(req, new Modal(Modal.ADVERTENCIA, "No se pudo generar la evaluación").lista(e.getErrores()).aceptar());
                mostrarPaso(req, resp, 3);
                return;
            }
        }
        preguntarExportar(req);
        mostrarPaso(req, resp, 3);
    }

    private void preguntarExportar(HttpServletRequest req) {
        modal(req, new Modal(Modal.OK, "Evaluación generada",
                "Se generaron los cuestionarios en estado \"Activo\".",
                "¿Desea exportar la lista de candidatos con sus claves a un archivo Excel?")
                .boton("SI", req.getContextPath() + "/evaluar", "accion", "exportar")
                .boton("NO", req.getContextPath() + "/menu", "fin", "evaluacion"));
    }

    /** Paso 17: el navegador guarda el archivo en la carpeta que elige el actor. */
    private void descargarClaves(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        EvaluacionEnCurso ev = estado(req);
        if (ev.evaluacion == null) {
            redirigir(req, resp, "/evaluar");
            return;
        }
        GestorEvaluacion gestor = GestorEvaluacion.getInstance();
        String nombre = gestor.nombreArchivoClaves(ev.evaluacion);
        resp.setContentType("application/vnd.ms-excel");
        String ascii = java.text.Normalizer.normalize(nombre, java.text.Normalizer.Form.NFD).replaceAll("[^ -~]", "");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + ascii + "\"; filename*=UTF-8''"
                + URLEncoder.encode(nombre, StandardCharsets.UTF_8).replace("+", "%20"));
        gestor.exportarClaves(ev.evaluacion, ev.claves, resp.getOutputStream());
    }

    private void buscarCandidatos(HttpServletRequest req) {
        Map<String, String> f = filtros(req);
        req.setAttribute("filtros", f);
        if (f.values().stream().anyMatch(v -> !v.isEmpty()) || "buscar".equals(param(req, "accion"))) {
            req.setAttribute("candidatos", GestorCandidatos.getInstance().buscarCandidatos(f));
        }
    }

    private void mostrarPaso(HttpServletRequest req, HttpServletResponse resp, int paso) throws ServletException, IOException {
        EvaluacionEnCurso ev = estado(req);
        if (req.getAttribute("filtros") == null) req.setAttribute("filtros", filtros(req));
        if (paso == 2) req.setAttribute("puestos", GestorPuestos.getInstance().buscarPuestos(Map.of()));
        if (paso == 4) req.setAttribute("nombreArchivo", GestorEvaluacion.getInstance().nombreArchivoClaves(ev.evaluacion));
        req.setAttribute("paso", paso);
        req.setAttribute("ev", ev);
        req.setAttribute("tituloPagina", "Evaluar Candidatos");
        mostrar(req, resp, "evaluar");
    }

    private static Map<String, String> filtros(HttpServletRequest req) {
        Map<String, String> f = new LinkedHashMap<>();
        f.put("apellido", param(req, "apellido").toUpperCase());
        f.put("nombre", param(req, "nombre").toUpperCase());
        f.put("nroCandidato", param(req, "nroCandidato").toUpperCase());
        return f;
    }

    private static EvaluacionEnCurso estado(HttpServletRequest req) {
        EvaluacionEnCurso ev = (EvaluacionEnCurso) req.getSession().getAttribute(ESTADO);
        if (ev == null) {
            ev = new EvaluacionEnCurso();
            req.getSession().setAttribute(ESTADO, ev);
        }
        return ev;
    }
}
