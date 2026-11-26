package capitalhumano;

import capitalhumano.entidades.Candidato;
import capitalhumano.entidades.CaracteristicaPuesto;
import capitalhumano.entidades.Competencia;
import capitalhumano.entidades.Cuestionario;
import capitalhumano.entidades.Empresa;
import capitalhumano.entidades.Evaluacion;
import capitalhumano.entidades.Factor;
import capitalhumano.entidades.ItemOpcionDeRespuesta;
import capitalhumano.entidades.OpcionDeRespuesta;
import capitalhumano.entidades.PonderacionItem;
import capitalhumano.entidades.Pregunta;
import capitalhumano.entidades.PreguntaCuestionario;
import capitalhumano.entidades.Puesto;
import capitalhumano.entidades.TipoDocumento;
import capitalhumano.entidades.estados.EstadoEnProceso;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

/** Arma un modelo chico en memoria (sin base de datos) para probar la lógica de las entidades. */
public final class Fixture {
    private static final AtomicInteger IDS = new AtomicInteger(1);

    public final OpcionDeRespuesta frecuencia = new OpcionDeRespuesta();
    public final ItemOpcionDeRespuesta siempre = item(1, "SIEMPRE");
    public final ItemOpcionDeRespuesta aVeces = item(2, "A VECES");
    public final ItemOpcionDeRespuesta nunca = item(3, "NUNCA");

    public Fixture() {
        frecuencia.setId(id());
        frecuencia.setNombre("FRECUENCIA");
        frecuencia.getItems().add(siempre);
        frecuencia.getItems().add(aVeces);
        frecuencia.getItems().add(nunca);
    }

    public static int id() { return IDS.getAndIncrement(); }

    private static ItemOpcionDeRespuesta item(int orden, String desc) {
        ItemOpcionDeRespuesta i = new ItemOpcionDeRespuesta();
        i.setId(id());
        i.setOrden(orden);
        i.setDescripcion(desc);
        return i;
    }

    public Competencia competencia(String nombre) {
        Competencia c = new Competencia();
        c.setId(id());
        c.setNombre(nombre);
        return c;
    }

    /** Factor con {@code cantPreguntas} preguntas que ponderan SIEMPRE=6, A VECES=4, NUNCA=0. */
    public Factor factor(Competencia c, String nombre, int cantPreguntas) {
        Factor f = new Factor();
        f.setId(id());
        f.setNombre(nombre);
        f.setCompetencia(c);
        c.getFactores().add(f);
        for (int i = 0; i < cantPreguntas; i++) {
            Pregunta p = new Pregunta(nombre + " P" + i, "ENUNCIADO " + nombre + " " + i, null, f, frecuencia);
            p.setId(id());
            p.agregarPonderacion(new PonderacionItem(6, siempre));
            p.agregarPonderacion(new PonderacionItem(4, aVeces));
            p.agregarPonderacion(new PonderacionItem(0, nunca));
            f.getPreguntas().add(p);
        }
        return f;
    }

    public Puesto puesto(Object... competenciaYMinimo) {
        Puesto p = new Puesto();
        p.setId(id());
        p.setCodigo("P" + p.getId());
        p.setNombre("PUESTO " + p.getId());
        p.setEmpresa(new Empresa(1, "EMPRESA"));
        for (int i = 0; i < competenciaYMinimo.length; i += 2) {
            CaracteristicaPuesto cp = new CaracteristicaPuesto();
            cp.setCompetencia((Competencia) competenciaYMinimo[i]);
            cp.setPonderacionMinima(((Number) competenciaYMinimo[i + 1]).doubleValue());
            p.getCaracteristicas().add(cp);
        }
        return p;
    }

    public Candidato candidato(String apellido) {
        Candidato c = new Candidato();
        c.setId(id());
        c.setApellido(apellido);
        c.setNombre("N");
        c.setNroCandidato(String.valueOf(c.getId()));
        c.setTipoDoc(TipoDocumento.DNI);
        c.setNroDoc(String.valueOf(10000000 + c.getId()));
        return c;
    }

    /** Cuestionario "En Proceso" con todas las preguntas de todos los factores del puesto, en un bloque cada 4. */
    public Cuestionario cuestionarioEnProceso(Puesto puesto, Candidato candidato) {
        Evaluacion e = new Evaluacion(puesto, null, 4);
        Cuestionario c = new Cuestionario();
        c.setId(id());
        c.setEvaluacion(e);
        c.setCandidato(candidato);
        c.setTiempoMax(60);
        c.setAccesosMaximos(5);
        c.setFechaInicio(LocalDateTime.now());
        c.setFechaCambioEstado(LocalDateTime.now());
        c.setEstadoActual(new EstadoEnProceso());
        int orden = 1;
        for (Competencia comp : puesto.getCompetencias()) {
            for (Factor f : comp.getFactores()) {
                for (Pregunta p : f.getPreguntas()) {
                    PreguntaCuestionario pc = new PreguntaCuestionario(p, orden, (orden - 1) / 4 + 1);
                    pc.setId(id());
                    c.agregarPregunta(pc);
                    orden++;
                }
            }
        }
        e.getCuestionarios().add(c);
        return c;
    }
}
