package capitalhumano.gestores;

import java.util.List;

/** Validaciones de negocio no superadas: la pantalla muestra TODOS los mensajes juntos. */
public class ValidacionException extends RuntimeException {
    private final List<String> errores;

    public ValidacionException(List<String> errores) {
        super(String.join(" / ", errores));
        this.errores = List.copyOf(errores);
    }

    public ValidacionException(String error) { this(List.of(error)); }

    public List<String> getErrores() { return errores; }
}
