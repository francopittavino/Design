package capitalhumano.entidades.estados;

public class EstadoCompleto extends EstadoFinal {
    public static final String NOMBRE = "COMPLETO";

    @Override
    public String getNombreEstado() { return NOMBRE; }
}
