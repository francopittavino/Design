package capitalhumano.dao.impl;

import capitalhumano.config.Configuracion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/** Conexiones JDBC a PostgreSQL 16 (camino de comunicación «JDBC» del diagrama de despliegue). */
public final class ConexionBD {

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private ConexionBD() {}

    public static Connection obtener() throws SQLException {
        Configuracion cfg = Configuracion.getInstance();
        return DriverManager.getConnection(cfg.getDbUrl(), cfg.getDbUsuario(), cfg.getDbPassword());
    }

    static Timestamp ts(LocalDateTime t) { return t == null ? null : Timestamp.valueOf(t); }

    static LocalDateTime ldt(Timestamp t) { return t == null ? null : t.toLocalDateTime(); }
}
