package capitalhumano.pantallas;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/menu")
public class PantallaMenuPrincipal extends PantallaBase {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("tituloPagina", "Menú principal");
        mostrar(req, resp, "menu");
    }

    /** Los botones de las ventanas de mensaje que "vuelven a la pantalla principal" llegan por POST. */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.getSession().removeAttribute("evaluacionEnCurso");
        redirigir(req, resp, "/menu");
    }
}
