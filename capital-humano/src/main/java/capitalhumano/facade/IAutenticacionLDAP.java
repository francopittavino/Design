package capitalhumano.facade;

import capitalhumano.entidades.Consultor;

public interface IAutenticacionLDAP {
    /** Devuelve el consultor si usuario y contraseña son válidos en el LDAP; null si no. */
    Consultor autenticar(String usuario, String password);
}
