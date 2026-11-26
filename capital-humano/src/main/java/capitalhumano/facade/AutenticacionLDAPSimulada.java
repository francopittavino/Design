package capitalhumano.facade;

import capitalhumano.config.Configuracion;
import capitalhumano.dao.ConsultorDAO;
import capitalhumano.dao.impl.ConsultorDAOImpl;
import capitalhumano.entidades.Consultor;

/**
 * Adaptador del LDAP de la organización. En el entorno del TP no hay servidor LDAP, así que se simula:
 * el usuario tiene que existir (activo) en la tabla consultor y la contraseña es la de
 * {@code ldap.simulado.password} en configuracion.properties. Para conectarlo a un LDAP real alcanza
 * con otra implementación de IAutenticacionLDAP (JNDI); el resto del sistema no cambia.
 */
public class AutenticacionLDAPSimulada implements IAutenticacionLDAP {
    private final ConsultorDAO consultorDAO = new ConsultorDAOImpl();

    @Override
    public Consultor autenticar(String usuario, String password) {
        if (usuario == null || password == null) return null;
        Consultor c = consultorDAO.buscarPorUsuario(usuario.trim());
        if (c == null || !c.isActivo()) return null;
        return password.equals(Configuracion.getInstance().getPasswordLdapSimulado()) ? c : null;
    }

    /** La sesión del consultor la maneja el contenedor web (HttpSession + FiltroSesion); el LDAP simulado no emite tokens. */
    @Override
    public boolean validarSesion(String token) {
        throw new UnsupportedOperationException("El LDAP simulado no emite tokens de sesión");
    }
}
