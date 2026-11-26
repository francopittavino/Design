package capitalhumano.facade;

import capitalhumano.entidades.Competencia;
import capitalhumano.entidades.Factor;
import capitalhumano.entidades.Pregunta;
import capitalhumano.entidades.Puesto;

public interface IServicioIA {
    Pregunta generarPregunta(Competencia competencia, Factor factor);
    /** CU009 - Dar de alta puesto con IA (fuera del alcance de las Etapas 7 y 8). */
    Puesto generarPuesto(String nombre);
}
