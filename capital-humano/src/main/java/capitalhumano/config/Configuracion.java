package capitalhumano.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Repositorio de configuraciones del sistema (artefacto configuracion.properties del diagrama
 * de despliegue). CU026: instrucciones, preguntas por bloque, tiempo máximo y accesos máximos.
 * Los datos de conexión se pueden pisar con variables de entorno (DB_URL, DB_USER, DB_PASSWORD).
 */
public final class Configuracion {
    private static Configuracion instance;
    private final Properties props = new Properties();

    private Configuracion() {
        try (InputStream in = Configuracion.class.getResourceAsStream("/configuracion.properties")) {
            if (in == null) throw new IllegalStateException("No se encontró configuracion.properties");
            props.load(new InputStreamReader(in, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new IllegalStateException("No se pudo leer configuracion.properties", e);
        }
    }

    public static synchronized Configuracion getInstance() {
        if (instance == null) instance = new Configuracion();
        return instance;
    }

    private String get(String clave, String env) {
        String v = env == null ? null : System.getenv(env);
        return v != null && !v.isBlank() ? v : props.getProperty(clave);
    }

    public String getDbUrl() { return get("db.url", "DB_URL"); }
    public String getDbUsuario() { return get("db.usuario", "DB_USER"); }
    public String getDbPassword() { return get("db.password", "DB_PASSWORD"); }

    public int getPreguntasPorBloque() { return Integer.parseInt(get("cuestionario.preguntasPorBloque", null)); }
    public int getTiempoMaxMinutos() { return Integer.parseInt(get("cuestionario.tiempoMaxMinutos", null)); }
    public int getAccesosMaximos() { return Integer.parseInt(get("cuestionario.accesosMaximos", null)); }
    public int getDiasMaxActivo() { return Integer.parseInt(get("cuestionario.diasMaxActivo", null)); }
    public String getInstrucciones() { return get("cuestionario.instrucciones", null); }

    /** Simulación del LDAP de la organización (no hay servidor LDAP en el entorno del TP). */
    public String getPasswordLdapSimulado() { return get("ldap.simulado.password", "LDAP_PASSWORD"); }
}
