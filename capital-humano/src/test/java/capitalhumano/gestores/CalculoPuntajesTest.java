package capitalhumano.gestores;

import capitalhumano.Fixture;
import capitalhumano.entidades.Competencia;
import capitalhumano.entidades.Cuestionario;
import capitalhumano.entidades.Factor;
import capitalhumano.entidades.PreguntaCuestionario;
import capitalhumano.entidades.ResultadoCompetencia;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Algoritmo (3) del CU026 y claves del CU025 (no necesitan base de datos). */
class CalculoPuntajesTest {
    private final Fixture fx = new Fixture();

    @Test
    void totalFactorEsSumaSobreDosYCompetenciaEsPromedioDeFactores() {
        Competencia c = fx.competencia("COMUNICACION");
        Factor oral = fx.factor(c, "ORAL", 2);
        Factor escrita = fx.factor(c, "ESCRITA", 2);
        Cuestionario q = fx.cuestionarioEnProceso(fx.puesto(c, 5), fx.candidato("A"));

        // ORAL: SIEMPRE (6) + A VECES (4) -> 10 / 2 = 5 ; ESCRITA: NUNCA (0) + A VECES (4) -> 4 / 2 = 2
        Map<Integer, Integer> resp = new HashMap<>();
        for (PreguntaCuestionario pc : q.getPreguntas()) {
            boolean primera = pc.getPregunta() == pc.getPregunta().getFactor().getPreguntas().get(0);
            if (pc.getPregunta().getFactor() == oral) resp.put(pc.getId(), primera ? fx.siempre.getId() : fx.aVeces.getId());
            else resp.put(pc.getId(), primera ? fx.nunca.getId() : fx.aVeces.getId());
        }
        q.responder(resp);

        GestorCuestionario.getInstance().calcularPuntajes(q);

        ResultadoCompetencia rc = q.getResultado(c);
        assertEquals(3.5, rc.getPuntajeObtenido(), 1e-9);   // (5 + 2) / 2 factores
        assertEquals(5.0, rc.getResultadosFactor().stream().filter(r -> r.getFactor() == oral).findFirst().orElseThrow().getPuntajeObtenido());
        assertEquals(2.0, rc.getResultadosFactor().stream().filter(r -> r.getFactor() == escrita).findFirst().orElseThrow().getPuntajeObtenido());
        assertTrue(rc.superaPonderacionMinima(3.5));
        assertFalse(rc.superaPonderacionMinima(3.51));
        assertEquals(3.5, q.getPuntajeTotal());
    }

    @Test
    void competenciaSinFactoresEvaluadosNoTieneResultado() {
        Competencia evaluada = fx.competencia("A");
        fx.factor(evaluada, "F", 2);
        Competencia sinPreguntas = fx.competencia("B");
        Cuestionario q = fx.cuestionarioEnProceso(fx.puesto(evaluada, 5, sinPreguntas, 5), fx.candidato("A"));
        Map<Integer, Integer> resp = new HashMap<>();
        q.getPreguntas().forEach(pc -> resp.put(pc.getId(), fx.siempre.getId()));
        q.responder(resp);

        GestorCuestionario.getInstance().calcularPuntajes(q);

        assertEquals(1, q.getResultados().size());
        assertEquals(6.0, q.getResultado(evaluada).getPuntajeObtenido());
    }

    @Test
    void lasClavesTienenOchoCaracteresConLetrasYDigitos() {
        GestorEvaluacion g = GestorEvaluacion.getInstance();
        for (int i = 0; i < 500; i++) {
            String clave = g.generarClave();
            assertEquals(8, clave.length());
            assertTrue(clave.chars().anyMatch(Character::isLetter), clave);
            assertTrue(clave.chars().anyMatch(Character::isDigit), clave);
            assertTrue(clave.chars().allMatch(Character::isLetterOrDigit), clave);
        }
    }

    @Test
    void generarCuestionariosNoRepiteClaves() {
        List<capitalhumano.dto.CandidatoGestionDTO> candidatos = new java.util.ArrayList<>();
        for (int i = 0; i < 50; i++) {
            capitalhumano.dto.CandidatoGestionDTO c = new capitalhumano.dto.CandidatoGestionDTO();
            c.setNroCandidato(String.valueOf(i));
            candidatos.add(c);
        }
        long distintas = GestorEvaluacion.getInstance().generarCuestionarios(candidatos).stream()
                .map(capitalhumano.dto.CandidatoCuestionarioDTO::getToken).distinct().count();
        assertEquals(50, distintas);
    }
}
