package capitalhumano.pantallas;

import capitalhumano.entidades.Consultor;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Comportamiento común de las pantallas (boundary): render de la vista JSP y utilidades de request. */
public abstract class PantallaBase extends HttpServlet {

    protected void mostrar(HttpServletRequest req, HttpServletResponse resp, String vista) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/" + vista + ".jsp").forward(req, resp);
    }

    protected void redirigir(HttpServletRequest req, HttpServletResponse resp, String ruta) throws IOException {
        resp.sendRedirect(req.getContextPath() + ruta);
    }

    protected void modal(HttpServletRequest req, Modal modal) { req.setAttribute("modal", modal); }

    protected static String param(HttpServletRequest req, String nombre) {
        String v = req.getParameter(nombre);
        return v == null ? "" : v.trim();
    }

    protected static int entero(HttpServletRequest req, String nombre) {
        try {
            return Integer.parseInt(param(req, nombre));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    protected static Consultor consultor(HttpServletRequest req) {
        return (Consultor) req.getSession().getAttribute("consultor");
    }
}
