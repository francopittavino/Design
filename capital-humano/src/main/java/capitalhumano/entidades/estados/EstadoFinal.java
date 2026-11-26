package capitalhumano.entidades.estados;

import capitalhumano.entidades.Cuestionario;

import java.util.Map;

/** Comportamiento común de los estados finales (Completo, Incompleto, Sin Contestar): no admiten cambios. */
abstract class EstadoFinal implements EstadoCuestionarioState {

    @Override
    public void responder(Cuestionario c, Map<Integer, Integer> resp) {
        throw new IllegalStateException("El cuestionario está " + getNombreEstado() + " y no se puede responder");
    }

    @Override
    public void finalizar(Cuestionario c) {
        throw new IllegalStateException("El cuestionario ya está " + getNombreEstado());
    }

    @Override
    public boolean verificarTiempo(Cuestionario c) { return false; }
}
