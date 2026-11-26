package capitalhumano.entidades.estados;

import capitalhumano.entidades.Cuestionario;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;

public class EstadoEnProceso implements EstadoCuestionarioState {
    public static final String NOMBRE = "EN PROCESO";

    /** En proceso se puede responder: no hay nada que validar. */
    @Override
    public void responder(Cuestionario c, Map<Integer, Integer> resp) { }

    @Override
    public void finalizar(Cuestionario c) {
        LocalDateTime ahora = LocalDateTime.now();
        c.setEstadoActual(new EstadoCompleto());
        c.setFechaFin(ahora);
        c.setFechaCambioEstado(ahora);
    }

    /**
     * CU026 1.A / obs. (2): ahora - fecha del primer ingreso &gt; tiempo máximo,
     * o se superó la cantidad máxima de accesos (precondición del CU) -&gt; "Incompleto".
     */
    @Override
    public boolean verificarTiempo(Cuestionario c) {
        LocalDateTime ahora = LocalDateTime.now();
        long minutos = Duration.between(c.getFechaInicio(), ahora).toMinutes();
        if (minutos > c.getTiempoMax() || c.getCantAccesos() > c.getAccesosMaximos()) {
            c.setEstadoActual(new EstadoIncompleto());
            c.setFechaCambioEstado(ahora);
            return true;
        }
        return false;
    }

    @Override
    public String getNombreEstado() { return NOMBRE; }
}
