package capitalhumano.facade;

import capitalhumano.entidades.Competencia;
import capitalhumano.entidades.Factor;
import capitalhumano.entidades.Pregunta;
import capitalhumano.entidades.Puesto;

/**
 * El CU033 (Generar pregunta con IA) no forma parte de las etapas 7 y 8. Esta implementación
 * se comporta como un servicio caído: el formulario muestra el mensaje de error de la
 * tabla de mensajes del CU021 ("Ocurrió un error al comunicarse con el servicio de IA...").
 */
public class ServicioIANoDisponible implements IServicioIA {
    @Override
    public Pregunta generarPregunta(Competencia competencia, Factor factor) {
        throw new ServicioExternoException("Servicio de IA no disponible");
    }

    @Override
    public Puesto generarPuesto(String nombre) {
        throw new ServicioExternoException("Servicio de IA no disponible");
    }
}
