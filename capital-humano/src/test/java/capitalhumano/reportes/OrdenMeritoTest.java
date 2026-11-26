package capitalhumano.reportes;

import capitalhumano.Fixture;
import capitalhumano.entidades.Competencia;
import capitalhumano.entidades.Cuestionario;
import capitalhumano.entidades.Puesto;
import capitalhumano.entidades.estados.EstadoCompleto;
import capitalhumano.entidades.estados.EstadoEnProceso;
import capitalhumano.entidades.estados.EstadoIncompleto;
import capitalhumano.exportacion.ExportadorFormato;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrdenMeritoTest {
    private final Fixture fx = new Fixture();

    private ItemOrdenMerito item(Puesto p, String apellido, Double puntaje, boolean cumple, String estado) {
        Cuestionario q = new Cuestionario();
        q.setCandidato(fx.candidato(apellido));
        q.setEstadoActual(switch (estado) {
            case "COMPLETO" -> new EstadoCompleto();
            case "INCOMPLETO" -> new EstadoIncompleto();
            default -> new EstadoEnProceso();
        });
        ItemOrdenMerito i = new ItemOrdenMerito(q);
        i.setPuntajeObtenido(puntaje);
        i.setCumplePonderaciones(cumple);
        return i;
    }

    @Test
    void ordenaPorPuntajeSoloALosQueAlcanzaronLosMinimosYAgrupaElRestoPorEstado() {
        Competencia c = fx.competencia("C");
        Puesto p = fx.puesto(c, 6);
        OrdenMerito om = new OrdenMerito(p);
        om.agregarItem(item(p, "LOPEZ", 7.2, true, "COMPLETO"));
        om.agregarItem(item(p, "TORRES", null, false, "INCOMPLETO"));
        om.agregarItem(item(p, "GARCIA", 8.5, true, "COMPLETO"));
        om.agregarItem(item(p, "MARTINEZ", 5.1, false, "COMPLETO"));
        om.agregarItem(item(p, "ROMERO", null, false, "EN PROCESO"));

        om.generar();

        assertEquals(List.of("GARCIA", "LOPEZ"), om.getAlcanzaron().stream().map(i -> i.getCandidato().getApellido()).toList());
        assertEquals(1, om.getAlcanzaron().get(0).getPosicion());
        assertEquals(2, om.getAlcanzaron().get(1).getPosicion());
        assertEquals(List.of("MARTINEZ"), om.getNoAlcanzaron().stream().map(i -> i.getCandidato().getApellido()).toList());
        assertEquals("NO ALCANZA MÍNIMO", om.getNoAlcanzaron().get(0).getEstado());
        assertEquals(List.of(EstadoEnProceso.NOMBRE, EstadoIncompleto.NOMBRE), List.copyOf(om.getPorEstado().keySet()));
        assertTrue(om.tieneCuestionariosCompletos());
    }

    @Test
    void sinCompletosNoHayPuntajes() {
        Puesto p = fx.puesto(fx.competencia("C"), 6);
        OrdenMerito om = new OrdenMerito(p);
        om.agregarItem(item(p, "TORRES", null, false, "INCOMPLETO"));
        om.generar();
        assertFalse(om.tieneCuestionariosCompletos());
        assertTrue(om.getAlcanzaron().isEmpty());
    }

    @Test
    void imprimirDelegaEnElExportadorRecibido() {
        Puesto p = fx.puesto(fx.competencia("C"), 6);
        OrdenMerito om = new OrdenMerito(p);
        om.agregarItem(item(p, "GARCIA", 8.5, true, "COMPLETO"));
        om.generar();
        AtomicReference<Map<String, List<List<String>>>> recibido = new AtomicReference<>();
        ExportadorFormato falso = (titulo, datos) -> { recibido.set(datos); return true; };

        om.setFormato(falso);
        assertTrue(om.imprimir());

        List<List<String>> grupoA = recibido.get().values().iterator().next();
        assertEquals("GARCIA", grupoA.get(1).get(3));   // fila 0 = encabezado
    }
}
