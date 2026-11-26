package capitalhumano.pantallas;

import capitalhumano.gestores.GestorFinalizacion;

/**
 * Punto de entrada de finalizar-cuestionarios.jar (CU032, fuera del alcance de las Etapas 7 y 8).
 * Lo dispara el planificador de tareas del servidor de aplicaciones.
 */
public final class InterfazBatch {
    private InterfazBatch() {}

    public static void main(String[] args) {
        GestorFinalizacion.getInstance().ejecutar();
    }
}
