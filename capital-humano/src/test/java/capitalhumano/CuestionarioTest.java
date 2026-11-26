package capitalhumano;

import capitalhumano.entidades.Candidato;
import capitalhumano.entidades.Competencia;
import capitalhumano.entidades.Cuestionario;
import capitalhumano.entidades.Factor;
import capitalhumano.entidades.PreguntaCuestionario;
import capitalhumano.entidades.Puesto;
import capitalhumano.entidades.estados.EstadoActivo;
import capitalhumano.entidades.estados.EstadoCompleto;
import capitalhumano.entidades.estados.EstadoIncompleto;
import capitalhumano.entidades.estados.EstadoSinContestar;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CuestionarioTest {
    private final Fixture fx = new Fixture();

    @Test
    void competenciaEvaluableSiTieneUnFactorConDosPreguntas() {
        Competencia c = fx.competencia("LIDERAZGO");
        fx.factor(c, "F1", 1);
        assertFalse(c.puedeEvaluarse());
        fx.factor(c, "F2", 2);
        assertTrue(c.puedeEvaluarse());

        Competencia sinFactores = fx.competencia("NEGOCIACION");
        Puesto p = fx.puesto(c, 5, sinFactores, 5);
        assertFalse(p.todasCompetenciasEvaluables());
    }

    @Test
    void responderGuardaLaPonderacionDelItemElegido() {
        Competencia c = fx.competencia("C");
        fx.factor(c, "F", 2);
        Cuestionario q = fx.cuestionarioEnProceso(fx.puesto(c, 5), fx.candidato("A"));
        PreguntaCuestionario pc = q.getPreguntasBloque(1).get(0);

        q.responder(Map.of(pc.getId(), fx.aVeces.getId()));

        assertEquals(4.0, pc.getRespuesta().getPonderacion());
        assertEquals(1, q.getPrimerBloqueSinResponder());   // la otra pregunta del bloque sigue sin responder
    }

    @Test
    void noSePuedeResponderDosVecesLaMismaPregunta() {
        Competencia c = fx.competencia("C");
        fx.factor(c, "F", 2);
        Cuestionario q = fx.cuestionarioEnProceso(fx.puesto(c, 5), fx.candidato("A"));
        int id = q.getPreguntasBloque(1).get(0).getId();
        q.responder(Map.of(id, fx.siempre.getId()));
        assertThrows(IllegalArgumentException.class, () -> q.responder(Map.of(id, fx.nunca.getId())));
    }

    @Test
    void enProcesoPasaAIncompletoAlSuperarElTiempo() {
        Competencia c = fx.competencia("C");
        fx.factor(c, "F", 2);
        Cuestionario q = fx.cuestionarioEnProceso(fx.puesto(c, 5), fx.candidato("A"));
        assertFalse(q.superaTiempoMaximo());

        q.setFechaInicio(LocalDateTime.now().minusMinutes(61));
        assertTrue(q.superaTiempoMaximo());
        assertInstanceOf(EstadoIncompleto.class, q.getEstadoActual());
        assertThrows(IllegalStateException.class, () -> q.responder(Map.of()));
    }

    @Test
    void enProcesoPasaAIncompletoAlSuperarLosAccesos() {
        Competencia c = fx.competencia("C");
        fx.factor(c, "F", 2);
        Cuestionario q = fx.cuestionarioEnProceso(fx.puesto(c, 5), fx.candidato("A"));
        for (int i = 0; i < 5; i++) q.registrarAcceso();
        assertFalse(q.superaTiempoMaximo());
        q.registrarAcceso();   // sexto acceso con máximo 5
        assertTrue(q.superaTiempoMaximo());
        assertEquals(EstadoIncompleto.NOMBRE, q.getNombreEstado());
    }

    @Test
    void activoPasaASinContestarSiNadieIngresaATiempo() {
        Cuestionario q = new Cuestionario();
        q.setEstadoActual(new EstadoActivo());
        q.setFechaCambioEstado(LocalDateTime.now().minusDays(30));
        assertTrue(q.superaTiempoMaximo());
        assertInstanceOf(EstadoSinContestar.class, q.getEstadoActual());
    }

    @Test
    void finalizarPasaACompletoConFechaFin() {
        Competencia c = fx.competencia("C");
        fx.factor(c, "F", 2);
        Cuestionario q = fx.cuestionarioEnProceso(fx.puesto(c, 5), fx.candidato("A"));
        assertNull(q.getFechaFin());
        q.finalizar();
        assertInstanceOf(EstadoCompleto.class, q.getEstadoActual());
        assertTrue(q.estaCompleto());
        assertThrows(IllegalStateException.class, q::finalizar);
    }

    @Test
    void candidatoConCuestionarioActivoNoPuedeSerEvaluado() {
        Candidato cand = fx.candidato("A");
        Cuestionario q = new Cuestionario();
        q.setEstadoActual(new EstadoCompleto());
        cand.getCuestionarios().add(q);
        assertFalse(cand.tieneCuestionarioActivo());
        Cuestionario otro = new Cuestionario();
        otro.setEstadoActual(new EstadoActivo());
        cand.getCuestionarios().add(otro);
        assertTrue(cand.tieneCuestionarioActivo());
    }

    @Test
    void factoresEvaluadosSoloLosQueTienenPreguntasEnElCuestionario() {
        Competencia c = fx.competencia("C");
        Factor f1 = fx.factor(c, "F1", 2);
        fx.factor(c, "F2", 0);
        Cuestionario q = fx.cuestionarioEnProceso(fx.puesto(c, 5), fx.candidato("A"));
        assertEquals(1, q.getFactoresEvaluados(c).size());
        assertEquals(f1.getId(), q.getFactoresEvaluados(c).get(0).getId());
    }
}
