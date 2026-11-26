package capitalhumano.entidades.estados;

public class EstadoSinContestar extends EstadoFinal {
    public static final String NOMBRE = "SIN CONTESTAR";

    @Override
    public String getNombreEstado() { return NOMBRE; }
}
