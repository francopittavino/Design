package capitalhumano.gestores;

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
}
