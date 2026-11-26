package capitalhumano.pantallas;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Precondición "Actor autenticado en el sistema": las pantallas del consultor piden un consultor
 * en la sesión y la del cuestionario un candidato autenticado (CU001).
 */
@WebFilter("/*")
public class FiltroSesion extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        String ruta = req.getRequestURI().substring(req.getContextPath().length());
        HttpSession s = req.getSession(false);
        boolean consultor = s != null && s.getAttribute("consultor") != null;
        boolean candidato = s != null && s.getAttribute("cuestionarioDTO") != null;

        boolean libre = ruta.equals("/") || ruta.startsWith("/login") || ruta.startsWith("/css/") || ruta.equals("/favicon.ico");
        if (libre
                || (ruta.startsWith("/cuestionario") && candidato)
                || (!ruta.startsWith("/cuestionario") && consultor)) {
            chain.doFilter(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}
