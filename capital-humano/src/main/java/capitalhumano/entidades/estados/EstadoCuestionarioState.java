package capitalhumano.entidades.estados;

import capitalhumano.entidades.Cuestionario;

import java.util.Map;

/** Patrón State: ciclo de vida del cuestionario (Activo, En Proceso, Completo, Incompleto, Sin Contestar). */
public interface EstadoCuestionarioState {
    /** Valida que se pueda responder; las RespuestaCandidata las crea el cuestionario. */
    void responder(Cuestionario c, Map<Integer, Integer> resp);
    void finalizar(Cuestionario c);
    /** Si se superó el tiempo (o los accesos) cambia el estado del cuestionario y devuelve true. */
    boolean verificarTiempo(Cuestionario c);
    String getNombreEstado();

    /** Reconstruye el estado a partir del valor persistido en cuestionario.estado. */
    static EstadoCuestionarioState desdeNombre(String nombre) {
        return switch (nombre) {
            case EstadoActivo.NOMBRE -> new EstadoActivo();
            case EstadoEnProceso.NOMBRE -> new EstadoEnProceso();
            case EstadoCompleto.NOMBRE -> new EstadoCompleto();
            case EstadoIncompleto.NOMBRE -> new EstadoIncompleto();
            case EstadoSinContestar.NOMBRE -> new EstadoSinContestar();
            default -> throw new IllegalArgumentException("Estado desconocido: " + nombre);
        };
    }
}
