package capitalhumano.dao;

/** Error de acceso a datos (envuelve la SQLException para no atar a los gestores a JDBC). */
public class DatosException extends RuntimeException {
    public DatosException(String mensaje, Throwable causa) { super(mensaje, causa); }
}
