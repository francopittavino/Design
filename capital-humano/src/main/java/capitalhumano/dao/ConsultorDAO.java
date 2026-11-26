package capitalhumano.dao;

import capitalhumano.entidades.Consultor;

/** Lo usa el adaptador de LDAP para devolver el Consultor autenticado (CU001). */
public interface ConsultorDAO {
    Consultor buscarPorUsuario(String usuario);
}
