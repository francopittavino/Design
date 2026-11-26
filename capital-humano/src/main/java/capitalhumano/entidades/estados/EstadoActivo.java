package capitalhumano.entidades.estados;

import capitalhumano.config.Configuracion;
import capitalhumano.entidades.Cuestionario;

import java.time.LocalDateTime;
import java.util.Map;

public class EstadoActivo implements EstadoCuestionarioState {
    public static final String NOMBRE = "ACTIVO";

    @Override
    public void responder(Cuestionario c, Map<Integer, Integer> resp) {
        throw new IllegalStateException("Primero hay que aceptar las instrucciones del cuestionario");
    }

    @Override
    public void finalizar(Cuestionario c) {
        throw new IllegalStateException("El cuestionario todavía no fue iniciado");
    }

    /** CU026 1.B: si estuvo "Activo" más tiempo del permitido pasa a "Sin Contestar". */
    @Override
    public boolean verificarTiempo(Cuestionario c) {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime limite = c.getFechaCambioEstado().plusDays(Configuracion.getInstance().getDiasMaxActivo());
        if (ahora.isAfter(limite)) {
            c.setEstadoActual(new EstadoSinContestar());
            c.setFechaCambioEstado(ahora);
            return true;
        }
        return false;
    }

    @Override
    public String getNombreEstado() { return NOMBRE; }
}
