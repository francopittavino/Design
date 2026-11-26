package capitalhumano.entidades.estados;

public class EstadoIncompleto extends EstadoFinal {
    public static final String NOMBRE = "INCOMPLETO";

    @Override
    public String getNombreEstado() { return NOMBRE; }
}
