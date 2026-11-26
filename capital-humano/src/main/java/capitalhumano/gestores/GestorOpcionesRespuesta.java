package capitalhumano.gestores;

import capitalhumano.dao.impl.RegistroAuditoriaDAOImpl;
import capitalhumano.dao.impl.PreguntaDAOImpl;
import capitalhumano.dao.RegistroAuditoriaDAO;
import capitalhumano.dao.PreguntaDAO;
import capitalhumano.dao.OpcionRespuestaDAO;
import capitalhumano.dao.impl.OpcionRespuestaDAOImpl;
import capitalhumano.dto.ItemOpcionRespuestaDTO;
import capitalhumano.dto.OpcionRespuestaDTO;
import capitalhumano.entidades.ItemOpcionDeRespuesta;
import capitalhumano.entidades.OpcionDeRespuesta;

import java.util.Comparator;
import java.util.List;

public final class GestorOpcionesRespuesta {
    private static GestorOpcionesRespuesta instance;

    private final OpcionRespuestaDAO opcionDAO = new OpcionRespuestaDAOImpl();
    private final PreguntaDAO preguntaDAO = new PreguntaDAOImpl();
    private final RegistroAuditoriaDAO auditoriaDAO = new RegistroAuditoriaDAOImpl();

    private GestorOpcionesRespuesta() {}

    public static synchronized GestorOpcionesRespuesta getInstance() {
        if (instance == null) instance = new GestorOpcionesRespuesta();
        return instance;
    }

    public List<OpcionRespuestaDTO> buscarOpciones(String nombre) {
        List<OpcionDeRespuesta> lista = (nombre == null || nombre.isBlank())
                ? opcionDAO.listarTodas() : opcionDAO.buscarPorNombre(nombre);
        return lista.stream().map(o -> new OpcionRespuestaDTO(o.getId(), o.getNombre())).toList();
    }

    /** CU021: ítems de la opción ordenados por Orden (grilla Orden / Descripción / Ponderación). */
    public List<ItemOpcionRespuestaDTO> obtenerItems(int idOpcion) {
        OpcionDeRespuesta o = opcionDAO.buscarPorId(idOpcion);
        if (o == null) return List.of();
        return o.getItems().stream().filter(ItemOpcionDeRespuesta::isActivo)
                .sorted(Comparator.comparingInt(ItemOpcionDeRespuesta::getOrden))
                .map(i -> new ItemOpcionRespuestaDTO(i.getId(), i.getOrden(), i.getDescripcion())).toList();
    }

    /** CU029 - Dar de alta opción de respuesta (fuera del alcance de las Etapas 7 y 8). */
    public OpcionRespuestaDTO crearOpcion(OpcionRespuestaDTO datos) {
        throw new UnsupportedOperationException("CU029 - Dar de alta opción de respuesta: fuera del alcance de las Etapas 7 y 8");
    }

    /** CU030 - Modificar opción de respuesta (fuera del alcance de las Etapas 7 y 8). */
    public OpcionRespuestaDTO modificarOpcion(int id, OpcionRespuestaDTO datos) {
        throw new UnsupportedOperationException("CU030 - Modificar opción de respuesta: fuera del alcance de las Etapas 7 y 8");
    }

    /** CU031 - Dar de baja opción de respuesta (fuera del alcance de las Etapas 7 y 8). */
    public void darDeBajaOpcion(int id) {
        throw new UnsupportedOperationException("CU031 - Dar de baja opción de respuesta: fuera del alcance de las Etapas 7 y 8");
    }

    /** CU029 - Validar opción de respuesta (fuera del alcance de las Etapas 7 y 8). */
    public List<String> validar(OpcionRespuestaDTO o) {
        throw new UnsupportedOperationException("CU029 - Validar opción de respuesta: fuera del alcance de las Etapas 7 y 8");
    }
}
