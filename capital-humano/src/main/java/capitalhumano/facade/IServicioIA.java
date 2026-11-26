package capitalhumano.facade;

import capitalhumano.entidades.Competencia;
import capitalhumano.entidades.Factor;
import capitalhumano.entidades.Pregunta;

public interface IServicioIA {
    Pregunta generarPregunta(Competencia competencia, Factor factor);
}
