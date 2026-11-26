package capitalhumano.gestores;

import capitalhumano.dao.CuestionarioDAO;
import capitalhumano.dao.RegistroEjecucionBatchDAO;
import capitalhumano.dao.impl.CuestionarioDAOImpl;
import capitalhumano.dao.impl.RegistroEjecucionBatchDAOImpl;
import capitalhumano.entidades.RegistroEjecucionBatch;

/**
 * CU032 - Proceso batch de finalización de cuestionarios (fuera del alcance de las Etapas 7 y 8).
 * Corre en finalizar-cuestionarios.jar (diagrama de despliegue). Mientras tanto, el CU026 hace los
 * mismos cambios de estado cuando el candidato vuelve a ingresar (1.A y 1.B).
 */
public final class GestorFinalizacion {
    private static GestorFinalizacion instance;

    private final CuestionarioDAO cuestionarioDAO = new CuestionarioDAOImpl();
    private final RegistroEjecucionBatchDAO registroEjecucionBatchDAO = new RegistroEjecucionBatchDAOImpl();

    private GestorFinalizacion() {}

    public static synchronized GestorFinalizacion getInstance() {
        if (instance == null) instance = new GestorFinalizacion();
        return instance;
    }

    /** Una ejecución del batch: pasa los vencidos a Incompleto, los no iniciados a Sin Contestar y lo registra. */
    public void ejecutar() {
        finalizarVencidos();
        marcarSinContestar();
        registroEjecucionBatchDAO.guardar(new RegistroEjecucionBatch("FINALIZACION DE CUESTIONARIOS"));
    }

    /** Cuestionarios "En Proceso" que superaron el tiempo máximo pasan a "Incompleto". */
    public void finalizarVencidos() {
        throw new UnsupportedOperationException("CU032 - Finalizar cuestionarios: fuera del alcance de las Etapas 7 y 8");
    }

    /** Cuestionarios "Activo" que superaron los días permitidos pasan a "Sin Contestar". */
    public void marcarSinContestar() {
        throw new UnsupportedOperationException("CU032 - Finalizar cuestionarios: fuera del alcance de las Etapas 7 y 8");
    }
}
