package capitalhumano.pantallas;

import capitalhumano.dto.CuestionarioDTO;
import capitalhumano.entidades.Consultor;
import capitalhumano.entidades.TipoDocumento;
import capitalhumano.gestores.GestorAutenticacion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/** CU001 - Autenticar usuario: "Ingresar como usuario consultor" o "Realizar cuestionario". */
@WebServlet({"/login", ""})
public class PantallaLogin extends PantallaBase {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("salir".equals(param(req, "accion"))) {
            HttpSession s = req.getSession(false);
            if (s != null) s.invalidate();
            redirigir(req, resp, "/login");
            return;
        }
        HttpSession s = req.getSession(false);
        if (s != null && s.getAttribute("consultor") != null) { redirigir(req, resp, "/menu"); return; }
        if (s != null && s.getAttribute("cuestionarioDTO") != null) { redirigir(req, resp, "/cuestionario"); return; }
        mostrarFormulario(req, resp, param(req, "tipo"));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        GestorAutenticacion gestor = GestorAutenticacion.getInstance();
        if ("candidato".equals(param(req, "tipo"))) {
            CuestionarioDTO dto = gestor.autenticarCandidato(param(req, "tipoDoc"), param(req, "nroDoc"), param(req, "clave").toUpperCase());
            if (dto == null) {
                // 3.A.2.2 - 3.A.2.4: mensaje y se blanquean los campos
                modal(req, new Modal(Modal.ADVERTENCIA, "Error de autenticación",
                        "Los datos ingresados no son válidos o no existe un cuestionario para el Candidato").aceptar());
                mostrarFormulario(req, resp, "candidato");
                return;
            }
            HttpSession s = req.getSession(true);
            s.invalidate();
            s = req.getSession(true);
            s.setAttribute("cuestionarioDTO", dto);
            redirigir(req, resp, "/cuestionario");
            return;
        }

        Consultor consultor = gestor.autenticarConsultor(param(req, "usuario"), req.getParameter("password"));
        if (consultor == null) {
            modal(req, new Modal(Modal.ADVERTENCIA, "Error de autenticación", "El usuario o la contraseña no son válidos").aceptar());
            mostrarFormulario(req, resp, "consultor");
            return;
        }
        HttpSession s = req.getSession(true);
        s.invalidate();
        s = req.getSession(true);
        s.setAttribute("consultor", consultor);
        redirigir(req, resp, "/menu");
    }

    private void mostrarFormulario(HttpServletRequest req, HttpServletResponse resp, String tipo) throws ServletException, IOException {
        req.setAttribute("tipo", tipo);
        req.setAttribute("tiposDoc", TipoDocumento.values());
        req.setAttribute("tituloPagina", "Ingreso");
        mostrar(req, resp, "login");
    }
}
